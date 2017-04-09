package com.acmeair;

import com.acmeair.loader.CustomerLoader;
import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import com.acmeair.web.dto.CustomerInfo;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.acmeair.entities.CustomerSession.SESSIONID_COOKIE_NAME;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static java.util.Collections.singletonList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AcmeAirApplication.class,
        webEnvironment = DEFINED_PORT,
        properties = {
                "spring.data.mongodb.host=localhost",
                "spring.data.mongodb.port=27017",
                "spring.data.mongodb.database=acmeair"
        })
public class AcmeAirApplicationTest {
    private static MongoClient      mongoClient;

    @ClassRule
    public static final WireMockRule wireMockRule = new WireMockRule(8082);

    private final ObjectMapper objectMapper   = new ObjectMapper();
    private final String       cookie         = SESSIONID_COOKIE_NAME + "=" + uniquify("session-id");
    private final String       toFlightId     = "toFlightId";
    private final String       toFlightSegId  = "toFlightSegId";
    private final String       retFlightId    = "retFlightId";
    private final String       retFlightSegId = "retFlightSegId";
    private final boolean      oneWay         = false;
    private final String       customerId     = "uid0@email.com";
    private final String       password       = "password";
    private final BookingImpl  booking        = new BookingImpl("1", new Date(), customerId, "SIN-2131");

    private final CustomerSessionInfo customerSession = new CustomerSessionInfo(
            "session-id-434200",
            "sean-123",
            new Date(),
            new Date()
    );

    private final CustomerInfo customerInfo = new CustomerInfo();

    @Value("${spring.data.mongodb.database}")
    private String                  databaseName;
    @Value("${spring.data.mongodb.port}")
    private int                     mongoDbPort;
    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    private CustomerLoader          customerLoader;
    @Autowired
    private TestRestTemplate        restTemplate;
    private MongoDatabase           database;

    @BeforeClass
    public static void setUpClass() throws Exception {
        mongoClient = new MongoClient("localhost", 27017);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        mongoClient.close();
    }

    @Before
    public void setUp() throws JsonProcessingException {
        customerInfo.setUsername(customerId);

        stubFor(post(urlEqualTo("/rest/api/login"))
                        .willReturn(
                                aResponse()
                                        .withStatus(SC_OK)
                                        .withHeader(SET_COOKIE, cookie)
                                        .withBody("logged in")));

        stubFor(post(urlEqualTo("/rest/api/login/validate"))
                        .willReturn(
                                aResponse()
                                        .withStatus(SC_OK)
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(customerSession))));

        stubFor(get(urlEqualTo("/rest/api/customer/" + customerId))
                        .willReturn(
                                aResponse()
                                        .withStatus(SC_OK)
                                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(objectMapper.writeValueAsString(customerSession))));


        mongoClient = new MongoClient("localhost", mongoDbPort);
        database = mongoClient.getDatabase(databaseName);

        addDocumentToCollection("booking", new HashMap<String, Object>() {{
            put("_id", booking.getBookingId());
            put("flightId", booking.getFlightId());
            put("customerId", booking.getCustomerId());
            put("dateOfBooking", booking.getDateOfBooking());
        }});

//        customerLoader.loadCustomers(1);

        addDocumentToCollection(
                "flight",
                new HashMap<String, Object>() {{
                    put("_id", toFlightId);
                }},
                new HashMap<String, Object>() {{
                    put("_id", retFlightId);
                }}
        );
    }

    @After
    public void tearDown() throws Exception {
        database.drop();
    }

    @Test
    public void checkBookingWithoutLoggedIn() {
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<BookingInfo> bookingInfoResponseEntity = restTemplate.exchange(
                "/bookings/rest/api/bookings/bybookingnumber/{userid}/{number}",
                GET,
                new HttpEntity<String>(headers),
                BookingInfo.class,
                booking.getCustomerId(),
                booking.getBookingId()
        );
        // Just make sure we need to login first
        assertThat(bookingInfoResponseEntity.getStatusCode(), is(FORBIDDEN));
    }

    @Test
    public void canRetrievedBookingInfoByNumberWhenLoggedIn() {
        HttpHeaders headers = headerWithCookieOfLoginSession();

        ResponseEntity<BookingInfo> bookingInfoResponseEntity = restTemplate.exchange(
                "/bookings/rest/api/bookings/bybookingnumber/{userid}/{number}",
                GET,
                new HttpEntity<String>(headers),
                BookingInfo.class,
                booking.getCustomerId(),
                booking.getBookingId()
        );

        assertThat(bookingInfoResponseEntity.getStatusCode(), is(OK));

        BookingInfo bookingInfo = bookingInfoResponseEntity.getBody();
        assertThat(bookingInfo.getBookingId(), is(booking.getBookingId()));
        assertThat(bookingInfo.getCustomerId(), is(booking.getCustomerId()));
        assertThat(bookingInfo.getFlightId(), is(booking.getFlightId()));
        assertThat(bookingInfo.getDateOfBooking(), is(booking.getDateOfBooking()));
    }

    @Test
    public void canBookFlightsWhenLoggedIn() {
        HttpHeaders headers = headerWithCookieOfLoginSession();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userid", customerId);
        map.add("toFlightId", toFlightId);
        map.add("toFlightSegId", toFlightSegId);
        map.add("retFlightId", retFlightId);
        map.add("retFlightSegId", retFlightSegId);
        map.add("oneWayFlight", String.valueOf(oneWay));

        ResponseEntity<BookingReceiptInfo> bookingInfoResponseEntity = restTemplate.exchange(
                "/bookings/rest/api/bookings/bookflights",
                POST,
                new HttpEntity<>(map, headers),
                BookingReceiptInfo.class
        );

        assertThat(bookingInfoResponseEntity.getStatusCode(), is(OK));

        BookingReceiptInfo bookingInfo = bookingInfoResponseEntity.getBody();

        assertThat(bookingInfo.isOneWay(), is(oneWay));
        assertThat(bookingInfo.getDepartBookingId(), is(notNullValue()));
        assertThat(bookingInfo.getReturnBookingId(), is(notNullValue()));
    }

    private HttpHeaders headerWithCookieOfLoginSession() {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "/customers/rest/api/login",
                loginRequest(customerId, password),
                String.class
        );

        assertThat(responseEntity.getStatusCode(), is(OK));

        List<String> cookies = responseEntity.getHeaders().get(SET_COOKIE);
        assertThat(cookies, contains(cookie));

        HttpHeaders headers = new HttpHeaders();
        headers.set(COOKIE, String.join(";", cookies));
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HttpEntity<MultiValueMap<String, String>> loginRequest(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("login", login);
        map.add("password", password);

        return new HttpEntity<>(map, headers);
    }

    @SafeVarargs
    private final void addDocumentToCollection(String collectionName, HashMap<String, Object>... maps) {
        database.createCollection(collectionName);
        Arrays.stream(maps).forEach(map -> database.getCollection(collectionName).insertOne(new Document(map)));
    }
}

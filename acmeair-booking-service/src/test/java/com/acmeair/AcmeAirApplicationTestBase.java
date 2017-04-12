package com.acmeair;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.morphia.repositories.BookingRepository;
import com.acmeair.morphia.repositories.FlightRepository;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import com.acmeair.web.dto.CustomerInfo;
import com.acmeair.web.dto.CustomerSessionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;

import static com.acmeair.entities.CustomerSession.SESSIONID_COOKIE_NAME;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class AcmeAirApplicationTestBase {
    @ClassRule
    public static final WireMockRule wireMockRule = new WireMockRule(8082);

    private static ConfigurableApplicationContext applicationContext;

    private final ObjectMapper objectMapper   = new ObjectMapper();
    private final String       cookie         = SESSIONID_COOKIE_NAME + "=" + uniquify("session-id");
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

    @Autowired
    private TestRestTemplate        restTemplate;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConfigurableApplicationContext context;

    private FlightImpl toFlight;
    private FlightImpl retFlight;

    @AfterClass
    public static void tearDownClass() throws Exception {
        applicationContext.close();
    }

    @Before
    public void setUp() throws JsonProcessingException {
        FixtureFactoryLoader.loadTemplates("com.acmeair.flight.templates");

        toFlight = Fixture.from(FlightImpl.class).gimme("valid");
        retFlight = Fixture.from(FlightImpl.class).gimme("valid");

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
                                        .withBody(objectMapper.writeValueAsString(customerInfo))));

        bookingRepository.save(booking);
        flightRepository.save(asList(toFlight, retFlight));
    }

    @After
    public void tearDown() throws Exception {
        bookingRepository.delete(booking);
        flightRepository.delete(toFlight);
        flightRepository.delete(retFlight);
        applicationContext = context;
    }

    @Test
    public void canRetrievedBookingInfoByNumberWhenLoggedIn() {
        HttpHeaders headers = headers();

        ResponseEntity<BookingInfo> bookingInfoResponseEntity = restTemplate.exchange(
                "/rest/api/bookings/bybookingnumber/{userid}/{number}",
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
        HttpHeaders headers = headers();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userid", customerId);
        map.add("toFlightId", toFlight.getFlightId());
        map.add("toFlightSegId", toFlight.getFlightSegmentId());
        map.add("retFlightId", retFlight.getFlightId());
        map.add("retFlightSegId", retFlight.getFlightSegmentId());
        map.add("oneWayFlight", String.valueOf(oneWay));

        ResponseEntity<BookingReceiptInfo> bookingInfoResponseEntity = restTemplate.exchange(
                "/rest/api/bookings/bookflights",
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

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}

package com.acmeair;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.loader.CustomerLoader;
import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.morphia.repositories.BookingRepository;
import com.acmeair.morphia.repositories.FlightRepository;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
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
                "spring.data.mongodb.host=localhost"
        })
@ActiveProfiles("mongodb")
public class AcmeAirApplicationIT {
    private final boolean oneWay         = false;

    private final String      customerId = "uid0@email.com";
    private final String      password   = "password";
    private final BookingImpl booking    = new BookingImpl("1", new Date(), customerId, "SIN-2131");

    @Autowired
    private CustomerLoader customerLoader;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private FlightImpl toFlight;
    private FlightImpl retFlight;

    @Before
    public void setUp() {
        FixtureFactoryLoader.loadTemplates("com.acmeair.flight.templates");

        toFlight = Fixture.from(FlightImpl.class).gimme("valid");
        retFlight = Fixture.from(FlightImpl.class).gimme("valid");

        customerLoader.loadCustomers(1);

        bookingRepository.save(booking);
        flightRepository.save(asList(toFlight, retFlight));
    }

    @Test
    public void countsNumberOfConsumers() {
        HttpHeaders headers = headerWithCookieOfLoginSession();

        ResponseEntity<Long> consumerCount = restTemplate.exchange(
                "/customers/rest/info/config/countCustomers",
                GET,
                new HttpEntity<>(headers),
                Long.class
        );

        assertThat(consumerCount.getStatusCode(), is(OK));
        assertThat(consumerCount.getBody(), is(1L));
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
        map.add("toFlightId", toFlight.getFlightId());
        map.add("toFlightSegId", toFlight.getFlightSegmentId());
        map.add("retFlightId", retFlight.getFlightId());
        map.add("retFlightSegId", retFlight.getFlightSegmentId());
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
        assertThat(cookies, is(notNullValue()));

        HttpHeaders headers = new HttpHeaders();
        headers.set(COOKIE, String.join(";", cookies));
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
}

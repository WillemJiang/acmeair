package com.acmeair.web;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.FlightRestTestApplication;
import com.acmeair.entities.Booking;
import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.service.BookingService;
import com.acmeair.service.FlightService;
import com.acmeair.service.UserService;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlightRestTestApplication.class)
@ActiveProfiles({"test", "dev"})
public class BookingsRESTTest {
    @MockBean
    private UserService    customerService;
    @MockBean
    private FlightService  flightService;
    @MockBean
    private BookingService bookingService;

    @Autowired
    private TestRestTemplate restTemplate;

    private Booking booking;

    private final String userId = uniquify("user-id");
    private final String bookingId = uniquify("booking-id");
    private final String toFlightSegId = uniquify("toFlightSegId");
    private final String retFlightSegId = uniquify("retFlightSegId");
    private final String toFlightId = uniquify("toFlightId");
    private final String retFlightId = uniquify("retFlightId");
    private final String toBookingId = uniquify("toBookingId");
    private final String retBookingId = uniquify("retBookingId");

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates("com.acmeair.booking.templates");

        booking = Fixture.from(BookingImpl.class).gimme("valid");
    }

    @Test
    public void getsBookingWithUnderlyingService() {
        when(bookingService.getBooking(userId, bookingId)).thenReturn(booking);

        ResponseEntity<BookingInfo> responseEntity = restTemplate.getForEntity(
                "/rest/api/bookings/bybookingnumber/{userid}/{number}",
                BookingInfo.class,
                userId,
                bookingId);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(new BookingInfo(booking)));
    }

    @Test
    public void getsAllBookingOfSpecifiedUser() {
        when(bookingService.getBookingsByUser(userId)).thenReturn(singletonList(booking));

        ResponseEntity<BookingInfo[]> responseEntity = restTemplate.getForEntity(
                "/rest/api/bookings/byuser/{user}",
                BookingInfo[].class,
                userId);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        assertThat(asList(responseEntity.getBody()), contains(new BookingInfo(booking)));
    }

    @Test
    public void cancelsBooking() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("number", bookingId);
        form.add("userid", userId);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/rest/api/bookings/cancelbooking",
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                String.class
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is("booking " + bookingId + " deleted."));
        verify(bookingService).cancelBooking(userId, bookingId);
    }

    @Test
    public void booksFlightWithUnderlyingService() {
        when(bookingService.bookFlight(userId, toFlightSegId, toFlightId)).thenReturn(toBookingId);
        when(bookingService.bookFlight(userId, retFlightSegId, retFlightId)).thenReturn(retBookingId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userid", userId);
        form.add("toFlightId", toFlightId);
        form.add("toFlightSegId", toFlightSegId);
        form.add("retFlightId", retFlightId);
        form.add("retFlightSegId", retFlightSegId);
        form.add("oneWayFlight", String.valueOf(false));

        ResponseEntity<BookingReceiptInfo> responseEntity = restTemplate.exchange(
                "/rest/api/bookings/bookflights",
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                BookingReceiptInfo.class
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        BookingReceiptInfo receiptInfo = responseEntity.getBody();

        assertThat(receiptInfo.getDepartBookingId(), is(toBookingId));
        assertThat(receiptInfo.getReturnBookingId(), is(retBookingId));
        assertThat(receiptInfo.isOneWay(), is(false));
    }
}

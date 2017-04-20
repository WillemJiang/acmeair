package com.acmeair.web;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.FlightRestTestApplication;
import com.acmeair.entities.Flight;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.service.FlightService;
import com.acmeair.service.UserService;
import com.acmeair.web.dto.FlightInfo;
import com.acmeair.web.dto.TripFlightOptions;
import com.acmeair.web.dto.TripLegInfo;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlightRestTestApplication.class)
@ActiveProfiles({"test", "dev"})
public class FlightsRESTTest {

    @MockBean
    private UserService   customerService;
    @MockBean
    private FlightService flightService;

    @Autowired
    private TestRestTemplate restTemplate;

    private final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    private final Calendar calendar = Calendar.getInstance();

    private Flight departFlight;
    private Flight returnFlight;

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates("com.acmeair.flight.templates");

        departFlight = Fixture.from(FlightImpl.class).gimme("valid");
        returnFlight = Fixture.from(FlightImpl.class).gimme("valid");

        trimMillis(departFlight.getScheduledDepartureTime());
        trimMillis(returnFlight.getScheduledDepartureTime());
    }

    @Test
    public void getsFlightsWithUnderlyingService() {
        when(flightService.getFlightByAirportsAndDepartureDate(
                departFlight.getFlightSegment().getOriginPort(),
                departFlight.getFlightSegment().getDestPort(),
                departFlight.getScheduledDepartureTime())).thenReturn(singletonList(departFlight));

        when(flightService.getFlightByAirportsAndDepartureDate(
                departFlight.getFlightSegment().getDestPort(),
                departFlight.getFlightSegment().getOriginPort(),
                returnFlight.getScheduledDepartureTime())).thenReturn(singletonList(returnFlight));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("fromAirport", departFlight.getFlightSegment().getOriginPort());
        form.add("toAirport", departFlight.getFlightSegment().getDestPort());
        form.add("fromDate", dateFormat.format(departFlight.getScheduledDepartureTime()));
        form.add("returnDate", dateFormat.format(returnFlight.getScheduledDepartureTime()));
        form.add("oneWay", String.valueOf(false));

        ResponseEntity<TripFlightOptions> responseEntity = restTemplate.exchange(
                "/rest/api/flights/queryflights",
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                TripFlightOptions.class
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        TripFlightOptions flightOptions = responseEntity.getBody();
        assertThat(flightOptions.getTripLegs(), is(2));

        List<TripLegInfo> tripFlights = flightOptions.getTripFlights();
        assertThat(tripFlights.get(0).getFlightsOptions(), contains(toFlightInfo(departFlight)));
        assertThat(tripFlights.get(1).getFlightsOptions(), contains(toFlightInfo(returnFlight)));
    }

    @Test
    public void browsesFlightsWithUnderlyingService() {
        when(flightService.getFlightByAirports(
                departFlight.getFlightSegment().getOriginPort(),
                departFlight.getFlightSegment().getDestPort())).thenReturn(singletonList(departFlight));

        when(flightService.getFlightByAirports(
                departFlight.getFlightSegment().getDestPort(),
                departFlight.getFlightSegment().getOriginPort())).thenReturn(singletonList(returnFlight));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("fromAirport", departFlight.getFlightSegment().getOriginPort());
        form.add("toAirport", departFlight.getFlightSegment().getDestPort());
        form.add("oneWay", String.valueOf(false));

        ResponseEntity<TripFlightOptions> responseEntity = restTemplate.exchange(
                "/rest/api/flights/browseflights",
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                TripFlightOptions.class
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        TripFlightOptions flightOptions = responseEntity.getBody();
        assertThat(flightOptions.getTripLegs(), is(2));

        List<TripLegInfo> tripFlights = flightOptions.getTripFlights();
        assertThat(tripFlights.get(0).getFlightsOptions(), contains(toFlightInfo(departFlight)));
        assertThat(tripFlights.get(1).getFlightsOptions(), contains(toFlightInfo(returnFlight)));
    }

    private FlightInfo toFlightInfo(Flight departFlight) {
        return new FlightInfo(departFlight);
    }

    private void trimMillis(Date time) {
        calendar.setTime(time);
        calendar.set(Calendar.MILLISECOND, 0);
        time.setTime(calendar.getTimeInMillis());
    }
}

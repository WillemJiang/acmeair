package com.acmeair.morphia.services;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Flight;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.service.KeyGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;

import java.util.Date;
import java.util.List;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceImplTest {
    private final Query query    = Mockito.mock(Query.class);
    private final FieldEnd<Query> fieldEnd = Mockito.mock(FieldEnd.class);

    @Mock
    private Datastore datastore;

    @Mock
    private KeyGenerator keyGenerator;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight flight;

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates("com.acmeair.flight.templates");

        flight = Fixture.from(FlightImpl.class).gimme("valid");

        when(keyGenerator.generate()).thenReturn(flight.getFlightId());

        when(datastore.find(any())).thenReturn(query);
        when(query.field(anyString())).thenReturn(fieldEnd);
        when(query.disableValidation()).thenReturn(query);
        when(fieldEnd.equal(any())).thenReturn(query);
    }

    @Test
    public void getsFlightByIdFromDatabase() {
        when(query.get()).thenReturn(flight);

        Flight actual = flightService.getFlightByFlightId(flight.getFlightId(), flight.getFlightSegmentId());

        assertThat(actual, is(flight));
    }

    @Test
    public void getsEmptyListIfNoFlightAvailableOnRequestedAirports() {
        List<Flight> flights = flightService.getFlightByAirportsAndDepartureDate(uniquify("unknown airport"), uniquify("unknown airport"), new Date());
        assertThat(flights.isEmpty(), is(true));

        flights = flightService.getFlightByAirports(uniquify("unknown airport"), uniquify("unknown airport"));
        assertThat(flights.isEmpty(), is(true));
    }

    @Test
    public void getsEmptyListIfNoFlightAvailableOnRequestedDate() {
        when(query.get()).thenReturn(flight.getFlightSegment());

        List<Flight> flights = flightService.getFlightByAirportsAndDepartureDate(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                new Date());

        assertThat(flights.isEmpty(), is(true));
    }

    @Test
    public void getsFlightsOnRequestedAirportAndDate() {
        when(query.get()).thenReturn(flight.getFlightSegment());
        when(query.asList()).thenReturn(singletonList(flight));

        List<Flight> flights = flightService.getFlightByAirportsAndDepartureDate(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                new Date());

        assertThat(flights, contains(flight));
    }

    @Test
    public void getsFlightsOnRequestedAirport() {
        when(query.get()).thenReturn(flight.getFlightSegment());
        when(query.asList()).thenReturn(singletonList(flight));

        List<Flight> flights = flightService.getFlightByAirports(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort());

        assertThat(flights, contains(flight));
    }


    @Test
    public void createsNewFlight() {
        Flight newFlight = flightService.createNewFlight(
                flight.getFlightSegmentId(),
                flight.getScheduledDepartureTime(),
                flight.getScheduledArrivalTime(),
                flight.getFirstClassBaseCost(),
                flight.getEconomyClassBaseCost(),
                flight.getNumFirstClassSeats(),
                flight.getNumEconomyClassSeats(),
                flight.getAirplaneTypeId()
        );

        assertThat(newFlight.getFlightId(), is(flight.getFlightId()));
        assertThat(newFlight.getFlightSegmentId(), is(flight.getFlightSegmentId()));
        assertThat(newFlight.getScheduledDepartureTime(), is(flight.getScheduledDepartureTime()));
        assertThat(newFlight.getScheduledArrivalTime(), is(flight.getScheduledArrivalTime()));
        assertThat(newFlight.getFirstClassBaseCost(), is(flight.getFirstClassBaseCost()));
        assertThat(newFlight.getEconomyClassBaseCost(), is(flight.getEconomyClassBaseCost()));
        assertThat(newFlight.getNumFirstClassSeats(), is(flight.getNumFirstClassSeats()));

        verify(datastore).save(newFlight);
    }

    @Test
    public void savesFlightSegmentToDatabase() {
        flightService.storeFlightSegment(
                flight.getFlightSegment().getFlightName(),
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                flight.getFlightSegment().getMiles()
        );

        verify(datastore).save(flight.getFlightSegment());
    }
}
package com.acmeair.morphia.services;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.acmeair.entities.Flight;
import com.acmeair.morphia.entities.FlightImpl;
import com.acmeair.morphia.entities.FlightSegmentImpl;
import com.acmeair.morphia.repositories.FlightRepository;
import com.acmeair.morphia.repositories.FlightSegmentRepository;
import com.acmeair.service.KeyGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceImplTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private FlightSegmentRepository segmentRepository;

    @Mock
    private KeyGenerator keyGenerator;

    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightImpl flight;

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates("com.acmeair.flight.templates");

        flight = Fixture.from(FlightImpl.class).gimme("valid");

        when(keyGenerator.generate()).thenReturn(flight.getFlightId());
    }

    @Test
    public void getsFlightByIdFromDatabase() {
        when(flightRepository.findOne(flight.getFlightId())).thenReturn(flight);

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
        when(segmentRepository.findByOriginPortAndDestPort(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort()
        )).thenReturn(((FlightSegmentImpl) flight.getFlightSegment()));

        List<Flight> flights = flightService.getFlightByAirportsAndDepartureDate(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                new Date());

        assertThat(flights.isEmpty(), is(true));
    }

    @Test
    public void getsFlightsOnRequestedAirportAndDate() {
        when(segmentRepository.findByOriginPortAndDestPort(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort()
        )).thenReturn(((FlightSegmentImpl) flight.getFlightSegment()));

        when(flightRepository.findByFlightSegmentIdAndScheduledDepartureTime(
                flight.getFlightSegment().getFlightName(),
                flight.getScheduledDepartureTime()
        )).thenReturn(singletonList(flight));

        List<Flight> flights = flightService.getFlightByAirportsAndDepartureDate(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                flight.getScheduledDepartureTime());

        assertThat(flights, contains(flight));
    }

    @Test
    public void getsFlightsOnRequestedAirport() {
        when(segmentRepository.findByOriginPortAndDestPort(
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort()
        )).thenReturn(((FlightSegmentImpl) flight.getFlightSegment()));

        when(flightRepository.findByFlightSegmentId(flight.getFlightSegment().getFlightName())).thenReturn(singletonList(flight));

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

        verify(flightRepository).save((FlightImpl) newFlight);
    }

    @Test
    public void savesFlightSegmentToDatabase() {
        flightService.storeFlightSegment(
                flight.getFlightSegment().getFlightName(),
                flight.getFlightSegment().getOriginPort(),
                flight.getFlightSegment().getDestPort(),
                flight.getFlightSegment().getMiles()
        );

        verify(segmentRepository).save((FlightSegmentImpl) flight.getFlightSegment());
    }
}

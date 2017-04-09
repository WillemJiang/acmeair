package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.FlightImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends MongoRepository<FlightImpl, String> {
    List<FlightImpl> findByFlightSegmentId(String flightSegmentId);

    List<FlightImpl> findByFlightSegmentIdAndScheduledDepartureTime(String flightSegmentId, Date scheduledDepartureTime);
}

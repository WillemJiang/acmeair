package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.FlightSegmentImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlightSegmentRepository extends MongoRepository<FlightSegmentImpl, String> {
    FlightSegmentImpl findByOriginPortAndDestPort(String originPort, String destPort);
}

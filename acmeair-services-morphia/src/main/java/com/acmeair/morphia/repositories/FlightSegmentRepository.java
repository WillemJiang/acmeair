package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.FlightSegmentImpl;
import org.springframework.data.repository.CrudRepository;

public interface FlightSegmentRepository extends CrudRepository<FlightSegmentImpl, String> {
    FlightSegmentImpl findByOriginPortAndDestPort(String originPort, String destPort);
}

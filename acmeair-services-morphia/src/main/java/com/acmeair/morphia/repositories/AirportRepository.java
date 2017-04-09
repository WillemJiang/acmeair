package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.AirportCodeMappingImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirportRepository extends MongoRepository<AirportCodeMappingImpl, String> {
}

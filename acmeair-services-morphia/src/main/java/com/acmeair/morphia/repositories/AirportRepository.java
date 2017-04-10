package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.AirportCodeMappingImpl;
import org.springframework.data.repository.CrudRepository;

public interface AirportRepository extends CrudRepository<AirportCodeMappingImpl, String> {
}

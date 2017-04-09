package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerSessionImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerSessionRepository extends MongoRepository<CustomerSessionImpl, String> {

}

package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerImpl, String> {

}

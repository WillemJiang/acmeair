package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongodb")
interface CustomerMongoRepository extends CustomerRepository, MongoRepository<CustomerImpl, String> {
}

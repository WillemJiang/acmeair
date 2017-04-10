package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerSessionImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongodb")
interface CustomerSessionMongoRepository extends CustomerSessionRepository, MongoRepository<CustomerSessionImpl, String> {
}

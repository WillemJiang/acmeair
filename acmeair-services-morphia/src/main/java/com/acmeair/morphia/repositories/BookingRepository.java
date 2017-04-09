package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.BookingImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<BookingImpl, String> {
    List<BookingImpl> findByCustomerId(String customerId);
}

package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.BookingImpl;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<BookingImpl, String> {
    List<BookingImpl> findByCustomerId(String customerId);
}

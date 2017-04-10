package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerImpl;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerImpl, String> {

}

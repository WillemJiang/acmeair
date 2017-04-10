package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CustomerSessionImpl;
import org.springframework.data.repository.CrudRepository;

public interface CustomerSessionRepository extends CrudRepository<CustomerSessionImpl, String> {

}

package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CouponImpl;
import org.springframework.data.repository.CrudRepository;

public interface CouponRepository extends CrudRepository<CouponImpl, String> {

}
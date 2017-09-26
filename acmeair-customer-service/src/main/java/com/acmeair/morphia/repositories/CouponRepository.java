package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.CouponImpl;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CouponRepository extends CrudRepository<CouponImpl, Integer> {
    CouponImpl findTopByCustomerIdOrderByIdDesc(String customerId);

    List<CouponImpl> findByCustomerIdAndIsUsed(String customerId, boolean isUsed);
}

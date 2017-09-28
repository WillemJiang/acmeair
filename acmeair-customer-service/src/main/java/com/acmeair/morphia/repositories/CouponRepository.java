package com.acmeair.morphia.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.acmeair.morphia.entities.CouponImpl;

public interface CouponRepository extends CrudRepository<CouponImpl, Integer> {
  CouponImpl findTopByCustomerIdOrderByIdDesc(String customerId);

  List<CouponImpl> findByCustomerIdAndIsUsed(String customerId, boolean isUsed);
}

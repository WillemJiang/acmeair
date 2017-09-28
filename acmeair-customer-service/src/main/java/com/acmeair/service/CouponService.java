package com.acmeair.service;

import java.util.List;

import com.acmeair.morphia.entities.CouponImpl;

public interface CouponService {
  void syncCoupons(String username);

  List<CouponImpl> getCoupons(String username);

  void useCoupon(int couponId);
}

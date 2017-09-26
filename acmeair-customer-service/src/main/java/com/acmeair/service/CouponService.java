package com.acmeair.service;

import com.acmeair.morphia.entities.CouponImpl;

import java.util.List;

public interface CouponService {
    void syncCoupons(String username);

    List<CouponImpl> getCoupons(String username);

    void useCoupon(int couponId);
}

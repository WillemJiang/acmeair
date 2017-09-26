package com.acmeair.service;

import com.acmeair.web.dto.CouponInfo;

import java.util.List;

public interface SeckillService {
    List<CouponInfo> getCoupons(String username);
}

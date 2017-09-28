package com.acmeair.service;

import java.util.List;

import com.acmeair.web.dto.CouponInfo;

public interface SeckillService {
  List<CouponInfo> getCoupons(String username);
}

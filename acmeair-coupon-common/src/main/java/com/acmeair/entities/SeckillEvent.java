package com.acmeair.entities;

import java.util.Date;

public interface SeckillEvent {
  String getCouponId();
  String getEventType();
  Date getSeckillTime();
  String getUser();
  double getDiscount();
}

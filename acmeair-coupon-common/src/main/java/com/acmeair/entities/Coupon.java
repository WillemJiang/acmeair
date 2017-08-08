package com.acmeair.entities;

import java.util.Date;

public interface Coupon {

  String getCouponId();

  Date getStartTime();
  Date getEndTime();

  long getTotalTicketCount();
  double getDiscount();

  //for seckill
  SeckillResultCode decreaseLeftTicket();

  //for query
  long getLeftTicketCount();
}

package com.acmeair.entities;

import java.util.Date;

public interface Ticket {
  String getTicketId();
  String getCouponId();
  Date getSeckillTime();
  String getUser();
  double getDiscount();

  boolean getUsed();
  void setUsed(boolean value);
}

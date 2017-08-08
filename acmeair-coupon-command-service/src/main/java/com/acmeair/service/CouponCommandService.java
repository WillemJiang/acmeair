package com.acmeair.service;

import com.acmeair.entities.Coupon;
import javax.inject.Inject;

public abstract class CouponCommandService {

  @Inject
  protected KeyGenerator keyGenerator;

  public abstract Coupon createCoupon(long numTickets,double discount);

  public abstract boolean doSeckill(String user);
}

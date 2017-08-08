package com.acmeair.service;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.Ticket;
import java.util.List;
import javax.inject.Inject;

public abstract class CouponQueryService {
  @Inject
  protected KeyGenerator keyGenerator;

  public abstract Coupon queryCoupon();
  public abstract List<Ticket> queryTickets(String user,boolean onlyUnused);
}

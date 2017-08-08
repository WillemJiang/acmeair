package com.acmeair.service;

import com.acmeair.entities.Coupon;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class CouponController {

  private Coupon current = null;

  public synchronized void startCoupon(Coupon coupon) {
    this.stopCoupon();
    current = coupon;
  }

  public synchronized void stopCoupon() {
    if(current != null) {
      //close old coupon
      current = null;
    }
  }

  public synchronized Coupon getCurrent() {
    return current;
  }

  //maybe no need
  public synchronized void decrease() {
    if(current != null){
      current.decreaseLeftTicket();
    }

  }
}

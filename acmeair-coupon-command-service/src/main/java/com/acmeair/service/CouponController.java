package com.acmeair.service;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.SeckillResult;
import com.acmeair.entities.SeckillResultCode;
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

  public synchronized SeckillResult doSeckill() {
    if(current != null) {
      return new SeckillResult(current.decreaseLeftTicket(),current);
    }
    else {
      return new SeckillResult(SeckillResultCode.Failed,null);
    }
  }
}

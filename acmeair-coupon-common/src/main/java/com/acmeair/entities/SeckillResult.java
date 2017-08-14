package com.acmeair.entities;

public class SeckillResult {
  private SeckillResultCode code = SeckillResultCode.Failed;
  private Coupon coupon = null;

  public SeckillResultCode getCode() {
    return code;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public SeckillResult(SeckillResultCode code, Coupon coupon) {
    this.code = code;
    this.coupon = coupon;
  }
}

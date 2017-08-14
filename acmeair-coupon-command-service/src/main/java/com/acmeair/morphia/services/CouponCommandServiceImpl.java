package com.acmeair.morphia.services;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.SeckillResult;
import com.acmeair.entities.SeckillResultCode;
import com.acmeair.entities.SeckillEvent;
import com.acmeair.morphia.entities.CouponImpl;
import com.acmeair.morphia.entities.SeckillEventImpl;
import com.acmeair.morphia.repositories.CouponRepository;
import com.acmeair.service.CouponCommandService;
import com.acmeair.service.CouponController;
import com.acmeair.service.EventSourcingRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponCommandServiceImpl extends CouponCommandService {

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private CouponController couponController;

  @Autowired
  private EventSourcingRegisterService eventSourcingService;

  @Override
  public Coupon createCoupon(long numTickets, double discount) {
    String id = keyGenerator.generate().toString();
    CouponImpl coupon = new CouponImpl(id,numTickets,discount);
    couponRepository.save(coupon);

    //add start event
    SeckillEvent ticket = new SeckillEventImpl(coupon.getCouponId(), SeckillEventImpl.Event_Type_CouponStarted, null,coupon.getDiscount());
    eventSourcingService.addEvent(ticket);

    couponController.startCoupon(coupon);

    return coupon;
  }

  @Override
  public boolean doSeckill(String user) {
    SeckillResult result = couponController.doSeckill();
    if (!result.getCode().equals(SeckillResultCode.Failed)) {
      //add user kill success event
      SeckillEvent ticket = new SeckillEventImpl(result.getCoupon().getCouponId(), SeckillEventImpl.Event_Type_KillSuccessed, user,result.getCoupon().getDiscount());
      eventSourcingService.addEvent(ticket);

      //add coupon finish event
      if(result.getCode().equals(SeckillResultCode.Finish)) {
        ticket = new SeckillEventImpl(result.getCoupon().getCouponId(), SeckillEventImpl.Event_Type_CouponFinish, null,result.getCoupon().getDiscount());
        eventSourcingService.addEvent(ticket);

        couponController.stopCoupon();
      }

      return true;
    } else {
      return false;
    }
  }
}

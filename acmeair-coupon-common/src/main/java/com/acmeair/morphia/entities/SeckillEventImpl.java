package com.acmeair.morphia.entities;

import com.acmeair.entities.SeckillEvent;
import java.io.Serializable;
import java.util.Date;

public class SeckillEventImpl implements SeckillEvent,Serializable {

  public static String Event_Type_KillSuccessed = "kill_successed";
  public static String Event_Type_CouponStarted = "coupon_started";
  public static String Event_Type_CouponFinish = "coupon_finish";

  private String douponid = null;
  private Date seckilltime;
  private String eventtype = null;
  private String user = null;
  private double discount = 1;

  @Override
  public String getCouponId() {
    return douponid;
  }

  @Override
  public String getEventType() {
    return eventtype;
  }

  @Override
  public Date getSeckillTime() {
    return seckilltime;
  }

  @Override
  public String getUser() {
    return user;
  }

  @Override
  public double getDiscount() {
    return discount;
  }

  public SeckillEventImpl(String douponId,String eventType, String user, double discount) {
    this.douponid = douponId;
    this.eventtype = eventType;
    this.seckilltime = new Date(System.currentTimeMillis());
    this.user = user;
    this.discount = discount;
  }

  @Override
  public String toString() {
    return "SeckillEventImpl{" +
        "douponid='" + douponid + '\'' +
        ", seckilltime=" + seckilltime +
        ", eventtype='" + eventtype + '\'' +
        ", user='" + user + '\'' +
        ", discount=" + discount +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SeckillEventImpl that = (SeckillEventImpl) o;

    if (Double.compare(that.discount, discount) != 0) {
      return false;
    }
    if (douponid != null ? !douponid.equals(that.douponid) : that.douponid != null) {
      return false;
    }
    if (seckilltime != null ? !seckilltime.equals(that.seckilltime) : that.seckilltime != null) {
      return false;
    }
    if (eventtype != null ? !eventtype.equals(that.eventtype) : that.eventtype != null) {
      return false;
    }
    return user != null ? user.equals(that.user) : that.user == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = douponid != null ? douponid.hashCode() : 0;
    result = 31 * result + (seckilltime != null ? seckilltime.hashCode() : 0);
    result = 31 * result + (eventtype != null ? eventtype.hashCode() : 0);
    result = 31 * result + (user != null ? user.hashCode() : 0);
    temp = Double.doubleToLongBits(discount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}

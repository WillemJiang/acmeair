package com.acmeair.web.dto;

import com.acmeair.entities.Coupon;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name="Coupon")
public class CouponInfo {

  @XmlElement(name = "id")
  private String id;

  @XmlElement(name = "start_time")
  private Date starttime;

  @XmlElement(name = "end_time")
  private Date endtime;

  @XmlElement(name = "total_count")
  private long totalcount;

  @XmlElement(name = "discount")
  private double discount;

  public String getId() {
    return id;
  }

  public Date getStarttime() {
    return starttime;
  }

  public Date getEndtime() {
    return endtime;
  }

  public long getTotalcount() {
    return totalcount;
  }

  public double getDiscount() {
    return discount;
  }

  public CouponInfo() {
  }

  public CouponInfo(Coupon coupon) {
    this.id = coupon.getCouponId();
    this.starttime = coupon.getStartTime();
    this.endtime = coupon.getEndTime();
    this.totalcount = coupon.getTotalTicketCount();
    this.discount = coupon.getDiscount();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CouponInfo that = (CouponInfo) o;

    if (totalcount != that.totalcount) {
      return false;
    }
    if (Double.compare(that.discount, discount) != 0) {
      return false;
    }
    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (starttime != null ? !starttime.equals(that.starttime) : that.starttime != null) {
      return false;
    }
    return endtime != null ? endtime.equals(that.endtime) : that.endtime == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id != null ? id.hashCode() : 0;
    result = 31 * result + (starttime != null ? starttime.hashCode() : 0);
    result = 31 * result + (endtime != null ? endtime.hashCode() : 0);
    result = 31 * result + (int) (totalcount ^ (totalcount >>> 32));
    temp = Double.doubleToLongBits(discount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}

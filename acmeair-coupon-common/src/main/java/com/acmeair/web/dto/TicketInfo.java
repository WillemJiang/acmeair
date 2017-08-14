package com.acmeair.web.dto;

import com.acmeair.entities.Ticket;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "Ticket")
public class TicketInfo {

  @XmlElement(name = "id")
  private String id;


  @XmlElement(name = "coupon_id")
  private String couponid;

  @XmlElement(name = "seckill_time")
  private Date seckilltime;


  @XmlElement(name = "user")
  private String user;


  @XmlElement(name = "discount")
  private double discount;


  @XmlElement(name = "used")
  private boolean used = false;

  public String getId() {
    return id;
  }

  public String getCouponid() {
    return couponid;
  }

  public Date getSeckilltime() {
    return seckilltime;
  }

  public String getUser() {
    return user;
  }

  public double getDiscount() {
    return discount;
  }

  public boolean isUsed() {
    return used;
  }

  public TicketInfo() {
  }

  public TicketInfo(Ticket ticket) {
    this.id = ticket.getTicketId();
    this.couponid = ticket.getCouponId();
    this.seckilltime = ticket.getSeckillTime();
    this.user = ticket.getUser();
    this.used = ticket.getUsed();
    this.discount = ticket.getDiscount();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TicketInfo that = (TicketInfo) o;

    if (Double.compare(that.discount, discount) != 0) {
      return false;
    }
    if (used != that.used) {
      return false;
    }
    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (couponid != null ? !couponid.equals(that.couponid) : that.couponid != null) {
      return false;
    }
    if (seckilltime != null ? !seckilltime.equals(that.seckilltime) : that.seckilltime != null) {
      return false;
    }
    return user != null ? user.equals(that.user) : that.user == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id != null ? id.hashCode() : 0;
    result = 31 * result + (couponid != null ? couponid.hashCode() : 0);
    result = 31 * result + (seckilltime != null ? seckilltime.hashCode() : 0);
    result = 31 * result + (user != null ? user.hashCode() : 0);
    temp = Double.doubleToLongBits(discount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (used ? 1 : 0);
    return result;
  }
}

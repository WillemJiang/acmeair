package com.acmeair.morphia.entities;

import com.acmeair.entities.Ticket;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket")
@Entity(name = "ticket")
public class TicketImpl implements Ticket,Serializable {

  @Id
  @Column(name = "id")
  private String id;
  @Override
  public String getTicketId() {
    return id;
  }

  @Column(name = "coupon_id")
  private String couponid;
  @Override
  public String getCouponId() {
    return couponid;
  }

  @Column(name = "seckill_time")
  private Date seckilltime;
  @Override
  public Date getSeckillTime() {
    return seckilltime;
  }

  @Column(name = "user")
  private String user;
  @Override
  public String getUser() {
    return user;
  }

  @Column(name = "discount")
  private double discount;
  @Override
  public double getDiscount() {
    return discount;
  }

  @Column(name = "used")
  private boolean used = false;
  @Override
  public boolean getUsed() {
    return used;
  }

  @Override
  public void setUsed(boolean value) {
    used = value;
  }

  public TicketImpl() { }

  public TicketImpl(String id,String couponId, Date seckillTime, String user, double discount) {
    this.id = id;
    this.couponid = couponId;
    this.seckilltime = seckillTime;
    this.user = user;
    this.discount = discount;
  }
}

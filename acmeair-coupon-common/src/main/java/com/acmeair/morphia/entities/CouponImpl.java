package com.acmeair.morphia.entities;

import com.acmeair.entities.Coupon;
import com.acmeair.entities.SeckillResultCode;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coupon")
@Entity(name = "coupon")
public class CouponImpl implements Coupon, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  private String id;

  @Override
  public String getCouponId() {
    return id;
  }

  @Column(name = "start_time")
  private Date starttime;

  @Override
  public Date getStartTime() {
    return starttime;
  }

  @Column(name = "end_time")
  private Date endtime;

  @Override
  public Date getEndTime() {
    return endtime;
  }

  @Column(name = "total_count")
  private long totalcount;

  @Override
  public long getTotalTicketCount() {
    return totalcount;
  }

  @Column(name = "discount")
  private double discount;

  @Override
  public double getDiscount() {
    return discount;
  }

  public CouponImpl(){}

  public CouponImpl(String id, long totalcount, double discount) {
    this.id = id;
    this.totalcount = totalcount;
    this.discount = discount;

    //auto start
    this.starttime = new Date(System.currentTimeMillis());
    //full count start
    this.leftcount = new AtomicLong(totalcount);
  }

  @Column(name = "left_count")
  private AtomicLong leftcount;

  @Override
  public long getLeftTicketCount() {
    return leftcount.get();
  }

  @Override
  public SeckillResultCode decreaseLeftTicket() {
    long current = leftcount.decrementAndGet();
    if (current > 0) {
      return SeckillResultCode.Success;
    } else if(current == 0) {
      return SeckillResultCode.Finish;
    } else {
      return SeckillResultCode.Failed;
    }
  }
}

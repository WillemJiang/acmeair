package com.acmeair.morphia.entities;

import com.acmeair.entities.Coupon;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Document(collection = "coupon")
@Entity(name = "coupon")
public class CouponImpl implements Coupon, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private int id;
    private String promotionId;
    private Date time;
    private float discount;
    private String customerId;

    private boolean isUsed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public CouponImpl(){
    }

    public CouponImpl(int id, String promotionId, Date time, float discount, String customerId, boolean isUsed) {
        this.id = id;
        this.promotionId = promotionId;
        this.time = time;
        this.discount = discount;
        this.customerId = customerId;
        this.isUsed = isUsed;
    }
}

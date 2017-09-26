package com.acmeair.entities;

import java.util.Date;

public interface Coupon {

    int getId();

    void setId(int id);

    String getPromotionId();

    void setPromotionId(String promotionId);

    Date getTime();

    void setTime(Date time);

    float getDiscount();

    void setDiscount(float discount);

    String getCustomerId();

    void setCustomerId(String customerId);

    boolean isUsed();

    void setUsed(boolean used);
}

package com.acmeair.web.dto;

import com.acmeair.entities.CustomerSession;

import java.util.Date;
import java.util.Objects;

public class CustomerSessionInfo implements CustomerSession {
    private String id;
    private String customerid;
    private Date   lastAccessedTime;
    private Date   timeoutTime;

    public CustomerSessionInfo() {
    }

    public CustomerSessionInfo(CustomerSession customerSession) {
        this(
                customerSession.getId(),
                customerSession.getCustomerid(),
                customerSession.getLastAccessedTime(),
                customerSession.getTimeoutTime()
        );
    }

    public CustomerSessionInfo(String id, String customerid, Date lastAccessedTime, Date timeoutTime) {
        this.id = id;
        this.customerid = customerid;
        this.lastAccessedTime = lastAccessedTime;
        this.timeoutTime = timeoutTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCustomerid() {
        return customerid;
    }

    @Override
    public Date getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public Date getTimeoutTime() {
        return timeoutTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public void setLastAccessedTime(Date lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public void setTimeoutTime(Date timeoutTime) {
        this.timeoutTime = timeoutTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerSessionInfo that = (CustomerSessionInfo) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(customerid, that.customerid) &&
               Objects.equals(lastAccessedTime, that.lastAccessedTime) &&
               Objects.equals(timeoutTime, that.timeoutTime);
    }

    @Override
    public String toString() {
        return "CustomerSessionInfo{" +
               "id='" + id + '\'' +
               ", customerid='" + customerid + '\'' +
               ", lastAccessedTime=" + lastAccessedTime +
               ", timeoutTime=" + timeoutTime +
               '}';
    }
}

package com.acmeair.entities;

public class TokenInfo {
    private String sessionid;

    public TokenInfo(String sessionid) {
        this.sessionid = sessionid;

    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

}

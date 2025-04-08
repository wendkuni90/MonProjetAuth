package com.elieltech.authenticator.models;

public class LogoutRequest {
    private String refresh;

    public LogoutRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
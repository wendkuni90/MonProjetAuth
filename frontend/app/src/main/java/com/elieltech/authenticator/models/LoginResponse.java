package com.elieltech.authenticator.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("refresh_token")
    private String refresh;
    @SerializedName("access_token")
    private String access;

    public String getRefresh() {
        return refresh;
    }

    public String getAccess() {
        return access;
    }
}

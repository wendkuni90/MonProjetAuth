package com.elieltech.authenticator.api;

import com.elieltech.authenticator.models.RegisterRequest;
import com.elieltech.authenticator.models.RegisterResponse;

import com.elieltech.authenticator.models.LoginRequest;
import com.elieltech.authenticator.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("api/register/")
    public Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("api/login/")
    public Call<LoginResponse> loginUser(@Body LoginRequest request);
}

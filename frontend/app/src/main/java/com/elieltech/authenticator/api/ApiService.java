package com.elieltech.authenticator.api;

import com.elieltech.authenticator.models.LogoutRequest;

import com.elieltech.authenticator.models.RegisterRequest;
import com.elieltech.authenticator.models.RegisterResponse;

import com.elieltech.authenticator.models.LoginRequest;
import com.elieltech.authenticator.models.LoginResponse;

import com.elieltech.authenticator.models.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface ApiService {
    @POST("api/register/")
    public Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("api/login/")
    public Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET("api/me-infos/")
    public Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);

    @POST("api/logout/")
    public Call<Void> logoutUser(
            @Header("Authorization") String token,
            @Body LogoutRequest logoutRequest
    );

}

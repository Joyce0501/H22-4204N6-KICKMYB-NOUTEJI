package com.example.kickmyb.http;

import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @POST("/api/id/signup")
    Call<SigninResponse> inscription(@Body SignupRequest s);
}

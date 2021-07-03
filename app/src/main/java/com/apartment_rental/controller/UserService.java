package com.apartment_rental.controller;

import com.apartment_rental.model.LoginPojo;
import com.apartment_rental.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("login/")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email,
                                  @Field("password") String password);
}

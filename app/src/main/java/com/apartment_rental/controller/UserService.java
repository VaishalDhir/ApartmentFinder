package com.apartment_rental.controller;

import com.apartment_rental.model.LoginResponse;
import com.apartment_rental.model.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @POST("login/")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email,
                                  @Field("password") String password);
    @POST("Signup/")
    @FormUrlEncoded
    Call<Register> userRegister(@Field("firstname") String firstname,@Field("lastname") String lastname,@Field("email") String email,
                                @Field("Contact") String Contact,@Field("password") String password,@Field("type") String type);

    @POST("reset/")
    @FormUrlEncoded
    Call<Register> resetPassword(@Field("email") String email,
                                  @Field("password") String password);
}

package com.apartment_rental.controller;

public class ApiUtils {

    public static final String BASE_URL = "http://10.0.2.2:4000/api/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}

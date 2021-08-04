package com.apartment_rental.controller;

import com.apartment_rental.model.AddApartment;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Favourite;
import com.apartment_rental.model.LoginResponse;
import com.apartment_rental.model.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    @POST("insertprop/")
    @FormUrlEncoded
    Call<AddApartment> InsertProperty(@Field("userId") int userId,
                                      @Field("Address") String Address, @Field("ApartmentType") String ApartmentType, @Field("Rent") String Rent,
                                      @Field("Size") String Size, @Field("Facility") String Facility,
                                      @Field("Description") String Description, @Field("RenterType") String RenterType,
                                      @Field("img1") byte[] img1, @Field("img2") byte[] img2, @Field("img3") byte[] img3,
            @Field("latitude") double latitude, @Field("longitude") double longitude);

    @GET("getprop/")
    Call<Apartments> getAllApartments();

    @POST("addtofav/")
    @FormUrlEncoded
    Call<AddApartment> LikeDisLike(@Field("apartmentId") int apartmentId,@Field("userId") int userId);


    @POST("viewfav/")
    @FormUrlEncoded
    Call<Favourite> GetFavourite(@Field("userId") int userId);

    @POST("getrenterprop")
    @FormUrlEncoded
    Call<Apartments> getRenterProp(@Field("userId") int userId);


    @POST("deleteprop/")
    @FormUrlEncoded
    Call<AddApartment> DeleteApt(@Field("apartmentId") int apartmentId);


    @POST("updateprop/")
    @FormUrlEncoded
    Call<AddApartment> UpdateProperty(@Field("apartmentId") int apartmentId,
                                      @Field("Address") String Address, @Field("ApartmentType") String ApartmentType, @Field("Rent") String Rent,
                                      @Field("Size") String Size, @Field("Facility") String Facility,
                                      @Field("Description") String Description, @Field("RenterType") String RenterType,
                                      @Field("img1") byte[] img1, @Field("img2") byte[] img2, @Field("img3") byte[] img3,
                                      @Field("latitude") double latitude, @Field("longitude") double longitude);

}

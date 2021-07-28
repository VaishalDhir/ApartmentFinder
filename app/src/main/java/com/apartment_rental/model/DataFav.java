
package com.apartment_rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFav {

    @SerializedName("favid")
    @Expose
    private Integer favid;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("apartmentId")
    @Expose
    private Integer apartmentId;

    public Integer getFavid() {
        return favid;
    }

    public void setFavid(Integer favid) {
        this.favid = favid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

}

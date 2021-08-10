
package com.apartment_rental.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datuap {

    @SerializedName("apartmentId")
    @Expose
    private Integer apartmentId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("ApartmentType")
    @Expose
    private String apartmentType;
    @SerializedName("Rent")
    @Expose
    private Integer rent;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("Facility")
    @Expose
    private String facility;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("RenterType")
    @Expose
    private String renterType;
    @SerializedName("img1")
    @Expose
    private Img1 img1;
    @SerializedName("img2")
    @Expose
    private Img2 img2;
    @SerializedName("img3")
    @Expose
    private Img3 img3;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("status")
    @Expose
    private Integer status;
    public Integer getApartmentId() {
        return apartmentId;
    }
    @SerializedName("email")
    @Expose
    private String email;



    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Integer getRent() {
        return rent;
    }

    public void setRent(Integer rent) {
        this.rent = rent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRenterType() {
        return renterType;
    }

    public void setRenterType(String renterType) {
        this.renterType = renterType;
    }

    public Img1 getImg1() {
        return img1;
    }

    public void setImg1(Img1 img1) {
        this.img1 = img1;
    }

    public Img2 getImg2() {
        return img2;
    }

    public void setImg2(Img2 img2) {
        this.img2 = img2;
    }

    public Img3 getImg3() {
        return img3;
    }

    public void setImg3(Img3 img3) {
        this.img3 = img3;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

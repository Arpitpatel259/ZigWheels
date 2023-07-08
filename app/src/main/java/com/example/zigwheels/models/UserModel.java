package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("first_name")
    private  String firstname;

    @SerializedName("last_name")
    private  String lastname;

    @SerializedName("email")
    private  String email;

    @SerializedName("mobile")
    private  String mobile;

    @SerializedName("address")
    private  String address;

    @SerializedName("city")
    private  String city;

    @SerializedName("state")
    private  String state;

    @SerializedName("country")
    private  String country;

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }
}

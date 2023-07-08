package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginResponseModel {

    @SerializedName("success")
    private String success;

    @SerializedName("user_details")
    private UserDetailModel userDetailObject;

    @SerializedName("message")
    private String message;

    public String getSuccess() {
        return success;
    }

    public UserDetailModel getUserDetailObject() {
        return userDetailObject;
    }

    public String getMessage() {
        return message;
    }
}

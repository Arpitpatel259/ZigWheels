package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetailModel {

    @SerializedName("user_details")
    private List<UserModel> userDetail;

    public List<UserModel> getUserDetail() {
        return userDetail;
    }
}

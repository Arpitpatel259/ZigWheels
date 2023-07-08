package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

public class ResetPassResponseModel {

    @SerializedName("success")
    private String success;

    @SerializedName("message")
    private String message;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

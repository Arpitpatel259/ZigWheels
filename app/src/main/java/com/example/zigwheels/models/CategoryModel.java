package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @SerializedName("category")
    private String category;

    public String getCategory() {
        return category;
    }
}

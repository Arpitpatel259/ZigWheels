package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponseModel {

    @SerializedName("categories")
    private List<CategoryModel> categories;

    public List<CategoryModel> getCategories() {
        return categories;
    }
}

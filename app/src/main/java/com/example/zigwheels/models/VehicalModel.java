package com.example.zigwheels.models;

import com.example.zigwheels.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class VehicalModel {

    @SerializedName("id")
    private String id;

    @SerializedName("category")
    private String category;

    @SerializedName("modal")
    private String modal;

    @SerializedName("company")
    private String company;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("image_2")
    private String image2;

    @SerializedName("image_3")
    private String image3;

    @SerializedName("image_4")
    private String image4;

    @SerializedName("launchyear")
    private String launchyear;

    @SerializedName("price")
    private String price;

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getModal() {
        return modal;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() { return image; }

    public String getLaunchyear() {
        return launchyear;
    }

    public String getPrice() {
        return price;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLaunchyear(String launchyear) {
        this.launchyear = launchyear;
    }

    public void setPrice(String price) {
        this.price = price;

    }
}
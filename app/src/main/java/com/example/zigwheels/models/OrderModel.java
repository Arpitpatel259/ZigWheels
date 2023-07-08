package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("id")
    private String id;

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("products")
    private String products;

    @SerializedName("total_amount")
    private String totalAmount;

    public String getId() {
        return id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getProducts() {
        return products;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}

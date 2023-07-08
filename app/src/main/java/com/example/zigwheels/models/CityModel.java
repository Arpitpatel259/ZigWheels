package com.example.zigwheels.models;

import androidx.annotation.NonNull;

public class CityModel {

    private String name;

    public CityModel(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

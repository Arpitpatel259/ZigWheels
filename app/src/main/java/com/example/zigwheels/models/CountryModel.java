package com.example.zigwheels.models;

import androidx.annotation.NonNull;

public class CountryModel {

    private String name;

    public CountryModel(String name)
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

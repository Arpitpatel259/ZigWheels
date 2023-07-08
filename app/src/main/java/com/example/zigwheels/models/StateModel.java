package com.example.zigwheels.models;

import androidx.annotation.NonNull;

public class StateModel {

    private String name;

    public StateModel(String name)
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

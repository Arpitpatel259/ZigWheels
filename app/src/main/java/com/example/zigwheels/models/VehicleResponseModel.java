package com.example.zigwheels.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleResponseModel {

    @SerializedName("vehicles")
    private List<VehicalModel> vehicles;

    public List<VehicalModel> getVehicles() {
        return vehicles;
    }
}

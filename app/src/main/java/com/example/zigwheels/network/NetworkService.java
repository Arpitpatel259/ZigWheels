package com.example.zigwheels.network;

import com.example.zigwheels.models.CategoryResponseModel;
import com.example.zigwheels.models.LoginResponseModel;
import com.example.zigwheels.models.OrderResponseModel;
import com.example.zigwheels.models.PlaceOrderResponse;
import com.example.zigwheels.models.RegistrationResponseModel;
import com.example.zigwheels.models.RegistrationResponseModel;
import com.example.zigwheels.models.ResetPassResponseModel;
import com.example.zigwheels.models.VehicleResponseModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetworkService {

    @POST("categories.php")
    Call<CategoryResponseModel> getCategoriesFromServer();

    @FormUrlEncoded
    @POST("vehicle.php")
    Call<VehicleResponseModel> getVehiclesByCategory(@Field("category") String category);

    @FormUrlEncoded
    @POST("registration.php")
    Call<RegistrationResponseModel> register(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponseModel> login(@Field("email") String email ,@Field("password") String password);

    @FormUrlEncoded
    @POST("place_order.php")
    Call<PlaceOrderResponse> placeOrder(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("order_history.php")
    Call<OrderResponseModel> getOrders(@Field("email") String email);

    @FormUrlEncoded
    @POST("ResetPass.php")
    Call<ResetPassResponseModel> resetPass(@Field("email") String email, @Field("username") String username, @Field("password") String password);

}

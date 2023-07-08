package com.example.zigwheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataShareWriteAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zigwheels.database.DatabaseHandler;
import com.example.zigwheels.models.PlaceOrderResponse;
import com.example.zigwheels.models.RegistrationResponseModel;
import com.example.zigwheels.models.VehicalModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    ImageView imageBack;
    TextView textTotalAmount;
    List<VehicalModel> cartItems;
    int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textTotalAmount = findViewById(R.id.text_total_amount);
        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cartRecyclerView.setHasFixedSize(true);

        cartItems = new DatabaseHandler(getApplicationContext()).getCartItems();
        cartRecyclerView.setAdapter(new VehiclesAdapter(cartItems));
        calCulateTotal();

        findViewById(R.id.button_place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
                Boolean isLoggedIn =preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN,false);

                if (!isLoggedIn) {
                    Toast.makeText(getApplicationContext(), "Please login First", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else if(totalAmount==0)
                {
                    Toast.makeText(getApplicationContext(), "Your cart is Empty!", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),PlaceOrder.class);
                    intent.putExtra("Username",getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.KEY_EMAIL, "N/A"));
                    intent.putExtra("email",getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.KEY_EMAIL, "N/A"));
                    intent.putExtra("mobile",getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.KEY_MOBILE, "N/A"));
                    intent.putExtra("address",getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.KEY_ADD, "N/A"));
                    intent.putExtra("total_amount", String.valueOf(totalAmount));
                    intent.putExtra("products", new Gson().toJson(cartItems));
                    startActivity(intent);
                }
            }
        });

    }

    private void calCulateTotal() {
        totalAmount = 0;
        for (int i = 0; i <= cartItems.size() - 1; i++) {
            totalAmount = totalAmount + Integer.parseInt(cartItems.get(i).getPrice());
        }
        textTotalAmount.setText("Total Amt. \u20B9 " + String.valueOf(totalAmount));
    }

    //VehiclesAdapter
    private class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder> {

        List<VehicalModel> vehicles;

        VehiclesAdapter(List<VehicalModel> vehicles) {
            this.vehicles = vehicles;
        }

        @Override
        public int getItemCount() {
            return vehicles.size();
        }

        @NonNull
        @Override
        public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VehicleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_container, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {

            if (vehicles.get(position).getModal() != null && !vehicles.get(position).getModal().equals("")) {
                holder.textName.setText(vehicles.get(position).getModal());
            } else {
                holder.textName.setVisibility(View.GONE);
            }

            if (vehicles.get(position).getPrice() != null && !vehicles.get(position).getPrice().equals("")) {
                holder.textPrice.setText("\u20B9" + vehicles.get(position).getPrice());
            } else {
                holder.textPrice.setVisibility(View.GONE);
            }

            if (vehicles.get(position).getCompany() != null && !vehicles.get(position).getCompany().equals(" ")) {
                holder.textCompany.setText(vehicles.get(position).getCompany());
            } else {
                holder.textCompany.setVisibility(View.GONE);
            }

            if (vehicles.get(position).getImage() != null && vehicles.get(position).getImage() != "") {
                Picasso.with(getApplicationContext())
                        .load(vehicles.get(position).getImage())
                        .into(holder.imageVehicle);
            } else {
                holder.imageVehicle.setVisibility(View.GONE);
            }

            holder.textRemoveItem.setPaintFlags(holder.textRemoveItem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.textRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatabaseHandler(getApplicationContext()).removeItem(vehicles.get(holder.getAdapterPosition()).getId());
                    cartItems.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), cartItems.size());
                    calCulateTotal();
                }
            });


        }

        //ViewHolder
        class VehicleViewHolder extends RecyclerView.ViewHolder {


            ImageView imageVehicle;
            TextView textName, textCompany, textPrice, textRemoveItem;

            VehicleViewHolder(View view) {
                super(view);
                imageVehicle = view.findViewById(R.id.image_vehicle);
                textName = view.findViewById(R.id.text_vehicle_name);
                textCompany = view.findViewById(R.id.text_vehicle_company);
                textPrice = view.findViewById(R.id.text_vehicle_price);
                textRemoveItem = view.findViewById(R.id.text_remove_item);
            }
        }
    }
}

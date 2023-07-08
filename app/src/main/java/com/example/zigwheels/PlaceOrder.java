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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zigwheels.database.DatabaseHandler;
import com.example.zigwheels.models.PlaceOrderResponse;
import com.example.zigwheels.models.VehicalModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrder extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    ImageView imageBack;
    TextView textTotalAmount, textUsername, textMobile, textEmail, textAddress;
    List<VehicalModel> cartItems;
    Button ChangeAdd;
    EditText NewAddress;
    SharedPreferences preferences;
    int totalAmount,t=1;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        textUsername = findViewById(R.id.text_username_placeorder);
        textEmail = findViewById(R.id.Email_place_order);
        textMobile = findViewById(R.id.Mobile_place_order);
        textAddress = findViewById(R.id.text_place_address);
        textTotalAmount = findViewById(R.id.text_total_amount);
        NewAddress = findViewById(R.id.edir_arrderss);

        preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        textUsername.setText(preferences.getString(Constants.KEY_USERNAME,"N/A"));
        textEmail.setText(getIntent().getStringExtra("email"));
        textMobile.setText(getIntent().getStringExtra("mobile"));
        textTotalAmount.setText("Total Amt. \u20B9 " + String.valueOf(getIntent().getStringExtra("total_amount")));
        textAddress.setText(getIntent().getStringExtra("address"));

        findViewById(R.id.btn_change_Address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t==1) {
                    textAddress.setVisibility(View.GONE);
                    NewAddress.setVisibility(View.VISIBLE);
                    t=0;
                }
                else
                {
                    textAddress.setVisibility(View.VISIBLE);
                    NewAddress.setVisibility(View.GONE);
                    t=1;
                }

            }
        });
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
        cartRecyclerView.setAdapter(new ItemAdapter(cartItems));

        findViewById(R.id.button_place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                Boolean isLoggedIn = preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN, false);

                if (!isLoggedIn) {
                    Toast.makeText(getApplicationContext(), "Please login First", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_email", preferences.getString(Constants.KEY_EMAIL, "N/A"));
                    params.put("total_amount", getIntent().getStringExtra("total_amount"));
                    if (NewAddress.getText().toString() != null)
                        params.put("address", NewAddress.getText().toString());
                    else
                        params.put("address", getIntent().getStringExtra("address"));
                    params.put("products", getIntent().getStringExtra("products"));
                    params.put("order_date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                    placeOrder(params);
                }
            }
        });

    }

    private void placeOrder(HashMap<String, String> params) {

        final ProgressDialog progressDialog = new ProgressDialog(PlaceOrder.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Placing order...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
        Call<PlaceOrderResponse> placeOrderCall = networkService.placeOrder(params);
        placeOrderCall.enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                PlaceOrderResponse responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        Toast.makeText(PlaceOrder.this, responseBody.getMessage(), Toast.LENGTH_LONG).show();
                        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                        databaseHandler.deleteCart();
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(PlaceOrder.this, responseBody.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private class ItemAdapter extends RecyclerView.Adapter<PlaceOrder.ItemAdapter.ItemViewHolder> {

        List<VehicalModel> items;

        ItemAdapter(List<VehicalModel> item) {
            this.items = item;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @NonNull
        @Override
        public PlaceOrder.ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PlaceOrder.ItemAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_item_container, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final PlaceOrder.ItemAdapter.ItemViewHolder holder, final int position) {

            if (items.get(position).getModal() != null && !items.get(position).getModal().equals("")) {
                holder.textName.setText(items.get(position).getModal());
            } else {
                holder.textName.setVisibility(View.GONE);
            }

            if (items.get(position).getPrice() != null && !items.get(position).getPrice().equals("")) {
                holder.textPrice.setText("\u20B9" + items.get(position).getPrice());
            } else {
                holder.textPrice.setVisibility(View.GONE);
            }

            if (items.get(position).getCompany() != null && !items.get(position).getCompany().equals("")) {
                holder.textSelection.setText(items.get(position).getCompany());
            } else {
                holder.textSelection.setVisibility(View.GONE);
            }

            if (items.get(position).getImage() != null && items.get(position).getImage() != "") {
                Picasso.with(getApplicationContext())
                        .load(items.get(position).getImage())
                        .into(holder.imageItem);
            } else {
                holder.imageItem.setVisibility(View.GONE);
            }
        }


        //ViewHolder
        final class ItemViewHolder extends RecyclerView.ViewHolder {


            ImageView imageItem;
            TextView textName, textSelection, textPrice;

            ItemViewHolder(View view) {
                super(view);
                imageItem = view.findViewById(R.id.image_items);
                textName = view.findViewById(R.id.text_item_name);
                textSelection = view.findViewById(R.id.text_item_selection);
                textPrice = view.findViewById(R.id.text_item_price);
            }
        }
    }
}
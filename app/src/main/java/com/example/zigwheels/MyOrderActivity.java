package com.example.zigwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zigwheels.models.OrderModel;
import com.example.zigwheels.models.OrderResponseModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderRecyclerView = findViewById(R.id.order_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getOrders();
    }
    private void getOrders() {

        final ProgressDialog progressDialog = new ProgressDialog(MyOrderActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting orders...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        Call<OrderResponseModel> getOrdersCall = service.getOrders(preferences.getString(Constants.KEY_EMAIL, "N/A"));
        getOrdersCall.enqueue(new Callback<OrderResponseModel>() {
            @Override
            public void onResponse(Call<OrderResponseModel> call, Response<OrderResponseModel> response) {
                OrderResponseModel orderResponse = response.body();
                if (orderResponse != null) {
                    orderRecyclerView.setAdapter(new OrderAdapter(orderResponse.getOrders()));
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OrderResponseModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }
    private class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

        List<OrderModel> orders;
        OrderAdapter(List<OrderModel> orders) { this.orders = orders; }

        @Override
        public int getItemCount () {
            return  orders.size();
        }

        @Override
        public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_container, parent, false));
        }
        @Override
        public void onBindViewHolder(OrderViewHolder holder, int position) {
            holder.textOrderId.setText("Order Id : "+orders.get(holder.getAdapterPosition()).getId());
            holder.textDate.setText("Order Date : "+orders.get(holder.getAdapterPosition()).getOrderDate());
            holder.textTotal.setText("Order Amount : \u20B9"+orders.get(holder.getAdapterPosition()).getTotalAmount());

            holder.layoutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                    intent.putExtra("products", orders.get(holder.getAdapterPosition()).getProducts());
                    startActivity(intent);
                }
            });
        }
    }
    private  class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textDate, textTotal;
        CardView layoutOrder;

        OrderViewHolder(View itemView) {
            super(itemView);



            textOrderId = itemView.findViewById(R.id.text_order_id);
            textDate = itemView.findViewById(R.id.text_order_date);
            textTotal = itemView.findViewById(R.id.text_order_total);
            layoutOrder = itemView.findViewById(R.id.vehicle_card_view);

        }
    }
}
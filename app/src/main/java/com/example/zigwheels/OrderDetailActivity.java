package com.example.zigwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zigwheels.models.VehicalModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ImageView imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        orderRecyclerView =findViewById(R.id.oredr_details_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<VehicalModel> vehicles  = new ArrayList<>();
        String products = getIntent().getStringExtra("products");
        Gson gson = new Gson();
        VehicalModel[] vehicleItems = gson.fromJson(products, VehicalModel[].class);

        vehicles = Arrays.asList(vehicleItems);
        vehicles = new ArrayList(vehicles);

        orderRecyclerView.setAdapter(new VehiclesAdapter(vehicles));

    }


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
            return new VehicleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item_container, parent, false));
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

            if (vehicles.get(position).getDescription() != null && !vehicles.get(position).getDescription().equals(" ")) {
                holder.textDescription.setText(vehicles.get(position).getDescription());
            } else {
                holder.textDescription.setVisibility(View.GONE);
            }

            if (vehicles.get(position).getImage() != null && vehicles.get(position).getImage() != "") {
                Picasso.with(getApplicationContext())
                        .load(vehicles.get(position).getImage())
                        .into(holder.imageVehicle);
            } else {
                holder.imageVehicle.setVisibility(View.GONE);
            }


            holder.cardVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), VehicleDetailActivity.class);
                    intent.putExtra("image", vehicles.get(holder.getAdapterPosition()).getImage());
                    intent.putExtra("image2", vehicles.get(holder.getAdapterPosition()).getImage2());
                    intent.putExtra("image3", vehicles.get(holder.getAdapterPosition()).getImage3());
                    intent.putExtra("image4", vehicles.get(holder.getAdapterPosition()).getImage4());
                    intent.putExtra("company", vehicles.get(holder.getAdapterPosition()).getCompany());
                    intent.putExtra("launchYear", vehicles.get(holder.getAdapterPosition()).getLaunchyear());
                    intent.putExtra("description", vehicles.get(holder.getAdapterPosition()).getDescription());
                    intent.putExtra("price", vehicles.get(holder.getAdapterPosition()).getPrice());
                    intent.putExtra("name", vehicles.get(holder.getAdapterPosition()).getModal());
                    intent.putExtra("id", vehicles.get(holder.getAdapterPosition()).getId());
                    intent.putExtra("category", vehicles.get(holder.getAdapterPosition()).getCategory());

                    startActivity(intent);
                }
            });


        }

        class VehicleViewHolder extends RecyclerView.ViewHolder {


            CardView cardVehicle;
            ImageView imageVehicle;
            TextView textName, textCompany, textPrice, textDescription;

            VehicleViewHolder(View view) {
                super(view);
                cardVehicle = view.findViewById(R.id.card_order_con);
                imageVehicle = view.findViewById(R.id.image_vehicle);
                textName = view.findViewById(R.id.text_order_name);
                textPrice = view.findViewById(R.id.text_order_price);
                textCompany = view.findViewById(R.id.text_order_company);
                textDescription = view.findViewById(R.id.text_order_des);
            }
        }
    }
}
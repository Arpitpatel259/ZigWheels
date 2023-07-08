package com.example.zigwheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zigwheels.models.VehicalModel;
import com.example.zigwheels.models.VehicleResponseModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleActivity extends AppCompatActivity {

    RecyclerView vehicleRecyclerView;
    TextView textToolBarTitle;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        textToolBarTitle = findViewById(R.id.text_toolbar_title);
        textToolBarTitle.setText(getIntent().getStringExtra("category"));

        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        vehicleRecyclerView = findViewById(R.id.vehicles_recycler_view);
        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vehicleRecyclerView.setHasFixedSize(true);
        getVehicle();
    }

    private void getVehicle() {

        ProgressDialog progressDialog = new ProgressDialog(VehicleActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting vehicles");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
        Call<VehicleResponseModel> getVehicles = networkService.getVehiclesByCategory(getIntent().getStringExtra("category"));

        getVehicles.enqueue(new Callback<VehicleResponseModel>() {
            @Override
            public void onResponse(Call<VehicleResponseModel> call, Response<VehicleResponseModel> response) {

                VehiclesAdapter vehicleAdapter = new VehiclesAdapter(response.body().getVehicles());
                vehicleRecyclerView.setAdapter(vehicleAdapter);
                progressDialog.cancel();

            }

            @Override
            public void onFailure(Call<VehicleResponseModel> call, Throwable t) {

                progressDialog.cancel();
            }
        });


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
            return new VehicleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item_container, parent, false));
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
            TextView textName, textCompany, textPrice;

            VehicleViewHolder(View view) {
                super(view);
                cardVehicle = view.findViewById(R.id.vehicle_card_view);
                imageVehicle = view.findViewById(R.id.image);
                textName = view.findViewById(R.id.text_vehicle_name);
                textCompany = view.findViewById(R.id.text_vehicle_company);
                textPrice = view.findViewById(R.id.text_vehicle_price);
            }
        }
    }
}

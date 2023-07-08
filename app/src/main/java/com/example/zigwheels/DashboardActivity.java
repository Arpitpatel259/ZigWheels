package com.example.zigwheels;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zigwheels.utils.Constants;

import org.w3c.dom.Text;

public class DashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView imageView;
    View viewLogin,viewLogout;
    TextView textUsername,textEmail;
    LinearLayout layoutLogin,layoutLogout;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        imageView = findViewById(R.id.image_menu);
        layoutLogin = findViewById(R.id.layout_login);
        layoutLogout = findViewById(R.id.layout_logout);
        viewLogin =findViewById(R.id.view_login);
        viewLogout =findViewById(R.id.view_logout);
        textUsername = findViewById(R.id.text_user);
        textEmail = findViewById(R.id.text_email);

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        Boolean isLoggedIn =preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN,false);

        if(!isLoggedIn)
        {
            textUsername.setText(R.string.Welcome_guest);
            textUsername.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.VISIBLE);
            viewLogin.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.GONE);
            viewLogout.setVisibility(View.GONE);
        }
        else
        {
            textUsername.setText(preferences.getString(Constants.KEY_USERNAME,"N/A"));
            textEmail.setText(preferences.getString(Constants.KEY_EMAIL,"N/A"));
            textUsername.setVisibility(View.VISIBLE);
            textEmail.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            viewLogin.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
            viewLogout.setVisibility(View.VISIBLE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        findViewById(R.id.layout_MY_oredr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,MyOrderActivity.class));
            }
        });
        findViewById(R.id.layout_My_Wishlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,CartActivity.class));
            }
        });
        findViewById(R.id.about_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,About_us.class));
            }
        });
        findViewById(R.id.card_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,MyOrderActivity.class));
            }
        });
        findViewById(R.id.card_vehicles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,Categories.class));
            }
        });

        findViewById(R.id.card_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,CartActivity.class));

            }
        });
        findViewById(R.id.layout_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,About_us.class));

            }
        });
        findViewById(R.id.layout_cars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,Categories.class));

            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to logout?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(DashboardActivity.this,"You have been Logged out",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();


            }
        });

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}
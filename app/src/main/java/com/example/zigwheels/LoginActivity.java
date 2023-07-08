package com.example.zigwheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zigwheels.models.LoginResponseModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView textCreateAccount;
    TextInputEditText inputEmail,inputPassword;
    Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textCreateAccount = findViewById(R.id.text_create_account);
        textCreateAccount.setPaintFlags(textCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        inputPassword =findViewById(R.id.input_password);
        buttonLogin =findViewById(R.id.btn_login);
        inputEmail=findViewById(R.id.input_email_login);
        textCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
        findViewById(R.id.text_Forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPass.class));
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputEmail.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
                }else  if(!emilValidator(inputEmail.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter valid email",Toast.LENGTH_LONG).show();
                }else  if(inputPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_LONG).show();
                }else{
                    login();

                }
            }
        });


    }
    private void login(){

        ProgressDialog progressDialog = new ProgressDialog( LoginActivity.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Logging...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
        Call<LoginResponseModel> login = networkService.login(inputEmail.getText().toString(),inputPassword.getText().toString());
        login.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseModel> call,@NonNull Response<LoginResponseModel> response) {
                LoginResponseModel responseBody = response.body();
                if(responseBody != null)
                {
                    if(responseBody.getSuccess().equals("1")){

                        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor =preferences.edit();
                        editor.putBoolean(Constants.KEY_ISE_LOGGED_IN,true);
                        editor.putString(Constants.KEY_USERNAME,responseBody.getUserDetailObject().getUserDetail().get(0).getFirstname()+" "+responseBody.getUserDetailObject().getUserDetail().get(0).getLastname());
                        editor.putString(Constants.KEY_EMAIL,responseBody.getUserDetailObject().getUserDetail().get(0).getEmail());
                        editor.putString(Constants.KEY_MOBILE,responseBody.getUserDetailObject().getUserDetail().get(0).getMobile());
                        editor.putString(Constants.KEY_ADD,responseBody.getUserDetailObject().getUserDetail().get(0).getAddress()+","+responseBody.getUserDetailObject().getUserDetail().get(0).getCity()+","+responseBody.getUserDetailObject().getUserDetail().get(0).getState()+","+responseBody.getUserDetailObject().getUserDetail().get(0).getCountry());
                        editor.apply();

                        Toast.makeText(LoginActivity.this,responseBody.getMessage(),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,responseBody.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"else",Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponseModel> call,@NonNull Throwable t) {

                Toast.makeText(LoginActivity.this,"faild",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }
    public boolean emilValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-za-z0-9-]+(\\.[_A-za-z0-9-]+)*@[A-za-z0-9]+(\\.[_A-za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
    }



}

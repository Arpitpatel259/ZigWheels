package com.example.zigwheels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zigwheels.models.ResetPassResponseModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;
import com.example.zigwheels.utils.Constants;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPass extends AppCompatActivity {

    Button show_pass4;
    Button show_pass5;
    EditText password, Cpass, fetch_email, fetch_username;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_reset_pass );

        preferences = getSharedPreferences ( Constants.PREFERENCE_NAME, MODE_PRIVATE );
        password = findViewById ( R.id.input_password);
        Cpass = findViewById ( R.id.input_confirm_password );
        fetch_email = findViewById ( R.id.input_email );
        fetch_username = findViewById ( R.id.input_username );

        Button cp = findViewById ( R.id.btn_Change_pass );

        cp.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                String pass = Objects.requireNonNull ( password.getText ( ) ).toString ( );
                String cpass = Objects.requireNonNull ( Cpass.getText ( ) ).toString ( );

                if (fetch_email.getText ( ).toString ( ).equals ( "" )) {
                    fetch_email.setError ( "Please Enter Email Address" );
                } else if (!Patterns.EMAIL_ADDRESS.matcher ( fetch_email.getText ( ).toString ( ) ).matches ( )) {
                    fetch_email.setError ( "Please Enter Valid Email-ID" );
                } else if (fetch_username.length ( ) == 0) {
                    fetch_username.setError ( "Please Enter Username" );
                } else if (pass.length ( ) == 0) {
                    password.setError ( "Please Enter Password" );
                } else if (pass.length ( ) <= 7) {
                    password.setError ( "Please Enter Password Minimum in 8 Char" );
                } else if (!pass.equals ( cpass )) {
                    Cpass.setError ( "Both Password are not Matched." );
                } else {
                    ResetPassword ( );
                }
            }
        } );
    }

    private void ResetPassword() {
        ProgressDialog progressDialog = new ProgressDialog ( ResetPass.this );
        progressDialog.setTitle ( "Please wait " );
        progressDialog.setMessage ( "Password Changing..." );
        progressDialog.setCancelable ( false );
        progressDialog.show ( );

        NetworkService networkService = NetworkClient.getClient ( ).create ( NetworkService.class );
        Call<ResetPassResponseModel> Password = networkService.resetPass ( fetch_email.getText ( ).toString ( ), fetch_username.getText ( ).toString ( ), password.getText ().toString () );
        Password.enqueue ( new Callback<ResetPassResponseModel>( ) {
            @Override
            public void onResponse(@NonNull Call<ResetPassResponseModel> call, @NonNull Response<ResetPassResponseModel> response) {
                ResetPassResponseModel responseBody = response.body ( );
                if (responseBody != null) {
                    if (responseBody.getSuccess ( ).equals ( "1" )) {
                        Toast.makeText ( ResetPass.this, responseBody.getMessage ( ), Toast.LENGTH_LONG ).show ( );
                        Intent intent = new Intent ( ResetPass.this, LoginActivity.class );
                        startActivity ( intent );
                        finish ( );
                    } else {
                        Toast.makeText ( ResetPass.this, responseBody.getMessage ( ), Toast.LENGTH_LONG ).show ( );
                    }
                } else {
                    Toast.makeText ( ResetPass.this, responseBody.getMessage ( ), Toast.LENGTH_LONG ).show ( );
                }
                progressDialog.dismiss ( );
            }

            @Override
            public void onFailure(@NonNull Call<ResetPassResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss ( );
            }
        } );
    }
}
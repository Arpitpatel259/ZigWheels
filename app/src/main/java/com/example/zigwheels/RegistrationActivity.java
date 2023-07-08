package com.example.zigwheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.PathEffect;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zigwheels.models.CityModel;
import com.example.zigwheels.models.CountryModel;
import com.example.zigwheels.models.RegistrationResponseModel;
import com.example.zigwheels.models.StateModel;
import com.example.zigwheels.network.NetworkClient;
import com.example.zigwheels.network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    Spinner countrySpinner,stateSpinner,citySpinner;
    EditText inputFirstName,inputLastName,inputMobile,inputEmail,inputPassword,inputConfirmPassword,inputUserName,inputAddre;
    RadioButton redioMale,redioFemale;
    Button buttonRegister;
    boolean isGenderSelected;
    String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        countrySpinner = findViewById(R.id.country_spinner);
        stateSpinner = findViewById(R.id.state_spinner);
        citySpinner = findViewById(R.id.city_spinner);

        inputUserName = findViewById(R.id.input_username);
        inputFirstName = findViewById(R.id.input_first_name);
        inputLastName = findViewById(R.id.input_last_name);
        inputMobile = findViewById(R.id.input_mobile);
        inputAddre = findViewById(R.id.input_adder);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        buttonRegister = findViewById(R.id.button_register);
        redioMale = findViewById(R.id.redio_male);
        redioFemale = findViewById(R.id.redio_female);

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        redioMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isGenderSelected = true;
                    selectedGender = "Male";
                }
            }
        });
        redioFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    isGenderSelected = true;
                    selectedGender= "Female";
                }
            }
        });

        initCountrySpinner();


        ArrayList<StateModel> states = new ArrayList<>();
        states.add(new StateModel("Select State"));
        ArrayAdapter<StateModel> stateAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        ArrayList<CityModel> cities = new ArrayList<>();
        cities.add(new CityModel("Select City"));
        ArrayAdapter<CityModel> citiesAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citiesAdapter);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputFirstName.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter first name",Toast.LENGTH_LONG).show();
                }else if(inputLastName.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter last name",Toast.LENGTH_LONG).show();
                }else if(inputUserName.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Username",Toast.LENGTH_LONG).show();
                }else if(!isGenderSelected)
                {
                    Toast.makeText(getApplicationContext(),"Please Select gender",Toast.LENGTH_LONG).show();
                }else if(inputMobile.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Mobile",Toast.LENGTH_LONG).show();
                }else if(inputMobile.getText().toString().length() < 10)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter valid Mobile",Toast.LENGTH_LONG).show();
                }else if(countrySpinner.getSelectedItem().toString().equals("Select Country"))
                {
                    Toast.makeText(getApplicationContext(),"Please select Country",Toast.LENGTH_LONG).show();
                }else if(stateSpinner.getSelectedItem().toString().equals("Select State"))
                {
                    Toast.makeText(getApplicationContext(),"Please select State",Toast.LENGTH_LONG).show();
                }else if(citySpinner.getSelectedItem().toString().equals("Select City"))
                {
                    Toast.makeText(getApplicationContext(),"Please select City",Toast.LENGTH_LONG).show();
                }else if(inputAddre.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Address",Toast.LENGTH_LONG).show();
                }else if(inputEmail.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Email",Toast.LENGTH_LONG).show();
                }else if(!emilValidator(inputEmail.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter valid Email",Toast.LENGTH_LONG).show();
                }
                else if(inputPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_LONG).show();
                }else if(inputConfirmPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please confirm password",Toast.LENGTH_LONG).show();
                }else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Password and Confirm password must be same",Toast.LENGTH_LONG).show();
                }else{
                    HashMap<String,String> params = new HashMap<>();
                    params.put("firstname",inputFirstName.getText().toString());
                    params.put("lastname",inputLastName.getText().toString());
                    params.put("username",inputUserName.getText().toString());
                    params.put("gender",selectedGender);
                    params.put("mobile",inputMobile.getText().toString());
                    params.put("email",inputEmail.getText().toString());
                    params.put("password",inputPassword.getText().toString());
                    params.put("country",countrySpinner.getSelectedItem().toString());
                    params.put("state",stateSpinner.getSelectedItem().toString());
                    params.put("city",citySpinner.getSelectedItem().toString());
                    params.put("addre",inputAddre.getText().toString());
                    register(params);
                }

            }
        });
    }

    private void initCountrySpinner(){
        ArrayList<CountryModel> countries = new ArrayList<>();
        countries.add(new CountryModel("Select Country"));
        countries.add(new CountryModel("India"));
        countries.add(new CountryModel("United State"));
        countries.add(new CountryModel("United Kingdom"));
        countries.add(new CountryModel("Australia"));

        ArrayAdapter<CountryModel> countriesAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,countries);
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countriesAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position ==1)
                {
                    ArrayList<StateModel> states = new ArrayList<>();
                    states.add(new StateModel("Select State"));
                    states.add(new StateModel("Gujarat"));
                    states.add(new StateModel("Maharashtra"));
                    states.add(new StateModel("Punjab"));

                    ArrayAdapter<StateModel> stateAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,states);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    stateSpinner.setAdapter(stateAdapter);

                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            if(position ==1) {
                                ArrayList<CityModel> cities = new ArrayList<>();
                                cities.add(new CityModel("Select City"));
                                cities.add(new CityModel("Rajkot"));
                                cities.add(new CityModel("Ahmedabad"));
                                cities.add(new CityModel("Junagadh"));
                                cities.add(new CityModel("Baroda"));

                                ArrayAdapter<CityModel> citiesAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,cities);
                                citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                citySpinner.setAdapter(citiesAdapter);
                            }else if(position ==2) {
                                ArrayList<CityModel> cities = new ArrayList<>();
                                cities.add(new CityModel("Select City"));
                                cities.add(new CityModel("Mumbai"));
                                cities.add(new CityModel("Pune"));

                                ArrayAdapter<CityModel> citiesAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,cities);
                                citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                citySpinner.setAdapter(citiesAdapter);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }else if(position==2) {
                    ArrayList<StateModel> states = new ArrayList<>();
                    states.add(new StateModel("Select State"));
                    states.add(new StateModel("Texas"));
                    states.add(new StateModel("California"));
                    states.add(new StateModel("Maryland"));

                    ArrayAdapter<StateModel> stateAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_spinner_item, states);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    stateSpinner.setAdapter(stateAdapter);
                }else if(position==2){
                    ArrayList<StateModel> states = new ArrayList<>();
                    states.add(new StateModel("Select State"));
                    states.add(new StateModel("Texas"));
                    states.add(new StateModel("California"));
                    states.add(new StateModel("Maryland"));

                    ArrayAdapter<StateModel> stateAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item,states);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    stateSpinner.setAdapter(stateAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
    private void register(HashMap<String,String> params){

        ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("Please wait ");
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
        Call<RegistrationResponseModel> registerCall = networkService.register(params);
        registerCall.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseModel> call,@NonNull Response<RegistrationResponseModel> response) {
                RegistrationResponseModel responseBody = response.body();
                if(responseBody != null)
                {
                    if(responseBody.getSuccess().equals("1")){
                        Toast.makeText(RegistrationActivity.this,responseBody.getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this,responseBody.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "else", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseModel> call,@NonNull  Throwable t) {
                Toast.makeText(RegistrationActivity.this,"fail",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }
}

package com.example.zigwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.zigwheels.database.DatabaseHandler;
import com.example.zigwheels.models.VehicalModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class VehicleDetailActivity extends AppCompatActivity {
    SliderLayout sliderLayout;
    ImageView imageAddToCart;
    TextView textCompany, textLaunchYear, textDescription, textPrice, textToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        imageAddToCart = findViewById(R.id.image_add_to_cart);
        textCompany = findViewById(R.id.text_company);
        textDescription = findViewById(R.id.text_description);
        textLaunchYear = findViewById(R.id.text_launch_year);
        textPrice = findViewById(R.id.text_price);
        textToolbarTitle = findViewById(R.id.text_toolbar_title);
        sliderLayout = findViewById(R.id.slider);


        textCompany.setText("By : " + getIntent().getStringExtra("company"));
        textLaunchYear.setText("Published : " + getIntent().getStringExtra("launchYear"));
        textDescription.setText(getIntent().getStringExtra("description"));
        textPrice.setText("\u20B9" + getIntent().getStringExtra("price"));
        textToolbarTitle.setText(getIntent().getStringExtra("name"));


        try {
            HashMap<String, String> url_maps = new HashMap<String, String>();
            url_maps.put("Image1", getIntent().getStringExtra("image"));
            url_maps.put("Image2", getIntent().getStringExtra("image2"));
            url_maps.put("Image3", getIntent().getStringExtra("image3"));
            url_maps.put("Image4", getIntent().getStringExtra("image4"));

            for (String name : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Foreground2Background);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(4000);
        } catch (Exception ignored) {
        }


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehicalModel vehicalModel = new VehicalModel();
                vehicalModel.setId(getIntent().getStringExtra("id"));
                vehicalModel.setCategory(getIntent().getStringExtra("category"));
                vehicalModel.setModal(getIntent().getStringExtra("name"));
                vehicalModel.setDescription(getIntent().getStringExtra("description"));
                vehicalModel.setCompany(getIntent().getStringExtra("company"));
                vehicalModel.setImage(getIntent().getStringExtra("image"));
                vehicalModel.setLaunchyear(getIntent().getStringExtra("launchYear"));
                vehicalModel.setPrice(getIntent().getStringExtra("price"));

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.addToCart(vehicalModel);
                Toast.makeText(getApplicationContext(), "Item Added to Cart!", Toast.LENGTH_LONG).show();
            }
        });

    }
}

package com.example.aihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.aihub.databinding.ActivityPredictionBinding;
import com.example.aihub.dataobjects.FoodVisionResponse;
import com.example.aihub.services.LoadingBar;
import com.google.common.base.Charsets;
import com.qandeelabbassi.dropsy.DropDownItem;
import com.qandeelabbassi.dropsy.DropDownView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PredictionActivity extends AppCompatActivity {

    private ActivityPredictionBinding binding;
    String url = "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/used-car-price/?";
    String[] locations = {"Ahmedabad","Bangalore","Chennai","Coimbatore","Delhi","Hyderabad","Jaipur","Kochi","Kolkata","Mumbai","Pune"};
    String location;
    String fuelType;
    int manualTransmission;

    int ownerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPredictionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra("title"));
        binding.btnNotebook.setOnClickListener(v -> {
            Toast.makeText(this, "Notebook", Toast.LENGTH_SHORT).show();
        });

        binding.locationDrpDwn.setItemClickListener(new DropDownView.ItemClickListener() {
            @Override
            public void onItemClick(int i, @NonNull DropDownItem dropDownItem) {
                location = dropDownItem.getText();
            }
        });

        binding.petrolBtn.setOnClickListener(v -> {
            fuelType = "Petrol";

        });
        binding.dieselBtn.setOnClickListener(v -> {
            fuelType = "Diesel";

        });
        binding.lpgBtn.setOnClickListener(v -> {
            fuelType = "LPG";

        });


        binding.manualBtn.setOnClickListener(v -> {
            manualTransmission = 1;
        });

        binding.automaticBtn.setOnClickListener(v -> {
            manualTransmission = 0;
        });

        binding.firstOwnerBtn.setOnClickListener(v -> {
            ownerType = 1;
        });

        binding.secondOwnerBtn.setOnClickListener(v -> {
            ownerType = 2;
        });

        binding.thirdOwnerBtn.setOnClickListener(v -> {
            ownerType = 3;
        });

        binding.fourthAndAboveOwnerBtn.setOnClickListener(v -> {
            ownerType = 4;
        });



        binding.btnSubmit.setOnClickListener(v -> {
            //check if every field is filled
            if(location == null || fuelType == null || manualTransmission == 0 || ownerType == 0 || binding.year.getText().toString().isEmpty() || binding.kmsDriven.getText().toString().isEmpty() || binding.kmsDriven.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            //check if year is valid
            if(Integer.parseInt(binding.year.getText().toString()) < 1980 || Integer.parseInt(binding.year.getText().toString()) > 2020)
            {
                Toast.makeText(this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
                return;
            }

            if(Integer.parseInt(binding.seats.getText().toString()) < 0 || Integer.parseInt(binding.seats.getText().toString()) > 13)
            {
                Toast.makeText(this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
                return;
            }

            //post request
            postRequest(binding.year.getText().toString(), binding.kmsDriven.getText().toString(), binding.seats.getText().toString(), fuelType, String.valueOf(ownerType), String.valueOf(manualTransmission), location, binding.mileage.getText().toString(), binding.engine.getText().toString(), binding.power.getText().toString());
        });


    }
    private void postRequest(String year, String kmsDriven, String seats, String fuelType, String ownerType, String manualTransmission, String location, String mileage,String engine, String Power) {
        //LoadingBar loadingBar = new LoadingBar(this, "Predicting Price");
        //loadingBar.show();
        OkHttpClient client = new OkHttpClient();
        String query = null;
        query = "&location="+location+"&year="+year+"&km_driven="+kmsDriven+"&fuel="+fuelType+"&owners="+ownerType+"&transmission="+manualTransmission+"&seats="+seats+"&mileage="+mileage+"&engine="+engine+"&power="+Power;

        Request request = new Request.Builder()
                .url(url+""+query)
                .get()
                .build();

        client.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.d("response", "FAIL: "+e.getMessage());
                //loadingBar.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String res = response.body().string();
                    //FoodVisionResponse responseObject = parseJson(res);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.result.setText(res);
                            //loadingBar.dismiss();
                        }
                    });
                    //deleteFileFromFirebase(ImageName);

                    Log.d("response", "onResponse: "+res);
                }
            }
        });

    }


}
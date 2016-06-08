package com.unimagdalena.edu.co.rent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RentApp rentApp = (RentApp) getApplication();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, rentApp.GET(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processListResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());

                Navigator.with(SplashScreenActivity.this).build().goTo(SettingsActivity.class).animation().commit();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void processListResponse(JSONObject response) {
        try {
            Integer state = response.getInt("estado");

            switch (state) {
                case 1:
                    JSONArray jsonArray = response.getJSONArray("inmuebles");

                    Gson gson = new Gson();

                    Rent[] rents = gson.fromJson(jsonArray.toString(), Rent[].class);

                    final ArrayList<Rent> rentArrayList = new ArrayList<>();

                    Collections.addAll(rentArrayList, rents);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent.putExtra("rents", rentArrayList);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    }, 2500);
                    break;
                case 2:
                    String message = response.getString("mensaje");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.unimagdalena.edu.co.rent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.GET, null,
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
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.teal_500));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.GET, null,
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
                    }
                });

                VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    public void processListResponse(JSONObject response) {
        try {
            Integer state = response.getInt("estado");

            switch (state) {
                case 1:
                    JSONArray mensaje = response.getJSONArray("inmuebles");

                    Rent[] rents = gson.fromJson(mensaje.toString(), Rent[].class);

                    ArrayList<Rent> rentArrayList = new ArrayList<>();

                    Collections.addAll(rentArrayList, rents);

                    RentAdapter rentAdapter = new RentAdapter(rentArrayList, MainActivity.this);
                    recyclerView.setAdapter(rentAdapter);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
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

    public void processCitiesResponse(JSONObject response) {
        try {
            Integer state = response.getInt("estado");

            switch (state) {
                case 1:
                    JSONArray mensaje = response.getJSONArray("ciudad");

                    final City[] cities = gson.fromJson(mensaje.toString(), City[].class);

                    CharSequence[] charSequences = new CharSequence[cities.length];

                    int i = 0;
                    for (City city : cities) {
                        charSequences[i] = city.getCiudad();
                        i++;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.report_by));
                    builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            loadByCity(cities[item].getCiudad());
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

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

    public void loadByCity(String city) {
        String path = Constants.GET_BY_CITY + "?ciudad=" + city.replace(" ", "%20");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void processResponse(JSONObject response) {
        try {
            String state = response.getString("estado");
            String message;

            switch (state) {
                case "1":
                    JSONArray mensaje = response.getJSONArray("inmuebles");

                    Rent[] rents = gson.fromJson(mensaje.toString(), Rent[].class);

                    ArrayList<Rent> rentArrayList = new ArrayList<>();

                    Collections.addAll(rentArrayList, rents);

                    RentAdapter rentAdapter = new RentAdapter(rentArrayList, MainActivity.this);
                    recyclerView.setAdapter(rentAdapter);
                    break;
                case "2":
                    message = response.getString("mensaje");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    message = response.getString("mensaje");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        JsonObjectRequest jsonObjectRequest;

        switch (id) {
            case R.id.action_report:
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.GET_CITIES, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                processCitiesResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", error.toString());
                    }
                });

                VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

                return true;
            case R.id.action_search:
                new MaterialDialog.Builder(this)
                        .title(R.string.report_by)
                        .items(R.array.types)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                loadByTypes(which + 1);
                            }
                        })
                        .show();

                return true;
            case R.id.action_about:
                new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .content("Proyecto Final Android 2016-1" +
                                "\n\nAlberto Rosentiehl - 2012114114\n" +
                                "Luis Miguel Moisés - 2012214068\n\n")
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadByTypes(int which) {
        String path = Constants.GET_BY_TYPE + "?id_tipo=" + which;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Intent intent = new Intent(this, CreateUpdateActivity.class);
        intent.putExtra("action", true);
        startActivity(intent);
    }
}

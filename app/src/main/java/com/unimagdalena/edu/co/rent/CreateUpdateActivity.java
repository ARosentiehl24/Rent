package com.unimagdalena.edu.co.rent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emmasuzuki.easyform.EasyFormEditText;

import org.fingerlinks.mobile.android.navigator.Navigator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUpdateActivity extends AppCompatActivity {

    private Boolean action;
    private RentApp rentApp;

    @Bind(R.id.code)
    EasyFormEditText code;
    @Bind(R.id.types)
    AppCompatSpinner types;
    @Bind(R.id.address)
    EasyFormEditText address;
    @Bind(R.id.price)
    EasyFormEditText price;
    @Bind(R.id.city)
    EasyFormEditText city;
    @Bind(R.id.agency)
    EasyFormEditText agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update);
        ButterKnife.bind(this);

        rentApp = (RentApp) getApplication();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        action = getIntent().getBooleanExtra("action", true);

        if (!action) {
            Rent rent = (Rent) getIntent().getSerializableExtra("rent");

            code.setText(rent.getCodigo_arriendo());
            code.setEnabled(false);
            address.setText(rent.getDireccion());
            price.setText(rent.getValor_arriendo());
            city.setText(rent.getCiudad());
            agency.setText(rent.getAgencia());
            types.setSelection(getIndexFor(rent.getTipo_vivenda(), R.array.types));
            types.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        Navigator.with(this).utils().finishWithAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.submit_button)
    public void onClick() {
        if (isEmpty(code) || isEmpty(address) || isEmpty(price) || isEmpty(city) || isEmpty(agency)) {
            Toast.makeText(this, "There're empty fields", Toast.LENGTH_SHORT).show();
        } else {
            String code = getTextFrom(this.code);
            String address = getTextFrom(this.address);
            String price = getTextFrom(this.price);
            String city = getTextFrom(this.city);
            String agency = getTextFrom(this.agency);
            String type = String.valueOf(getIndexFor(getTextFrom(types), R.array.types) + 1);

            HashMap<String, String> map = new HashMap<>();

            map.put("codigo_arriendo", code);
            map.put("ciudad", city);
            map.put("direccion", address);
            map.put("agencia", agency);
            map.put("valor_arriendo", price);
            map.put("id_tipo", type);

            JSONObject jsonObject = new JSONObject(map);

            Log.d("BUG", jsonObject.toString());

            JsonObjectRequest jsonObjectRequest;

            if (action) {
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, rentApp.INSERT(), jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                processCreateUpdateResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateUpdateActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                };
            } else {
                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, rentApp.UPDATE(), jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                processCreateUpdateResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateUpdateActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                };
            }

            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    public void processCreateUpdateResponse(JSONObject response) {
        try {
            String state = response.getString("estado");
            String message = response.getString("mensaje");

            switch (state) {
                case "1":
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case "2":
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(EasyFormEditText easyFormEditText) {
        return easyFormEditText.getText().length() == 0;
    }

    public String getTextFrom(EasyFormEditText textInputEditText) {
        return textInputEditText.getText().toString();
    }

    public String getTextFrom(AppCompatSpinner appCompatSpinner) {
        return appCompatSpinner.getSelectedItem().toString();
    }

    public int getIndexFor(String item, int resId) {
        int i = 0;

        String[] androidStrings = getResources().getStringArray(resId);

        for (String string : androidStrings) {
            if (item.equals(string)) {
                break;
            }

            i++;
        }

        return i;
    }
}

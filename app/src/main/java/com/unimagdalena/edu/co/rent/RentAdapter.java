package com.unimagdalena.edu.co.rent;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentAdapter extends /*BaseQuickAdapter<Rent>*/ RecyclerView.Adapter<RentAdapter.ViewHolder> {

    private ArrayList<Rent> rents;
    private Activity activity;

    public RentAdapter(ArrayList<Rent> rents, Activity activity) {
        this.rents = rents;
        this.activity = activity;
    }

    @Override
    public RentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rent_list_item_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RentAdapter.ViewHolder holder, int position) {
        Rent rent = rents.get(position);

        holder.code.setText(String.format("Rent Code: %s", rent.getCodigo_arriendo()));
        holder.description.setText(String.format("%s en %s", rent.getTipo_vivenda(), rent.getCiudad()));
        holder.location.setText(rent.getDireccion());
        holder.price.setText(formatoPeso(rent.getValor_arriendo()));
        holder.agency.setText(rent.getAgencia());
    }

    @Override
    public int getItemCount() {
        return rents.size();
    }

    public void delete(int position) {
        rents.remove(position);
        notifyItemRemoved(position);
    }

    public static String formatoPeso(String valor) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(Double.parseDouble(valor));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.code)
        TextView code;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.agency)
        TextView agency;
        @Bind(R.id.price)
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, CreateUpdateActivity.class);
            intent.putExtra("action", false);
            intent.putExtra("rent", rents.get(getLayoutPosition()));

            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.delete);
            builder.setMessage(R.string.delete_message);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("codigo_arriendo", rents.get(getLayoutPosition()).getCodigo_arriendo());

                    JSONObject jsonObject = new JSONObject(map);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, Constants.DELETE, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    processDeleteResponse(response, getLayoutPosition());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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

                    VolleySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return false;
        }

        public void processDeleteResponse(JSONObject response, int position) {
            try {
                String state = response.getString("estado");
                String message = response.getString("mensaje");

                switch (state) {
                    case "1":
                        delete(position);
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                        break;
                    case "2":
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

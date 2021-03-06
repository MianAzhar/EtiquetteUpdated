package com.EA.Scenario.etiquette.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mian_ on 10/7/2015.
 * Fetch Etiquette from Server
 */
public class EtiquetteFetcher {

    ListView listView;
    EtiquetteListAdapter etiquetteListAdapter;
    ArrayList<Etiquette> etiquetteArrayList;
    Map<String, String> parameters;

    TransparentProgressDialog progressDialog;

    boolean showDialog;

    public void getEtiquette(final Context context, String url, ListView lv, EtiquetteListAdapter ad, ArrayList<Etiquette> list, Map<String, String> params, boolean dialog)
    {
        listView = lv;
        etiquetteListAdapter = ad;
        etiquetteArrayList = list;
        parameters = params;
        showDialog = dialog;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(showDialog) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }

                            if (MainActivity.gps.canGetLocation()) {
                                //MainActivity.location = MainActivity.gps.getLocation();

                            } else {
                                // Can't get location.
                                // GPS or network is not enabled.
                                // Ask user to enable GPS/network in settings.
                                if(MainActivity.askGps) {
                                    MainActivity.askGps = false;
                                    MainActivity.gps.showSettingsAlert();
                                }
                            }

                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success")) {
                                Gson gson = new Gson();
                                Etiquette[] result = gson.fromJson(jsonResponse.getString("data"), Etiquette[].class);
                                etiquetteArrayList.clear();
                                etiquetteListAdapter.clear();
                                etiquetteArrayList.addAll(Arrays.asList(result));
                                etiquetteListAdapter.notifyDataSetChanged();
                                MainActivity.showDialog = false;
                            }
                            else {
                                etiquetteListAdapter.clear();
                                etiquetteListAdapter.notifyDataSetChanged();
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(showDialog) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        if (MainActivity.gps.canGetLocation()) {
                            //MainActivity.location = MainActivity.gps.getLocation();

                        } else {
                            // Can't get location.
                            // GPS or network is not enabled.
                            // Ask user to enable GPS/network in settings.
                            if(MainActivity.askGps) {
                                MainActivity.gps.showSettingsAlert();
                                MainActivity.askGps = false;
                            }
                        }
                        Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return parameters;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        if(showDialog) {
            progressDialog = new TransparentProgressDialog(context, R.drawable.loading3);
            progressDialog.show();
        }

        MainActivity.networkQueue.add(postRequest);
    }

}

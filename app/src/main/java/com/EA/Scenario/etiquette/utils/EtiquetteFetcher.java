package com.EA.Scenario.etiquette.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

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

    ProgressDialog progressDialog;

    public void getEtiquette(final Context context, String url, ListView lv, EtiquetteListAdapter ad, ArrayList<Etiquette> list, Map<String, String> params)
    {
        listView = lv;
        etiquetteListAdapter = ad;
        etiquetteArrayList = list;
        parameters = params;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success")) {
                                Gson gson = new Gson();
                                Etiquette[] result = gson.fromJson(jsonResponse.getString("data"), Etiquette[].class);
                                etiquetteArrayList.clear();
                                etiquetteArrayList.addAll(Arrays.asList(result));
                                etiquetteListAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        progressDialog = null;
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

        progressDialog = ProgressDialog.show(context, null, "Fetching data", true, false);
        MainActivity.networkQueue.add(postRequest);
    }

}

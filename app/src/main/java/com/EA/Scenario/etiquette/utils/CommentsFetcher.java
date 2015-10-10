package com.EA.Scenario.etiquette.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.CommentListAdapter;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by mian_ on 10/10/2015.
 */
public class CommentsFetcher {

    ListView listView;
    CommentListAdapter etiquetteListAdapter;
    ArrayList<CommentClass> etiquetteArrayList;
    Map<String, String> parameters;

    ProgressDialog progressDialog;

    public void getComments(final Context context, ListView lv, CommentListAdapter ad, ArrayList<CommentClass> list, Map<String, String> params)
    {
        String url = "http://etiquette-app.azurewebsites.net/get-all-comment-of-scenario";
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
                                CommentClass[] result = gson.fromJson(jsonResponse.getString("data"), CommentClass[].class);
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
                        progressDialog.dismiss();
                        progressDialog = null;
                        error.printStackTrace();

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

        //RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        //postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(context, null, "Fetching data", true, false);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);

    }
}

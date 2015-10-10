package com.EA.Scenario.etiquette.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by mian_ on 10/10/2015.
 */
public class AddComment {
    Map<String , String > parameters;

    public void addComment(final Context context , Map<String , String> params)
    {
        String url = "http://etiquette-app.azurewebsites.net/add-comment";
        parameters = params;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");
                            if(msg.equals("fail"))
                            {
                                Toast.makeText(context, "Comment Not Added Something went Wrong", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "Comment Added", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);

    }
}

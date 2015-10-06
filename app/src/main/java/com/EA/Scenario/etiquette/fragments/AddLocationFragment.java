package com.EA.Scenario.etiquette.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.services.GPSTracker;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationFragment extends android.support.v4.app.Fragment {


    public static double x1;
    public static double x2;
    public static double y1;
    public static double y2;
    public static String url;
    ProgressDialog progress;

    public AddLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);


        // Check if GPS enabled
        if (MainActivity.gps.canGetLocation()) {
            double R = 6371;  // earth radius in km
            double radius = 50; // km


            double lat = MainActivity.location.getLatitude();
            double lon = MainActivity.location.getLongitude();



            x1 = lon - Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat)));

            x2 = lon + Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat)));

            y1 = lat + Math.toDegrees(radius / R);

            y2 = lat - Math.toDegrees(radius / R);

            url = "http://api.geonames.org/citiesJSON?north=" + y1 + "&south=" + y2 + "&east=" + x2 + "&west=" + x1 + "&lang=de&username=aliilyas";
            // Toast.makeText(getApplicationContext(),  "lat: " +lat+ "long" +lon, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "west " + x1 + "\n east: " + x2 + "\nnorth: " + y1 + "\nsouth: " + y2, Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            func(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });

            progress = ProgressDialog.show(getActivity(), "Processing",
                    "Receiving Data", true);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(stringRequest);
            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lon, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            MainActivity.gps.showSettingsAlert();
        }


    }

    void func(String obj)
    {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(obj);

            JSONArray array = json.getJSONArray("geonames");

            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                list.add(object.getString("name"));
            }
        }
        catch (Exception ex){
            Toast.makeText(getActivity(), "Some Exception", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);

        ListView lv = (ListView)getActivity().findViewById(R.id.listView);
        lv.setAdapter(ad);
        progress.dismiss();

        return;
    }


    }


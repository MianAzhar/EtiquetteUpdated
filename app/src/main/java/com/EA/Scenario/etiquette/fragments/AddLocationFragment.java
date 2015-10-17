package com.EA.Scenario.etiquette.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.TransparentProgressDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public static double x1;
    public static double x2;
    public static double y1;
    public static double y2;
    TransparentProgressDialog progress;

    public static String selectedCity = null;

    public static String url;
    public static String googleUrl;

    double longitude;
    double latitude;

    EditText cityNameField;

    ArrayList<String> list;

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

        cityNameField = (EditText)getActivity().findViewById(R.id.cityName);

        cityNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String name = cityNameField.getText().toString();
                    if (name.length() < 1) {
                        Toast.makeText(getActivity(), "Enter name of city", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    View v = getActivity().getCurrentFocus();
                    if (v != null) {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    getCities(name);
                    return true;
                }
                return false;
            }
        });

        ImageButton search = (ImageButton)getActivity().findViewById(R.id.searchCities);
        search.setOnClickListener(this);

        ImageView back = (ImageView)getActivity().findViewById(R.id.locationBack);
        back.setOnClickListener(this);

        ImageButton tick = (ImageButton)getActivity().findViewById(R.id.tickCities);
        tick.setOnClickListener(this);

        ListView lv = (ListView)getActivity().findViewById(R.id.listView2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                returnResult(list.get(i));
            }
        });



        if(MainActivity.location != null)
        {
            longitude = MainActivity.location.getLongitude();
            latitude = MainActivity.location.getLatitude();
            getNearByCities();
        }
        else {
            MainActivity.location = LocationServices.FusedLocationApi.getLastLocation(MainActivity.mGoogleApiClient);
            if(MainActivity.location != null)
            {
                longitude = MainActivity.location.getLongitude();
                latitude = MainActivity.location.getLatitude();
                getNearByCities();
            }
        }
    }

    void returnResult(String name)
    {
        selectedCity = name;
        AddLocationFragment newFrag1 = new AddLocationFragment();
        android.support.v4.app.FragmentTransaction trans1 = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.searchCities || view.getId() == R.id.tickCities)
        {
            String name = cityNameField.getText().toString();
            if(name.length() < 1)
            {
                Toast.makeText(getActivity(), "Enter name of city", Toast.LENGTH_SHORT).show();
                return;
            }
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            getCities(name);
        }
        else if(view.getId() == R.id.locationBack)
        {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public void getCities(String city)
    {
        city = city.replaceAll(" ", "%20");
        googleUrl = "http://maps.google.com/maps/api/geocode/json?address=" + city +"&sensor=false";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, googleUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        progress = null;
                        try
                        {
                            JSONObject  jsonObject = new JSONObject(response.toString());

                            if(getLatLong(jsonObject))
                            {
                                getNearByCities();
                            }
                            else {
                                Toast.makeText(getActivity(), "City not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e)
                        {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                progress = null;
                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

        //progress = ProgressDialog.show(getActivity(), null, "Receiving Data", true);
        progress = new TransparentProgressDialog(getActivity(), R.drawable.loading3);
        progress.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private boolean getLatLong(JSONObject jsonObject)
    {
        try {

            longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            //Toast.makeText(getActivity(),  "lat: " +latitude+ "long" +longitude, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {

            longitude=0;
            latitude = 0;

            return false;

        }

        return true;
    }

    void getNearByCities()
    {
        double R = 6371;  // earth radius in km
        double radius = 50; // km

        x1 = longitude - Math.toDegrees(radius / R / Math.cos(Math.toRadians(latitude)));

        x2 = longitude + Math.toDegrees(radius / R / Math.cos(Math.toRadians(latitude)));

        y1 = latitude + Math.toDegrees(radius / R);

        y2 = latitude - Math.toDegrees(radius / R);

        url = "http://api.geonames.org/citiesJSON?north=" + y1 + "&south=" + y2 + "&east=" + x2 + "&west=" + x1 + "&lang=de&username=aliilyas";
        // Toast.makeText(getApplicationContext(),  "lat: " +lat+ "long" +lon, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "west " + x1 + "\n east: " + x2 + "\nnorth: " + y1 + "\nsouth: " + y2, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        progress = null;
                        parseCities(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                progress = null;

                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        });

        //progress = ProgressDialog.show(getActivity(), null, "Getting cities", true);
        progress = new TransparentProgressDialog(getActivity(), com.EA.Scenario.etiquette.R.drawable.loading3);
        progress.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    void parseCities(String obj)
    {
        list = new ArrayList<>();
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

        ListView lv = (ListView)getActivity().findViewById(R.id.listView2);
        lv.setAdapter(ad);
    }


    }


package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.RoundedImageView;
import com.EA.Scenario.etiquette.utils.User;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    ProgressDialog progressDialog;
    User user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        user = new User();

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        final ImageButton editButton = (ImageButton)getActivity().findViewById(R.id.imageEdit);
        editButton.setOnClickListener(this);

        SharedPreferences pref = getActivity().getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);

        user.User_Name = pref.getString("userName", "");
        user.Phone = pref.getString("phoneNumber", "");

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/get-profile",
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
                                user = gson.fromJson(jsonResponse.getString("data"), User.class);
                                setData();
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
                        Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("language", "english");
                params.put("Phone_Number", user.Phone);
                params.put("User_Name", user.User_Name);

                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Getting Profile", true, false);
        MainActivity.networkQueue.add(postRequest);

        TextView userName = (TextView)getActivity().findViewById(R.id.userNameText);
        userName.setText(user.User_Name);

        TextView phoneNumberText = (TextView)getActivity().findViewById(R.id.phoenNumberText);
        phoneNumberText.setText(user.Phone);
    }

    void setData()
    {
        if(user.Name != null)
        {
            ((TextView)getActivity().findViewById(R.id.fullNameField)).setText(user.Name);
            ((TextView)getActivity().findViewById(R.id.displayName)).setText(user.Name);
        }
        if(user.Email != null)
        {
            ((TextView)getActivity().findViewById(R.id.emailField)).setText(user.Email);
        }
        if(user.Picture != null)
        {
            /*
            ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
            RoundedImageView pic = (RoundedImageView)getActivity().findViewById(R.id.profilePic);
            Picasso.with(getActivity()).load(user.Picture).into(cover);
            Picasso.with(getActivity()).load(user.Picture).into(pic);
            */

            Bitmap image = StringToBitMap(user.Picture);

            if(image != null)
            {
                ((ImageView)getActivity().findViewById(R.id.coverPic)).setImageBitmap(image);
                ((RoundedImageView)getActivity().findViewById(R.id.profilePic)).setImageBitmap(image);
            }

        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.imageEdit)
        {
            Bundle args = new Bundle();
            args.putSerializable("data", user);
            EditProfileFragment newFrag = new EditProfileFragment();
            newFrag.setArguments(args);
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.addToBackStack(null);
            trans.replace(R.id.fragment_container, newFrag, Constants.EditProfileFragmentTag).commit();
        }
        else if(view.getId() == R.id.drawMenu)
        {
            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);
        }
    }

}

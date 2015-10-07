package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    String[] list;
    String[] codes;
    TextView codeText;
    EditText phoneNumberField;
    ProgressDialog progressDialog;
    //String phoneNumber;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        final ImageButton signInButton = (ImageButton)getActivity().findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        phoneNumberField = (EditText)getActivity().findViewById(R.id.phoneNumber);

        codes = getResources().getStringArray(R.array.codes);
        list = getResources().getStringArray(R.array.country_name);

        codeText = (TextView)getActivity().findViewById(R.id.countryCode);

        Spinner name = (Spinner)getActivity().findViewById(R.id.countryName);

        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(dataAdapter);

        name.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
            codeText.setText(codes[pos]);
        }

        @Override
        public void onNothingSelected(AdapterView parent) {

        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.signInButton)
        {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            /*
            SignUpFragment newFrag = new SignUpFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.SignUpFragmentTag).commit();
            */
            verifyNumber();
        }
    }

    public void verifyNumber()
    {
        String phone = phoneNumberField.getText().toString();
        if (phone.length() < 1)
        {
            Toast.makeText(getActivity(), "Enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        //final ProgressDialog progressDialog = new ProgressDialog(getActivity(), null, "Siging In", true);
        final String phoneNumber = codeText.getText().toString() + phone;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/signin-signup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("message");

                            if(msg.equals("Signed in successfully. Phone number is verified"))
                            {
                                String userName = jsonResponse.getString("data");
                                if(userName.length() < 1)
                                {
                                    SignUpFragment newFrag = new SignUpFragment();
                                    Bundle args = new Bundle();
                                    args.putString("phoneNumber", phoneNumber);
                                    newFrag.setArguments(args);
                                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    trans.replace(R.id.fragment_container, newFrag, Constants.SignUpFragmentTag).commit();
                                }
                                else {
                                    SharedPreferences pref = getActivity().getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putString("userName", userName);
                                    editor.putString("phoneNumber", phoneNumber);
                                    editor.commit();

                                    MainActivity.userName = userName;

                                    PopularFragment newFrag = new PopularFragment();
                                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    trans.replace(R.id.fragment_container, newFrag, Constants.PopularFragmentTag).commit();
                                }
                            }
                            else if(msg.equals("Signed in successfully. After adding phone number"))
                            {
                                SignUpFragment newFrag = new SignUpFragment();
                                Bundle args = new Bundle();
                                args.putString("phoneNumber", phoneNumber);
                                newFrag.setArguments(args);
                                android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                trans.replace(R.id.fragment_container, newFrag, Constants.SignUpFragmentTag).commit();
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
                params.put("phoneNumber", phoneNumber);
                return params;
            }
        };

        progressDialog = ProgressDialog.show(getActivity(), null, "Signing In", true, false);
        MainActivity.networkQueue.add(postRequest);
    }

}

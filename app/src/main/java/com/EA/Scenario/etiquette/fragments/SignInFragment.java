package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    String[] list;
    String[] codes;
    TextView codeText;

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
            SignUpFragment newFrag = new SignUpFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.SignUpFragmentTag).commit();
        }
    }

}

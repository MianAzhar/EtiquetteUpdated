package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        final ImageButton signUpButton = (ImageButton)getActivity().findViewById(R.id.doneSignUpButton);
        signUpButton.setOnClickListener(this);

        final ImageView pic = (ImageView)getActivity().findViewById(R.id.profilePic);
        pic.setOnClickListener(this);

        final ImageView username = (ImageView)getActivity().findViewById(R.id.generateUsername);
        username.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                ImageView image = (ImageView)getActivity().findViewById(R.id.profilePic);
                image.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.doneSignUpButton)
        {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            PopularFragment newFrag = new PopularFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "PopularFragment").commit();
        }
        else if(view.getId() == R.id.profilePic)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
        else if (view.getId() == R.id.generateUsername)
        {
            EditText et = (EditText)getActivity().findViewById(R.id.realName);
            String get = et.getText().toString();
            if(get !=null) {
                Random r = new Random();
                int Low = 100;
                int High = 999;
                int num = r.nextInt(High - Low) + Low;
                get = get + num;
                TextView tv = (TextView)getActivity().findViewById(R.id.userName);
                tv.setText(get);
            }
            else{

                Toast.makeText(getActivity(), "Enter Your Name First", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

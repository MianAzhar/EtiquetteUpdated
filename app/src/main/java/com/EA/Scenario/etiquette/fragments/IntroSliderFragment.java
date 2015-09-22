package com.EA.Scenario.etiquette.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.EA.Scenario.etiquette.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroSliderFragment extends android.support.v4.app.Fragment {


    public IntroSliderFragment() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment getInstance(int position)
    {
        return new IntroSliderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_intro_slider, container, false);
    }


}

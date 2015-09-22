package com.EA.Scenario.etiquette.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.EA.Scenario.etiquette.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoiceFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public ChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choice, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        LinearLayout c1 = (LinearLayout)getActivity().findViewById(R.id.choice_1_layout);
        LinearLayout c2 = (LinearLayout)getActivity().findViewById(R.id.choice_2_layout);
        LinearLayout c3 = (LinearLayout)getActivity().findViewById(R.id.choice_3_layout);
        LinearLayout c4 = (LinearLayout)getActivity().findViewById(R.id.choice_4_layout);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.drawMenu)
        {
            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);
        }
        else if(view.getId() == R.id.choice_1_layout || view.getId() == R.id.choice_2_layout || view.getId() == R.id.choice_3_layout || view.getId() == R.id.choice_4_layout)
        {
            AnswerFragment newFrag = new AnswerFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.addToBackStack(null);
            trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
        }
    }


}

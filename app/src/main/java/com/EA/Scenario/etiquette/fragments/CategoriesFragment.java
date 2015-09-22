package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.EA.Scenario.etiquette.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton latest = (ImageButton) getActivity().findViewById(R.id.latestButton_categories);
        ImageButton popular = (ImageButton) getActivity().findViewById(R.id.popularButton_categories);

        latest.setOnClickListener(this);
        popular.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.popularButton_categories)
        {
            PopularFragment newFrag = new PopularFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "PopularFragment").commit();
        }
        else if(view.getId() == R.id.latestButton_categories)
        {
            LatestFragment newFrag = new LatestFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "LatestFragment").commit();
        }
        else if(view.getId() == R.id.drawMenu)
        {

            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);

            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);

        }
    }

}

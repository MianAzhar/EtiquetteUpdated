package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    public LatestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton latest = (ImageButton)getActivity().findViewById(R.id.popularButton_latest);
        ImageButton categories = (ImageButton)getActivity().findViewById(R.id.categoryButton_latest);

        latest.setOnClickListener(this);
        categories.setOnClickListener(this);

        final ListView list = (ListView)getActivity().findViewById(R.id.latestList);

        list.setAdapter(MainActivity.adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    NoChoiceFragment newFrag = new NoChoiceFragment();
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, "NoChoiceFragment").commit();
                }
                else if(i == 1)
                {
                    AnswerFragment newFrag = new AnswerFragment();
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
                }
                else if(i == 2)
                {
                    ChoiceFragment newFrag = new ChoiceFragment();
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, "ChoiceFragment").commit();
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.popularButton_latest)
        {
            PopularFragment newFrag = new PopularFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "PopularFragment").commit();
        }
        else if(view.getId() == R.id.categoryButton_latest)
        {
            CategoriesFragment newFrag = new CategoriesFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "CategoriesFragment").commit();
        }
        else if(view.getId() == R.id.drawMenu)
        {
            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);
        }
    }

}

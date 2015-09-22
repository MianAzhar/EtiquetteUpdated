package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.adapters.DiscoverListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends android.support.v4.app.Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        ArrayList<String> list = new ArrayList<String>();
        list.add("City 1");
        list.add("City 2");
        list.add("City 3");
        list.add("City 4");
        list.add("City 5");
        list.add("City 6");
        list.add("City 7");

        DiscoverListAdapter ad = new DiscoverListAdapter(getActivity(), list);
        ListView textView = (ListView)getActivity().findViewById(R.id.listView);
        textView.setAdapter(ad);

    }


}

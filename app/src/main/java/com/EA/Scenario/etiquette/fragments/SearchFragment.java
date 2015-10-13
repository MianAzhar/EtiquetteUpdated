package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.DiscoverListAdapter;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.EtiquetteFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    boolean tagsSelected = true;
    boolean peopleSelected = false;
    boolean placesSelected = false;

    EditText searchText;

    ListView list;

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

        searchText = (EditText)getActivity().findViewById(R.id.searchText);

        Bundle args = getArguments();
        String text = args.getString("text", "");

        ImageButton back = (ImageButton)getActivity().findViewById(R.id.searchBackButton);
        back.setOnClickListener(this);

        if(text.length() > 0)
        {
            searchText.setText(text);
            Map<String, String> params = new HashMap<>();
            params.put("Hash_Tags", "#" + text);
            getEtiquette(Constants.TagScenario, params);
        }



        ImageButton tags = (ImageButton)getActivity().findViewById(R.id.tags);
        tags.setOnClickListener(this);

        ImageButton places = (ImageButton)getActivity().findViewById(R.id.places);
        places.setOnClickListener(this);

        ImageButton people = (ImageButton)getActivity().findViewById(R.id.people);
        people.setOnClickListener(this);

        ImageButton search = (ImageButton)getActivity().findViewById(R.id.searchIcon);
        search.setOnClickListener(this);

        list = (ListView)getActivity().findViewById(R.id.searchList);

        list.setAdapter(MainActivity.adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("data", MainActivity.etiquetteList.get(i));
                args.putInt("index", i);

                if (MainActivity.etiquetteList.get(i).Scenario_Option_1 == null) {
                    NoChoiceFragment newFrag = new NoChoiceFragment();
                    newFrag.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //trans.addToBackStack(null);
                    //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                    //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                } else {
                    ChoiceFragment newFrag = new ChoiceFragment();
                    newFrag.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, Constants.ChoiceFragmentTag).commit();
                }
            }
        });

    }

    @Override
    public void onClick(View view)
    {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        Map<String, String> params = new HashMap<>();

        if(view.getId() == R.id.tags)
        {
            if(tagsSelected)
                return;

            peopleSelected = false;
            placesSelected = false;
            tagsSelected = true;

            if(searchText.getText().toString().length() > 1)
            {
                params.put("Hash_Tags", "#" + searchText.getText().toString());
                getEtiquette(Constants.TagScenario, params);
            }
        }
        else if(view.getId() == R.id.places)
        {
            if(placesSelected)
                return;

            peopleSelected = false;
            placesSelected = true;
            tagsSelected = false;

            if(searchText.getText().toString().length() > 1)
            {
                params.put("Location", searchText.getText().toString());
                getEtiquette(Constants.PlaceScenario, params);
            }
        }
        else if(view.getId() == R.id.people)
        {
            if(peopleSelected)
                return;

            peopleSelected = true;
            placesSelected = false;
            tagsSelected = false;

            if(searchText.getText().toString().length() > 1)
            {
                params.put("User_Name", searchText.getText().toString());
                getEtiquette(Constants.UserScenario, params);
            }
        }
        else if(view.getId() == R.id.searchIcon)
        {
            if(searchText.getText().toString().length() < 1) {
                Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT).show();
                return;
            }

            if(tagsSelected)
            {
                params.put("Hash_Tags", "#" + searchText.getText().toString());
                getEtiquette(Constants.TagScenario, params);
            }
            else if(placesSelected)
            {
                params.put("Location", searchText.getText().toString());
                getEtiquette(Constants.PlaceScenario, params);
            }
            else {
                params.put("User_Name", searchText.getText().toString());
                getEtiquette(Constants.UserScenario, params);
            }
        }
        else if(view.getId() == R.id.searchBackButton)
        {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    void getEtiquette(String tag, Map<String, String> params)
    {
        String url = "http://etiquette-app.azurewebsites.net/" + tag;

        // the POST parameters:
        params.put("language", "english");

        EtiquetteFetcher etiquetteFetcher = new EtiquetteFetcher();
        etiquetteFetcher.getEtiquette(getActivity(), url, list, MainActivity.adapter, MainActivity.etiquetteList, params);

    }

}

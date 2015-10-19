package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.EA.Scenario.etiquette.utils.EtiquetteFetcher;
import com.EA.Scenario.etiquette.utils.TransparentProgressDialog;
import com.EA.Scenario.etiquette.utils.UpdateCounter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public static ArrayList<Etiquette> etiquetteList1;
    public static EtiquetteListAdapter adapter1 = null;

    public static boolean showDialog = true;

    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        if(adapter1 == null) {
            etiquetteList1 = new ArrayList<>();

            adapter1 = new EtiquetteListAdapter(getActivity(), etiquetteList1);
        }

        DrawerLayout drawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton latest = (ImageButton)getActivity().findViewById(R.id.latestButton_popular);
        ImageButton categories = (ImageButton)getActivity().findViewById(R.id.categoryButton_popular);

        latest.setOnClickListener(this);
        categories.setOnClickListener(this);

        final ListView list = (ListView)getActivity().findViewById(R.id.popularList);

        list.setAdapter(adapter1);

        /*
        SharedPreferences pref = getActivity().getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);

        String user = pref.getString("userName", "");

        Map<String, String> params = new HashMap<>();
        // the POST parameters:
        params.put("language", "english");
        params.put("User_Name", user);

        EtiquetteFetcher etiquetteFetcher = new EtiquetteFetcher();
        etiquetteFetcher.getEtiquette(getActivity(), "http://etiquette-app.azurewebsites.net/get-all-scenarios-of-user", list, MainActivity.adapter, MainActivity.etiquetteList, params);
        */

        Map<String, String> params = new HashMap<>();
        // the POST parameters:
        params.put("language", "english");
        params.put("is_Popular", "true");

        EtiquetteFetcher etiquetteFetcher = new EtiquetteFetcher();
        etiquetteFetcher.getEtiquette(getActivity(), "http://etiquette-app.azurewebsites.net/get-all-scenarios", list, adapter1, etiquetteList1, params, showDialog);

        showDialog = false;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable("data", MainActivity.etiquetteList.get(i));
                args.putInt("index", i);

                if(MainActivity.etiquetteList.get(i).Scenario_Option_1 == null)
                {
                    NoChoiceFragment newFrag = new NoChoiceFragment();
                    newFrag.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //trans.addToBackStack(null);
                    //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                    //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                }
                else
                {
                    if(MainActivity.etiquetteList.get(i).Scenario_Option_1.length() > 0) {
                        ChoiceFragment newFrag = new ChoiceFragment();
                        newFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.addToBackStack(null);
                        trans.replace(R.id.fragment_container, newFrag, Constants.ChoiceFragmentTag).commit();
                    }
                    else
                    {
                        NoChoiceFragment newFrag = new NoChoiceFragment();
                        newFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.addToBackStack(null);
                        //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                        //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.latestButton_popular)
        {
            LatestFragment newFrag = new LatestFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.LatestFragmentTag).commit();
        }
        else if(view.getId() == R.id.categoryButton_popular)
        {
            CategoriesFragment newFrag = new CategoriesFragment();

            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.CategoriesFragmentTag).commit();
        }
        else if(view.getId() == R.id.drawMenu)
        {
            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);
        }
    }

}

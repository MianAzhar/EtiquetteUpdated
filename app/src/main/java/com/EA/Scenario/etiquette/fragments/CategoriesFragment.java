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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.CategoriesAdapter;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.EA.Scenario.etiquette.utils.EtiquetteFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    ArrayList<String> categoryList;

    ListView list;
    //EtiquetteListAdapter listAdapter;
    //ArrayList<Etiquette> etiquetteArrayList;

    //GridView gridView;

    LinearLayout categories;

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

        categories = (LinearLayout)getActivity().findViewById(R.id.items);

        ImageButton travel = (ImageButton)getActivity().findViewById(R.id.travelButton);
        travel.setOnClickListener(this);

        ImageButton toilet = (ImageButton)getActivity().findViewById(R.id.toiletButton);
        toilet.setOnClickListener(this);

        ImageButton road = (ImageButton)getActivity().findViewById(R.id.roadButton);
        road.setOnClickListener(this);

        ImageButton other = (ImageButton)getActivity().findViewById(R.id.otherButton);
        other.setOnClickListener(this);

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);



        list = (ListView)getActivity().findViewById(R.id.categoryList);
        //etiquetteArrayList = new ArrayList<>();
        //listAdapter = new EtiquetteListAdapter(getActivity(), etiquetteArrayList);
        list.setAdapter(MainActivity.adapter);

        ImageButton latest = (ImageButton) getActivity().findViewById(R.id.latestButton_categories);
        ImageButton popular = (ImageButton) getActivity().findViewById(R.id.popularButton_categories);
        ImageButton cat = (ImageButton) getActivity().findViewById(R.id.categoryButton_categories);

        latest.setOnClickListener(this);
        popular.setOnClickListener(this);
        cat.setOnClickListener(this);

        /*
        gridView = (GridView)getActivity().findViewById(R.id.gridView);

        categoryList = new ArrayList<>();

        categoryList.add("TRAVEL");
        categoryList.add("TOILET");
        categoryList.add("ROAD & TRAFFIC");
        categoryList.add("OTHER");
        //arrayList.add("Category 5");
        //arrayList.add("Category 6");

        int[] res = new int[]{R.drawable.travel, R.drawable.toilet, R.drawable.road_and_traffic, R.drawable.other};

        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), categoryList, res);

        gridView.setAdapter(adapter);




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Category_Name", categoryList.get(position));

                EtiquetteFetcher etiquetteFetcher = new EtiquetteFetcher();
                etiquetteFetcher.getEtiquette(getActivity(), "http://etiquette-app.azurewebsites.net/get-all-scenarios-of-category", list, MainActivity.adapter, MainActivity.etiquetteList, params);

                ((TextView)getActivity().findViewById(R.id.screenTitle)).setText(categoryList.get(position));

                gridView.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });

*/
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
                    //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                    //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                }
                else
                {
                    ChoiceFragment newFrag = new ChoiceFragment();
                    newFrag.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, Constants.ChoiceFragmentTag).commit();
                }
            }
        });

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
        else if(view.getId() == R.id.categoryButton_categories)
        {
            ((TextView)getActivity().findViewById(R.id.screenTitle)).setText("CATEGORIES");
            list.setVisibility(View.GONE);
            categories.setVisibility(View.VISIBLE);
        }
        else if(view.getId() == R.id.travelButton)
        {
            getPosts("travel");
        }
        else if(view.getId() == R.id.toiletButton)
        {
            getPosts("toilet");
        }
        else if(view.getId() == R.id.roadButton)
        {
            getPosts("ROAD & TRAFFIC");
        }
        else if(view.getId() == R.id.otherButton)
        {
            getPosts("other");
        }
    }

    void getPosts(String cat)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Category_Name", cat);

        EtiquetteFetcher etiquetteFetcher = new EtiquetteFetcher();
        etiquetteFetcher.getEtiquette(getActivity(), "http://etiquette-app.azurewebsites.net/get-all-scenarios-of-category", list, MainActivity.adapter, MainActivity.etiquetteList, params);

        ((TextView)getActivity().findViewById(R.id.screenTitle)).setText(cat);

        categories.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }

}

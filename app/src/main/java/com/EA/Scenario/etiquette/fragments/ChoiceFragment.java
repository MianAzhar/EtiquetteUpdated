package com.EA.Scenario.etiquette.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoiceFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    Etiquette etiquette;

    ArrayList<View> choicesView;

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

        choicesView = new ArrayList<>();

        Bundle args = getArguments();
        etiquette = (Etiquette)args.getSerializable("data");

        if(etiquette.Scenario_Option_1.length() > 0)
        {
            addOption(etiquette.Scenario_Option_1, "A");
        }

        if(etiquette.Scenario_Option_2.length() > 0)
        {
            addOption(etiquette.Scenario_Option_2, "B");
        }

        if(etiquette.Scenario_Option_3.length() > 0)
        {
            addOption(etiquette.Scenario_Option_3, "C");
        }

        if(etiquette.Scenario_Option_4.length() > 0)
        {
            addOption(etiquette.Scenario_Option_4, "D");
        }

        if(etiquette.Scenario_Option_5.length() > 0)
        {
            addOption(etiquette.Scenario_Option_5, "E");
        }

        if(etiquette.Scenario_Option_6.length() > 0)
        {
            addOption(etiquette.Scenario_Option_6, "F");
        }

        if(etiquette.Scenario_Option_7.length() > 0)
        {
            addOption(etiquette.Scenario_Option_7, "G");
        }

        if(etiquette.Scenario_Option_8.length() > 0)
        {
            addOption(etiquette.Scenario_Option_8, "H");
        }

        if(etiquette.Scenario_Option_9.length() > 0)
        {
            addOption(etiquette.Scenario_Option_9, "I");
        }



        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton share = (ImageButton)getActivity().findViewById(R.id.shareButton);
        share.setOnClickListener(this);

        TextView title = (TextView)getActivity().findViewById(R.id.titleText);
        title.setText(etiquette.Scenario_Description);

        TextView type = (TextView)getActivity().findViewById(R.id.typeText);
        type.setText(etiquette.Category_Name);

        TextView userName = (TextView)getActivity().findViewById(R.id.userName);
        userName.setText(etiquette.User_Full_Name);

        TextView views = (TextView)getActivity().findViewById(R.id.views);
        views.setText(etiquette.Scenario_Number_Of_Views);

        if(etiquette.Scenario_Picture != null)
        {
            ImageView img = (ImageView)getActivity().findViewById(R.id.etiquetetImage);
            ///img.setImageBitmap(StringToBitMap(textList.get(position).Scenario_Picture));
            Picasso.with(getActivity()).load(etiquette.Scenario_Picture).into(img);
        }

        if(etiquette.User_Picture != null)
        {
            ImageView img = (ImageView)getActivity().findViewById(R.id.userImage);
            //img.setImageBitmap(StringToBitMap(textList.get(position).User_Picture));
            Picasso.with(getActivity()).load(etiquette.User_Picture).into(img);
        }

        ImageView rating = (ImageView)getActivity().findViewById(R.id.ratingImage);

        switch (etiquette.Scenario_Level)
        {
            case 0:
                rating.setImageResource(R.drawable._10);
                break;
            case 1:
                rating.setImageResource(R.drawable._5);
                break;
            case 2:
                rating.setImageResource(R.drawable._0);
                break;
            case 3:
                rating.setImageResource(R.drawable.__5);
                break;
            case 4:
                rating.setImageResource(R.drawable.__10);
                break;
            default:
                rating.setImageResource(R.drawable._0);
                break;
        }

        /*
        LinearLayout c1 = (LinearLayout)getActivity().findViewById(R.id.choice_1_layout);
        LinearLayout c2 = (LinearLayout)getActivity().findViewById(R.id.choice_2_layout);
        LinearLayout c3 = (LinearLayout)getActivity().findViewById(R.id.choice_3_layout);
        LinearLayout c4 = (LinearLayout)getActivity().findViewById(R.id.choice_4_layout);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        */

        SwipyRefreshLayout m = (SwipyRefreshLayout)getActivity().findViewById(R.id.scrollView1);

        m.setRefreshing(false);


        m.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                AnswerFragment newFrag = new AnswerFragment();
                android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                trans.addToBackStack(null);
                trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
            }
        });

    }

    void addOption(String text, String alphabet)
    {
        if(text.length() > 0)
        {
            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View row = inflator.inflate(R.layout.etiquette_choice_layout, null);
            row.setOnClickListener(this);

            TextView num = (TextView)row.findViewById(R.id.choiceNumber);
            num.setText(alphabet);

            TextView t = (TextView)row.findViewById(R.id.choiceText);
            t.setText(text);

            LinearLayout l = (LinearLayout) getActivity().findViewById(R.id.choice_layout);
            l.addView(row);

            choicesView.add(row);
        }
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
        else if(view.getId() == R.id.shareButton){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is text of etiquette.");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Via"));
        }
        else if(view.getId() == R.id.choiceView)
        {
            AnswerFragment newFrag = new AnswerFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.addToBackStack(null);
            //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
            trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
        }
    }


}

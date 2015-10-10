package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.AddComment;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    ArrayList<View> answerLayout;

    Etiquette etiquette;

    int max, min;

    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        answerLayout = new ArrayList<>();

        Bundle args = getArguments();

        etiquette = (Etiquette)args.getSerializable("data");

        String op = args.getString("option");



        if(etiquette.Scenario_Option_1.length() > 0)
        {
            min = etiquette.Scenario_Value_1;
            max = etiquette.Scenario_Value_1;

            addOption(etiquette.Scenario_Option_1, etiquette.Scenario_Value_1);
        }

        if(etiquette.Scenario_Option_2.length() > 0)
        {
            addOption(etiquette.Scenario_Option_2, etiquette.Scenario_Value_2);
        }

        if(etiquette.Scenario_Option_3.length() > 0)
        {
            addOption(etiquette.Scenario_Option_3, etiquette.Scenario_Value_3);
        }

        if(etiquette.Scenario_Option_4.length() > 0)
        {
            addOption(etiquette.Scenario_Option_4, etiquette.Scenario_Value_4);
        }

        if(etiquette.Scenario_Option_5.length() > 0)
        {
            addOption(etiquette.Scenario_Option_5, etiquette.Scenario_Value_5);
        }

        if(etiquette.Scenario_Option_6.length() > 0)
        {
            addOption(etiquette.Scenario_Option_6, etiquette.Scenario_Value_6);
        }

        if(etiquette.Scenario_Option_7.length() > 0)
        {
            addOption(etiquette.Scenario_Option_7, etiquette.Scenario_Value_7);
        }

        if(etiquette.Scenario_Option_8.length() > 0)
        {
            addOption(etiquette.Scenario_Option_8, etiquette.Scenario_Value_8);
        }

        if(etiquette.Scenario_Option_9.length() > 0)
        {
            addOption(etiquette.Scenario_Option_9, etiquette.Scenario_Value_9);
        }

        for(int i = 0; i < answerLayout.size(); i++)
        {
            View row = answerLayout.get(i);

            String temp = ((TextView)row.findViewById(R.id.optionValue)).getText().toString();

            if(temp.equals(max + "%"))
                ((FrameLayout)row.findViewById(R.id.answerBackground)).setBackgroundResource(R.drawable.option1);
            else if(temp.equals(min + "%"))
                ((FrameLayout)row.findViewById(R.id.answerBackground)).setBackgroundResource(R.drawable.option4);
        }

        TextView choose = (TextView)getActivity().findViewById(R.id.choosenOption);
        choose.setText("YOU CHOSE OPTION \"" + op + "\"");

        TextView title = (TextView)getActivity().findViewById(R.id.titleText);
        title.setText(etiquette.Scenario_Description);

        TextView type = (TextView)getActivity().findViewById(R.id.typeText);
        type.setText(etiquette.Category_Name);

        TextView userName = (TextView)getActivity().findViewById(R.id.userName);
        userName.setText(etiquette.User_Full_Name);

        TextView views = (TextView)getActivity().findViewById(R.id.views);
        views.setText(etiquette.Scenario_Number_Of_Views);

        TextView time = (TextView)getActivity().findViewById(R.id.time);
        long t = System.currentTimeMillis() - etiquette.Scenario_Entry_Time;
        time.setText(t/3600000 + "h");

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


        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton submitComment = (ImageButton)getActivity().findViewById(R.id.submitComment);
        submitComment.setOnClickListener(this);

        ScrollView scroll = (ScrollView)getActivity().findViewById(R.id.answerScroll);

        LinearLayout moreComments = (LinearLayout)getActivity().findViewById(R.id.loadComments);
        moreComments.setOnClickListener(this);
    }

    void addOption(String text, int alphabet)
    {
        if(text.length() > 0)
        {
            if(min > alphabet)
                min = alphabet;
            if(max < alphabet)
                max = alphabet;

            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View row = inflator.inflate(R.layout.answer_layout, null);
            row.setOnClickListener(this);

            TextView num = (TextView)row.findViewById(R.id.optionValue);
            num.setText(alphabet + "%");

            TextView t = (TextView)row.findViewById(R.id.optionText);
            t.setText(text);

            LinearLayout l = (LinearLayout) getActivity().findViewById(R.id.answerLayout);
            l.addView(row);

            answerLayout.add(row);
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
        else if(view.getId() == R.id.submitComment)
        {
            String text = ((EditText)getActivity().findViewById(R.id.commentText)).getText().toString();

            if(text.length() < 1)
            {
                Toast.makeText(getActivity(), "Enter comment", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                AddComment com = new AddComment();

                Map<String, String> params = new HashMap<>();

                params.put("Scenario_Id", etiquette.Etiquette_Id);
                params.put("Comment", text);
                params.put("User_Name", MainActivity.userName);

                com.addComment(getActivity(), params);

                ((EditText)getActivity().findViewById(R.id.commentText)).setText("");
                //Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.loadComments)
        {
            Bundle args = new Bundle();
            args.putString("data", etiquette.Etiquette_Id);

            CommentsFragment newFrag = new CommentsFragment();
            newFrag.setArguments(args);
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            trans.addToBackStack(null);
            trans.replace(R.id.fragment_container, newFrag, "CommentsFragment").commit();
        }
    }




}

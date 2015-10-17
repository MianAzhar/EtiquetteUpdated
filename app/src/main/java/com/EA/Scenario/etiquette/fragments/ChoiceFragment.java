package com.EA.Scenario.etiquette.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.EA.Scenario.etiquette.utils.RoundedImageView;
import com.EA.Scenario.etiquette.utils.TransparentProgressDialog;
import com.EA.Scenario.etiquette.utils.UpdateCounter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChoiceFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    Etiquette etiquette;

    int index;
    String selectedOption;

    SwipyRefreshLayout swipeUp;

    ArrayList<View> choicesView;

    TransparentProgressDialog progressDialog;

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
        index = args.getInt("index", 0);

        UpdateCounter counter = new UpdateCounter();

        Map<String, String> map = new HashMap<String, String>();

        map.put("Scenario_Id", etiquette.Etiquette_Id);

        counter.updateCounter(getActivity(), map);

        if(etiquette.Scenario_Option_1 != null)
        {
            addOption(etiquette.Scenario_Option_1, "A");
        }

        if(etiquette.Scenario_Option_2 != null)
        {
            addOption(etiquette.Scenario_Option_2, "B");
        }

        if(etiquette.Scenario_Option_3 != null)
        {
            addOption(etiquette.Scenario_Option_3, "C");
        }

        if(etiquette.Scenario_Option_4 != null)
        {
            addOption(etiquette.Scenario_Option_4, "D");
        }

        if(etiquette.Scenario_Option_5 != null)
        {
            addOption(etiquette.Scenario_Option_5, "E");
        }

        if(etiquette.Scenario_Option_6 != null)
        {
            addOption(etiquette.Scenario_Option_6, "F");
        }

        if(etiquette.Scenario_Option_7 != null)
        {
            addOption(etiquette.Scenario_Option_7, "G");
        }

        if(etiquette.Scenario_Option_8 != null)
        {
            addOption(etiquette.Scenario_Option_8, "H");
        }

        if(etiquette.Scenario_Option_9 != null)
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

        swipeUp = (SwipyRefreshLayout)getActivity().findViewById(R.id.scrollView1);


        swipeUp.setRefreshing(false);


        swipeUp.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(index < MainActivity.etiquetteList.size() - 1) {
                    Bundle args = new Bundle();
                    args.putSerializable("data", MainActivity.etiquetteList.get(index+1));
                    args.putInt("index", index+1);

                    if(MainActivity.etiquetteList.get(index+1).Scenario_Option_1 == null)
                    {
                        NoChoiceFragment newFrag = new NoChoiceFragment();
                        newFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        //trans.addToBackStack(null);
                        trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                        trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                    }
                    else
                    {
                        if(MainActivity.etiquetteList.get(index+1).Scenario_Option_1.length() > 0) {
                            ChoiceFragment newFrag = new ChoiceFragment();
                            newFrag.setArguments(args);
                            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //trans.addToBackStack(null);
                            trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                            trans.replace(R.id.fragment_container, newFrag, Constants.ChoiceFragmentTag).commit();
                        }
                        else
                        {
                            NoChoiceFragment newFrag = new NoChoiceFragment();
                            newFrag.setArguments(args);
                            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //trans.addToBackStack(null);
                            trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                            trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                        }
                    }
                }
                else {
                    swipeUp.setRefreshing(false);
                }
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, etiquette.Scenario_Description);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Via"));
        }
        else if(view.getId() == R.id.choiceView)
        {
            /*
            AnswerFragment newFrag = new AnswerFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.addToBackStack(null);
            //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
            trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
            */

            TextView chNo = (TextView)view.findViewById(R.id.choiceNumber);
            selectedOption = chNo.getText().toString();

            choiceSelected(selectedOption);
        }
    }

    void choiceSelected(String choice)
    {
        final int number = choice.charAt(0) - 64;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/get-scenario-option-result",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;

                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("message");

                            if(msg.equals("Result is extracted successfully")) {
                                JSONObject data = jsonResponse.getJSONObject("data");
                                etiquette.Scenario_Value_1 = data.getInt("Option1");
                                etiquette.Scenario_Value_2 = data.getInt("Option2");
                                etiquette.Scenario_Value_3 = data.getInt("Option3");
                                etiquette.Scenario_Value_4 = data.getInt("Option4");
                                etiquette.Scenario_Value_5 = data.getInt("Option5");
                                etiquette.Scenario_Value_6 = data.getInt("Option6");
                                etiquette.Scenario_Value_7 = data.getInt("Option7");
                                etiquette.Scenario_Value_8 = data.getInt("Option8");
                                etiquette.Scenario_Value_9 = data.getInt("Option9");


                                int no = data.getInt("Total_Comments");

                                Bundle args = new Bundle();
                                args.putInt("totalComments", no);

                                String comments = jsonResponse.getString("Comments");
                                args.putString("comments", comments);
                                gotoAnswer(args);
                            }
                            else if(msg.equals("Option is already updated by user")){
                                JSONObject data = jsonResponse.getJSONObject("data");
                                int no = data.getInt("Total_Comments");

                                Bundle args = new Bundle();
                                args.putInt("totalComments", no);

                                String comments = jsonResponse.getString("Comments");
                                args.putString("comments", comments);
                                gotoAnswer(args);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(MainActivity.showDialog) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("Selected_Option", Integer.toString(number));
                parameters.put("Scenario_Id", etiquette.Etiquette_Id);
                parameters.put("User_Name", etiquette.User_Name);

                return parameters;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = new TransparentProgressDialog(getActivity(), R.drawable.loading3);
        progressDialog.show();
        //progressDialog = ProgressDialog.show(getActivity(), null, "Fetching data", true, false);

        MainActivity.networkQueue.add(postRequest);
    }

    void gotoAnswer(Bundle args)
    {
        args.putSerializable("data", etiquette);
        args.putString("option", selectedOption);


        AnswerFragment newFrag = new AnswerFragment();
        newFrag.setArguments(args);
        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        trans.addToBackStack(null);
        //trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
        trans.replace(R.id.fragment_container, newFrag, "AnswerFragment").commit();
    }

}

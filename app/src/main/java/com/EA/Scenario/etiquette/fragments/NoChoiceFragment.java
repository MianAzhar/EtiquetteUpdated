package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.AddComment;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.EA.Scenario.etiquette.utils.RoundedImageView;
import com.EA.Scenario.etiquette.utils.UpdateCounter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoChoiceFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    Etiquette etiquette;

    ProgressDialog progressDialog;



    int index;

    SwipyRefreshLayout swipeUp;

    public NoChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_no_choice, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        Bundle args = getArguments();
        etiquette = (Etiquette)args.getSerializable("data");
        index = args.getInt("index", 0);

        UpdateCounter counter = new UpdateCounter();

        Map<String, String> map = new HashMap<String, String>();

        map.put("Scenario_Id", etiquette.Etiquette_Id);

        counter.updateCounter(getActivity(), map);

        ImageButton submitComment = (ImageButton)getActivity().findViewById(R.id.submitComment);
        submitComment.setOnClickListener(this);

        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        LinearLayout moreComments = (LinearLayout)getActivity().findViewById(R.id.loadComments);
        moreComments.setOnClickListener(this);

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

        getComments();

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

        swipeUp = (SwipyRefreshLayout)getActivity().findViewById(R.id.scrollView1);


        swipeUp.setRefreshing(false);


        swipeUp.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(index < MainActivity.etiquetteList.size() - 1) {
                    Bundle args = new Bundle();
                    args.putSerializable("data", MainActivity.etiquetteList.get(index+1));
                    args.putInt("index", index+1);

                    if(MainActivity.etiquetteList.get(index+1).Scenario_Option_1.length() < 1)
                    {
                        NoChoiceFragment newFrag = new NoChoiceFragment();
                        newFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.addToBackStack(null);
                        trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                        trans.replace(R.id.fragment_container, newFrag, Constants.NoChoiceFragmentTag).commit();
                    }
                    else
                    {
                        ChoiceFragment newFrag = new ChoiceFragment();
                        newFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.addToBackStack(null);
                        trans.setCustomAnimations(R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom);
                        trans.replace(R.id.fragment_container, newFrag, Constants.ChoiceFragmentTag).commit();
                    }
                }
                else {
                    swipeUp.setRefreshing(false);
                }
            }
        });
    }

    void getComments()
    {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/get-scenario-option-result",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;

                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success")) {
                                JSONObject data = jsonResponse.getJSONObject("data");

                                int no = data.getInt("Total_Comments");

                                ((TextView)getActivity().findViewById(R.id.numberOfComments)).setText(no + "");

                                JSONArray comments = jsonResponse.getJSONArray("Comments");

                                if(comments.length() > 0)
                                {
                                    JSONObject obj1 = comments.getJSONObject(0);

                                    LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                                    View row1 = inflator.inflate(R.layout.comment_list_item, null);

                                    Picasso.with(getActivity()).load(obj1.getString("User_Picture")).into(((RoundedImageView) row1.findViewById(R.id.userImage)));

                                    ((TextView)row1.findViewById(R.id.userName)).setText(obj1.getString("Full_User_Name"));
                                    ((TextView)row1.findViewById(R.id.commentBody)).setText(obj1.getString("1"));

                                    LinearLayout l = (LinearLayout)getActivity().findViewById(R.id.fewComments);
                                    l.addView(row1);

                                    if(comments.length() > 1)
                                    {
                                        JSONObject obj2 = comments.getJSONObject(1);

                                        View row2 = inflator.inflate(R.layout.comment_list_item, null);

                                        Picasso.with(getActivity()).load(obj2.getString("User_Picture")).into(((RoundedImageView) row2.findViewById(R.id.userImage)));

                                        ((TextView)row2.findViewById(R.id.userName)).setText(obj2.getString("Full_User_Name"));
                                        ((TextView)row2.findViewById(R.id.commentBody)).setText(obj2.getString("2"));

                                        l.addView(row2);
                                    }
                                }


                                //gotoAnswer();
                            }
                            else {
                                //gotoAnswer();
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

                parameters.put("Scenario_Id", etiquette.Etiquette_Id);
                parameters.put("User_Name", etiquette.User_Name);

                return parameters;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Fetching data", true, false);

        MainActivity.networkQueue.add(postRequest);
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

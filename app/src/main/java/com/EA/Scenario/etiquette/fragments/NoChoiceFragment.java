package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoChoiceFragment extends android.support.v4.app.Fragment implements GestureDetector.OnGestureListener, View.OnClickListener {

    private GestureDetector gDetector;

    public NoChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_no_choice, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return gDetector.onTouchEvent(event);
                }
                return true;


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        gDetector = new GestureDetector(this);

        ImageButton submitComment = (ImageButton)getActivity().findViewById(R.id.submitComment);
        submitComment.setOnClickListener(this);

        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageView moreComments = (ImageView)getActivity().findViewById(R.id.loadComments);
        moreComments.setOnClickListener(this);


        ScrollView mScrollView = (ScrollView)getActivity().findViewById(R.id.scrollView);
        mScrollView.requestDisallowInterceptTouchEvent(true);
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
                MainActivity.arrayList.add(text);
                ((EditText)getActivity().findViewById(R.id.commentText)).setText("");
                Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.loadComments)
        {
            CommentsFragment newFrag = new CommentsFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            trans.addToBackStack(null);
            trans.replace(R.id.fragment_container, newFrag, "CommentsFragment").commit();
        }
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onFling(MotionEvent start, MotionEvent finish, float xVelocity, float yVelocity) {
        if (start.getRawY() > finish.getRawY()) {
            if(start.getRawY() - finish.getRawY() > 200)
                Toast.makeText(getActivity().getApplicationContext(), "Swipe Up", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }
}

package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.CommentListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    CommentListAdapter ad;

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ListView list = (ListView)getActivity().findViewById(R.id.commentList);


        ad = new CommentListAdapter(getActivity(), MainActivity.arrayList);
        list.setAdapter(ad);

        ImageButton submitComment = (ImageButton)getActivity().findViewById(R.id.submitComment);
        submitComment.setOnClickListener(this);
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
                ad.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

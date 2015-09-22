package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.CustomSeekbar;
import com.EA.Scenario.etiquette.utils.Etiquette;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScenarioFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;
    Uri selectedImageUri = null;
    ArrayList<View> choices;

    public AddScenarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_scenario, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        choices = new ArrayList<View>();

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton done = (ImageButton)getActivity().findViewById(R.id.addScenarioButton);
        done.setOnClickListener(this);

        ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(this);

        ImageView userImage = (ImageView)getActivity().findViewById(R.id.userImage);
        userImage.setOnClickListener(this);

        img = (ImageView)getActivity().findViewById(R.id.userImage);

        //ImageButton choice = (ImageButton)getActivity().findViewById(R.id.addChoice);
        //choice.setOnClickListener(this);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate(R.layout.choice_layout, null);

        row.findViewById(R.id.addChoice).setOnClickListener(this);

        LinearLayout l = (LinearLayout)getActivity().findViewById(R.id.grandParent);
        l.addView(row);

        choices.add(row);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                //selectedImagePath = getPath(selectedImageUri);
                ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
                chooseImage.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageURI(selectedImageUri);
                //chooseImage.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.chooseImage)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
        else if(view.getId() == R.id.userImage)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
        else if(view.getId() == R.id.addScenarioButton)
        {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            Etiquette etiquette = new Etiquette();
            etiquette.setTitle(((EditText)getActivity().findViewById(R.id.captionText)).getText().toString());
            etiquette.setType(((Spinner) getActivity().findViewById(R.id.category)).getSelectedItem().toString());
            etiquette.setMeter(((CustomSeekbar) getActivity().findViewById(R.id.customSeekBar)).getProgress());
            etiquette.setUri(selectedImageUri);

            if(choices.size() == 4) {
                EditText text = (EditText) choices.get(0).findViewById(R.id.choiceText);
                etiquette.setOpt1_text(text.getText().toString());

                text = (EditText) choices.get(1).findViewById(R.id.choiceText);
                etiquette.setOpt2_text(text.getText().toString());

                text = (EditText) choices.get(2).findViewById(R.id.choiceText);
                etiquette.setOpt3_text(text.getText().toString());

                text = (EditText) choices.get(3).findViewById(R.id.choiceText);
                etiquette.setOpt4_text(text.getText().toString());
            }

            Toast.makeText(getActivity(), "Scenario Added", Toast.LENGTH_SHORT).show();

            MainActivity.etiquetteList.add(etiquette);
            MainActivity.adapter.notifyDataSetChanged();

        }
        else if(view.getId() == R.id.addChoice)
        {
            addChoice();
        }
        else if(view.getId() == R.id.drawMenu)
        {
            DrawerLayout d = (DrawerLayout)getActivity().findViewById(R.id.drawer);
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            d.openDrawer(navigationView);
        }
    }

    void addChoice()
    {
        if(choices.size() < 4) {
            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View row = inflator.inflate(R.layout.choice_layout, null);

            row.findViewById(R.id.addChoice).setOnClickListener(this);

            LinearLayout l = (LinearLayout) getActivity().findViewById(R.id.grandParent);
            l.addView(row);

            choices.add(row);
        }
    }

    /*
    public void fun()
    {
        float d = getActivity().getResources().getDisplayMetrics().density;

        FrameLayout parent = new FrameLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int)d*8, 0, (int)d*8, 0);
        parent.setLayoutParams(params);
        parent.setBackgroundResource(R.drawable.ad_title);

        EditText editText = new EditText(getActivity());
        editText.setHint(R.string.add_choice);
        editText.setSingleLine();
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editTextParams.setMargins((int)d*8, 0, (int)d*8, 0);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextSize((int) d * 8);
        editText.setLayoutParams(editTextParams);

        ImageView divider = new ImageView(getActivity());
        FrameLayout.LayoutParams dividerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(0, 0, (int)d*50, 0);
        dividerParams.gravity = Gravity.RIGHT | Gravity.TOP;
        divider.setBackgroundResource(R.drawable.divider_small);
        divider.setLayoutParams(dividerParams);

        ImageButton plus = new ImageButton(getActivity());
        FrameLayout.LayoutParams plusParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
        plusParams.setMargins(0, 0, (int)d*20, 0);
        plusParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        plus.setBackgroundResource(R.drawable.plus_icon);
        plus.setLayoutParams(plusParams);

        parent.addView(editText);
        parent.addView(divider);
        parent.addView(plus);

        LinearLayout l = (LinearLayout)getActivity().findViewById(R.id.grandParent);
        l.addView(parent);
    }
    */

}

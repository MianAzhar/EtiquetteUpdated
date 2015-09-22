package com.EA.Scenario.etiquette.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private static final int TAKE_PICTURE = 1234;

    private String selectedImagePath;
    private ImageView img;
    Uri selectedImageUri = null;
    ArrayList<View> choices;
    Dialog dialog;

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

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton cam = (ImageButton)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        ImageButton  gal = (ImageButton)dialog.findViewById(R.id.fromGallery);
        gal.setOnClickListener(this);

        choices = new ArrayList<View>();

        EditText location = (EditText)getActivity().findViewById(R.id.addLocationField);
        location.setOnClickListener(this);

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
        if (requestCode == TAKE_PICTURE && resultCode == getActivity().RESULT_OK) {
            dialog.hide();
            selectedImageUri = data.getData();
            ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
            chooseImage.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            img.setImageURI(selectedImageUri);
        }
        else if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                dialog.hide();
                selectedImageUri = data.getData();
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
            dialog.show();
        }
        else if(view.getId() == R.id.userImage)
        {
            dialog.show();
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

            MainActivity.etiquetteList.add(etiquette);
            MainActivity.adapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), "Scenario Added", Toast.LENGTH_SHORT).show();

            PopularFragment newFrag = new PopularFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "PopularFragment").commit();
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
        else if(view.getId() == R.id.addLocationField)
        {
            /*
            AddLocationFragment newFrag1 = new AddLocationFragment();
            android.support.v4.app.FragmentTransaction trans1 = getActivity().getSupportFragmentManager().beginTransaction();
            trans1.addToBackStack(null);
            trans1.replace(R.id.fragment_container, newFrag1, "LocationFragment").commit();
            */
        }
        else if(view.getId() == R.id.fromCamera)
        {
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(picIntent, TAKE_PICTURE);
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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
            EditText te = (EditText)row.findViewById(R.id.choiceText);
            te.requestFocus();
        }
    }

}

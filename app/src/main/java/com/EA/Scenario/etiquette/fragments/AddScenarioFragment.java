package com.EA.Scenario.etiquette.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.CustomSeekbar;
import com.EA.Scenario.etiquette.utils.Etiquette;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScenarioFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private String selectedImagePath;
    private ImageView img;
    Bitmap selectedImageUri = null;
    ArrayList<View> choices;
    Dialog dialog;

    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ImageView dot4;
    ImageView dot5;

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

        dot1 = (ImageView)getActivity().findViewById(R.id.dot1);
        dot2 = (ImageView)getActivity().findViewById(R.id.dot2);
        dot3 = (ImageView)getActivity().findViewById(R.id.dot3);
        dot4 = (ImageView)getActivity().findViewById(R.id.dot4);
        dot5 = (ImageView)getActivity().findViewById(R.id.dot5);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cam = (Button)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        Button  gal = (Button)dialog.findViewById(R.id.fromGallery);
        gal.setOnClickListener(this);

        CustomSeekbar bar = (CustomSeekbar)getActivity().findViewById(R.id.customSeekBar);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                switch (progress) {
                    case 0:
                        dot1.setVisibility(View.GONE);
                        dot2.setImageResource(R.drawable.fill_icon);
                        dot2.setVisibility(View.VISIBLE);
                        dot3.setVisibility(View.VISIBLE);
                        dot3.setImageResource(R.drawable.highllightred);
                        dot4.setImageResource(R.drawable.highllightred);
                        dot4.setVisibility(View.VISIBLE);
                        dot5.setImageResource(R.drawable.highllightred);
                        dot5.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        dot1.setVisibility(View.VISIBLE);
                        dot1.setImageResource(R.drawable.highllightred);
                        dot2.setVisibility(View.GONE);
                        dot3.setVisibility(View.VISIBLE);
                        dot3.setImageResource(R.drawable.highllightred);
                        dot4.setImageResource(R.drawable.highllightred);
                        dot4.setVisibility(View.VISIBLE);
                        dot5.setImageResource(R.drawable.highllightred);
                        dot5.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        dot1.setVisibility(View.VISIBLE);
                        dot1.setImageResource(R.drawable.highllightred);
                        dot2.setVisibility(View.VISIBLE);
                        dot2.setImageResource(R.drawable.highllightred);
                        dot3.setVisibility(View.GONE);
                        dot4.setImageResource(R.drawable.highllightred);
                        dot4.setVisibility(View.VISIBLE);
                        dot5.setImageResource(R.drawable.highllightred);
                        dot5.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        dot1.setVisibility(View.VISIBLE);
                        dot1.setImageResource(R.drawable.highllightred);
                        dot2.setVisibility(View.VISIBLE);
                        dot2.setImageResource(R.drawable.highllightred);
                        dot3.setVisibility(View.VISIBLE);
                        dot3.setImageResource(R.drawable.highllightred);
                        dot4.setVisibility(View.GONE);
                        dot5.setImageResource(R.drawable.highllightred);
                        dot5.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        dot1.setVisibility(View.VISIBLE);
                        dot1.setImageResource(R.drawable.highllightred);
                        dot2.setVisibility(View.VISIBLE);
                        dot2.setImageResource(R.drawable.highllightred);
                        dot3.setVisibility(View.VISIBLE);
                        dot3.setImageResource(R.drawable.highllightred);
                        dot4.setImageResource(R.drawable.fill_icon);
                        dot4.setVisibility(View.VISIBLE);
                        dot5.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

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
        if (requestCode == Constants.TAKE_PICTURE_ADD_SCENARIO && resultCode == getActivity().RESULT_OK) {
            dialog.hide();
            Uri uri = data.getData();
            selectedImageUri = (Bitmap) data.getExtras().get("data");
            ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
            chooseImage.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            //String path = getPath(uri);
            //fun(path);
            img.setImageBitmap(selectedImageUri);
        }
        else if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE_ADD_SCENARIO) {
                dialog.hide();
                Uri uri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    selectedImageUri = Bitmap.createScaledBitmap(image, 500, 300, true);
                    //addDream.setImageBitmap(resized);
                    ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
                    chooseImage.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                /*
                Uri uri = data.getData();
                ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
                chooseImage.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                String path = getPath(uri);
                fun(path);
                */
                //img.setImageURI(selectedImageUri);
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
            getActivity().startActivityForResult(picIntent, Constants.TAKE_PICTURE_ADD_SCENARIO);

            //Toast.makeText(getActivity(), "Functionality not available", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE_ADD_SCENARIO);
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

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void fun(String path)
    {
        selectedImageUri = decodeSampledBitmapFromResource(getResources(), path, 100, 100);
        img.setImageBitmap(selectedImageUri);
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, String resId,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}

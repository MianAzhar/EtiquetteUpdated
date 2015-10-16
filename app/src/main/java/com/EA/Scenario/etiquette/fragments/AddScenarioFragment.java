package com.EA.Scenario.etiquette.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.CropingOptionAdapter;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.CropingOption;
import com.EA.Scenario.etiquette.utils.CustomSeekbar;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScenarioFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private String selectedImagePath;
    private ImageView img;
    Bitmap selectedImageUri;
    ArrayList<View> choices;
    Dialog dialog;

    private Uri mImageCaptureUri;
    private File outPutFile = null;

    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    ImageView dot4;
    ImageView dot5;

    TextView locationField;

    EditText captionTextField;
    Spinner categoryField;
    CustomSeekbar meterBar;

    String cityName = null;

    ProgressDialog progressDialog;

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

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        captionTextField = (EditText)getActivity().findViewById(R.id.captionText);
        categoryField = (Spinner)getActivity().findViewById(R.id.category);
        meterBar = (CustomSeekbar)getActivity().findViewById(R.id.customSeekBar);

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

        locationField = (TextView)getActivity().findViewById(R.id.addLocationField);
        locationField.setOnClickListener(this);

        ImageView menu = (ImageView)getActivity().findViewById(R.id.drawMenu);
        menu.setOnClickListener(this);

        ImageButton done = (ImageButton)getActivity().findViewById(R.id.addScenarioButton);
        done.setOnClickListener(this);

        ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(this);

        img = (ImageView)getActivity().findViewById(R.id.userImage);
        img.setOnClickListener(this);

        if(selectedImageUri != null)
        {
            chooseImage.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            img.setImageBitmap(selectedImageUri);
        }

        //ImageButton choice = (ImageButton)getActivity().findViewById(R.id.addChoice);
        //choice.setOnClickListener(this);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate(R.layout.choice_layout, null);

        row.findViewById(R.id.addChoice).setOnClickListener(this);

        LinearLayout l = (LinearLayout)getActivity().findViewById(R.id.grandParent);
        l.addView(row);

        choices.add(row);

        // Check if GPS enabled
        if (MainActivity.gps.canGetLocation()) {
            MainActivity.location = MainActivity.gps.getLocation();

        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            if(MainActivity.askGps) {
                MainActivity.gps.showSettingsAlert();
                MainActivity.askGps = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.GET_LOCATION && requestCode == getActivity().RESULT_OK)
        {
            cityName = data.getStringExtra("city");
        }
        else if (requestCode == Constants.TAKE_PICTURE_ADD_SCENARIO && resultCode == getActivity().RESULT_OK) {
            dialog.hide();
            CropingIMG();
            /*
            Uri uri = data.getData();
            selectedImageUri = (Bitmap) data.getExtras().get("data");
            ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
            chooseImage.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            //String path = getPath(uri);
            //fun(path);
            img.setImageBitmap(selectedImageUri);
            */
        }
        else if (requestCode == Constants.SELECT_PICTURE_ADD_SCENARIO && resultCode == getActivity().RESULT_OK) {
            dialog.hide();

            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI : "+mImageCaptureUri);
            CropingIMG();

            /*
            Uri uri = data.getData();
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                selectedImageUri = Bitmap.createScaledBitmap(image, 600, 400, true);
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
            */
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
        else if (requestCode == Constants.CROP_IMAGE_ADD_SCENARIO) {

            try {
                if(outPutFile.exists()){
                    selectedImageUri = decodeFile(outPutFile);

                    ImageButton chooseImage = (ImageButton)getActivity().findViewById(R.id.chooseImage);
                    chooseImage.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(selectedImageUri);
                }
                else {
                    Toast.makeText(getActivity(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CropingIMG() {

        final ArrayList cropOptions = new ArrayList();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(getActivity(), "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 256 + 128);
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 2);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo)list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                getActivity().startActivityForResult(i, Constants.CROP_IMAGE_ADD_SCENARIO);
            } else {
                for (Object res1 : list) {
                    final CropingOption co = new CropingOption();
                    ResolveInfo res = (ResolveInfo)res1;
                    co.title  = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        getActivity().startActivityForResult(((CropingOption) cropOptions.get(item)).appIntent, Constants.CROP_IMAGE_ADD_SCENARIO);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
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

            addScenario();
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
            AddLocationFragment newFrag1 = new AddLocationFragment();
            android.support.v4.app.FragmentTransaction trans1 = getActivity().getSupportFragmentManager().beginTransaction();
            trans1.addToBackStack(null);
            trans1.replace(R.id.fragment_container, newFrag1, "LocationFragment").commit();
        }
        else if(view.getId() == R.id.fromCamera)
        {

            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
            mImageCaptureUri = Uri.fromFile(f);
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
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

    @Override
    public void onResume()
    {
        super.onResume();

        if(AddLocationFragment.selectedCity != null)
        {
            locationField.setText(AddLocationFragment.selectedCity);
            AddLocationFragment.selectedCity = null;
        }
    }

    void addScenario()
    {
        final String caption = captionTextField.getText().toString();
        final String category = categoryField.getSelectedItem().toString();

        if(selectedImageUri == null){
            Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(caption.length() < 1){
            Toast.makeText(getActivity(), "Please add caption", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (category.equals(getResources().getString(R.string.select_catagory))){
            Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        Etiquette etiquette = new Etiquette();
        etiquette.setTitle(((EditText)getActivity().findViewById(R.id.captionText)).getText().toString());
        etiquette.setType(((Spinner) getActivity().findViewById(R.id.category)).getSelectedItem().toString());
        etiquette.setMeter(((CustomSeekbar) getActivity().findViewById(R.id.customSeekBar)).getProgress());
        etiquette.setUri(selectedImageUri);

        if(choices.size() == 4) {
            EditText text = (EditText) choices.get(0).findViewById(R.id.choiceText);
            etiquette.setScenario_Option_1(text.getText().toString());

            text = (EditText) choices.get(1).findViewById(R.id.choiceText);
            etiquette.setScenario_Option_2(text.getText().toString());

            text = (EditText) choices.get(2).findViewById(R.id.choiceText);
            etiquette.setScenario_Option_3(text.getText().toString());

            text = (EditText) choices.get(3).findViewById(R.id.choiceText);
            etiquette.setScenario_Option_4(text.getText().toString());
        }

        /*
        Web API Call
         */

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/add-scenario",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success"))
                            {
                                Toast.makeText(getActivity(), "Scenario Added", Toast.LENGTH_SHORT).show();
                                PopularFragment newFrag = new PopularFragment();
                                android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                trans.replace(R.id.fragment_container, newFrag, Constants.PopularFragmentTag).commit();
                            }
                            else {
                                Toast.makeText(getActivity(), "Status fail", Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
                        progressDialog = null;
                        Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();

                // the POST parameters:
                params.put("User_Name", MainActivity.userName);
                params.put("Scenario_Description", caption);
                params.put("Scenario_Category", category);
                params.put("Scenario_Current_Location", "" + MainActivity.gps.getLatitude() + ", " + MainActivity.gps.getLongitude());
                params.put("Scenario_Entry_Time", Long.toString(System.currentTimeMillis()));
                params.put("Scenario_Level", Integer.toString(meterBar.getProgress() - 2));

                String city = locationField.getText().toString();
                if(city.length() > 0)
                    params.put("Scenario_Location", city);



                String format = "yyyy-MM-dd HH:mm:ss";
                final SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String utcTime = sdf.format(new Date());

                params.put("Scenario_Id",utcTime);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImageUri.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String str = Base64.encodeToString(byteArray, Base64.DEFAULT);

                params.put("Scenario_Picture", str);

                int index = 1;

                for(int i = 0; i < choices.size(); i++)
                {
                    String temp = ((EditText)choices.get(i).findViewById(R.id.choiceText)).getText().toString();
                    if(temp.length() > 0)
                    {
                        params.put("Scenario_Option_"+index, temp);
                        index++;
                    }
                }

                return params;
            }
        };


        /*
        Web API Call end
         */

        //MainActivity.etiquetteList.add(etiquette);
        //MainActivity.adapter.notifyDataSetChanged();

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Adding Scenario", true, false);
        MainActivity.networkQueue.add(postRequest);
    }

    void addChoice()
    {
        if(choices.size() < 9) {
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

package com.EA.Scenario.etiquette.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.User;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    Dialog dialog;
    User user;
    String phoneNumber;
    EditText realName;
    EditText userName;
    ProgressDialog progressDialog;
    Bitmap userImage = null;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        Bundle args = getArguments();
        phoneNumber = args.getString("phoneNumber");

        realName = (EditText)getActivity().findViewById(R.id.realName);
        userName = (EditText)getActivity().findViewById(R.id.userName);


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cam = (Button)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        Button  gal = (Button)dialog.findViewById(R.id.fromGallery);
        gal.setOnClickListener(this);

        final ImageButton signUpButton = (ImageButton)getActivity().findViewById(R.id.doneSignUpButton);
        signUpButton.setOnClickListener(this);

        final ImageView pic = (ImageView)getActivity().findViewById(R.id.profilePic);
        pic.setOnClickListener(this);

        final ImageView username = (ImageView)getActivity().findViewById(R.id.generateUsername);
        username.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.TAKE_PICTURE_SIGN_UP && resultCode == FragmentActivity.RESULT_OK) {
            dialog.hide();
            Uri selectedImageUri = data.getData();
            //Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ImageView img = (ImageView) getActivity().findViewById(R.id.profilePic);
            //img.setImageBitmap(bmp);
            //String path = getPath(selectedImageUri);
            //fun(path);

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(thumbnail);
            userImage = thumbnail;
        }
        else if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE_SIGN_UP) {
                dialog.hide();
                Uri selectedImageUri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImageUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    Bitmap resized = Bitmap.createScaledBitmap(image, 500, 300, true);
                    //addDream.setImageBitmap(resized);
                    ImageView mImageView = (ImageView)getActivity().findViewById(R.id.profilePic);
                    mImageView.setImageBitmap(resized);
                    userImage = resized;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                /*
                Uri selectedImageUri = data.getData();
                ImageView image = (ImageView)getActivity().findViewById(R.id.profilePic);
                //image.setImageURI(selectedImageUri);
                String path = getPath(selectedImageUri);
                fun(path);
                */
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.doneSignUpButton)
        {
            String fName = realName.getText().toString();
            String uName = userName.getText().toString();

            if(fName.length() < 1)
            {
                Toast.makeText(getActivity(), "Enter Full Name", Toast.LENGTH_SHORT);
                return;
            }
            if(uName.length() < 1)
            {
                Toast.makeText(getActivity(), "User Name cannot empty", Toast.LENGTH_SHORT);
                return;
            }
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            checkUserName();
        }
        else if(view.getId() == R.id.profilePic)
        {
            dialog.show();
        }
        else if (view.getId() == R.id.generateUsername)
        {
            EditText et = (EditText)getActivity().findViewById(R.id.realName);
            String get = et.getText().toString();
            get = get.replaceAll(" ", "");
            if(get !=null) {
                Random r = new Random();
                int Low = 100;
                int High = 999;
                int num = r.nextInt(High - Low) + Low;
                get = get + num;
                EditText tv = (EditText)getActivity().findViewById(R.id.userName);
                tv.setText(get);
            }
            else{

                Toast.makeText(getActivity(), "Enter Your Name First", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.fromCamera)
        {
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //File photo=new File(Environment.getExternalStorageDirectory(), "photo.jpg");
            //picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            getActivity().startActivityForResult(picIntent, Constants.TAKE_PICTURE_SIGN_UP);

            //Toast.makeText(getActivity(), "Functionality not available", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE_SIGN_UP);
        }
    }

    void checkUserName()
    {
        user = new User();
        final String fName = realName.getText().toString();
        final String uName = userName.getText().toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/signin-signup-username",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("message");

                            if(msg.equals("User name is accepted") || msg.equals("User name is verified"))
                            {
                                StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/get-profile",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    progressDialog = null;
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    String msg = jsonResponse.getString("status");

                                                    if(msg.equals("success")) {
                                                        Gson gson = new Gson();
                                                        user = gson.fromJson(jsonResponse.getString("data"), User.class);

                                                        SharedPreferences pref = getActivity().getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = pref.edit();
                                                        String picture = jsonResponse.getJSONObject("data").getString("Picture");
                                                        editor.putString("Picture", user.Picture);
                                                        editor.putString("userName", user.User_Name);
                                                        editor.putString("Name", user.Name);
                                                        editor.putString("phoneNumber", user.Mobile_Number);
                                                        editor.commit();
                                                        ImageView imgview = (ImageView) getActivity().findViewById(R.id.userpicture);
                                                        TextView tv = (TextView) getActivity().findViewById(R.id.unametv);
                                                        tv.setText(user.User_Name);
                                                        Picasso.with(getActivity()).load(user.Picture).into(imgview);
                                                        MainActivity.userName = user.User_Name;

                                                        PopularFragment newFrag = new PopularFragment();
                                                        android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                        trans.replace(R.id.fragment_container, newFrag, Constants.PopularFragmentTag).commit();
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
                                        params.put("language", "english");
                                        params.put("Phone_Number", phoneNumber);

                                        params.put("User_Name", userName.getText().toString());

                                        return params;
                                    }
                                };

                                RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                postRequest.setRetryPolicy(policy);
                                progressDialog = ProgressDialog.show(getActivity(), null, "Setting up", true, false);
                                MainActivity.networkQueue.add(postRequest);

                                /*
                                SharedPreferences pref = getActivity().getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userName", uName);
                                editor.putString("phoneNumber", phoneNumber);


                                editor.commit();

                                MainActivity.userName = uName;
                                */

                               /*
                                PopularFragment newFrag = new PopularFragment();
                                android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                trans.replace(R.id.fragment_container, newFrag, Constants.PopularFragmentTag).commit();
                                */
                            }
                            else if(msg.equals("User name already exists"))
                            {
                                userName.setTextColor(Color.RED);
                                Toast.makeText(getActivity(), "UserName not available", Toast.LENGTH_SHORT).show();
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
                params.put("language", "english");
                params.put("phoneNumber", phoneNumber);
                params.put("name", fName);
                params.put("UserName", uName);

                if(userImage != null)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    userImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    params.put("Picture", str);
                }
                return params;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Checking UserName", true, false);
        MainActivity.networkQueue.add(postRequest);
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
        ImageView mImageView = (ImageView)getActivity().findViewById(R.id.profilePic);
        mImageView.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), path, 100, 100));
        BitmapFactory.decodeFile("");
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

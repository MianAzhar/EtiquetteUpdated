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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.activities.MainActivity;
import com.EA.Scenario.etiquette.adapters.CropingOptionAdapter;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.CropingOption;
import com.EA.Scenario.etiquette.utils.RoundedImageView;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE = 1234;
    Dialog dialog;

    Bitmap userImage = null;

    ProgressDialog progressDialog;

    private Uri mImageCaptureUri;
    private File outPutFile = null;

    User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        Bundle args = getArguments();

        user = (User)args.getSerializable("data");

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cam = (Button)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        Button  gal = (Button)dialog.findViewById(R.id.fromGallery);
        gal.setOnClickListener(this);

        ImageButton changePic = (ImageButton)getActivity().findViewById(R.id.editPic);
        changePic.setOnClickListener(this);

        ImageView save = (ImageView)getActivity().findViewById(R.id.saveProfile);
        save.setOnClickListener(this);

        TextView userName = (TextView)getActivity().findViewById(R.id.userNameText);
        userName.setText(user.User_Name);

        ((EditText)getActivity().findViewById(R.id.fullNameField)).setText(user.Name);
        ((TextView)getActivity().findViewById(R.id.displayName)).setText(user.Name);

        if(user.Email != null)
        {
            if(user.Email.length() > 0)
                ((EditText)getActivity().findViewById(R.id.emailField)).setText(user.Email);
        }

        ((EditText)getActivity().findViewById(R.id.phoneNumberField)).setText(user.Mobile_Number);

        if(user.Picture != null)
        {

            ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
            RoundedImageView pic = (RoundedImageView)getActivity().findViewById(R.id.profilePic);
            Picasso.with(getActivity()).load(user.Picture).into(cover);
            Picasso.with(getActivity()).load(user.Picture).into(pic);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.TAKE_PICTURE_EDIT_PROFILE && resultCode == FragmentActivity.RESULT_OK) {
            dialog.hide();
            CropingIMG();
            /*
            ImageView img = (ImageView) getActivity().findViewById(R.id.profilePic);

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
            }  catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(thumbnail);
            ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
            cover.setImageBitmap(thumbnail);
            userImage = thumbnail;
            */
        }
        else if (requestCode == Constants.SELECT_PICTURE_EDIT_PROFILE && resultCode == FragmentActivity.RESULT_OK) {
                dialog.hide();

                mImageCaptureUri = data.getData();
                System.out.println("Gallery Image URI : "+mImageCaptureUri);
                CropingIMG();

                /*
                Uri selectedImageUri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImageUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    Bitmap resized = Bitmap.createScaledBitmap(image, 600, 400, true);
                    ImageView mImageView = (ImageView)getActivity().findViewById(R.id.profilePic);
                    mImageView.setImageBitmap(resized);

                    ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
                    cover.setImageBitmap(resized);
                    userImage = resized;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                */
        }
        else if (requestCode == Constants.CROP_IMAGE_EDIT_PROFILE) {

            try {
                if(outPutFile.exists()){
                    userImage = decodeFile(outPutFile);
                    ImageView mImageView = (ImageView)getActivity().findViewById(R.id.profilePic);
                    mImageView.setImageBitmap(userImage);

                    ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
                    cover.setImageBitmap(userImage);
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

                getActivity().startActivityForResult(i, Constants.CROP_IMAGE_EDIT_PROFILE);
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
                        getActivity().startActivityForResult(((CropingOption) cropOptions.get(item)).appIntent, Constants.CROP_IMAGE_EDIT_PROFILE);
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
        if(view.getId() == R.id.editPic)
        {
            dialog.show();
        }
        else if(view.getId() == R.id.fromCamera)
        {
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
            mImageCaptureUri = Uri.fromFile(f);
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            getActivity().startActivityForResult(picIntent, Constants.TAKE_PICTURE_EDIT_PROFILE);

            //getActivity().startActivityForResult(picIntent, Constants.TAKE_PICTURE_EDIT_PROFILE);

            //Toast.makeText(getActivity(), "Functionality not available", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE_EDIT_PROFILE);
        }
        else if(view.getId() == R.id.saveProfile)
        {
            updateProfile();
            //getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    void updateProfile()
    {
        final String name = ((EditText)getActivity().findViewById(R.id.fullNameField)).getText().toString();
        final String email = ((EditText)getActivity().findViewById(R.id.emailField)).getText().toString();

        if(name.length() < 1)
        {
            Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/edit-profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success")) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
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
                params.put("Phone_Number", user.Mobile_Number);
                params.put("User_Name", user.User_Name);
                params.put("Name", name);
                params.put("Email", email);

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

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Updating Profile", true, false);
        MainActivity.networkQueue.add(postRequest);
    }

}






/*
public class EditProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE = 1234;
    Dialog dialog;

    Bitmap userImage = null;

    ProgressDialog progressDialog;

    User user;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        Bundle args = getArguments();

        user = (User)args.getSerializable("data");

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cam = (Button)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        Button  gal = (Button)dialog.findViewById(R.id.fromGallery);
        gal.setOnClickListener(this);

        ImageButton changePic = (ImageButton)getActivity().findViewById(R.id.editPic);
        changePic.setOnClickListener(this);

        ImageView save = (ImageView)getActivity().findViewById(R.id.saveProfile);
        save.setOnClickListener(this);

        TextView userName = (TextView)getActivity().findViewById(R.id.userNameText);
        userName.setText(user.User_Name);

        ((EditText)getActivity().findViewById(R.id.fullNameField)).setText(user.Name);
        ((TextView)getActivity().findViewById(R.id.displayName)).setText(user.Name);

        if(user.Email != null)
        {
            if(user.Email.length() > 0)
                ((EditText)getActivity().findViewById(R.id.emailField)).setText(user.Email);
        }

        ((EditText)getActivity().findViewById(R.id.phoneNumberField)).setText(user.Mobile_Number);

        if(user.Picture != null)
        {

            ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
            RoundedImageView pic = (RoundedImageView)getActivity().findViewById(R.id.profilePic);
            Picasso.with(getActivity()).load(user.Picture).into(cover);
            Picasso.with(getActivity()).load(user.Picture).into(pic);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.TAKE_PICTURE_EDIT_PROFILE && resultCode == FragmentActivity.RESULT_OK) {
            dialog.hide();

            ImageView img = (ImageView) getActivity().findViewById(R.id.profilePic);

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
            }  catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(thumbnail);
            ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
            cover.setImageBitmap(thumbnail);
            userImage = thumbnail;
        }
        else if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE_EDIT_PROFILE) {
                dialog.hide();
                Uri selectedImageUri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImageUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    Bitmap resized = Bitmap.createScaledBitmap(image, 600, 400, true);
                    ImageView mImageView = (ImageView)getActivity().findViewById(R.id.profilePic);
                    mImageView.setImageBitmap(resized);

                    ImageView cover = (ImageView)getActivity().findViewById(R.id.coverPic);
                    cover.setImageBitmap(resized);
                    userImage = resized;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.editPic)
        {
            dialog.show();
        }
        else if(view.getId() == R.id.fromCamera)
        {
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(picIntent, Constants.TAKE_PICTURE_EDIT_PROFILE);

            //Toast.makeText(getActivity(), "Functionality not available", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE_EDIT_PROFILE);
        }
        else if(view.getId() == R.id.saveProfile)
        {
            updateProfile();
            //getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    void updateProfile()
    {
        final String name = ((EditText)getActivity().findViewById(R.id.fullNameField)).getText().toString();
        final String email = ((EditText)getActivity().findViewById(R.id.emailField)).getText().toString();

        if(name.length() < 1)
        {
            Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://etiquette-app.azurewebsites.net/edit-profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("status");

                            if(msg.equals("success")) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
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
                params.put("Phone_Number", user.Mobile_Number);
                params.put("User_Name", user.User_Name);
                params.put("Name", name);
                params.put("Email", email);

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

        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);

        progressDialog = ProgressDialog.show(getActivity(), null, "Updating Profile", true, false);
        MainActivity.networkQueue.add(postRequest);
    }

}
*/
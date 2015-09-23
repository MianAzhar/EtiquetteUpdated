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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE = 1234;
    Dialog dialog;

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

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton cam = (ImageButton)dialog.findViewById(R.id.fromCamera);
        cam.setOnClickListener(this);

        ImageButton  gal = (ImageButton)dialog.findViewById(R.id.fromGallery);
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
        if (requestCode == TAKE_PICTURE && resultCode == getActivity().RESULT_OK) {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(thumbnail);
        }
        else if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                dialog.hide();
                Uri selectedImageUri = data.getData();
                ImageView image = (ImageView)getActivity().findViewById(R.id.profilePic);
                //image.setImageURI(selectedImageUri);
                String path = getPath(selectedImageUri);
                fun(path);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.doneSignUpButton)
        {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            PopularFragment newFrag = new PopularFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "PopularFragment").commit();
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
            /*
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(picIntent, TAKE_PICTURE);
            */
            Toast.makeText(getActivity(), "Functionality not available", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.fromGallery)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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

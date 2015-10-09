package com.EA.Scenario.etiquette.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mian on 9/17/2015.
 * Custom Adapter
 */
public class EtiquetteListAdapter extends ArrayAdapter<Etiquette>
{
    //ArrayList<Etiquette> resources;
    ArrayList<Etiquette> textList;
    Context context;
    public EtiquetteListAdapter(Context c, ArrayList<Etiquette> text)
    {
        super(c, R.layout.etiquette_list_item, text);
        context = c;
        //this.resources = objects;
        this.textList = text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.etiquette_list_item, parent, false);
        }

        /*
        TextView title = (TextView)row.findViewById(R.id.titleText);
        title.setText(textList.get(position).getTitle());

        TextView type = (TextView)row.findViewById(R.id.typeText);
        type.setText(textList.get(position).getType());

        if(textList.get(position).getUri() != null)
        {
            ImageView img = (ImageView)row.findViewById(R.id.etiquetetImage);
            img.setImageBitmap(textList.get(position).getUri());
        }

        ImageView rating = (ImageView)row.findViewById(R.id.ratingImage);

        switch (textList.get(position).getMeter())
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
        */


        TextView title = (TextView)row.findViewById(R.id.titleText);
        title.setText(textList.get(position).Scenario_Description);

        TextView type = (TextView)row.findViewById(R.id.typeText);
        type.setText(textList.get(position).Category_Name);

        TextView userName = (TextView)row.findViewById(R.id.userName);
        userName.setText(textList.get(position).User_Full_Name);

        TextView views = (TextView)row.findViewById(R.id.views);
        views.setText(textList.get(position).Scenario_Number_Of_Views);

        if(textList.get(position).Scenario_Picture != null)
        {
            ImageView img = (ImageView)row.findViewById(R.id.etiquetetImage);
            ///img.setImageBitmap(StringToBitMap(textList.get(position).Scenario_Picture));
            Picasso.with(context).load(textList.get(position).Scenario_Picture).into(img);
        }

        if(textList.get(position).User_Picture != null)
        {
            ImageView img = (ImageView)row.findViewById(R.id.userImage);
            //img.setImageBitmap(StringToBitMap(textList.get(position).User_Picture));
            Picasso.with(context).load(textList.get(position).User_Picture).into(img);
        }

        ImageView rating = (ImageView)row.findViewById(R.id.ratingImage);

        switch (textList.get(position).Scenario_Level)
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

        return row;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

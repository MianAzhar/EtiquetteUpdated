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
    ArrayList<Etiquette> textList;
    Context context;
    public EtiquetteListAdapter(Context c, ArrayList<Etiquette> text)
    {
        super(c, R.layout.etiquette_list_item, text);
        context = c;
        this.textList = text;
    }

    @Override
    public int getCount()
    {
        return textList.size();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        EtiquetteViewHolder holder;

        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.etiquette_list_item, parent, false);

            holder = new EtiquetteViewHolder();

            holder.title = (TextView)row.findViewById(R.id.titleText);
            holder.time = (TextView)row.findViewById(R.id.time);
            holder.type = (TextView)row.findViewById(R.id.typeText);
            holder.userName = (TextView)row.findViewById(R.id.userName);
            holder.views = (TextView)row.findViewById(R.id.views);
            holder.scenarioImg = (ImageView)row.findViewById(R.id.etiquetetImage);
            holder.userImg = (ImageView)row.findViewById(R.id.userImage);
            holder.rating = (ImageView)row.findViewById(R.id.ratingImage);
            row.setTag(holder);
        }
        else
        {
            holder = (EtiquetteViewHolder)row.getTag();
        }

        Etiquette obj = textList.get(position);

        holder.title.setText(obj.Scenario_Description);

        long t = System.currentTimeMillis() - obj.Scenario_Entry_Time;
        holder.time.setText(t/3600000 + "h");

        holder.type.setText(obj.Category_Name);

        holder.userName.setText(obj.User_Full_Name);

        holder.views.setText(obj.Scenario_Number_Of_Views);

        if(obj.Scenario_Picture != null)
        {
            Picasso.with(context).load(obj.Scenario_Picture).into(holder.scenarioImg);
        }

        if(obj.User_Picture != null)
        {
            Picasso.with(context).load(textList.get(position).User_Picture).into(holder.userImg);
        }

        switch (obj.Scenario_Level)
        {
            case 0:
                holder.rating.setImageResource(R.drawable._10);
                break;
            case 1:
                holder.rating.setImageResource(R.drawable._5);
                break;
            case 2:
                holder.rating.setImageResource(R.drawable._0);
                break;
            case 3:
                holder.rating.setImageResource(R.drawable.__5);
                break;
            case 4:
                holder.rating.setImageResource(R.drawable.__10);
                break;
            default:
                holder.rating.setImageResource(R.drawable._0);
                break;
        }

        return row;
    }

    static class EtiquetteViewHolder {
        TextView title;
        TextView time;
        TextView type;
        TextView userName;
        TextView views;
        ImageView userImg;
        ImageView scenarioImg;
        ImageView rating;
    }
}



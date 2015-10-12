package com.EA.Scenario.etiquette.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.utils.CommentClass;
import com.EA.Scenario.etiquette.utils.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mian on 9/19/2015.
 */
public class CommentListAdapter extends ArrayAdapter<CommentClass>
{
    //ArrayList<Etiquette> resources;
    ArrayList<CommentClass> textList;
    Context context;
    public CommentListAdapter(Context c, ArrayList<CommentClass> text)
    {
        super(c , R.layout.comment_list_item, text);
        context = c;
        //this.resources = objects;
        this.textList = text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.comment_list_item, parent, false);
        }

        TextView body = (TextView)row.findViewById(R.id.commentBody);

        body.setText(textList.get(position).comment);

        RoundedImageView img = (RoundedImageView)row.findViewById(R.id.userImage);
        Picasso.with(context).load(textList.get(position).picture).into(img);

        TextView user = (TextView)row.findViewById(R.id.userName);
        user.setText(textList.get(position).name);

        return row;
    }
}
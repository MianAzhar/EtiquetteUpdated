package com.EA.Scenario.etiquette.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.EA.Scenario.etiquette.R;

import java.util.ArrayList;

/**
 * Created by Mian on 9/19/2015.
 */
public class CommentListAdapter extends ArrayAdapter<String>
{
    //ArrayList<Etiquette> resources;
    ArrayList<String> textList;
    Context context;
    public CommentListAdapter(Context c, ArrayList<String> text)
    {
        super(c , R.layout.comment_list_item, text);
        context = c;
        //this.resources = objects;
        this.textList = text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate(R.layout.comment_list_item, parent ,false);

        TextView body = (TextView)row.findViewById(R.id.commentBody);

        body.setText(textList.get(position));

        return row;
    }
}
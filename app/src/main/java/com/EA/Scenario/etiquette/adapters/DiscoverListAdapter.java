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
public class DiscoverListAdapter extends ArrayAdapter<String> {

    ArrayList<String> textList;
    Context context;
    public DiscoverListAdapter(Context c, ArrayList<String> text)
    {
        super(c , R.layout.discover_list_item, R.id.cityName, text);
        context = c;
        this.textList = text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.discover_list_item, parent, false);
        }

        TextView textView = (TextView)row.findViewById(R.id.cityName);
        textView.setText(textList.get(position));

        return row;
    }
}

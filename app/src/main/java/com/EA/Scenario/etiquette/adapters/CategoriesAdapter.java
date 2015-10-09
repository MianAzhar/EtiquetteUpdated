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
 * Created by mian_ on 10/8/2015.
 */
public class CategoriesAdapter extends ArrayAdapter<String> {
    ArrayList<String> textList;
    Context context;
    public CategoriesAdapter(Context c, ArrayList<String> text)
    {
        super(c , R.layout.category_item, text);
        context = c;
        this.textList = text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate(R.layout.category_item, parent ,false);

        TextView textView = (TextView) row.findViewById(R.id.categoryTitle);
        textView.setText(textList.get(position));

        return row;
    }
}

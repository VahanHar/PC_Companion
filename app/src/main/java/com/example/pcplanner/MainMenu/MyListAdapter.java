package com.example.pcplanner.MainMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pcplanner.R;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {
    private ArrayList<String> collections;

    public MyListAdapter(Context context, int textViewResourceId, ArrayList<String> collections) {
        super(context, textViewResourceId, collections);
        this.collections = collections;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        textView.setText(collections.get(position));

        return rowView;
    }
}


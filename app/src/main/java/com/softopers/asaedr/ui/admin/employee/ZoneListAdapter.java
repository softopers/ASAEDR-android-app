package com.softopers.asaedr.ui.admin.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.ZoneList;

import java.util.ArrayList;

public class ZoneListAdapter extends ArrayAdapter<ZoneList> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<ZoneList> values;

    public ZoneListAdapter(Context context, int textViewResourceId,
                           ArrayList<ZoneList> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public ZoneList getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView row = (TextView) inflater.inflate(R.layout.spinner_item, parent, false);
        row.setText(values.get(position).getZoneName());

        return row;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView row = (TextView) inflater.inflate(R.layout.spinner_item_dropdown, parent, false);
        row.setText(values.get(position).getZoneName());

        return row;
    }
}
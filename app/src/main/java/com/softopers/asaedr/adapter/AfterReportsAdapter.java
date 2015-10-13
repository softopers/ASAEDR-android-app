package com.softopers.asaedr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.DateList;

import java.util.ArrayList;

public class AfterReportsAdapter extends ArrayAdapter<DateList> {

    public AfterReportsAdapter(Context context, ArrayList<DateList> dateLists) {
        super(context, R.layout.listitem_reporting, dateLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DateList dateList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_reporting, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.listitem_reporting_date);
            viewHolder.listitem_reporting_unread = (TextView) convertView.findViewById(R.id.listitem_reporting_unread);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.date.setText(dateList.getDate());

        if (dateList.getUnReadCounter() == 0) {
            viewHolder.listitem_reporting_unread.setVisibility(View.GONE);
        } else if (dateList.getUnReadCounter() >= 99) {
            viewHolder.listitem_reporting_unread.setVisibility(View.VISIBLE);
            viewHolder.listitem_reporting_unread.setText(99 + "");
        } else {
            viewHolder.listitem_reporting_unread.setVisibility(View.VISIBLE);
            viewHolder.listitem_reporting_unread.setText(dateList.getUnReadCounter() + "");
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView date, listitem_reporting_unread;
        RelativeLayout relativeLayout;
    }
}
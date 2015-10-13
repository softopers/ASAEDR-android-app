package com.softopers.asaedr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.ReportList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EmployeeReportingListAdapter extends ArrayAdapter<ReportList> {

    public EmployeeReportingListAdapter(Context context, ArrayList<ReportList> dateLists) {
        super(context, R.layout.admin_listitem_reporting_list, dateLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ReportList reportList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_reporting_list, parent, false);
            viewHolder.listitem_reporting_user = (TextView) convertView.findViewById(R.id.listitem_reporting_user);
            viewHolder.listitem_reporting_admin = (TextView) convertView.findViewById(R.id.listitem_reporting_admin);
            viewHolder.listitem_reporting_time = (TextView) convertView.findViewById(R.id.listitem_reporting_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        if (reportList.getIsComment()) {
            viewHolder.listitem_reporting_admin.setText(reportList.getDescription().trim());
            viewHolder.listitem_reporting_user.setVisibility(View.GONE);
            viewHolder.listitem_reporting_admin.setVisibility(View.VISIBLE);
        } else {
            viewHolder.listitem_reporting_user.setText(reportList.getDescription().trim());
            viewHolder.listitem_reporting_admin.setVisibility(View.GONE);
            viewHolder.listitem_reporting_user.setVisibility(View.VISIBLE);
        }

        String formattedDateTime = getItem(position).getPostDateTime();

        try {

            DateFormat format = new SimpleDateFormat("dd/MM hh:mm aaa", Locale.ENGLISH);

            SimpleDateFormat targetTime = new SimpleDateFormat("hh:mm aaa");

            Date date = format.parse(formattedDateTime);

            formattedDateTime = targetTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.listitem_reporting_time.setText(formattedDateTime);

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView listitem_reporting_user, listitem_reporting_admin, listitem_reporting_time;
    }
}
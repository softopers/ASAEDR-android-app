package com.softopers.asaedr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.ReportList;
import com.softopers.asaedr.util.PrefUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EmployeeReportingListAdapter extends ArrayAdapter<ReportList> {

    public EmployeeReportingListAdapter(Context context, ArrayList<ReportList> dateLists) {
        super(context, R.layout.listitem_reporting_list, dateLists);
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

            viewHolder.admin_name = (TextView) convertView.findViewById(R.id.admin_name);
            viewHolder.listitem_reporting_admin_linear = (LinearLayout) convertView.findViewById(R.id.listitem_reporting_admin_linear);
            viewHolder.read_mark = (ImageView) convertView.findViewById(R.id.read_mark);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        if (reportList.getIsComment()) {
            if (reportList.getAdminName().isEmpty()) {
                viewHolder.listitem_reporting_admin.setText(reportList.getDescription().trim());
            } else {
                if (PrefUtils.getUser(getContext()).getIsAdmin()) {
                    if (reportList.getIsRead()) {
                        viewHolder.admin_name.setText(reportList.getAdminName());
                        viewHolder.listitem_reporting_admin.setText(reportList.getDescription().trim());
                        viewHolder.read_mark.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.admin_name.setText(reportList.getAdminName());
                        viewHolder.read_mark.setVisibility(View.GONE);
                        viewHolder.listitem_reporting_admin.setText(reportList.getDescription().trim());
                    }
                } else {
                    viewHolder.admin_name.setText(reportList.getAdminName());
                    viewHolder.listitem_reporting_admin.setText(reportList.getDescription().trim());
                    viewHolder.read_mark.setVisibility(View.GONE);
                }
            }
            viewHolder.listitem_reporting_user.setVisibility(View.GONE);
            viewHolder.listitem_reporting_admin_linear.setVisibility(View.VISIBLE);
        } else {
            viewHolder.listitem_reporting_user.setText(reportList.getDescription().trim());
            viewHolder.listitem_reporting_admin_linear.setVisibility(View.GONE);
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
        TextView listitem_reporting_user, listitem_reporting_admin, listitem_reporting_time, admin_name;
        ImageView read_mark;
        LinearLayout listitem_reporting_admin_linear;
    }
}
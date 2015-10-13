package com.softopers.asaedr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.UserList;

import java.util.ArrayList;

public class AdminReportTab1Adapter extends ArrayAdapter<UserList> {

    public AdminReportTab1Adapter(Context context, ArrayList<UserList> userLists) {
        super(context, R.layout.listitem_reporting, userLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserList userList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_reporting, parent, false);
            viewHolder.employeeName = (TextView) convertView.findViewById(R.id.listitem_reporting_date);
            viewHolder.listitem_reporting_unread = (TextView) convertView.findViewById(R.id.listitem_reporting_unread);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.employeeName.setText(userList.getEmpName());

        if (userList.getUnReadCounter() == 0) {
            viewHolder.listitem_reporting_unread.setVisibility(View.GONE);
        } else if (userList.getUnReadCounter() >= 999) {
            viewHolder.listitem_reporting_unread.setVisibility(View.VISIBLE);
            viewHolder.listitem_reporting_unread.setText(999 + "");
        } else {
            viewHolder.listitem_reporting_unread.setVisibility(View.VISIBLE);
            viewHolder.listitem_reporting_unread.setText(userList.getUnReadCounter() + "");
        }
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView employeeName, listitem_reporting_unread;
    }
}
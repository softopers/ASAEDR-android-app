package com.softopers.asaedr.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.MessageMaster;
import com.softopers.asaedr.ui.admin.message.SentMessageActivity;

import java.util.ArrayList;

public class MessageListAdapter extends ArrayAdapter<MessageMaster> {

    public MessageListAdapter(Context context, ArrayList<MessageMaster> messageMasters) {
        super(context, R.layout.listitem_reporting_list, messageMasters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MessageMaster reportList = getItem(position);
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
        if (reportList.getIsSent()) {

            viewHolder.listitem_reporting_user.setText(reportList.getMessageContent().trim());
            viewHolder.listitem_reporting_admin_linear.setVisibility(View.GONE);
            viewHolder.listitem_reporting_user.setVisibility(View.VISIBLE);
        } else {
            viewHolder.admin_name.setText(reportList.getAdminName());
            viewHolder.listitem_reporting_admin.setText(reportList.getMessageContent().trim());
            viewHolder.read_mark.setVisibility(View.GONE);

            viewHolder.listitem_reporting_user.setVisibility(View.GONE);
            viewHolder.listitem_reporting_admin_linear.setVisibility(View.VISIBLE);

        }

        String formattedDateTime = getItem(position).getCreateDateTime();

        viewHolder.listitem_reporting_time.setText(formattedDateTime);

        viewHolder.listitem_reporting_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SentMessageActivity.class);
                intent.putExtra("message_id", reportList.getMessageId());
                getContext().startActivity(intent);
            }
        });

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
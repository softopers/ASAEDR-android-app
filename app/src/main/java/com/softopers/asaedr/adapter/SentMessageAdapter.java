package com.softopers.asaedr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.SentMessages;

import java.util.ArrayList;

public class SentMessageAdapter extends ArrayAdapter<SentMessages> {

    public SentMessageAdapter(Context context, ArrayList<SentMessages> sentMessages) {
        super(context, R.layout.listitem_sent_message, sentMessages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SentMessages sentMessages = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_sent_message, parent, false);
            viewHolder.text_read = (TextView) convertView.findViewById(R.id.text_read);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        if (sentMessages.getIsRead().equalsIgnoreCase("true")) {
            viewHolder.status.setText("Read");
            viewHolder.time.setText(sentMessages.getReadDateTime());
        } else if (sentMessages.getIsSent().equalsIgnoreCase("true")) {
            viewHolder.status.setText("Sent");
            viewHolder.time.setText(sentMessages.getSentDateTime());
        } else {
            viewHolder.status.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.GONE);
        }

        viewHolder.text_read.setText(sentMessages.getEmpName());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView text_read, status, time;
    }
}
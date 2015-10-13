package com.softopers.asaedr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softopers.asaedr.R;
import com.softopers.asaedr.model.TemplateDetail;
import com.softopers.asaedr.ui.user.TemplateFragment;

import java.util.ArrayList;

public class EmployeeTemplateListAdapter extends ArrayAdapter<TemplateDetail> {

    TemplateFragment templateFragment;

    public EmployeeTemplateListAdapter(TemplateFragment templateFragment, ArrayList<TemplateDetail> templateDetails) {
        super(templateFragment.getActivity(), R.layout.listitem_template, templateDetails);
        this.templateFragment = templateFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TemplateDetail templateDetails = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_template, parent, false);
            viewHolder.listitem_template_name = (TextView) convertView.findViewById(R.id.listitem_template_name);
            viewHolder.listitem_template_detail = (TextView) convertView.findViewById(R.id.listitem_template_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.listitem_template_name.setText(templateDetails.getTemplateName());
        viewHolder.listitem_template_detail.setText(templateDetails.getTemplateContent());
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    public static class ViewHolder {
        public TextView listitem_template_name, listitem_template_detail;
    }
}
package com.ci.shopper.widget;

import java.util.List;
import java.util.Map;

import com.ci.shopper.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupedListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> groupItems;
    private List<String> groups;

    public GroupedListAdapter(Activity context, List<String> groups,
								 Map<String, List<String>> groupItems) {
        this.context = context;
        this.groupItems = groupItems;
        this.groups = groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return groupItems.get(groups.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView item = (TextView) convertView.findViewById(android.R.id.text1);

        item.setText(laptop);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return groupItems.get(groups.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(android.R.layout.simple_expandable_list_item_2,
												null);
        }
        TextView item = (TextView) convertView.findViewById(android.R.id.text1);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;

import androidx.fragment.app.FragmentActivity;

import com.R;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saferoad-Dev1 on 8/28/2017.
 */

public class HelpAdapter extends BaseExpandableListAdapter {

    private List<String> groupItem;
    private List<String> Childtem;
    private LayoutInflater minflater;
    public FragmentActivity activity;

    public HelpAdapter(ArrayList<String> grList, ArrayList<String> childItem) {
        groupItem = grList;
        this.Childtem = childItem;
    }

    public void setInflater(LayoutInflater mInflater, FragmentActivity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        List<String> tempChild= Childtem;
        String chlid = tempChild.get(groupPosition);
        TextViewRegular text = null;
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.adapter_help_child, null);
        }
        text = (TextViewRegular) convertView.findViewById(R.id.childTextview);
        text.setText(chlid);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.adapter_help_parent, null);
        }
        ((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

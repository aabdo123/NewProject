package com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.R;
import com.adapters.HelpAdapter;

import java.util.ArrayList;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class HelpFragment extends Fragment {

    private ExpandableListView expandbleLis;
    private HelpAdapter helpAdapter;
    private ArrayList<String> groupItem;
    private ArrayList<String> childItem;

    public HelpFragment() {
        // Required empty public constructor
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
        }
        setGroupData();
        setChildGroupData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        initView(rootView);
        initAdapter();
        return rootView;
    }

    public void initView(View rootView) {
        expandbleLis = (ExpandableListView) rootView.findViewById(R.id.helpRecyclerView);
    }

    private void initAdapter() {
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);
        helpAdapter = new HelpAdapter(groupItem, childItem);
        helpAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
        expandbleLis.setAdapter(helpAdapter);
    }


    public void setGroupData() {
        groupItem = new ArrayList<String>();
        groupItem.add("1. Login password, prompt \"account or password error\" what to do?");
        groupItem.add("2. Why Device show offline?");
        groupItem.add("3. Why equipment can't locate?");
        groupItem.add("4. Why history playback show a straight line?");
    }

    public void setChildGroupData() {
        childItem = new ArrayList<>();
        /**
         * Add Data For TecthNology
         */
//        ArrayList<String> child = new ArrayList<String>();
//        child.add("Answer: Contact the service provider to reset the password.");
        childItem.add("Answer: Contact the service provider to reset the password.");

        /**
         * Add Data For Mobile
         */
//        child = new ArrayList<String>();
//        child.add("Answer: 1. Check whether the device battery is normal.\n2. Check if sim card have credit or not.\n3. The device in a bad signal area.");
        childItem.add("Answer: 1. Check whether the device battery is normal.\n2. Check if sim card have credit or not.\n3. The device in a bad signal area.");
        /**
         * Add Data For Manufacture
         */
//        child = new ArrayList<String>();
//        child.add("Answer: If device online but no location, please confirm whether device in an area with GPS signal.");
        childItem.add("Answer: If device online but no location, please confirm whether device in an area with GPS signal.");
        /**
         * Add Data For Extras
         */
//        child = new ArrayList<String>();
//        child.add("Answer: Due to device installation position, in a weak signal environment, positioning instability will lead to the straight line (Occasionally electricity jump and drift is a normal phenomenon).");
        childItem.add("Answer: Due to device installation position, in a weak signal environment, positioning instability will lead to the straight line (Occasionally electricity jump and drift is a normal phenomenon).");
    }
}

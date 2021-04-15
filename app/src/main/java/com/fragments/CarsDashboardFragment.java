package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.R;
import com.adapters.CarsGridAdapter;
import com.models.DashboardModel;
import com.views.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

import static com.utilities.constants.AppConstant.CARS_DASHBOARD_MODEL_ARGS;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class CarsDashboardFragment extends Fragment {

    private static CarsDashboardFragment fragment;
    private Activity activity;
    private DashboardModel dashboardModel;
    private ExpandableHeightGridView carsGridView;


    public CarsDashboardFragment() {
        // Required empty public constructor
    }

    public static CarsDashboardFragment newInstance(DashboardModel dashboardModel) {
//        if (fragment == null) {
            fragment = new CarsDashboardFragment();
            Bundle args = new Bundle();
            args.putParcelable(CARS_DASHBOARD_MODEL_ARGS, dashboardModel);
            fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
            dashboardModel = mBundle.getParcelable(CARS_DASHBOARD_MODEL_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cars_dashboard, container, false);
        initView(rootView);
        initAdapter();
        return rootView;
    }

    private void initView(View rootView) {
        carsGridView = (ExpandableHeightGridView) rootView.findViewById(R.id.carsGridView);
        carsGridView.setExpanded(true);
    }

    private List<String> dashboardModelList() {
        List<String> modelList = new ArrayList<>();
        modelList.add(String.valueOf(dashboardModel.getTotalVehiclesNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalOfflineNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalOnlineNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalAlarmsCount()));
        return modelList;
    }

    private void initAdapter() {
        CarsGridAdapter carsGridAdapter = new CarsGridAdapter(activity, dashboardModelList());
        carsGridView.setAdapter(carsGridAdapter);
        carsGridAdapter.notifyDataSetChanged();
    }

}
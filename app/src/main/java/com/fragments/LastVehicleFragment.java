package com.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.R;
import com.models.DashboardModel;
import com.views.TextViewRegular;

import static com.utilities.constants.AppConstant.LAST_VEHICLE_DASHBOARD_MODEL_ARGS;


/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class LastVehicleFragment extends Fragment {

    public static LastVehicleFragment fragment;
    private DashboardModel dashboardModel;
    private TextViewRegular longtimeOfflineTextView;
    private TextViewRegular expiringUsersTextView;
    private TextViewRegular expiringUsersLabelTextView;
    private TextViewRegular plateNumberTextView;
    private RelativeLayout lastViewVehicleLayout;

    public LastVehicleFragment() {
        // Required empty public constructor
    }

    public static LastVehicleFragment newInstance(DashboardModel dashboardModel) {
//        if (fragment == null) {
            fragment = new LastVehicleFragment();
            Bundle args = new Bundle();
            args.putParcelable(LAST_VEHICLE_DASHBOARD_MODEL_ARGS, dashboardModel);
            fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
            dashboardModel = mBundle.getParcelable(LAST_VEHICLE_DASHBOARD_MODEL_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_last_vehicle, container, false);
        initView(rootView);
        setUpData();
        return rootView;
    }

    private void initView(View rootView) {
        longtimeOfflineTextView = (TextViewRegular) rootView.findViewById(R.id.longtimeOfflineTextView);
        expiringUsersTextView = (TextViewRegular) rootView.findViewById(R.id.expiringUsersTextView);
        expiringUsersLabelTextView = (TextViewRegular) rootView.findViewById(R.id.expiringUsersLabelTextView);
        plateNumberTextView = (TextViewRegular) rootView.findViewById(R.id.plateNumberTextView);
        lastViewVehicleLayout = (RelativeLayout) rootView.findViewById(R.id.lastViewVehicleLayout);
    }

    private void setUpData() {
        longtimeOfflineTextView.setText("" + dashboardModel.getTotalLongtimeOffline());
        expiringUsersTextView.setText("" + dashboardModel.getTotleExpiringUsers());
        if (dashboardModel.getLastVehicleLocation() != null) {
            lastViewVehicleLayout.setVisibility(View.VISIBLE);
            plateNumberTextView.setText(
                    dashboardModel.getLastVehicleLocation().getLabel());
        } else {
            lastViewVehicleLayout.setVisibility(View.GONE);
        }
    }
}
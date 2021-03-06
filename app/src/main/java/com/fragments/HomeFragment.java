package com.fragments;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.DashboardModel;
import com.utilities.AppUtils;
import com.views.Progress;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment fragment;
    public static Context context;
    private FrameLayout progressCounter;
    private FrameLayout dashboardCounter;
    private FrameLayout vehicleCounter;
    private SwipeRefreshLayout pullToRefresh;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
//        if (fragment == null) {
        fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
        }
        dashboardApiCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(rootView);
        initListeners();
        pullToRefresh();
        return rootView;
    }

    public void initView(View rootView) {
        progressCounter = (FrameLayout) rootView.findViewById(R.id.progressCounter);
        dashboardCounter = (FrameLayout) rootView.findViewById(R.id.dashboardCounter);
        vehicleCounter = (FrameLayout) rootView.findViewById(R.id.vehicleCounter);
        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);

    }

    private void pullToRefresh() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashboardApiCall();
            }
        });
    }


    private void initListeners() {
    }

    public void addProgressFragment(DashboardModel dashboardModel) {
//        ProgressFragment progressFragment = new ProgressFragment();
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.progressCounter, ProgressFragment.newInstance(dashboardModel)).commit();
        }
    }

    public void addCarsDashboardFragment(DashboardModel dashboardModel) {
//        CarsDashboardFragment carsDashboardFragment = new CarsDashboardFragment();
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.dashboardCounter, CarsDashboardFragment.newInstance(dashboardModel)).commit();
        }
    }

    public void addLastVehicleFragment(DashboardModel dashboardModel) {
//        LastVehicleFragment lastVehicleFragment = new LastVehicleFragment();
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.vehicleCounter, LastVehicleFragment.newInstance(dashboardModel)).commit();
        }
    }

    private void dashboardApiCall() {
        if (pullToRefresh != null) {
            pullToRefresh.setRefreshing(true);
        } else
            Progress.showLoadingDialog(getActivity());
        BusinessManager.postDashboard(new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                DashboardModel dashboardModel = (DashboardModel) responseObject;
                if (dashboardModel != null) {
                    addProgressFragment(dashboardModel);
                    addCarsDashboardFragment(dashboardModel);
                    addLastVehicleFragment(dashboardModel);
                }
                if (pullToRefresh != null)
                    pullToRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
                if (statusCode == 401) {
                    AppUtils.endSession(getActivity());
                }
                if (pullToRefresh != null)
                    pullToRefresh.setRefreshing(false);
            }
        });
    }


}

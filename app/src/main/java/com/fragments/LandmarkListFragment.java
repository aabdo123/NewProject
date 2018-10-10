package com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.R;
import com.adapters.LandmarkListAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.LandmarkModel;
import com.utilities.Utils;
import com.views.Click;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LandmarkListFragment extends Fragment {

    private FragmentActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextViewRegular emptyListTextView;
    private RecyclerView landmarkRecyclerView;

    public LandmarkListFragment() {
        // Required empty public constructor
    }

    public static LandmarkListFragment newInstance() {
        return new LandmarkListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landmark_list, container, false);
        initView(rootView);
        Progress.showLoadingDialog(activity);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getScheduledReportsApiCall();
    }

    private void initView(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        emptyListTextView = (TextViewRegular) rootView.findViewById(R.id.emptyListTextView);
        landmarkRecyclerView = (RecyclerView) rootView.findViewById(R.id.landmarkRecyclerView);
        landmarkRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getScheduledReportsApiCall();
            }
        });
    }


    private void initAdapter(List<LandmarkModel> scheduledReportslList) {
        LandmarkListAdapter landmarksAdapter = new LandmarkListAdapter(activity, scheduledReportslList, new Click() {
            @Override
            public void onClick() {
                isEmptyListTextVisible(true);
            }

            @Override
            public void addMaps() {

            }
        });
        landmarkRecyclerView.setAdapter(landmarksAdapter);
    }

    private void getScheduledReportsApiCall() {
        swipeRefreshLayout.setRefreshing(true);
        BusinessManager.postLandMarkList("-1", new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
                LandmarkModel[] model = (LandmarkModel[]) responseObject;
                List<LandmarkModel> scheduledReportslList = new ArrayList<>();
                Collections.addAll(scheduledReportslList, model);
                if (Utils.isNotEmptyList(scheduledReportslList)) {
                    initAdapter(scheduledReportslList);
                    isEmptyListTextVisible(false);
                } else {
                    isEmptyListTextVisible(true);
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void isEmptyListTextVisible(boolean isVisible) {
        emptyListTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        landmarkRecyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
}
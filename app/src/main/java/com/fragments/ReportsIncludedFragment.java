package com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.R;
import com.adapters.ReportsIncludedAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.ReportsTypeModel;
import com.utilities.Utils;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportsIncludedFragment extends Fragment {

    public static ReportsIncludedFragment includedFragment;
    private FragmentActivity activity;
    private TextViewRegular emptyListTextView;
    private RecyclerView reportsIncludedRecyclerView;

    public ReportsIncludedFragment() {
        // Required empty public constructor
    }

    public static ReportsIncludedFragment newInstance() {
        if (includedFragment == null)
            includedFragment = new ReportsIncludedFragment();
        return includedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports_included, container, false);
        initView(rootView);
        getReportsTypeApiCall();
        return rootView;
    }

    private void initView(View rootView) {
        emptyListTextView = (TextViewRegular) rootView.findViewById(R.id.emptyListTextView);
        reportsIncludedRecyclerView = (RecyclerView) rootView.findViewById(R.id.reportsIncludedRecyclerView);
        reportsIncludedRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }


    private void initAdapter(List<ReportsTypeModel> typeList) {
        ReportsIncludedAdapter adapter = new ReportsIncludedAdapter(getActivity(), typeList);
        reportsIncludedRecyclerView.setAdapter(adapter);
    }

    private void getReportsTypeApiCall() {
        Progress.showLoadingDialog(activity);
        List<ReportsTypeModel> typeList = new ArrayList<>();
        BusinessManager.getReportsType(new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                ReportsTypeModel[] model = (ReportsTypeModel[]) responseObject;
                Collections.addAll(typeList, model);
                if (Utils.isNotEmptyList(typeList)) {
                    initAdapter(typeList);
                } else {
                    emptyListTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }
}
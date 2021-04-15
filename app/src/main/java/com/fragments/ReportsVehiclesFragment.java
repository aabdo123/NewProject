package com.fragments;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsActivity;
import com.adapters.ReportsVehiclesAdapter;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.ListOfVehiclesSmallModel;
import com.utilities.AnimationUtils;
import com.utilities.Utils;
import com.views.Progress;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportsVehiclesFragment extends Fragment {

    public static ReportsVehiclesFragment reportsVehiclesFragment;
    private Activity activity;
    private RecyclerView vehiclesRecyclerView;
    private String responseObj = null;
    private String vehicleId;

    public ReportsVehiclesFragment() {
        // Required empty public constructor
    }

    public static ReportsVehiclesFragment newInstance() {
        if (reportsVehiclesFragment == null)
            reportsVehiclesFragment = new ReportsVehiclesFragment();
        return reportsVehiclesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports_vehicles, container, false);
        initView(rootView);
        getVehicles();
        return rootView;
    }

    private void initView(View rootView) {
        vehiclesRecyclerView = (RecyclerView) rootView.findViewById(R.id.vehiclesRecyclerView);
    }

    public void getVehicles() {
//        Progress.showLoadingDialog(activity);
        if (responseObj != null && responseObj.length() > 0) {
            initListFromApi(responseObj);
        } else {
            getVehiclesApiCall();
        }
    }

    private void getVehiclesApiCall() {
        BusinessManager.postVehiclesSmall(new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                responseObj = responseObject;
                initListFromApi(responseObj);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
//                Progress.dismissLoadingDialog();
            }
        });
    }

    private void initListFromApi(String responseObject) {
        ListOfVehiclesSmallModel.Vehicles[] vehicleModel = ((AddReportsActivity) activity).gHelper().fromJson(responseObject,ListOfVehiclesSmallModel.Vehicles[].class);
        ArrayList<ListOfVehiclesSmallModel> listOfVehiclesModels = new ArrayList<>();
        ListOfVehiclesSmallModel listOfVehiclesModel = new ListOfVehiclesSmallModel();
        listOfVehiclesModel.setHeader(getString(R.string.vehicles_list));
        listOfVehiclesModel.setVehicleModel(Arrays.asList(vehicleModel));
        listOfVehiclesModels.add(listOfVehiclesModel);
        // SET UP DATA IN VIEWS
        if (listOfVehiclesModels.get(0).getVehicleModel() != null)
            initAdapter(listOfVehiclesModels);
    }

    private void initAdapter(ArrayList<ListOfVehiclesSmallModel> arrayList) {
        if (Utils.isNotEmptyList(arrayList)) {
            if (Utils.isNotEmptyList(arrayList.get(0).getVehicleModel())) {
                ReportsVehiclesAdapter adapter = new ReportsVehiclesAdapter(activity, arrayList);
                vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                vehiclesRecyclerView.setAdapter(adapter);
                adapter.expandParent(arrayList.get(0));
                AnimationUtils.loadListAnimation(vehiclesRecyclerView);
            }
        }
//        Progress.dismissLoadingDialog();
    }
}
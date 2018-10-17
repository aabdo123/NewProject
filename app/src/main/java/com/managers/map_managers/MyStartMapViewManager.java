package com.managers.map_managers;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.R;
import com.activities.MainActivity;
import com.fragments.LisOfVehiclesMapFragment;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.AllVehiclesInHashModel;
import com.models.ListOfVehiclesModel;
import com.utilities.Utils;
import com.views.Progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MyStartMapViewManager {

    private Activity activity;
    private ArrayList<ListOfVehiclesModel> listOfVehiclesModels;
    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> mapList;
    private String responseObj = null;

    public MyStartMapViewManager(Activity activity){
        this.activity = activity;
        getVehiclesApiCall();
    }

    private void getVehiclesApiCall() {
        Progress.showLoadingDialog(activity);
        BusinessManager.postVehicles(new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                responseObj = responseObject;
                initListFromApi(responseObj);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
 //                ((MainActivity) activity).call(LisOfVehiclesMapFragment.newInstance(new ArrayList<>(), new ArrayList<>()), activity.getString(R.string.nav_map));
            }
        });
    }

    private void initListFromApi(String responseObject) {
        ListOfVehiclesModel.VehicleModel[] vehicleModel = ((MainActivity) activity).gHelper().fromJson(responseObject, ListOfVehiclesModel.VehicleModel[].class);
        listOfVehiclesModels = new ArrayList<ListOfVehiclesModel>();
        ListOfVehiclesModel listOfVehiclesModel = new ListOfVehiclesModel();
        listOfVehiclesModel.setHeader(activity.getString(R.string.vehicles_list));
        listOfVehiclesModel.setVehicleModel(Arrays.asList(vehicleModel));
        listOfVehiclesModels.add(listOfVehiclesModel);
        initListFromApiWithoutHeader(responseObj);
        // SET UP DATA IN VIEWS
        if (Utils.isNotEmptyList(mapList)) {
            ((MainActivity) activity).call(LisOfVehiclesMapFragment.newInstance(mapList, listOfVehiclesModels), activity.getString(R.string.nav_map));
        }
    }

    private void initListFromApiWithoutHeader(String responseObject) {
        mapList = new ArrayList<>();
        AllVehiclesInHashModel.AllVehicleModel[] vehicleModel = ((MainActivity) activity).gHelper().fromJson(responseObject, AllVehiclesInHashModel.AllVehicleModel[].class);
        Collections.addAll(mapList, vehicleModel);
    }


    public void viewAllMarkers(ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehiclesList ){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
            LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());
            builder.include(lng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        googleMap.animateCamera(cu);
    }

}

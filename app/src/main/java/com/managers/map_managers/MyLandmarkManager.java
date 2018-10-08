package com.managers.map_managers;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.R;
import com.activities.MainActivity;
import com.fragments.LandmarkFragment;
import com.fragments.LisOfVehiclesMapFragment;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.LandmarkModel;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.map.MapUtils;
import com.views.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.utilities.AppUtils.getLandMarkerIcon;
import static com.utilities.constants.AppConstant.MARKER_PADDING_OFFSET;
import static com.utilities.constants.AppConstant.ZOOM_VALUE_18;

public class MyLandmarkManager {

    private Activity activity;
    private GoogleMap googleMap;
    private LatLng myCurrentLatLng;
    private Fragment targetFragment;
    private Marker landmarkNewMarker;
    private static List<LandmarkModel> landmarkList;
    private static List<Marker> landmarkMarkersList;
    private LandmarkClusterManager landmarkClusterManager;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    public MyLandmarkManager(Activity activity, GoogleMap googleMap, LatLng myCurrentLatLng) {
        this.activity = activity;
        this.googleMap = googleMap;
        this.myCurrentLatLng = myCurrentLatLng;
    }

    public void setTargetFragment(LisOfVehiclesMapFragment fragment) {
        this.targetFragment = fragment;
    }

    public void setViews(boolean isShowCliched, boolean isAddClicked) {
        if (isShowCliched) {
            getLandmarkListApiCall();
        }

        if (isAddClicked) {
            LandmarkFragment fenceFragment = LandmarkFragment.newInstance(myCurrentLatLng.latitude, myCurrentLatLng.longitude);
            if (targetFragment != null)
                fenceFragment.setTargetFragment(targetFragment, AppConstant.TARGET_FRAGMENT_GEOFENCE);
            ((MainActivity) activity).callUpAnimation(fenceFragment, activity.getString(R.string.landmark));
        }
    }

    public void setLandMarkClusterManager(boolean isShowCluster) {
        if (isShowCluster) {
            landmarkClusterManager = new LandmarkClusterManager(activity, googleMap, landmarkList);
            setMarkersVisibility(false);
            landmarkClusterManager.startClustering();
        } else {
            if (landmarkClusterManager != null) {
                setMarkersVisibility(true);
                landmarkClusterManager.removeLandMarkCluster();
                landmarkClusterManager = null;
            }
        }
    }

    private void getLandmarkListApiCall() {
        Progress.showLoadingDialog(activity);
        landmarkList = new ArrayList<>();
        BusinessManager.postLandMarkList("-1", new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                LandmarkModel[] geoFenceModel = (LandmarkModel[]) responseObject;
                Collections.addAll(landmarkList, geoFenceModel);
                addLandMarkersOnMap();
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }

    private void addLandMarkersOnMap() {
        landmarkMarkersList = new ArrayList<>();
        if (Utils.isNotEmptyList(landmarkList)) {
            for (LandmarkModel model : landmarkList) {
                LatLng landmarkLatLng = new LatLng(model.getLatitude(), model.getLongitude());
                addAllLandmarkOnMap(landmarkLatLng, model.getIcon(), model.getPOIName());
                builder.include(landmarkLatLng);
            }
        }
    }

    public void addLandMarker(LatLng lng, int landmarkId) {
        addLandmarkMarkerOnMap(lng, landmarkId);
        animateCamera(lng);
    }

    public void afterSuccessAddLandMarkerMarker(LatLng lng, int landmarkId, String poiName) {
        landmarkNewMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                .icon(getLandMarkerIcon(landmarkId))
                .anchor(0.5f, 0.5f)
                .title(poiName));
        animateCamera(lng);
    }

    public void newLandMarkerItemToList() {
        landmarkMarkersList.add(landmarkNewMarker);
    }

    private void animateCamera(LatLng locationCamera) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCamera, ZOOM_VALUE_18));
    }

    private void addAllLandmarkOnMap(LatLng lng, String iconName, String poiName) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(lng)
                .icon(getLandMarkerIcon(iconName))
                .anchor(0.5f, 0.5f)
                .title(poiName));
        landmarkMarkersList.add(marker);
    }


    public void removeLandMarkerFromMap() {
        if (Utils.isNotEmptyList(landmarkMarkersList)) {
            for (Marker marker : landmarkMarkersList) {
                marker.remove();
            }
        }
    }

    private void addLandmarkMarkerOnMap(LatLng lng, int landmarkId) {
//        googleMap.addMarker(MapUtils.createMarker(lng, getLandMarkerIcon(landmarkId)));
        landmarkNewMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                .icon(getLandMarkerIcon(landmarkId))
                .anchor(0.5f, 0.5f));
    }

    public void updateLandmarkIconOnMap(LatLng lng, int landmarkId) {
        if (lng != null) {
            landmarkNewMarker.remove();
            addLandmarkMarkerOnMap(lng, landmarkId);
        }
    }

    public void setMarkersVisibility(boolean visibility) {
        if (Utils.isNotEmptyList(landmarkMarkersList)) {
            for (Marker marker : landmarkMarkersList) {
                marker.setVisible(visibility);
            }
        }
    }


    private void setLocationIfNeeded() {
        LatLngBounds bounds = builder.build();

        //Setting the width and height of your screen
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;
        double random = Utils.getRandomNumber(0.12, 0.10);
//        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        int padding = (int) (width * random);

//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Utils.getRandomNumber(115, 105));
        CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        googleMap.animateCamera(cu);
    }

}

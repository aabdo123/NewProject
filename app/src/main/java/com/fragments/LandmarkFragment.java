package com.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.R;
import com.activities.MainActivity;
import com.adapters.LandmarkAdapter;
import com.managers.ApiCallResponseRetrofit;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.BusinessManagers;
import com.managers.PreferencesManager;
import com.managers.map_managers.MyLandmarkManager;
import com.managers.map_managers.MyMapStyleManager;
import com.models.LandMarkerTypeModel;
import com.utilities.ToastHelper;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.utilities.map.MapUtils;
import com.views.AppCompatEditTextLight;
import com.views.ButtonBold;
import com.views.ClickOnList;
import com.views.MyMapView;
import com.views.Progress;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.utilities.constants.AppConstant.KSA_LATLNG;
import static com.utilities.constants.AppConstant.ZOOM_VALUE_15;
import static com.utilities.constants.AppConstant.ZOOM_VALUE_18;
import static com.utilities.constants.AppConstant.ZOOM_VALUE_6;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class LandmarkFragment extends Fragment implements MapStyleDialogFragment.OnSelectMapStyle, OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

    public static LandmarkFragment landmarkFragment;
    private FragmentActivity activity;
    private Context context;

    private com.github.clans.fab.FloatingActionButton mapStylingFab;
    private MyMapView mMapView;
    private GoogleMap googleMap;
    private ScrollView scrollView;
    private AppCompatEditTextLight landmarkNameEditText;
    private ButtonBold addLandmarkButton;
    private RecyclerView landmarkRecyclerView;
    private LinearLayoutManager layoutManager;

    private LatLng landLatLng;
    private double latArgs;
    private double lngArgs;
    private List<LandMarkerTypeModel> landmarkList;
    private int selectedListItem = 0;

    private MyLandmarkManager myLandmarkManager;
    private MyMapStyleManager myMapStyleManager;
    private OnAddLandmark onAddLandmark;

    public interface OnAddLandmark {
        void onAddLandmark(LatLng geoLatLng, int landmarkId, String poiName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            if (getTargetFragment() instanceof OnAddLandmark) {
                onAddLandmark = (OnAddLandmark) getTargetFragment();
            } else {
                onAddLandmark = (OnAddLandmark) context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAddLandmark = null;
    }

    public LandmarkFragment() {
        // Required empty public constructor
    }

    public static LandmarkFragment newInstance(double lat, double lng) {
//        if (fragment == null) {
        landmarkFragment = new LandmarkFragment();
        Bundle args = new Bundle();
        args.putDouble(AppConstant.GEO_LAT_ARGS, lat);
        args.putDouble(AppConstant.GEO_LNG_ARGS, lng);
        landmarkFragment.setArguments(args);
//        }
        return landmarkFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            latArgs = mBundle.getDouble(AppConstant.GEO_LAT_ARGS);
            lngArgs = mBundle.getDouble(AppConstant.GEO_LNG_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landmark, container, false);
        initView(rootView);
        initListeners();
        setLandmarkAdapter();

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(activity.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        return rootView;
    }

    private void initView(View rootView) {
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);

        mapStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);

        mMapView = (MyMapView) rootView.findViewById(R.id.mapView);
        landmarkNameEditText = (AppCompatEditTextLight) rootView.findViewById(R.id.landmarkNameEditText);
        addLandmarkButton = (ButtonBold) rootView.findViewById(R.id.addLandmarkButton);
        landmarkRecyclerView = (RecyclerView) rootView.findViewById(R.id.landmarkRecyclerView);
        layoutManager = new LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false);
        landmarkRecyclerView.setLayoutManager(layoutManager);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        mapStylingFab.setOnClickListener(this);

        addLandmarkButton.setOnClickListener(this);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return mMapView.onTouchEvent(event);
            }
        });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        landLatLng = latLng;
        googleMap.clear();
        setMyLandmarkManager();
    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        setMapStyleDialog();
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (latArgs != 0.0 && lngArgs != 0.0) {
            landLatLng = new LatLng(latArgs, lngArgs);
            animateCameraToKsa(landLatLng, ZOOM_VALUE_15);
            setMyLandmarkManager();
        } else {
            animateCameraToKsa(KSA_LATLNG, ZOOM_VALUE_6);
        }
    }

    private void setMyLandmarkManager() {
        myLandmarkManager = new MyLandmarkManager(activity, googleMap, landLatLng);
        myLandmarkManager.addLandMarker(landLatLng, landmarkList.get(selectedListItem).getLandmarkId());
    }

    private void updateLandmarkIconOnMap() {
        if (myLandmarkManager != null){
            myLandmarkManager.updateLandmarkIconOnMap(landLatLng, landmarkList.get(selectedListItem).getLandmarkId());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addLandmarkButton:
                validation();
                break;

            case R.id.mapStylingFab:
                openMapStyleDialog();
                break;
        }
    }

    private void setMapStyleDialog() {
        myMapStyleManager = new MyMapStyleManager(activity, googleMap);
        myMapStyleManager.setTargetFragment(this);
        myMapStyleManager.setSelectedStyle(PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_ID));
    }

    private void openMapStyleDialog() {
        if (myMapStyleManager != null)
            myMapStyleManager.showDialog();
    }

    @Override
    public void onSelectMapStyle(int selectedItemId) {
        if (myMapStyleManager != null) {
            myMapStyleManager.setSelectedStyle(selectedItemId);
            PreferencesManager.getInstance().setBooleanValue(true, SharesPrefConstants.IS_MAP_STYLE_CHANGED);
        }
    }

    private void validation() {
        if (landLatLng == null) {
            ToastHelper.toastWarningLong(activity, "set location first ");
            return;
        }

        if (landmarkNameEditText.getText().toString().isEmpty()) {
            ToastHelper.toastWarningLong(activity, getString(R.string.landmark_name_is_required));
            return;
        }

        geoFenceApiCall();
    }

    private void geoFenceApiCall() {
        Progress.showLoadingDialog(activity);
        BusinessManagers.postAddLandMark(
                landLatLng.longitude,
                landLatLng.latitude,
                getLandMarkerIconName(landmarkList.get(selectedListItem).getLandmarkId()),
                landmarkName(),
                new ApiCallResponseString() {
                    @Override
                    public void onSuccess(int statusCode, String responseObject) {
                        Progress.dismissLoadingDialog();
                        if (responseObject.equals("true"))
                            onSuccessSaveLandmark();
                        else
                            ToastHelper.toastWarningLong(activity, activity.getString(R.string.something_went_worng));
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                        Progress.dismissLoadingDialog();
                        ToastHelper.toastWarningLong(context, activity.getString(R.string.something_went_worng));
                    }
                });
    }

    private void setLandmarkAdapter() {
        LandmarkAdapter landmarkAdapter = new LandmarkAdapter(activity, getLandmarkList(), new ClickOnList() {
            @Override
            public void onClick(int position) {
                selectedListItem = position;
                updateLandmarkIconOnMap();
            }
        });
        landmarkRecyclerView.setAdapter(landmarkAdapter);
    }

    private void onSuccessSaveLandmark() {
        if (onAddLandmark != null && onAddLandmark == LisOfVehiclesMapFragment.fragment)
            onAddLandmark.onAddLandmark(landLatLng, landmarkList.get(selectedListItem).getLandmarkId(), landmarkNameEditText.getText().toString());
        landmarkNameEditText.setText("");
        ToastHelper.toastSuccessLong(activity, getString(R.string.done));
        ((MainActivity) activity).onBackPressed();
    }

//    private String landmarkName() {
//        String name = landmarkNameEditText.getText().toString().trim();
//        if (landmarkNameEditText.getText().toString().contains(" ")) {
//            name = landmarkNameEditText.getText().toString().trim();
//            name = name.replaceAll(" ", "%20");
//        }
//        return name;
//    }

    private String landmarkName() {
        String name = landmarkNameEditText.getText().toString().trim();
            try {
                return URLEncoder.encode(name,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return name;
            }
    }

    private void animateCameraToKsa(LatLng locationLatLng, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(locationLatLng).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private List<LandMarkerTypeModel> getLandmarkList() {
        landmarkList = new ArrayList<>();
        landmarkList.add(new LandMarkerTypeModel(0, R.string.bank, activity.getString(R.string.bank), activity.getDrawable(R.drawable.ic_bank)));
        landmarkList.add(new LandMarkerTypeModel(0, R.string.building, activity.getString(R.string.building), activity.getDrawable(R.drawable.ic_building)));
        landmarkList.add(new LandMarkerTypeModel(0, R.string.landmark_home, activity.getString(R.string.landmark_home), activity.getDrawable(R.drawable.ic_home_landmark)));
        landmarkList.add(new LandMarkerTypeModel(0, R.string.office, activity.getString(R.string.office), activity.getDrawable(R.drawable.ic_office)));
        landmarkList.add(new LandMarkerTypeModel(0, R.string.school, activity.getString(R.string.school), activity.getDrawable(R.drawable.ic_school)));
        landmarkList.add(new LandMarkerTypeModel(0, R.string.university, activity.getString(R.string.university), activity.getDrawable(R.drawable.ic_university)));
        return landmarkList;
    }

    private String getLandMarkerIconName(int landmarkId) {
        switch (landmarkId) {
            case R.string.bank:
                return "Bank";

            case R.string.building:
                return "Building";

            case R.string.landmark_home:
                return "Home";

            case R.string.office:
                return "Office";

            case R.string.school:
                return "School";

            case R.string.university:
                return "University";

            default:
                return "";
        }
    }
}

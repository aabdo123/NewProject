package com.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.R;
import com.activities.MainActivity;
import com.managers.ApiCallResponse;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.managers.ShortTermManager;
import com.managers.map_managers.MyGeoFenceManager;
import com.managers.map_managers.MyMapStyleManager;
import com.models.GeoFenceModel;
import com.models.ListOfVehiclesModel;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.utilities.map.MapAreaManager;
import com.utilities.map.MapAreaWrapper;
import com.utilities.map.MapUtils;
import com.views.AppCompatEditTextLight;
import com.views.ButtonBold;
import com.views.MyMapView;
import com.views.Progress;
import com.views.TextViewLight;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class GeoFenceFragment extends Fragment implements MapStyleDialogFragment.OnSelectMapStyle, OnMapReadyCallback, VehiclesDialogFragment.OnFinishSelectVehicles, View.OnClickListener {

    public static GeoFenceFragment geoFenceFragment;
    private FragmentActivity activity;
    private Context context;
    private String vehicleId;

    private com.github.clans.fab.FloatingActionButton mapStylingFab;

    private TextViewLight radiusTextView;
    private AppCompatEditTextLight geoNameEditText;
    private AppCompatEditTextLight geoSpeedEditText;
    private RelativeLayout vehicleListLayout;
    private TextViewLight vehicleListTextView;
    private ImageView emptyListImageView;
    private Switch geoInSwitch;
    private Switch geoOutSwitch;
    private ButtonBold addGeoButton;

    private MyMapView mMapView;
    private GoogleMap googleMap;
    private ScrollView scrollView;
    private LatLng geoLatLog;
    private double latArgs;
    private double lngArgs;
    private String radius;

    private MyGeoFenceManager myGeoFenceManager;
    private MyMapStyleManager myMapStyleManager;

    private ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence = new ArrayList<>();

    private OnAddGeoFence onAddGeoFence;

    public interface OnAddGeoFence {
        void onAddGeoFence(LatLng geoLatLng, String radius);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            if (getTargetFragment() instanceof OnAddGeoFence) {
                onAddGeoFence = (OnAddGeoFence) getTargetFragment();
            } else {
                onAddGeoFence = (OnAddGeoFence) context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAddGeoFence = null;
    }

    public GeoFenceFragment() {
        // Required empty public constructor
    }

    public static GeoFenceFragment newInstance(String vehicle_id, ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence, double lat, double lng) {
//        if (fragment == null) {
        geoFenceFragment = new GeoFenceFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.VEHICLE_ID_ARGS, vehicle_id);
        args.putDouble(AppConstant.GEO_LAT_ARGS, lat);
        args.putDouble(AppConstant.GEO_LNG_ARGS, lng);
        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS, listOfVehiclesForGeoFence);
        geoFenceFragment.setArguments(args);
//        }
        return geoFenceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("VEHICLE_ID_ARGS", "getArgs");
            vehicleId = mBundle.getString(AppConstant.VEHICLE_ID_ARGS);
            listOfVehiclesForGeoFence = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS);
            resetIsSelectedFromList();
            latArgs = mBundle.getDouble(AppConstant.GEO_LAT_ARGS);
            lngArgs = mBundle.getDouble(AppConstant.GEO_LNG_ARGS);
            if (latArgs != 0.0 && lngArgs != 0.0) {
                geoLatLog = new LatLng(latArgs, lngArgs);
            } else {
                geoLatLog = new LatLng(0.0, 0.0);
            }
//            else {
//                geoLatLog = new LatLng(listOfVehiclesForGeoFence.get(0).getVehicleModel().get(0).getLastLocation().getLatitude(), listOfVehiclesForGeoFence.get(0).getVehicleModel().get(0).getLastLocation().getLatitude());
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo_fence, container, false);
        initView(rootView);
        initListeners();

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
        mapStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        mMapView = (MyMapView) rootView.findViewById(R.id.mapView);
        radiusTextView = (TextViewLight) rootView.findViewById(R.id.radiusTextView);
        geoNameEditText = (AppCompatEditTextLight) rootView.findViewById(R.id.geoNameEditText);
        geoSpeedEditText = (AppCompatEditTextLight) rootView.findViewById(R.id.geoSpeedEditText);
        vehicleListLayout = (RelativeLayout) rootView.findViewById(R.id.vehicleListLayout);
        vehicleListTextView = (TextViewLight) rootView.findViewById(R.id.vehicleListTextView);
        emptyListImageView = (ImageView) rootView.findViewById(R.id.emptyListImageView);
        geoInSwitch = (Switch) rootView.findViewById(R.id.geoInSwitch);
        geoOutSwitch = (Switch) rootView.findViewById(R.id.geoOutSwitch);
        addGeoButton = (ButtonBold) rootView.findViewById(R.id.addGeoButton);

        if (!Utils.isNotEmptyList(listOfVehiclesForGeoFence)) {
            vehicleListLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {
        mapStylingFab.setOnClickListener(this);

        vehicleListTextView.setOnClickListener(this);
        addGeoButton.setOnClickListener(this);
        emptyListImageView.setOnClickListener(this);

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
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        setMapStyleDialog();
        googleMaps.getUiSettings().setMyLocationButtonEnabled(false);
        if (geoLatLog != null) {
            setGeoFenceManager();
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void setGeoFenceManager() {
        myGeoFenceManager = new MyGeoFenceManager(activity, googleMap, geoLatLog, listOfVehiclesForGeoFence);
        myGeoFenceManager.setCircleManager(new MapAreaManager.CircleManagerListener() {
            @Override
            public void onCreateCircle(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "drag end circle: " + draggableCircle);
                setRadiusInfo(draggableCircle);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(draggableCircle.getCenter(), MapUtils.getZoomLevel(draggableCircle.getRadius())));
            }

            @Override
            public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "crate circle: " + draggableCircle);
                setRadiusInfo(draggableCircle);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(draggableCircle.getCenter(), MapUtils.getZoomLevel(draggableCircle.getRadius())));
            }

            @Override
            public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "moved circle: " + draggableCircle);
                setRadiusInfo(draggableCircle);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(draggableCircle.getCenter(), MapUtils.getZoomLevel(draggableCircle.getRadius())));
            }

            @Override
            public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "move circle start: " + draggableCircle);
                setRadiusInfo(draggableCircle);
            }

            @Override
            public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "resize circle start: " + draggableCircle);
                setRadiusInfo(draggableCircle);
            }

            @Override
            public void onMinRadius(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "min radius: " + draggableCircle);
                setRadiusInfo(draggableCircle);
            }

            @Override
            public void onMaxRadius(MapAreaWrapper draggableCircle) {
//                        ToastHelper.toastInfo(activity, "max radius: " + draggableCircle);
                setRadiusInfo(draggableCircle);
            }
        });

    }

    private void setRadiusInfo(MapAreaWrapper draggableCircle) {
        geoLatLog = draggableCircle.getCenter();
        String radiusThumb = getString(R.string.radius) + " " + String.format(Locale.getDefault(), "%.0f", draggableCircle.getRadius()) + " " + getString(R.string.meter);
        radius = String.format(Locale.ENGLISH, "%.0f", draggableCircle.getRadius());
        radiusTextView.setTextColor(context.getResources().getColor(R.color.gray_dark));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                radiusTextView.setText(radiusThumb);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vehicleListTextView:
                showVehiclesDialog();
                break;

            case R.id.emptyListImageView:
                resetIsSelectedFromList();
                setVehicleListTextView("", "");
                break;

            case R.id.addGeoButton:
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
        if (vehicleId == null || vehicleId.equals("")) {
            ToastHelper.toastWarningLong(activity, getString(R.string.please_select_atleast_one_vehicle));
            return;
        }

        if (geoLatLog == null) {
            ToastHelper.toastWarningLong(activity, "set location first ");
            return;
        }

//        if (vehicleListLayout.getVisibility() == View.VISIBLE) {
//            if (!Utils.isNotEmptyList(listOfVehiclesForGeoFence)) {
//                ToastHelper.toastWarningLong(activity, getString(R.string.please_select_atleast_one_vehicle));
//                return;
//            }
//        }

        if (radius == null || radius.equals("")) {
            ToastHelper.toastWarningLong(activity, "set location first ");
            return;
        }

        if (geoNameEditText.getText().toString().isEmpty()) {
            geoNameEditText.setError(getString(R.string.filed_is_required));
            return;
        }

        if (geoSpeedEditText.getText().toString().isEmpty()) {
            geoSpeedEditText.setError(getString(R.string.filed_is_required));
            return;
        }

        geoFenceApiCall();
    }

    private void geoFenceApiCall() {
        Progress.showLoadingDialog(activity);
        BusinessManager.postGeoFence(vehicleId,
                geoName(),
                geoLatLog.longitude, geoLatLog.latitude,
                radius,
                geoSpeedEditText.getText().toString(),
                geoInSwitch.isChecked(),
                geoOutSwitch.isChecked(),
                new ApiCallResponseString() {
                    @Override
                    public void onSuccess(int statusCode, String responseObject) {
                        try {
                            BusinessManager.postGeoFenceList("-1", new ApiCallResponse() {
                                @Override
                                public void onSuccess(int statusCode, Object responseObjectApi) {
                                    Progress.dismissLoadingDialog();
                                    if (responseObject.equals("true")) {
                                        ShortTermManager.getInstance().setGeoFenceRequest(responseObjectApi);
                                        onSuccessSaveGeoFence();
                                    } else {
                                        ToastHelper.toastWarningLong(activity, activity.getString(R.string.something_went_worng));
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String errorResponse) {
                                    Progress.dismissLoadingDialog();
                                    ToastHelper.toastWarningLong(activity, activity.getString(R.string.something_went_worng));
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                        Progress.dismissLoadingDialog();
                        ToastHelper.toastWarningLong(activity, activity.getString(R.string.something_went_worng));
                    }
                });
    }


    private void onSuccessSaveGeoFence() {
        if (onAddGeoFence != null && onAddGeoFence == LisOfVehiclesMapFragment.fragment)
            onAddGeoFence.onAddGeoFence(geoLatLog, radius);
        geoNameEditText.setText("");
        geoSpeedEditText.setText("");
        resetIsSelectedFromList();
        setVehicleListTextView("", "");
        ToastHelper.toastSuccessLong(activity, activity.getString(R.string.geofence_is_added));
        ((MainActivity) activity).onBackPressed();
    }

//    private String geoName() {
//        String e = geoNameEditText.getText().toString().trim();
//        if (geoNameEditText.getText().toString().contains(" ")) {
//            e = geoNameEditText.getText().toString().replaceAll(" ", "%20");
//        }
//        return e;
//    }

    private String geoName() {
        String name = geoNameEditText.getText().toString().trim();
        try {
            return URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return name;
        }
    }

    private void showVehiclesDialog() {
        FragmentManager fm = getFragmentManager();
        VehiclesDialogFragment vehiclesDialogFragment = VehiclesDialogFragment.newInstance(listOfVehiclesForGeoFence);
        vehiclesDialogFragment.setTargetFragment(geoFenceFragment, AppConstant.TARGET_FRAGMENT_DIALOG);
        assert fm != null;
        vehiclesDialogFragment.show(fm, "VehiclesDialogFragment");
    }


    private void resetIsSelectedFromList() {
        if (!Utils.isNotEmptyList(listOfVehiclesForGeoFence))
            return;
        if (!Utils.isNotEmptyList(listOfVehiclesForGeoFence.get(0).getVehicleModel()))
            return;
        for (int i = 0; i < listOfVehiclesForGeoFence.get(0).getVehicleModel().size(); i++) {
            listOfVehiclesForGeoFence.get(0).getVehicleModel().get(i).setSelected(false);
        }
    }

    private void setVehicleListTextView(String vehicleIds, String vehiclesLabels) {
        if (vehiclesLabels != null && vehiclesLabels.length() > 0) {
            vehicleListTextView.setText(vehiclesLabels);
            vehicleListTextView.setTextColor(getResources().getColor(R.color.gray_light));
            emptyListImageView.setVisibility(View.VISIBLE);
            vehicleId = vehicleIds;
        } else {
            vehicleListTextView.setText(getString(R.string.select_vehicles_for_geo_fence));
            vehicleListTextView.setTextColor(getResources().getColor(R.color.hint_gray));
            emptyListImageView.setVisibility(View.INVISIBLE);
            vehicleId = "";
        }
    }

    @Override
    public void onSelectedVehiclesDone(String vehicleIds, String vehiclesLabels) {
        setVehicleListTextView(vehicleIds, vehiclesLabels);
    }

    @Override
    public void onSelectedVehiclesCanceled() {
        resetIsSelectedFromList();
    }
}

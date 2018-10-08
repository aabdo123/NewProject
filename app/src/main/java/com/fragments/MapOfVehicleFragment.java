package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.R;
import com.managers.PreferencesManager;
import com.managers.map_managers.MyMapStyleManager;
import com.models.ListOfVehiclesModel;
import com.models.SignalRCommandModel;
import com.models.SignalRModel;
import com.services.SignalR;
import com.services.SignalRService;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.LogHelper;
import com.utilities.Route;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.views.AlertDialogView;
import com.views.ClickWithTwoParam;
import com.views.PopupDialog;
import com.views.TextViewLight;

import static android.content.Context.LOCATION_SERVICE;

public class MapOfVehicleFragment extends Fragment implements MapStyleDialogFragment.OnSelectMapStyle, OnMapReadyCallback, View.OnClickListener, LocationSource, LocationListener {

    public static MapOfVehicleFragment fragment;

    private SignalRService mService;

    private boolean mBound = false;

    private MapView mMapView;
    private GoogleMap googleMap;
    private MarkerOptions marker;
    private CameraPosition cameraPosition;
    private LatLng carLatLng;
    private LatLng myCurrentLatLng;
    private boolean firstTimeOpenMap = false;

    private com.github.clans.fab.FloatingActionButton mapStylingFab;

    private CheckBox arrowCheckBox;
    private TextViewLight streetTextView;
    private RelativeLayout addressLayout;
    private LinearLayout sliderLayout;

    private TextViewLight groupTextView;
    private TextViewLight mileageTextView;
    private TextViewLight workingHoursTextView;
    private TextViewLight accTextView;
    private TextViewLight displayNameTextView;
    private TextViewLight directionTextView;
    private TextViewLight locationTimeTextView;
    private TextViewLight vehicleStatusTextView;

    private FloatingActionMenu fabMenu;
    private FloatingActionButton plusFab;

    private com.github.clans.fab.FloatingActionButton drawPathFab;
    private com.github.clans.fab.FloatingActionButton changeMapTypeFab;
    private com.github.clans.fab.FloatingActionButton myLocationFab;
    private com.github.clans.fab.FloatingActionButton carLocationFab;

    private Route route;
    private OnLocationChangedListener mapLocationListener = null;
    private LocationManager locationManager = null;
    private Criteria criteria = new Criteria();
    private Context context;
    private Activity activity;
    private FragmentActivity fragmentActivity;
    private ListOfVehiclesModel.VehicleModel vehicleModel;
    private int cameraTilt = 30;

    private MyMapStyleManager myMapStyleManager;

    public MapOfVehicleFragment() {
        // Required empty public constructor
    }

    public static MapOfVehicleFragment newInstance(ListOfVehiclesModel.VehicleModel vehicleModel) {
//        if (fragment == null) {
        fragment = new MapOfVehicleFragment();
        Bundle args = new Bundle();
        args.putParcelable(AppConstant.VEHICLE_MODEL_ARGS, vehicleModel);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstant.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapView.getMapAsync(this);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        startSignalRService();
        super.onResume();
        mMapView.onResume();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(0L, 0.0f, criteria, this, null);
        }

        if (googleMap != null) {
            googleMap.setLocationSource(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        firstTimeOpenMap = false;
        googleMap.setLocationSource(null);
        locationManager.removeUpdates(this);
        stopSignalRService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firstTimeOpenMap = false;
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        fragmentActivity = getActivity();
//        startSignalRService();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            LogHelper.LOG_D("MAP", "getArgs");
            vehicleModel = mBundle.getParcelable(AppConstant.VEHICLE_MODEL_ARGS);
            carLatLng = new LatLng(vehicleModel.getLastLocation().getLatitude(), vehicleModel.getLastLocation().getLongitude());
            PreferencesManager.getInstance().setVehicleModel(vehicleModel, AppConstant.VEHICLE_MODEL_ARGS);
            LogHelper.LOG_D("CAR_LAT_LNG   ", "Lat:  " + vehicleModel.getLastLocation().getLatitude() + " Lng:  " + vehicleModel.getLastLocation().getLongitude());
            LogHelper.LOG_D("CAR_ID   ", "+++++++++++++   " + vehicleModel.getVehicleID() + "   +++++++++++++++");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_of_vehicle, container, false);
        initViews(view);
        initListeners();
        if (!AppUtils.checkLocationPermissions(activity)) {
            AlertDialogView.showGPSDisabledDialog(activity);
        }
        setUpCarInfo();

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(context.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        return view;
    }

    private void initViews(View rootView) {
        mapStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        addressLayout = (RelativeLayout) rootView.findViewById(R.id.addressLayout);
        arrowCheckBox = (CheckBox) rootView.findViewById(R.id.arrowCheckBox);
        sliderLayout = (LinearLayout) rootView.findViewById(R.id.sliderLayout);
        sliderLayout.setVisibility(View.GONE);

        streetTextView = (TextViewLight) rootView.findViewById(R.id.streetTextView);
        groupTextView = (TextViewLight) rootView.findViewById(R.id.groupTextView);
        mileageTextView = (TextViewLight) rootView.findViewById(R.id.mileageTextView);
        workingHoursTextView = (TextViewLight) rootView.findViewById(R.id.workingHoursTextView);
        accTextView = (TextViewLight) rootView.findViewById(R.id.accTextView);
        displayNameTextView = (TextViewLight) rootView.findViewById(R.id.displayNameTextView);
        directionTextView = (TextViewLight) rootView.findViewById(R.id.directionTextView);
        locationTimeTextView = (TextViewLight) rootView.findViewById(R.id.locationTimeTextView);
        vehicleStatusTextView = (TextViewLight) rootView.findViewById(R.id.vehicleStatusTextView);

        plusFab = (FloatingActionButton) rootView.findViewById(R.id.plusFab);
        drawPathFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.drawPathFab);
        changeMapTypeFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.changeMapTypeFab);
        myLocationFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.myLocationFab);
        carLocationFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.carLocationFab);

        fabMenu = (FloatingActionMenu) rootView.findViewById(R.id.fabMenu);
        fabMenu.setIconAnimated(false);
//        fabMenu.hideMenuButton(false);
    }

    private void initListeners() {
        mapStylingFab.setOnClickListener(this);

        addressLayout.setOnClickListener(this);
        plusFab.setOnClickListener(this);
        drawPathFab.setOnClickListener(this);
        changeMapTypeFab.setOnClickListener(this);
        myLocationFab.setOnClickListener(this);
        carLocationFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addressLayout:
                showHideSliderLayout();
                break;

            case R.id.plusFab:
                openPopupFabMenu();
                break;

            case R.id.drawPathFab:
                if (myCurrentLatLng != null && carLatLng != null)
                    drawPathOnMap();
                else
                    AlertDialogView.showGPSDisabledDialog(activity);
                break;

            case R.id.changeMapTypeFab:
                setChangeMapTypeFab();
                break;

            case R.id.myLocationFab:
                if (myCurrentLatLng != null)
                    setLocationIfNeeded(myCurrentLatLng);
                else
                    AlertDialogView.showGPSDisabledDialog(activity);
                break;

            case R.id.carLocationFab:
//                if (carLatLng != null)
                setLocationIfNeeded(carLatLng);
//                else
//                    AlertDialogView.showGPSDisabledDialog(activity);
                break;

            case R.id.mapStylingFab:
                openMapStyleDialog();
                break;
        }
    }


    private void setMapStyleDialog() {
        myMapStyleManager = new MyMapStyleManager(fragmentActivity, googleMap);
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
        }
    }

    private void setUpCarInfo() {
        streetTextView.setText(vehicleModel.getLastLocation().getAddress());
        groupTextView.setText("N/A");
        displayNameTextView.setText(vehicleModel.getLabel());
        workingHoursTextView.setText(Utils.doubleToString(AppUtils.secondsToHours(vehicleModel.getLastLocation().getTotalWorkingHours())));
        mileageTextView.setText(Utils.doubleToString(AppUtils.meterToKilometer(vehicleModel.getLastLocation().getTotalMileage())));
        accTextView.setText(getOnline(vehicleModel.getLastLocation().getIsOnline()));
        directionTextView.setText(AppUtils.getDirectionDegree(context, vehicleModel.getLastLocation().getDirection()));
        vehicleStatusTextView.setText(AppUtils.getCarStatus(activity, vehicleModel.getLastLocation().getVehicleStatus()));
        if (vehicleModel.getLastLocation().getLatitude() != 0.0 || vehicleModel.getLastLocation().getLongitude() != 0.0) {
            locationTimeTextView.setText(Utils.getDateUpUtcToNormalFormat(vehicleModel.getLastLocation().getRecordDateTime()));
        }
    }

    private void updateCarInfo(SignalRModel.A aModel) {
        try {
            streetTextView.setText(aModel.getAddress());
            groupTextView.setText(aModel.getGroupName());
            displayNameTextView.setText(aModel.getVehicleDisplayName());
            workingHoursTextView.setText(Utils.doubleToString(AppUtils.secondsToHours(aModel.getWorkingHours())));
            mileageTextView.setText(Utils.doubleToString(AppUtils.meterToKilometer(aModel.getMileage())));
            accTextView.setText(getOnline(aModel.getEngineStatus()));
            directionTextView.setText(AppUtils.getDirectionDegree(context, aModel.getDirection()));
            locationTimeTextView.setText(Utils.getDateUpUtcToNormalFormat(aModel.getRecordDateTime()));
            vehicleStatusTextView.setText(AppUtils.getCarStatus(activity, String.valueOf(aModel.getVehicleStatus())));
        } catch (IllegalStateException e) {
            setUpCarInfo();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOnline(Boolean isOnline) {
        if (isOnline) {
            return context.getString(R.string.on);
        } else {
            return context.getString(R.string.off);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        setMapStyleDialog();
        addCarMarker();
        googleMap.addMarker(marker);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestLocationUpdates(0L, 0.0f, criteria, this, null);

        googleMaps.setLocationSource(this);
        googleMaps.setMyLocationEnabled(true);
        googleMaps.getUiSettings().setMyLocationButtonEnabled(false);

        setLocationIfNeeded(carLatLng);

        new Handler().postDelayed(this::startLiveTarcking, 1000);
    }

    public void showHideSliderLayout() {
        if (sliderLayout.getVisibility() == View.GONE) {
            AnimationUtils.expand(sliderLayout);
            plusFab.hide();
            fabMenu.hideMenuButton(true);
            arrowCheckBox.setChecked(true);
            mapStylingFab.hide(true);
        } else {
            AnimationUtils.collapse(sliderLayout);
            plusFab.show();
            fabMenu.showMenuButton(true);
            arrowCheckBox.setChecked(false);
            mapStylingFab.show(true);
        }
    }

    private void drawPathOnMap() {
        if (route == null) {
            route = new Route();
            route.drawRoute(googleMap, context, myCurrentLatLng, carLatLng, false, AppConstant.LANGUAGE_EN);
            route.setDistanceListener(new ClickWithTwoParam() {
                @Override
                public void onClick(String text, String text1) {
                    if (text.equals("error")) {
                        ToastHelper.toastInfo(context, context.getString(R.string.invalid_address));
                    }
                }
            });
        } else {
            route.clearRoute();
            route = null;
        }
    }

    private void openPopupFabMenu() {
        new PopupDialog(context)
                .setLayoutResourceId(R.layout.layout_tip_content)//layout resource id
                .setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                .setLocationByAttachedView(plusFab)
                .setGravity(PopupDialog.GRAVITY_TOP)
                .setAnimationTranslationShow(PopupDialog.DIRECTION_X, 350, 400, 0)
                .setAnimationTranslationDismiss(PopupDialog.DIRECTION_X, 350, 0, 400)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(ContextCompat.getColor(context, R.color.trans_black))
                .show(String.valueOf(vehicleModel.getVehicleID()), carLatLng.latitude, carLatLng.longitude);
    }

    private void setChangeMapTypeFab() {
        if (googleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void setLocationIfNeeded(LatLng locationIfNeeded) {
        if (locationIfNeeded != null) {
            cameraPosition = new CameraPosition.Builder().target(locationIfNeeded).zoom(AppConstant.ZOOM_VALUE_18).tilt(cameraTilt).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void addCarMarker() {
        marker = new MarkerOptions().position(carLatLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_101))
                .anchor(0.5f, 0.5f)
                .rotation((float) vehicleModel.getLastLocation().getDirection())
                .flat(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mapLocationListener != null) {
            mapLocationListener.onLocationChanged(location);
            myCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//            LogHelper.LOG_D("onLocationChanged >>>> ", "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
            if (firstTimeOpenMap) {
                cameraPosition = new CameraPosition.Builder().target(myCurrentLatLng).zoom(AppConstant.ZOOM_VALUE_18).tilt(cameraTilt).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                firstTimeOpenMap = false;
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            AlertDialogView.showGPSDisabledDialog(activity);
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        this.mapLocationListener = listener;
    }

    @Override
    public void deactivate() {
        this.mapLocationListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.GPS_ENABLE_REQUEST) {
            if (locationManager == null) {
                locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            }

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialogView.showGPSDisabledDialog(activity);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            firstTimeOpenMap = true;
        }
    }

    private void markStartingLocationOnMap(final SignalRModel signalRModel) {
        SignalRModel.A aModel = signalRModel.getA().get(0);
        LatLng newLocation = new LatLng(aModel.getLatitude(), aModel.getLongitude());
        carLatLng = newLocation;
        LogHelper.LOG_D("newLocation >>>> ", "Lat: " + newLocation.latitude + " Long: "
                + newLocation.longitude
                + "\nDirection: " + aModel.getDirection()
                + "\nVehicle ID: " + aModel.getVehicleID());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(newLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_101))
                        .anchor(0.5f, 0.5f)
                        .rotation(aModel.getDirection().floatValue())
                        .flat(true));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                updateCarInfo(aModel);
            }
        });
    }

    public void startLiveTarcking() {
        if (mBound) {
//             Call a method from the SignalRService.
//             However, if this call were something that might hang, then this request should
//             occur in a separate thread to avoid slowing down the activity performance.
            mService.invokeService(new SignalR() {
                @Override
                public void onMessageReceived(SignalRModel signalRModel) {
                    if (signalRModel.getA().get(0).getVehicleID() == PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.LAST_VIEW_VEHICLE_ID)) {
                        markStartingLocationOnMap(signalRModel);
                    }
                }

                @Override
                public void onCommandReceived(SignalRCommandModel signalRCommandModel) {

                }
            });
        }
    }

    private void startSignalRService() {
        Intent intent = new Intent();
        intent.setClass(context, SignalRService.class);
        activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopSignalRService() {
        // Unbind from the service
        if (mBound) {
            activity.unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onStop() {
        stopSignalRService();
        super.onStop();
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            try {
                LogHelper.LOG_D("onServiceConnected", "" + service.getInterfaceDescriptor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
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

import java.util.Locale;
import java.util.Objects;

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

    private ImageView arrowCheckBox;
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
    private TextViewLight speed_add;
    private TextViewLight plateNumberLabelTextView;

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
    private Handler handler = new Handler();
    private Runnable refresh;

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
        Utils.hidKeyBoard(Objects.requireNonNull(getActivity()));
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
        startSignalRService();
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
        arrowCheckBox = (ImageView) rootView.findViewById(R.id.arrowCheckBox);
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
        speed_add = (TextViewLight) rootView.findViewById(R.id.speedTextView);
        plateNumberLabelTextView= (TextViewLight) rootView.findViewById(R.id.plateNumberLabelTextView);


        plusFab = (FloatingActionButton) rootView.findViewById(R.id.plusFab);
        drawPathFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.drawPathFab);
        changeMapTypeFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.changeMapTypeFab);
        myLocationFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.myLocationFab);
        carLocationFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.carLocationFab);

        fabMenu = (FloatingActionMenu) rootView.findViewById(R.id.fabMenu);
        fabMenu.setIconAnimated(false);

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
        workingHoursTextView.setText(vehicleModel.getPlateNumber());
        mileageTextView.setText(String.format(Locale.getDefault(), "%s %s", Utils.doubleToStringTwoDigits(AppUtils.meterToKilometer(vehicleModel.getLastLocation().getTotalMileage())), context.getString(R.string.km)));
        accTextView.setText(getOnline(vehicleModel.getLastLocation().getEngineStatus()));
        int value = (int) (vehicleModel.getLastLocation().getDirection() % 360);
        directionTextView.setText(String.format(Locale.getDefault(), "%s°", value == 0 ? "0" : value));
        int value1 = (int) (vehicleModel.getLastLocation().getSpeed() % 360);
        speed_add.setText(String.format(Locale.getDefault(), "%s°", value1 == 0 ? "0" : value1));
        vehicleStatusTextView.setText(AppUtils.getCarStatus(activity, vehicleModel.getLastLocation().getVehicleStatus()));

        if (vehicleModel.getLastLocation().getLatitude() != 0.0 || vehicleModel.getLastLocation().getLongitude() != 0.0) {
            locationTimeTextView.setText(Utils.parseTimeWithPlusThree(vehicleModel.getLastLocation().getRecordDateTime()));
        }
        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 1000);
            }
        };
        handler.post(refresh);

       // Toast.makeText(context, "id: " +vehicleModel.getVehicleID(), Toast.LENGTH_LONG).show();
    }

    private void updateCarInfo(SignalRModel.A aModel) {
        try {
            streetTextView.setText(aModel.getAddress());


//            displayNameTextView.setText(aModel.getVehicleDisplayName());
            //mileageTextView.setText(String.format(Locale.getDefault(), "%s %s", Utils.doubleToStringTwoDigits(aModel.getMileage()), context.getString(R.string.km)));
            accTextView.setText(getOnline(aModel.getEngineStatus()));
            int value = (int) (aModel.getDirection() % 360);
            directionTextView.setText(String.format(Locale.getDefault(), "%s°", value == 0 ? "0" : value));
            speed_add.setText(Integer.parseInt(aModel.getSpeed().toString()));
            locationTimeTextView.setText(Utils.parseTime(aModel.getRecordDateTime()));
            vehicleStatusTextView.setText(AppUtils.getCarStatus(activity, aModel.getVehicleStatus().toString()));


           // Toast.makeText(context, "Miallege: " + Utils.doubleToStringTwoDigits(aModel.getMileage())+" id: "+aModel.getVehicleID(), Toast.LENGTH_LONG).show();
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
            arrowCheckBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down));
            mapStylingFab.hide(true);
        } else {
            AnimationUtils.collapse(sliderLayout);
            plusFab.show();
            fabMenu.showMenuButton(true);
            mapStylingFab.show(true);
            arrowCheckBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up));

        }
    }

    private void drawPathOnMap() {
        if (route == null) {
            route = new Route();
            route.drawRoute(googleMap, context, myCurrentLatLng, carLatLng, false, AppConstant.LANGUAGE_EN);
            route.setDistanceListener(new ClickWithTwoParam() {
                @Override
                public void onClick(String distanceString, String durationString, String distanceValue, String durationValue) {
                    if (distanceString.equals("error")) {
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
        BitmapDescriptor bitmapDescriptor = AppUtils.getCarIcon(vehicleModel != null && vehicleModel.getLastLocation() != null && vehicleModel.getLastLocation().getVehicleStatus() != null ? vehicleModel.getLastLocation().getVehicleStatus() : "0");
        marker = new MarkerOptions().position(carLatLng)
                .icon(bitmapDescriptor)
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

    private void markStartingLocationOnMap(final SignalRModel.A aModel) {
        try {
            LatLng newLocation = new LatLng(aModel.getLatitude(), aModel.getLongitude());
            carLatLng = newLocation;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(newLocation)
                            .icon(AppUtils.getCarIcon(aModel.getVehicleStatus().toString()))
                            .anchor(0.5f, 0.5f)
                            .rotation(aModel.getDirection().floatValue())
                            .flat(true));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                    updateCarInfo(aModel);

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startLiveTarcking() {

            mService.invokeService(new SignalRService.FireBaseListener() {
                @Override
                public void dataSnapShot(String dataSnapshot) {
                    SignalRModel.A aModel = new Gson().fromJson(dataSnapshot,SignalRModel.A.class);
                    if ((aModel.getSerialNumber())== PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LAST_VIEW_VEHICLE_SERIAL)) {
                        markStartingLocationOnMap(aModel);
                        Log.e("TRY SUCCESS log",dataSnapshot);
                }
            }
            });

        Log.e("STRATLIVETR","SUCCESS");

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
            try {
                // We've bound to SignalRService, cast the IBinder and get SignalRService instance
                try {
                    LogHelper.LOG_D("onServiceConnected", "" + service.getInterfaceDescriptor());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
                mService = binder.getService();

                mBound = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
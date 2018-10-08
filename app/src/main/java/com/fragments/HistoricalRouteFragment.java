package com.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.R;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.managers.map_managers.MyMapStyleManager;
import com.models.CarHistoryModel;
import com.models.ListOfVehiclesModel;
import com.utilities.AppUtils;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class HistoricalRouteFragment extends Fragment implements MapStyleDialogFragment.OnSelectMapStyle, OnMapReadyCallback, View.OnClickListener {

    private final static String VEHICLE_ID_ARGS = "vehicle_id_args";
    public static HistoricalRouteFragment fragment;
    private String vehicleId;
    private Context context;
    private FragmentActivity activity;

    private FloatingActionButton mapStylingFab;
    private FloatingActionButton changeMapTypeFab;

    private ImageView firstLocationImageView;
    private ImageView playImageView;
    private ImageView pauseImageView;
    private ImageView lastLocationImageView;

    private TextViewRegular dateFromTextView;
    private TextViewRegular dateToTextView;
    private TextViewRegular goTextView;

    private LinearLayout dateFromLayout;
    private LinearLayout dateToLayout;

    private String fromDate;
    private String toDate;

    private MapView mMapView;
    private GoogleMap googleMap;
    //    private MarkerOptions markerOptions;
    private Marker marker;
    private ArrayList<CarHistoryModel> carHistoryList;
    private Bitmap mMarkerIcon;
    private int MOVE_ANIMATION_DURATION = 1800;
    private int TURN_ANIMATION_DURATION = 300;
    private int mIndexCurrentPoint = 0;
    private List<LatLng> mPathPolygonPoints;
    private boolean isPause = false;
    private ListOfVehiclesModel.VehicleModel vehicleModel;
    private LatLng carLatLng;
    private Date dateFrom;

    private MyMapStyleManager myMapStyleManager;

    public HistoricalRouteFragment() {
        // Required empty public constructor
    }

    public static HistoricalRouteFragment newInstance(String viewType) {
//        if (fragment == null) {
        fragment = new HistoricalRouteFragment();
        Bundle args = new Bundle();
        args.putString(VEHICLE_ID_ARGS, viewType);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        googleMap.setLocationSource(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            vehicleId = mBundle.getString(VEHICLE_ID_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historical_route, container, false);
        initViews(view);
        initListeners();
        setDateToLayout();
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
        mapStylingFab = (FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);
        changeMapTypeFab = (FloatingActionButton) rootView.findViewById(R.id.changeMapTypeFab);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMarkerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.car_101);

        firstLocationImageView = (ImageView) rootView.findViewById(R.id.firstLocationImageView);
        playImageView = (ImageView) rootView.findViewById(R.id.playImageView);
        pauseImageView = (ImageView) rootView.findViewById(R.id.pauseImageView);
        lastLocationImageView = (ImageView) rootView.findViewById(R.id.lastLocationImageView);

        dateFromLayout = (LinearLayout) rootView.findViewById(R.id.dateFromLayout);
        dateToLayout = (LinearLayout) rootView.findViewById(R.id.dateToLayout);

        dateFromTextView = (TextViewRegular) rootView.findViewById(R.id.dateFromTextView);
        dateToTextView = (TextViewRegular) rootView.findViewById(R.id.dateToTextView);
        goTextView = (TextViewRegular) rootView.findViewById(R.id.goTextView);

        if (AppUtils.isArabic()) {
            firstLocationImageView.setRotation(180);
            lastLocationImageView.setRotation(180);
        }
    }

    private void initListeners() {
        mapStylingFab.setOnClickListener(this);
        changeMapTypeFab.setOnClickListener(this);

        firstLocationImageView.setOnClickListener(this);
        playImageView.setOnClickListener(this);
        pauseImageView.setOnClickListener(this);
        lastLocationImageView.setOnClickListener(this);

        dateFromLayout.setOnClickListener(this);
        dateToLayout.setOnClickListener(this);
        goTextView.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        setMapStyleDialog();
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        vehicleModel = PreferencesManager.getInstance().getVehicleModel(AppConstant.VEHICLE_MODEL_ARGS);
        addCarMarkerFirstTime();
    }

    private void addCarMarkerFirstTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (vehicleModel != null) {
                    carLatLng = new LatLng(vehicleModel.getLastLocation().getLatitude(), vehicleModel.getLastLocation().getLongitude());
                    addCarMarker(carLatLng);
                    moveCamera(carLatLng);
                }
            }
        }, 100);

    }

    private void setChangeMapType() {
        if (googleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void updateCamera(LatLng locationIfNeeded) {
        if (locationIfNeeded != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationIfNeeded, AppConstant.ZOOM_VALUE_18));
        }
    }

    private void moveCamera(LatLng locationIfNeeded) {
        if (locationIfNeeded != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(locationIfNeeded)
                    .zoom(AppConstant.ZOOM_VALUE_18)    // Sets the zoom
                    .bearing(0)         // Sets the orientation of the camera to east
                    .tilt(30).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void addCarMarker(LatLng latLng) {
//        markerOptions = new MarkerOptions().position(carLatLng)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top32x32))
//                .anchor(0.5f, 0.5f)
//                .rotation((float) vehicleModel.getLastLocation().getDirection())
//                .flat(true);
//        googleMap.clear();
        marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_101));
        marker.setFlat(true);
        marker.setAnchor(0.5f, 0.5f);
    }

    private void removeMarker() {
        if (marker != null)
            marker.remove();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateFromLayout:
                isPause = true;
                setPlayPauseButton();
                fromDatePickerDialog();
                break;

            case R.id.dateToLayout:
                isPause = true;
                setPlayPauseButton();
                toDatePickerDialog();
                break;

            case R.id.goTextView:
                isPause = true;
                setPlayPauseButton();
                googleMap.clear();
                historicalRouteApiCall();
                break;

            case R.id.firstLocationImageView:
                setFirstLocationImageView();
                break;

            case R.id.playImageView:
                if (isPause) {
                    isPause = false;
                    if (Utils.isNotEmptyList(mPathPolygonPoints))
                        nextMoveAnimation();
                } else {
                    if (mPathPolygonPoints == null || mPathPolygonPoints.size() <= 1) {
                        return;
                    } else {
                        isPause = false;
                        animateCarMove(marker, mPathPolygonPoints.get(0), mPathPolygonPoints.get(1), MOVE_ANIMATION_DURATION);
                    }
                }
                setPlayPauseButton();
                break;

            case R.id.pauseImageView:
                isPause = true;
                setPlayPauseButton();
                break;

            case R.id.lastLocationImageView:
                setLastLocationImageView();
                break;

            case R.id.changeMapTypeFab:
                setChangeMapType();
                break;

            case R.id.mapStylingFab:
                openMapStyleDialog();
                break;
        }
    }

    private void setPlayPauseButton() {
        if (mPathPolygonPoints == null || mPathPolygonPoints.size() <= 1) {
            return;
        }
        if (playImageView.getVisibility() == View.VISIBLE) {
            playImageView.setVisibility(View.INVISIBLE);
            pauseImageView.setVisibility(View.VISIBLE);
        } else {
            playImageView.setVisibility(View.VISIBLE);
            pauseImageView.setVisibility(View.INVISIBLE);
        }
    }

    private void setFirstLocationImageView() {
        if (mPathPolygonPoints == null || mPathPolygonPoints.size() <= 1) {
            return;
        }
        removeMarker();
        mIndexCurrentPoint = 0;
        isPause = true;
        addCarMarker(mPathPolygonPoints.get(mIndexCurrentPoint));
        moveCamera(mPathPolygonPoints.get(mIndexCurrentPoint));
    }

    private void setLastLocationImageView() {
        if (mPathPolygonPoints == null || mPathPolygonPoints.size() <= 1) {
            return;
        }
        removeMarker();
        isPause = true;
        mIndexCurrentPoint = mPathPolygonPoints.size() - 1;
        addCarMarker(mPathPolygonPoints.get(mIndexCurrentPoint));
        moveCamera(mPathPolygonPoints.get(mIndexCurrentPoint));
    }

    private void setDateToLayout() {
        Calendar dateNow = Calendar.getInstance();
        int hour = dateNow.get(Calendar.HOUR_OF_DAY);
        int minute = dateNow.get(Calendar.MINUTE);
        int year = dateNow.get(Calendar.YEAR);
        int month = dateNow.get(Calendar.MONTH) + 1;
        int dayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
        int dayBefore = dateNow.get(Calendar.DAY_OF_MONTH) - 1 == 0 ? dateNow.get(Calendar.DAY_OF_MONTH) : dateNow.get(Calendar.DAY_OF_MONTH) - 1;

        fromDate = AppUtils.dateForm(year, month, dayBefore) + "%20" + Utils.time24H(hour, minute);
        toDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(hour, minute);

        dateFromTextView.setText(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
        dateToTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(hour, minute));

        dateFrom = new Date(System.currentTimeMillis());
    }

    private void fromDatePickerDialog() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(activity, R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> {
            dateFrom = new Date(Utils.componentTimeToTimestamp(year1, month1, dayOfMonth, 0, 0));
            fromTimePickerDialog(year1, month1 + 1, dayOfMonth);
        }, year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_from));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void fromTimePickerDialog(int year, int month, int dayOfMonth) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
            fromDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(selectedHour, selectedMinute);
            dateFromTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_from));
//        mTimePicker.getDatePicker().setMaxDate(new Date().getTime());
        mTimePicker.show();
    }

    private void toDatePickerDialog() {
        Calendar mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(activity, R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> toTimePickerDialog(year1, month1 + 1, dayOfMonth), year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_to));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(dateFrom.getTime());
        datePickerDialog.show();
    }

    private void toTimePickerDialog(int year, int month, int dayOfMonth) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
            toDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(selectedHour, selectedMinute);
            dateToTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_to));
        mTimePicker.show();
    }

    private void historicalRouteApiCall() {
        Progress.showLoadingDialog(activity);
//        BusinessManager.postHistoricalRoute(String.valueOf(vehicleModel.getVehicleID()), "09/16/2017%2013:15", "09/17/2017%2013:25", new ApiCallResponse() {
        BusinessManager.postHistoricalRoute(vehicleId, fromDate, toDate, new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                CarHistoryModel[] carHistoryModels = (CarHistoryModel[]) responseObject;
                carHistoryList = getCarHistoryList(carHistoryModels);
                if (carHistoryList.size() > 0) {
                    mPathPolygonPoints = getLatLngList(carHistoryModels);
                } else {
                    mPathPolygonPoints = new ArrayList<>();
                    ToastHelper.toastWarningLong(activity, getString(R.string.no_available_data));
                    addCarMarkerFirstTime();
                }
                if (mPathPolygonPoints != null && mPathPolygonPoints.size() > 0) {
                    moveCamera(mPathPolygonPoints.get(0));
                    drawHistoricalRouteOnMap();
                    addCarMarker(mPathPolygonPoints.get(0));
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }

    private ArrayList<CarHistoryModel> getCarHistoryList(CarHistoryModel[] model) {
        carHistoryList = new ArrayList<>();
        Collections.addAll(carHistoryList, model);
        return carHistoryList;
    }

//    private ArrayList<LatLng> getLatLngList(CarHistoryModel[] model) {
//        ArrayList<LatLng> latLngList = new ArrayList<>();
//        for (CarHistoryModel model1 : model) {
//            latLngList.add(new LatLng(model1.getLatitude(), model1.getLongitude()));
//        }
//        return latLngList;
//    }

    private ArrayList<LatLng> getLatLngList(CarHistoryModel[] model) {
        // remove the first zero speed
        ArrayList<LatLng> latLngList = new ArrayList<>();
        boolean isLastVehicleZeroSpeed = model[0].getSpeed() == 0;
        for (CarHistoryModel model1 : model) {
            if (isLastVehicleZeroSpeed && model1.getSpeed() == 0.0) {
                isLastVehicleZeroSpeed = true;
                continue;
            }
            latLngList.add(new LatLng(model1.getLatitude(), model1.getLongitude()));
            isLastVehicleZeroSpeed = model1.getSpeed() == 0;
        }
        return latLngList;
    }

    private void drawHistoricalRouteOnMap() {
        PolylineOptions polylineOptions = new PolylineOptions();
// Create polyline options with existing LatLng ArrayList
        polylineOptions.addAll(mPathPolygonPoints);
        polylineOptions
                .width(AppConstant.POLY_THICKNESS)
                .color(ContextCompat.getColor(context, R.color.colorPrimaryDark));
// Adding multiple points in map using polyline and arraylist
        googleMap.addPolyline(polylineOptions);
    }

    private void animateCarMove(final Marker marker, final LatLng beginLatLng, final LatLng endLatLng, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();

        // set car bearing for current part of path
        float angleDeg = (float) (180 * getAngle(beginLatLng, endLatLng) / Math.PI);
        Matrix matrix = new Matrix();
        matrix.postRotate(angleDeg);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true)));

        handler.post(new Runnable() {
            @Override
            public void run() {
                // calculate phase of animation
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                // calculate new position for marker
                double lat = (endLatLng.latitude - beginLatLng.latitude) * t + beginLatLng.latitude;
                double lngDelta = endLatLng.longitude - beginLatLng.longitude;

                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * t + beginLatLng.longitude;

                LatLng newLatLng = new LatLng(lat, lng);
                marker.setPosition(newLatLng);
                updateCamera(newLatLng);
                // if not end of line segment of path
                if (isPause) {
                    handler.removeCallbacks(this);
                    return;
                }
                if (t < 1.0) {
                    // call next marker position
                    handler.postDelayed(this, 16);
                } else {
                    // call turn animation
                    nextTurnAnimation();
                }
            }
        });
    }

    private void nextTurnAnimation() {
        mIndexCurrentPoint++;
        if (mIndexCurrentPoint < mPathPolygonPoints.size() - 1) {
            LatLng prevLatLng = mPathPolygonPoints.get(mIndexCurrentPoint - 1);
            LatLng currLatLng = mPathPolygonPoints.get(mIndexCurrentPoint);
            LatLng nextLatLng = mPathPolygonPoints.get(mIndexCurrentPoint + 1);

            float beginAngle = (float) (180 * getAngle(prevLatLng, currLatLng) / Math.PI);
            float endAngle = (float) (180 * getAngle(currLatLng, nextLatLng) / Math.PI);

            animateCarTurn(marker, beginAngle, endAngle, TURN_ANIMATION_DURATION);
        } else {
            isPause = true;
            mIndexCurrentPoint = 0;
            setPlayPauseButton();
        }
    }

    private void animateCarTurn(final Marker marker, final float startAngle, final float endAngle, final long duration) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final Interpolator interpolator = new LinearInterpolator();

        final float dAndgle = endAngle - startAngle;

        Matrix matrix = new Matrix();
        matrix.postRotate(startAngle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), matrix, true);
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(rotatedBitmap));

        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                Matrix m = new Matrix();
                m.postRotate(startAngle + dAndgle * t);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(mMarkerIcon, 0, 0, mMarkerIcon.getWidth(), mMarkerIcon.getHeight(), m, true)));

                if (isPause) {
                    handler.removeCallbacks(this);
                    return;
                }

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    nextMoveAnimation();
                }
            }
        });
    }

    private void nextMoveAnimation() {
        if (mIndexCurrentPoint < mPathPolygonPoints.size() - 1) {
            animateCarMove(marker, mPathPolygonPoints.get(mIndexCurrentPoint), mPathPolygonPoints.get(mIndexCurrentPoint + 1), MOVE_ANIMATION_DURATION);
        }
    }

    private double getAngle(LatLng beginLatLng, LatLng endLatLng) {
        double f1 = Math.PI * beginLatLng.latitude / 180;
        double f2 = Math.PI * endLatLng.latitude / 180;
        double dl = Math.PI * (endLatLng.longitude - beginLatLng.longitude) / 180;
        return Math.atan2(Math.sin(dl) * Math.cos(f2), Math.cos(f1) * Math.sin(f2) - Math.sin(f1) * Math.cos(f2) * Math.cos(dl));
    }

    private float duration(LatLng beginLatLng, LatLng endLatLng, float speed) {
        try {
            return (speed / distanceInMeter(beginLatLng, endLatLng));
        } catch (Exception e) {
            return 16;
        }
    }

    private float distanceInMeter(LatLng beginLatLng, LatLng endLatLng) {
        return Utils.getDistanceBetweenToLocations(
                "locationA", beginLatLng.latitude, beginLatLng.longitude,
                "locationA", endLatLng.latitude, endLatLng.longitude);
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

}
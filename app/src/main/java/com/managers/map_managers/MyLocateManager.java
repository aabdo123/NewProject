package com.managers.map_managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import com.R;
import com.models.AllVehiclesInHashModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.Route;
import com.utilities.ToastHelper;
import com.utilities.constants.AppConstant;
import com.utilities.map.MapUtils;
import com.views.ButtonBold;
import com.views.Click;
import com.views.ClickWithTwoParam;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import java.util.LinkedHashMap;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyLocateManager implements View.OnClickListener {

    public static View popupView;
    private Context context;
    private UpdatePopup updatePopup;
    private GoogleMap googleMap;
    private View rootView;
    private LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap;
    private Route route;

    private MapView mapView;
    private ButtonBold startButton;
    private ButtonBold endButton;
    private ImageView locateMarkerImageView;

    private LatLng myCurrentLatLng;
    private LatLng redMarkerAddress;
    private LatLng blueMarkerAddress;

    private ImageView cancelImageView;
    private ImageView startMarkerImageView;
    private View dividerView;
    private ImageView endMarkerImageView;
    private TextViewRegular governorateFromTextView;
    private TextViewLight streetFromTextView;
    private TextViewRegular governorateToTextView;
    private TextViewLight streetToTextView;
    private TextViewRegular distanceTextView;
    private TextViewRegular durationTextView;
    private LinearLayout routeInfoLayout;

    private FloatingActionButton mapStylingFab;

    private Marker redMarker;
    private Marker blueMarker;
    private Click afterOnDismiss;

    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;

    private interface UpdatePopup {
        void updateAddressFrom(String govFrom, String strFrom);

        void updateAddressTo(String govTo, String strTo);

        void updateDistance(String distance, String duration);
    }

    public MyLocateManager(Context context, View view, GoogleMap googleMap, LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap, LatLng myCurrentLatLng) {
        this.context = context;
        this.googleMap = googleMap;
        this.vehiclesHashMap = vehiclesHashMap;
        this.rootView = view;
        this.myCurrentLatLng = myCurrentLatLng;
    }

    public void setViews(MapView mMapView) {
        this.mapView = mMapView;
        builder = new LatLngBounds.Builder();
        locateMarkerImageView = (ImageView) rootView.findViewById(R.id.locateMarkerImageView);
        startButton = (ButtonBold) rootView.findViewById(R.id.startButton);
        endButton = (ButtonBold) rootView.findViewById(R.id.endButton);
        locateMarkerImageView.setVisibility(View.VISIBLE);
        mapStylingFab = rootView.findViewById(R.id.mapStylingFab);
        setListeners();
        setMapToLocate();
    }

    public void setAfterOnDismissListeners(Click afterOnDismiss) {
        this.afterOnDismiss = afterOnDismiss;
    }

    private void setListeners() {
        startButton.setOnClickListener(this);
        endButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                onRedButtonClick();
                break;

            case R.id.endButton:
                onBlueButtonClick();
                break;
        }
    }

    private void onRedButtonClick() {
        if (redMarkerAddress == null) {
            redMarkerAddress = googleMap.getCameraPosition().target;
        }
        String[] arr = MapUtils.getAddressByLatLng(context, redMarkerAddress);
        if (arr == null || arr.length == 0) {
            ToastHelper.toastInfo(context, context.getResources().getString(R.string.something_went_worng));
            return;
        }
        startMarkerImageView.setAlpha(1.0f);
        dividerView.setAlpha(1.0f);
        governorateFromTextView.setAlpha(1.0f);
        streetFromTextView.setAlpha(1.0f);

        redMarker = googleMap.addMarker(MapUtils.createMarker(redMarkerAddress, MapUtils.bitmapDescriptorFromVector(R.drawable.locate_marker_start)));
        locateMarkerImageView.setImageResource(R.drawable.locate_marker_end);
        startButton.setVisibility(View.GONE);
        endButton.setVisibility(View.VISIBLE);

        builder.include(redMarkerAddress);
        updatePopup.updateAddressFrom(arr[0], arr[3]);
    }

    private void onBlueButtonClick() {
        if (blueMarkerAddress == null) {
            blueMarkerAddress = googleMap.getCameraPosition().target;
        }

        builder.include(blueMarkerAddress);
        drawPathOnMap(redMarkerAddress, blueMarkerAddress);
    }

    private void onRouteSuccess() {
        endMarkerImageView.setAlpha(1.0f);
        governorateToTextView.setAlpha(1.0f);
        streetToTextView.setAlpha(1.0f);

        blueMarker = googleMap.addMarker(MapUtils.createMarker(blueMarkerAddress, MapUtils.bitmapDescriptorFromVector(R.drawable.locate_marker_end)));
        locateMarkerImageView.setVisibility(View.GONE);
        endButton.setVisibility(View.GONE);

        bounds = builder.build();
        animateCamera();

        AnimationUtils.expand(rootView.findViewById(R.id.moreOptionsLayout));
        String[] arr = MapUtils.getAddressByLatLng(context, blueMarkerAddress);
        updatePopup.updateAddressTo(arr[0], arr[3]);
    }

    private void setMapToLocate() {
        if (googleMap == null) {
            ToastHelper.toastInfo(context, context.getString(R.string.please_wait_until_map_show));
            return;
        }
//        googleMap.clear();
        setLocatePopupWindowClick();
        locateMarkerImageView.setImageResource(R.drawable.locate_marker_start);
        animateCamera(myCurrentLatLng);
        AnimationUtils.collapse(rootView.findViewById(R.id.moreOptionsLayout));
        AnimationUtils.expand(startButton);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        //get latlng at the center by calling
                        if (startButton.getVisibility() == View.VISIBLE) {
                            redMarkerAddress = googleMap.getCameraPosition().target;
                        } else {
                            blueMarkerAddress = googleMap.getCameraPosition().target;
                        }
                    }
                });
            }
        });
    }

    private void setLocatePopupWindowClick() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_locate_layout, null);

        mapView.addView(popupView);
        popupView.setFocusable(false);

        cancelImageView = (ImageView) popupView.findViewById(R.id.cancelImageView);
        startMarkerImageView = (ImageView) popupView.findViewById(R.id.startMarkerImageView);
        dividerView = popupView.findViewById(R.id.dividerView);
        endMarkerImageView = (ImageView) popupView.findViewById(R.id.endMarkerImageView);
        governorateFromTextView = (TextViewRegular) popupView.findViewById(R.id.governorateFromTextView);
        streetFromTextView = (TextViewLight) popupView.findViewById(R.id.streetFromTextView);
        governorateToTextView = (TextViewRegular) popupView.findViewById(R.id.governorateToTextView);
        streetToTextView = (TextViewLight) popupView.findViewById(R.id.streetToTextView);
        distanceTextView = (TextViewRegular) popupView.findViewById(R.id.distanceTextView);
        durationTextView = (TextViewRegular) popupView.findViewById(R.id.durationTextView);
        routeInfoLayout = (LinearLayout) popupView.findViewById(R.id.routeInfoLayout);

        hideMapStylingFab();
        updatePopup = new UpdatePopup() {
            @Override
            public void updateAddressFrom(String govFrom, String strFrom) {
                governorateFromTextView.setText(govFrom);
                streetFromTextView.setText(strFrom);
            }

            @Override
            public void updateAddressTo(String govTo, String strTo) {
                governorateToTextView.setText(govTo);
                streetToTextView.setText(strTo);
            }

            @Override
            public void updateDistance(String distance, String duration) {
                routeInfoLayout.setVisibility(View.VISIBLE);
                distanceTextView.setText(distance);
                durationTextView.setText(duration);
            }
        };

        if (((ViewGroup) popupView.getParent()) != null)
            cancelImageView.setOnClickListener(v -> {
                ((ViewGroup) popupView.getParent()).removeView(popupView);
            });
        else
            cancelImageView.setOnClickListener(v -> removeViewPopup());

        if (((ViewGroup) popupView.getParent()) != null)
            ((ViewGroup) popupView.getParent()).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {

                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    onDismissPopup();
                }
            });
    }

    public void removeViewPopup() {
        mapView.removeView(popupView);
    }

    private void onDismissPopup() {
        if (route != null)
            route.clearRoute();
//        new MyResetMapManager(context, googleMap).addVehiclesMarkers(vehiclesHashMap, true);
        AnimationUtils.expand(rootView.findViewById(R.id.moreOptionsLayout));

        if (locateMarkerImageView.getVisibility() == View.VISIBLE) {
            locateMarkerImageView.setVisibility(View.GONE);
        }

        if (startButton.getVisibility() == View.VISIBLE) {
            startButton.setVisibility(View.GONE);
        }

        if (endButton.getVisibility() == View.VISIBLE) {
            endButton.setVisibility(View.GONE);
        }

        if (redMarker != null) {
            redMarker.remove();
        }

        if (blueMarker != null) {
            blueMarker.remove();
        }
        afterOnDismiss.onClick();
        showMapStylingFab();
    }

    private void drawPathOnMap(LatLng start, LatLng end) {
        if (route == null) {
            route = new Route();
            route.drawRoute(googleMap, context, start, end, false, AppUtils.getRouteLanguage());
            route.setDistanceListener(new ClickWithTwoParam() {
                @Override
                public void onClick(String text, String text1) {
                    if (text.equals("error")) {
                        ToastHelper.toastInfo(context, context.getString(R.string.invalid_address));
                    } else {
                        onRouteSuccess();
                        updatePopup.updateDistance(text, text1);
                    }
                }
            });
        } else {
            route.clearRoute();
            route = null;
        }
    }

    private void animateCamera(LatLng locationCamera) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(locationCamera).zoom(AppConstant.ZOOM_VALUE_18).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void animateCamera() {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.40);
        try {
            CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
            googleMap.animateCamera(cu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMapStylingFab() {
        mapStylingFab.show(true);
    }

    private void hideMapStylingFab() {
        mapStylingFab.hide(true);
    }
}

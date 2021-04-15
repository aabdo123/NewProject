package com.managers.map_managers;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.adapters.SelectedLocationsAdapter;
import com.fragments.LisOfVehiclesMapFragment;
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
import com.managers.PreferencesManager;
import com.models.AllVehiclesInHashModel;
import com.models.LatLogModel;
import com.models.LocationLocateModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.Route;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.utilities.map.MapUtils;
import com.views.ButtonBold;
import com.views.Click;
import com.views.SpeedyLinearLayoutManager;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Handler;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.adapters.SelectedLocationsAdapter.END_HIDDEN;
import static com.adapters.SelectedLocationsAdapter.END_OPENED;
import static com.adapters.SelectedLocationsAdapter.START_HIDDEN;
import static com.adapters.SelectedLocationsAdapter.START_OPENED;

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
    //    private ButtonBold endButton;
    private ImageView locateMarkerImageView;

    private LatLng myCurrentLatLng;
    private LatLng redMarkerAddress;
    private LatLng blueMarkerAddress;

    private ImageView cancelImageView;
    private RecyclerView listOfLocationsRecyclerView;
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
    private ArrayList<Route> routeArrayList;

    private FloatingActionButton mapStylingFab;
    int counter = 0;
    private Marker redMarker;
    private Marker blueMarker;
    private Click afterOnDismiss;
    private ArrayList<LatLogModel> selectedLocations;
    private ArrayList<LocationLocateModel> locationLocateModels;
    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;
    private SelectedLocationsAdapter locationsAdapter;
    private LandMarkCheck landMarkCheck;

    private interface UpdatePopup {
        void updateAddressFrom(String govFrom, String strFrom);

        void updateAddressTo(String govTo, String strTo);

        void updateDistance(String distance, String duration);


        void updateDistanceList(ArrayList<LocationLocateModel> locationLocateModels);
    }

    public MyLocateManager(Context context, View view, GoogleMap googleMap, LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap, LatLng myCurrentLatLng ) {
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
//        endButton = (ButtonBold) rootView.findViewById(R.id.endButton);
        locateMarkerImageView.setVisibility(View.VISIBLE);
        mapStylingFab = rootView.findViewById(R.id.mapStylingFab);
        setListeners();
        setMapToLocate();
    }

    public void setAfterOnDismissListeners(Click afterOnDismiss) {
        this.afterOnDismiss = afterOnDismiss;
    }

    public void setLandMarkListeners(LandMarkCheck landMarkListeners) {
        this.landMarkCheck = landMarkListeners;
    }

    private void setListeners() {
        startButton.setOnClickListener(this);
//        endButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                if (counter == 0) {
                    onRedButtonClick();
                    counter++;
                } else {
                    onBlueButtonClick();
                }
                break;
            default:
                break;
        }
    }

    private void onRedButtonClick() {
        startButton.setText(String.format(Locale.getDefault(), "%s", context.getResources().getString(R.string.select)));
        if (redMarkerAddress == null) {
            redMarkerAddress = googleMap.getCameraPosition().target;
            if (selectedLocations.size() == 2) {
                LatLogModel latLogModel = new LatLogModel(START_OPENED);
                latLogModel.setLatLng(redMarkerAddress);
                selectedLocations.set(0, latLogModel);
                notifyAdapterOfLocations();
            }
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

//        startButton.setVisibility(View.GONE);
//        endButton.setVisibility(View.VISIBLE);

        builder.include(redMarkerAddress);
        updatePopup.updateAddressFrom(arr[0], arr[3]);
    }

    private void onBlueButtonClick() {
        try {
            blueMarkerAddress = googleMap.getCameraPosition().target;
            builder.include(blueMarkerAddress);
            drawPathOnMap();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawPathOnMap() {
        try {
            if (routeArrayList == null)
                routeArrayList = new ArrayList<>();
            route = new Route();
            route.drawMultiRoute(googleMap, context, selectedLocations.get(selectedLocations.size() - 1).getLatLng() != null ? selectedLocations.get(selectedLocations.size() - 1).getLatLng() : redMarkerAddress, blueMarkerAddress, false, AppUtils.getRouteLanguage());
            route.setDistanceListener((text, text1, distanceValue, durationValue) -> {
                if (text.equals("error")) {
                    ToastHelper.toastInfo(context, context.getString(R.string.invalid_address));
                } else {
                    googleMap.addMarker(MapUtils.createMarker(blueMarkerAddress, MapUtils.bitmapDescriptorFromVector(R.drawable.locate_marker_end)));
                    LatLogModel latLogModels = new LatLogModel(END_OPENED);
                    latLogModels.setLatLng(blueMarkerAddress);
                    if (selectedLocations != null && selectedLocations.size() == 2) {
                        if (selectedLocations.get(1).getLatLng() == null) {
                            selectedLocations.set(1, latLogModels);
                        } else {
                            selectedLocations.add(latLogModels);
                        }
                    } else if (selectedLocations != null) {
                        selectedLocations.add(latLogModels);
                    }
                    notifyAdapterOfLocations();
                    //onRouteSuccess();
                    LocationLocateModel locationLocateModel = new LocationLocateModel();
                    locationLocateModel.setDistance(text);
                    locationLocateModel.setDuration(text1);
                    locationLocateModel.setDistanceValue(distanceValue);
                    locationLocateModel.setDurationValue(durationValue);
                    locationLocateModels.add(locationLocateModel);
                    updatePopup.updateDistanceList(locationLocateModels);
                    if (selectedLocations != null && selectedLocations.size() > selectedLocations.size() - 1) {
                        int position = selectedLocations.size() - 1;
                        LatLogModel latLogModel = selectedLocations.get(position);
                        latLogModel.setLocationLocateModel(locationLocateModel);
                        selectedLocations.set(position, latLogModel);
                        locationsAdapter.notifyItemChanged(position);
                    }

                }
            });
            routeArrayList.add(route);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onRouteSuccess() {
        endMarkerImageView.setAlpha(1.0f);
        governorateToTextView.setAlpha(1.0f);
        streetToTextView.setAlpha(1.0f);

        blueMarker = googleMap.addMarker(MapUtils.createMarker(blueMarkerAddress, MapUtils.bitmapDescriptorFromVector(R.drawable.locate_marker_end)));
        locateMarkerImageView.setVisibility(View.GONE);

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
//                        if (startButton.getVisibility() == View.VISIBLE) {
//                            redMarkerAddress = googleMap.getCameraPosition().target;
//                        } else {
//                            blueMarkerAddress = googleMap.getCameraPosition().target;
//                        }
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
        listOfLocationsRecyclerView = (RecyclerView) popupView.findViewById(R.id.listOfLocationsRecyclerView);
//        listOfLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        listOfLocationsRecyclerView.setLayoutManager(new SpeedyLinearLayoutManager(context, SpeedyLinearLayoutManager.VERTICAL, false));
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
        selectedLocations = new ArrayList<>();
        locationLocateModels = new ArrayList<>();
        selectedLocations.add(new LatLogModel(START_HIDDEN));
        selectedLocations.add(new LatLogModel(END_HIDDEN));
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
                try {
                    routeInfoLayout.setVisibility(View.VISIBLE);
                    distanceTextView.setText(distance);
                    durationTextView.setText(duration);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void updateDistanceList(ArrayList<LocationLocateModel> locationLocateModels) {
                try {
                    routeInfoLayout.setVisibility(View.VISIBLE);
                    Double distance = 0.0;// /1000
                    Double duration = 0.0;// /60
                    for (LocationLocateModel locationLocateModel : locationLocateModels) {
                        Double distanceValueDouble = Utils.round(Double.valueOf(locationLocateModel.getDistanceValue()), 2) / 1000;
                        Double durationValueDouble = Utils.round(Double.valueOf(locationLocateModel.getDurationValue()), 2) / 60;
                        distance = distanceValueDouble + distance;
                        duration = durationValueDouble + duration;
                    }
                    distanceTextView.setText(String.format(Locale.getDefault(), "%.2f %s", distance, context.getString(R.string.km)));
                    Long longValueCalc = Math.round(duration);
                    durationTextView.setText(String.format(Locale.getDefault(), "%.0f %s", Double.valueOf(longValueCalc), context.getString(R.string.min)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        if (((ViewGroup) popupView.getParent()) != null)
            cancelImageView.setOnClickListener(v -> {
                ((ViewGroup) popupView.getParent()).removeView(popupView);
//                if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
//                    slideUpFragment.showLandMark(true);
//                }
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
        initAdapterOfLocations();
    }


    private void initAdapterOfLocations() {
        try {
            locationsAdapter = new SelectedLocationsAdapter(context, selectedLocations);
            listOfLocationsRecyclerView.setAdapter(locationsAdapter);
            locationsAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void notifyAdapterOfLocations() {
        try {
            locationsAdapter.notifyDataSetChanged();
            listOfLocationsRecyclerView.smoothScrollToPosition(selectedLocations.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeViewPopup() {
        mapView.removeView(popupView);
    }
    public interface LandMarkCheck{
        void landMark();

    }

    private void onDismissPopup() {
        if (route != null)
            route.clearRoute();
        if (routeArrayList != null && routeArrayList.size() > 0) {
            for (Route route : routeArrayList) {
                route.clearRoute();
            }
        }
//        new MyResetMapManager(context, googleMap).addVehiclesMarkers(vehiclesHashMap, true);
        AnimationUtils.expand(rootView.findViewById(R.id.moreOptionsLayout));

        if (locateMarkerImageView.getVisibility() == View.VISIBLE) {
            locateMarkerImageView.setVisibility(View.GONE);
        }

        if (startButton.getVisibility() == View.VISIBLE) {
            startButton.setVisibility(View.GONE);
        }

//        if (endButton.getVisibility() == View.VISIBLE) {
//            endButton.setVisibility(View.GONE);
//        }

        if (redMarker != null) {
            redMarker.remove();
        }
        googleMap.clear();
        if (blueMarker != null) {
            blueMarker.remove();
        }
        afterOnDismiss.onClick();
        afterOnDismiss.addMaps();
        showMapStylingFab();
        //landMarkCheck.landMark();
    }


    private void animateCamera(LatLng locationCamera) {
        try {
            if (locationCamera != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(locationCamera).zoom(AppConstant.ZOOM_VALUE_18).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

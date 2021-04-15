package com.managers.map_managers;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.R;
import com.activities.MainActivity;
import com.fragments.GeoFenceFragment;
import com.fragments.LisOfVehiclesMapFragment;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.managers.ShortTermManager;
import com.models.GeoFenceModel;
import com.models.LandmarkModel;
import com.models.ListOfVehiclesModel;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.map.MapAreaManager;
import com.utilities.map.MapAreaMeasure;
import com.utilities.map.MapAreaWrapper;
import com.utilities.map.MapUtils;
import com.views.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MyGeoFenceManager {

    private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float ACCURACY = 0.01f;

    private Activity activity;
    private GoogleMap googleMap;
    private ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence;
    private static ArrayList<GeoFenceModel> geoFenceList = new ArrayList<>();
    private static ArrayList<Circle> geoFenceCircleList = new ArrayList<>();
    private LatLng geoLatLng;
    private Fragment targetFragment;

    private MapAreaManager circleManager;

    public MyGeoFenceManager(Activity activity, GoogleMap googleMap, LatLng geoLatLng, ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence) {
        this.activity = activity;
        this.googleMap = googleMap;
        this.geoLatLng = geoLatLng;
        this.listOfVehiclesForGeoFence = listOfVehiclesForGeoFence;
    }

    public void setTargetFragment(LisOfVehiclesMapFragment fragment) {
        this.targetFragment = fragment;
    }

    public void setViews(boolean isShowCliched, boolean isAddClicked) {
        if (isShowCliched) {
            geoFenceListApiCall();
        }

        if (isAddClicked) {
            GeoFenceFragment fenceFragment = GeoFenceFragment.newInstance("", listOfVehiclesForGeoFence, geoLatLng.latitude, geoLatLng.longitude);
            if (targetFragment != null)
                fenceFragment.setTargetFragment(targetFragment, AppConstant.TARGET_FRAGMENT_GEOFENCE);
            ((MainActivity) activity).callUpAnimation(fenceFragment, activity.getString(R.string.geo_fence));
        }
    }

    public void addGeoFenceCircleOnMap(LatLng circleLatLng, int radius) {
        drawCircle(circleLatLng, radius);
        animateCamera(circleLatLng, radius);
    }

    private void drawCircleOnMap() {
        geoFenceCircleList = new ArrayList<>();
        if (Utils.isNotEmptyList(geoFenceList)) {
            for (GeoFenceModel model : geoFenceList) {
                LatLng circleLatLng = getLatLng(model.getGeofenceCenterPoint());
                if (circleLatLng != null) {
                    int radius = model.getGeofenceRadius();
                    drawCircle(circleLatLng, radius);
                }
            }
        }
    }

    private void drawCircle(LatLng point, int radius) {
//        return googleMap.addCircle(new CircleOptions()
//                .center(point)
//                .radius(radius)
//                .strokeColor(Utils.colorInt("#FFFFFF"))
//                .strokeWidth(4)
//                .fillColor(AppConstant.GEO_FENCE_COLORS_RGB[getRandomNumber()]));

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(point)
                .radius(radius)
                .strokeColor(Utils.colorInt("#FFFFFF"))
                .strokeWidth(4)
                .fillColor(AppConstant.GEO_FENCE_COLORS_RGB[getRandomNumber()]));
        geoFenceCircleList.add(circle);
    }

    public void setCirclesVisibility(boolean visibility) {
        if (Utils.isNotEmptyList(geoFenceCircleList)) {
            for (Circle circle : geoFenceCircleList) {
                circle.setVisible(visibility);
            }
        }
    }

    public void removeCircleFromMap() {
        if (Utils.isNotEmptyList(geoFenceCircleList)) {
            for (Circle circle : geoFenceCircleList) {
                circle.remove();
            }
            geoFenceCircleList.clear();
        }
    }

    private CircleOptions drawCircleOption(LatLng point, int radius) {
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(radius);
        // Border color of the circle
        circleOptions.strokeColor(Utils.colorInt("#FFFFFF"));
        // Fill color of the circle
        circleOptions.fillColor(AppConstant.GEO_FENCE_COLORS_RGB[getRandomNumber()]);
        // Border width of the circle
        circleOptions.strokeWidth(4);

        return circleOptions;
    }

    private void addCircleOptionOnMap(CircleOptions circleOptions) {
        googleMap.addCircle(circleOptions);
        geoFenceCircleList.add(googleMap.addCircle(circleOptions));
    }

    private void addMarkerOnMap(LatLng lng) {
        googleMap.addMarker(MapUtils.createMarker(lng, MapUtils.bitmapDescriptorFromVector(R.drawable.locate_marker_end)));
    }

    private void animateCamera(LatLng locationCamera, int radius) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCamera, MapUtils.getZoomLevel(radius)));
    }

    private void geoFenceListApiCall() {
        try {
            if (geoFenceList == null) {
                geoFenceList = new ArrayList<>();
            }else {
                geoFenceList.clear();
            }
            if (ShortTermManager.getInstance().getGeoFenceRequest() == null) {
                Progress.showLoadingDialog(activity);
                BusinessManager.postGeoFenceList("-1", new ApiCallResponse() {
                    @Override
                    public void onSuccess(int statusCode, Object responseObject) {
                        Progress.dismissLoadingDialog();
                        ShortTermManager.getInstance().setGeoFenceRequest(responseObject);
                        GeoFenceModel[] geoFenceModel = (GeoFenceModel[]) responseObject;
                        Collections.addAll(geoFenceList, geoFenceModel);
                        drawCircleOnMap();
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                        Progress.dismissLoadingDialog();
                    }
                });
            } else {
                GeoFenceModel[] geoFenceModel = (GeoFenceModel[]) ShortTermManager.getInstance().getGeoFenceRequest();
                Collections.addAll(geoFenceList, geoFenceModel);
                drawCircleOnMap();
                BusinessManager.postGeoFenceList("-1", new ApiCallResponse() {
                    @Override
                    public void onSuccess(int statusCode, Object responseObject) {
                        ShortTermManager.getInstance().setGeoFenceRequest(responseObject);
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private LatLng getLatLng(String loc) {
        LatLng resultLatLng;
        try {
            if (loc != null && loc.length() > 3) {
                String result = loc;
                if (loc.contains("(") || loc.contains(")")) {
                    result = loc.replaceAll("[()]", "");
                }
                String[] splitter = result.split(",");
                if (splitter[0].length() < 3 && splitter[1].length() < 3) {
                    return null;
                }
                resultLatLng = new LatLng(Double.parseDouble(splitter[0]), Double.parseDouble(splitter[1]));
                return resultLatLng;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(8) + 1;
    }


    public void setCircleManager(MapAreaManager.CircleManagerListener circleManagerListener) {
        circleManager = new MapAreaManager(googleMap, true, geoLatLng,
                2, Color.WHITE, Color.HSVToColor(70, new float[]{200, 1, 1}), //styling
//                R.drawable.ic_move, R.drawable.ic_drag, //custom drawables for move and resize icons
                R.drawable.locate_marker_end, R.drawable.ic_drag_32px, //custom drawables for move and resize icons
                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle
//                new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)
                new MapAreaMeasure(100, MapAreaMeasure.Unit.meters), //circles will start with 100 pixels (independent of zoom level)
                circleManagerListener);
        circleManager.setMinRadius(100);
    }


    //////////////////////
    //////////////////////
    //////////////////////
    //////////////////////
    //////////////////////
    //////////////////////
    private void setCircleManager() {
        circleManager = new MapAreaManager(googleMap, false, null,
                4, Color.RED, Color.HSVToColor(70, new float[]{1, 1, 200}), //styling
                R.drawable.ic_move, R.drawable.ic_drag, //custom drawables for move and resize icons
                0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle
                new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)

                new MapAreaManager.CircleManagerListener() { //listener for all circle events

                    @Override
                    public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "drag end circle: " + draggableCircle);
                        setBoundsAnimateCamera(draggableCircle.getCenter(), (float) draggableCircle.getRadius(), (float) draggableCircle.getRadius());
                    }

                    @Override
                    public void onCreateCircle(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "crate circle: " + draggableCircle);
                    }

                    @Override
                    public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "moved circle: " + draggableCircle);
                    }

                    @Override
                    public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "move circle start: " + draggableCircle);
                    }

                    @Override
                    public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "resize circle start: " + draggableCircle);
                    }

                    @Override
                    public void onMinRadius(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "min radius: " + draggableCircle);
                    }

                    @Override
                    public void onMaxRadius(MapAreaWrapper draggableCircle) {
                        ToastHelper.toastInfo(activity, "max radius: " + draggableCircle);
                    }
                });
        circleManager.setMinRadius(100);
    }

    private void setBoundsAnimateCamera(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        LatLngBounds bounds = boundsWithCenterAndLatLngDistance(center, latDistanceInMeters, lngDistanceInMeters);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
    }

    private LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        latDistanceInMeters /= 2;
        lngDistanceInMeters /= 2;
        LatLngBounds.Builder builder = LatLngBounds.builder();
        float[] distance = new float[1];
        {
            boolean foundMax = false;
            double foundMinLngDiff = 0;
            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
                float distanceDiff = distance[0] - lngDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLngDiff = assumedLngDiff;
                        assumedLngDiff *= 2;
                    } else {
                        double tmp = assumedLngDiff;
                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
                        foundMinLngDiff = tmp;
                    }
                } else {
                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
            builder.include(east);
            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
            builder.include(west);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffNorth;
                        assumedLatDiffNorth *= 2;
                    } else {
                        double tmp = assumedLatDiffNorth;
                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
            builder.include(north);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffSouth;
                        assumedLatDiffSouth *= 2;
                    } else {
                        double tmp = assumedLatDiffSouth;
                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
            builder.include(south);
        }
        return builder.build();
    }
}

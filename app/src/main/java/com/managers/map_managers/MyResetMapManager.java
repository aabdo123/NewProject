//package com.managers.map_managers;
//
//import android.content.Context;
//
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.models.AllVehiclesInHashModel;
//import com.utilities.AppUtils;
//import com.utilities.Utils;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//
//public class MyResetMapManager {
//
//    private Marker marker;
//    private GoogleMap googleMap;
//    private Context context;
//    private LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap;
//
//    public MyResetMapManager(Context context, GoogleMap googleMap) {
//        this.context = context;
//        this.googleMap = googleMap;
//    }
//
//    public void addVehiclesMarkers(LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap, boolean clearMap) {
//        if (clearMap)
//            googleMap.clear();
//        for (LinkedHashMap.Entry<Marker, AllVehiclesInHashModel> mapEntry : vehiclesHashMap.entrySet()) {
//            AllVehiclesInHashModel vehiclesInHashModel = mapEntry.getValue();
//            AllVehiclesInHashModel.AllVehicleModel.LastLocation lastLocation = vehiclesInHashModel.getAllVehicleModel().getLastLocation();
//            LatLng lng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            marker = googleMap.addMarker(new MarkerOptions().position(lng)
//                    .icon(AppUtils.getCarIcon(lastLocation.getVehicleStatus()))
//                    .anchor(0.5f, 0.5f)
//                    .rotation((float) lastLocation.getDirection())
//                    .flat(true));
//        }
//    }
//
//    public void viewAllMarkers(LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap) {
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LinkedHashMap.Entry<Marker, AllVehiclesInHashModel> mapEntry : vehiclesHashMap.entrySet()) {
//            AllVehiclesInHashModel vehiclesInHashModel = mapEntry.getValue();
//            AllVehiclesInHashModel.AllVehicleModel.LastLocation lastLocation = vehiclesInHashModel.getAllVehicleModel().getLastLocation();
//            LatLng lng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//        }
//
//        LatLngBounds bounds = builder.build();
//
//        //Setting the width and height of your screen
//        int width = context.getResources().getDisplayMetrics().widthPixels;
//        int height = context.getResources().getDisplayMetrics().heightPixels;
//        double random = Utils.getRandomNumber(0.12, 0.10);
////        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
//        int padding = (int) (width * random);
//
////        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Utils.getRandomNumber(115, 105));
//        CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
//        googleMap.animateCamera(cu);
//    }
//
//}

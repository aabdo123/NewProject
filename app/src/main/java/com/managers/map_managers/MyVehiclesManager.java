//package com.managers.map_managers;
//
//import android.util.Log;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.models.AllVehiclesInHashModel;
//import com.models.ListOfVehiclesModel;
//import com.utilities.AppUtils;
//import com.utilities.Utils;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//
//public class MyVehiclesManager {
//
//    private ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence = new ArrayList<>();
//    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehiclesList = new ArrayList<>();
//    private LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap = new LinkedHashMap<>();
//
//
//
//
//
//
//
//    private void addVehiclesMarkers() {
//        if (Utils.isNotEmptyList(vehiclesList))
//            for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
//                AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
//                inHashModel.setVehicleId(allVehicleModel.getVehicleID());
//                inHashModel.setAllVehicleModel(allVehicleModel);
//                LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLatitude());
//                Marker addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
//                        .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
//                        .anchor(0.5f, 0.5f)
//                        .rotation((float) allVehicleModel.getLastLocation().getDirection())
//                        .flat(true));
//                inHashModel.setMarker(addMarker);
//                vehiclesHashMap.put(addMarker, inHashModel);
//                builder.include(lng);
//                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        Log.e("onMarkerClick", "@@@@@@@@@@@@@@@@@@@@@@@" + marker.getId());
////                    AllVehiclesInHashModel model = vehiclesHashMap.get(marker.getId());
////                    ToastHelper.toastWarningLong(activity, model.getVehicleId() + "");
//                        return false;
//                    }
//                });
//            }
//    }
//}

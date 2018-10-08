package com.utilities.constants;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Saferoad-Dev1 on 10/4/2017.
 */

public interface AppConstant {

    String LANGUAGE_EN = "en";
    String LANGUAGE_AR = "ar";

    String SETTINGS = "SETTINGS";

    String POST = "POST";
    String POST_TEXT = "POST_TEXT";
    String POST_JSON = "POST_JSON";
    String POST_WITH_CONTEXT = "POST_WITHOUT";
    String POST_TIMEOUT = "POST_TIMEOUT";

    String GET = "GET";
    String GET_PARAMS = "GET_PARAMS";
    String GET_TEXT = "GET_TEXT";
    String GET_JSON = "GET_JSON";
    String FILES = "FILES";

    int POLY_THICKNESS = 9;
    int PERMISSION_REQUEST_CODE = 8;
    int GPS_ENABLE_REQUEST = 9;

    int LOADING_DELAY = 2000;
    int ANIMATION_DURATION = 500;
    int ANIMATION_DELAY = 500;

    float INITIAL_POSITION = 0.0f;
    float ROTATED_POSITION = 126f;

    String PROGRESS_DASHBOARD_MODEL_ARGS = "progress_dashboard_model_args";
    String CARS_DASHBOARD_MODEL_ARGS = "cars_dashboard_model_args";
    String LAST_VEHICLE_DASHBOARD_MODEL_ARGS = "last_vehicle_dashboard_model_args";

    String ALL_CARS = "all_cars";
    String ONLINE_CARS = "online_casr";
    String OFFLINE_CARS = "offline_cars";

    String VEHICLE_MODEL_ARGS = "vehicle_model_args";
    String VEHICLE_ID_ARGS = "vehicle_id_args";
    String VEHICLES_LIST_MODEL_ARGS = "vehicles_list_model_args";
    String VEHICLES_LIST_FOR_GEO_FENCE_ARGS = "vehicles_list_for_geo_fence_args";
    String GEO_LAT_ARGS = "geo_lat_args";
    String GEO_LNG_ARGS = "geo_lng_args";

    int TARGET_FRAGMENT_DIALOG = 1992;
    int TARGET_FRAGMENT_GEOFENCE = 1991;
    int TARGET_FRAGMENT_MAP_STYLE = 1990;

    LatLng KSA_LATLNG = new LatLng(24.629778, 46.799308);
    int ZOOM_VALUE_6 = 6;
    int ZOOM_VALUE_18 = 18;
    int ZOOM_VALUE_15 = 15;
    int MARKER_PADDING_OFFSET = 110; // offset from edges of the map in pixels

    String[] GEO_FENCE_COLORS = {"#99F5B426", "#9977C344", "#99EF5931", "#9979D6F9", "#998E5AF7", "#99DA407A", "#99FB0006", "#992632DA", "#99FFFF00"};
    int[] GEO_FENCE_COLORS_INT = {0x99F5B426, 0x9977C344, 0x99EF5931, 0x9979D6F9, 0x998E5AF7, 0x00DA407A, 0x99FB0006, 0x992632DA, 0x80FFFF00};
    int[] GEO_FENCE_COLORS_RGB = {Color.argb(0, 245, 180, 38),
            Color.argb(50, 119, 195, 68),
            Color.argb(50, 239, 89, 49),
            Color.argb(50, 121, 214, 249),
            Color.argb(50, 142, 90, 247),
            Color.argb(50, 218, 64, 122),
            Color.argb(50, 251, 0, 6),
            Color.argb(50, 38, 50, 218),
            Color.argb(50, 255, 255, 0)};
}

package com.utilities.constants;

/**
 * Created by Saferoad-Dev1 on 10/4/2017.
 */

public interface ApiConstants {

    String ROOT_API = "http://api.saferoad.net:8081/api/";
    String TOKEN = "http://api.saferoad.net:8081/token";
    String LOCATION_HUB = "http://saferoad.cloudapp.net:5010";
    String GOOGLE_API_KE = "AIzaSyB8CaFPkBKNnwrke1TZQzJB-CNp5NF8PtI";
    String GOOGLE_API_KEY = "AIzaSyDi-bSYjtEknQ7O7V05Hg7oWRLWPnU0rXU";

    String VEHICLES = "vehicles";
    String VehiclesV2 = "vehicles";

    String vehicleid = "?vehicleid=";

    String DASHBOARD = "dashboard?VehicleID=0";
    String VEHICLE_TREE = "VehicleTree";

    String DATA_ANALYSIS = "Reports?VehicleID=";
    String PAGE = "&Page=";
    String PAGE_INDEX = "PageIndex=";

    String ALARM_NOTIFICATIONS = "Notifications?vehicleid=";

    String GEO_FECES = "Geofences";
    String GEO_FECES_ = "Geofences?VehicleID=";
    String GEO_FECES_LIST = "Geofences?";
    String GEO_FECES_DELETE = "Geofences?DeleteID=";

    String LOCATIONS = "Locations?VehicleID=";
    String START_DATE = "&StartDate=";
    String END_DATE = "&EndDate=";

    String REPORTS_TYPE_LIST = "ReportsList";
    String SCHEDULED_REPORTS = "ScheduledReports";
    String SCHEDULED_REPORTS_DELETE = "ScheduledReports?reportID=";
    String VEHICLES_LIST= "VehicleLIst";
    String USER_LIST= "UserList";

    String ADD_LANDMARK= "LandMark/0";
    String ADD_LANDMARK_= "LandMark/0?";
    String LANDMARK_LIST = "LandMark?Page=";
    String LANDMARK_DELETE = "LandMark?DeleteID=";
}
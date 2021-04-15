package com.managers;

import android.util.Log;

import com.R;
import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.models.AlarmNotificationModel;
import com.models.CarHistoryModel;
import com.models.DashboardModel;
import com.models.DataAnalysisModel;
import com.models.GeneralMessageModel;
import com.models.GeoFenceModel;
import com.models.LandmarkModel;
import com.models.ListOfUsersModel;
import com.models.ReportsTypeModel;
import com.models.ScheduledReportsModel;
import com.models.TokenModel;
import com.utilities.constants.ApiConstants;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.activities.BaseActivity.mContext;


/**
 * Created by malikabuqauod on 6/5/17.
 */

public class BusinessManager {

    public static void postLogin(String userName, String password, final ApiCallResponse callResponse) {
        final String url = ApiConstants.TOKEN;
        RequestParams params = new RequestParams();
        params.put("grant_type", "password");
        params.put("username", userName);
        params.put("password", password);
//        params.put("username", "t.haddad@saferoad.com.sa");
//        params.put("password", "P@$$w0rd");
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postLogin", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postLogin", url + "   >>>   " + statusCode + "   >>>>    ", throwable);
                Log.e("postLogin", url + "   >>>   " + statusCode + "   >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, TokenModel.class);
            }
        });
    }
    public static void postVehiclesWithId(String vehicleId, final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.VehiclesV2 + ApiConstants.vehicleid + vehicleId;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.POST, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehicles", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postDashboard(final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.DASHBOARD + PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.LAST_VIEW_VEHICLE_ID);
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postDashboard", url + "   >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postDashboard", url + "    >>>   " + statusCode + "    >>>>    ", throwable);
                Log.e("postDashboard", url + "    >>>   " + statusCode + "    >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, DashboardModel.class);
            }
        });
    }

    public static void postVehicles(final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.VEHICLES;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.POST, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehicles", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void getMainVehiclesListWithStates(String states, final ApiCallResponseString callResponse) {
        String mainUrl = null;
        if (!states.equalsIgnoreCase(mContext.getString(R.string.all))) {
            mainUrl = ApiConstants.ROOT_API + ApiConstants.VEHICLE_TREE + "?status=" + states;
        } else {
            mainUrl = ApiConstants.ROOT_API + ApiConstants.VEHICLE_TREE;
        }
        RequestParams params = new RequestParams();
        String finalMainUrl = mainUrl;
        ConnectionManager.doRequestText(AppConstant.GET, mainUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehicles", finalMainUrl + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehicles", finalMainUrl + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postVehicles", finalMainUrl + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }


    public static void getMainVehiclesList(final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.VEHICLE_TREE;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehicles", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void getMainVehiclesListWithQuery(String id, final ApiCallResponseString callResponse) {
        List<String> value = new ArrayList<>();
        final String url = ApiConstants.ROOT_API + ApiConstants.VEHICLE_TREE + "?query=" + id;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehicles", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postVehicles", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void getJsonFromUrl(final String url, final ApiCallResponseString callResponse) {
        ConnectionManager.doRequestText(AppConstant.GET, url, null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("getJsonFromUrl", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("getJsonFromUrl", url + "   >>>   " + statusCode + "    >>>>    ", throwable);
                Log.e("getJsonFromUrl", url + "   >>>   " + statusCode + "    >>>>    " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postDataAnalysis(String vehicleId, String pageNumber, final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.DATA_ANALYSIS + vehicleId + ApiConstants.PAGE + pageNumber;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postDataAnalysis", url + "   >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postDataAnalysis", url + "   >>>   " + statusCode + "   >>>>   ", throwable);
                Log.e("postDataAnalysis", url + "   >>>   " + statusCode + "   >>>>   " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, DataAnalysisModel[].class);
            }
        });
    }

    public static void postAlarmNotification(String vehicleId, String pageNumber, final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.ALARM_NOTIFICATIONS + vehicleId + ApiConstants.PAGE + pageNumber;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postAlarmNotification", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postAlarmNotification", url + "    >>>   " + statusCode + "    >>>>    ", throwable);
                Log.e("postAlarmNotification", url + "    >>>   " + statusCode + "    >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, AlarmNotificationModel[].class);

            }
        });
    }

    public static void postHistoricalRoute(String vehicleId, String startDate, String endDate, final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.LOCATIONS + vehicleId + ApiConstants.START_DATE + startDate + ApiConstants.END_DATE + endDate;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postHistoricalRoute", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postHistoricalRoute", url + "   >>>   " + statusCode + "   >>>>    ", throwable);
                Log.e("postHistoricalRoute", url + "   >>>   " + statusCode + "   >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, CarHistoryModel[].class);
            }
        });
    }


    public static void postGeoFenceList(String page, final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.GEO_FECES_LIST + ApiConstants.PAGE + page;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postGeoFenceList", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postGeoFenceList", url + "   >>>   " + statusCode + "   >>>>    ", throwable);
                Log.e("postGeoFenceList", url + "   >>>   " + statusCode + "   >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, GeoFenceModel[].class);
            }
        });
    }

    public static void postGeoFence(String vehicleId, String geoName, double longitude, double latitude, String radius, String speed, boolean isin, boolean isout, final ApiCallResponseString callResponse) {
//        final String url = ApiConstants.ROOT_API + ApiConstants.GEO_FECES;
        final String url = ApiConstants.ROOT_API + ApiConstants.GEO_FECES_ + vehicleId + "&geofenceName=" + geoName + "&Longitude=" + longitude + "&Latitude=" + latitude +
                "&Radius=" + radius + "&Speed=" + speed + "&isin=" + isin + "&isout=" + isout;
        RequestParams params = new RequestParams();
//        params.put("VehicleID", vehicleId);
//        params.put("Longitude", longitude);
//        params.put("Latitude", latitude);
//        params.put("Radius", radius);
//        params.put("Speed", speed);
//        params.put("isin", isin);
//        params.put("isout", isout);
        ConnectionManager.doRequestText(AppConstant.POST, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postGeoFence", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postGeoFence", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postGeoFence", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postDeleteGeoFence(String geoFenceId, final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.GEO_FECES_DELETE + geoFenceId;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postDeleteGeoFence", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postDeleteGeoFence", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postDeleteGeoFence", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void getReportsType(final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.REPORTS_TYPE_LIST;
        ConnectionManager.doRequest(mContext, AppConstant.GET, url, null, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("getReportsType", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("getReportsType", url + "   >>>   " + statusCode + "    >>>>    ", throwable);
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, ReportsTypeModel[].class);
            }
        });
    }

    public static void getScheduledReports(final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.SCHEDULED_REPORTS;
        ConnectionManager.doRequest(mContext, AppConstant.GET, url, null, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("getScheduledReports", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("getScheduledReports", url + "   >>>   " + statusCode + "    >>>>    ", throwable);
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, ScheduledReportsModel[].class);
            }
        });
    }

    public static void postSubmitScheduledReports(String ReportScheduleID,
                                                  String ScheduleFrequencyID,
                                                  String Description,
                                                  String ScheduleVehicleID,
                                                  String ScheduleUserID,
                                                  String ScheduleReportsIncludedID,
                                                  String AdditionalEmails,
                                                  String AdditionalNumbers,
                                                  String FrequencyTitle,
                                                  String ScheduleFrequency,
                                                  final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.SCHEDULED_REPORTS;
        RequestParams params = new RequestParams();
        params.put("ReportScheduleID", ReportScheduleID);
        params.put("ScheduleFrequencyID", ScheduleFrequencyID);
        params.put("Description", Description);
        params.put("ScheduleVehicleID", ScheduleVehicleID);
        params.put("ScheduleUserID", ScheduleUserID);
        params.put("ScheduleReportsIncludedID", ScheduleReportsIncludedID);
        params.put("AdditionalEmails", AdditionalEmails);
        params.put("AdditionalNumbers", AdditionalNumbers);
        params.put("FrequencyTitle", FrequencyTitle);
        params.put("ScheduleFrequency", ScheduleFrequency);
        ConnectionManager.doRequestText(AppConstant.POST, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postScheduledReports", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postScheduledReports", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postDeleteScheduledReports(String scheduledReportsId, final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.SCHEDULED_REPORTS_DELETE + scheduledReportsId;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("DeleteScheduledReports", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("DeleteScheduledReports", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void getListOfUsers(final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.USER_LIST;
        ConnectionManager.doRequest(mContext, AppConstant.GET, url, null, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("getListOfUsers", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("getListOfUsers", url + "   >>>   " + statusCode + "    >>>>    ", throwable);
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, ListOfUsersModel[].class);
            }
        });
    }


    public static void postVehiclesSmall(final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.VEHICLES_LIST;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postVehiclesSmall", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postVehiclesSmall", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postAddLandMark(double longitude, double latitude, String Icon, String POIName, final ApiCallResponseString callResponse) {
//        final String url = ApiConstants.ROOT_API + ApiConstants.ADD_LANDMARK;
        final String url = ApiConstants.ROOT_API + ApiConstants.ADD_LANDMARK_
                + "Longitude=" + longitude + "&Latitude=" + latitude + "&Icon=" + Icon + "&POIName=" + POIName;
        RequestParams params = new RequestParams();
//        params.put("Longitude", longitude);
//        params.put("Latitude", latitude);
//        params.put("Icon", Icon);
//        params.put("POIName", POIName);
        ConnectionManager.doRequestText(AppConstant.POST, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postAddLandMark", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postAddLandMark", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                Log.e("postAddLandMark", url + "   >>>   " + statusCode + "  >>>>  " + String.valueOf(responseString));
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }

    public static void postLandMarkList(String page, final ApiCallResponse callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.LANDMARK_LIST + page;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequest(mContext, AppConstant.POST, url, params, new BaseJsonHttpResponseHandler<Object>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.e("postLandMarkList", url + "  >>> " + statusCode + "\n>>>>" + rawJsonResponse);
                callResponse.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.e("postLandMarkList", url + "   >>>   " + statusCode + "   >>>>    ", throwable);
                Log.e("postLandMarkList", url + "   >>>   " + statusCode + "   >>>>    " + String.valueOf(errorResponse));
                callResponse.onFailure(statusCode, rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure)
                    return new Gson().fromJson(rawJsonData, GeneralMessageModel.class);
                else
                    return new Gson().fromJson(rawJsonData, LandmarkModel[].class);
            }
        });
    }

    public static void postDeleteLandMark(String landmarkId, final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.LANDMARK_DELETE + landmarkId;
        RequestParams params = new RequestParams();
        ConnectionManager.doRequestText(AppConstant.GET, url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("postDeleteLandMark", url + "  >>> " + statusCode + "\n>>>>" + responseString);
                callResponse.onSuccess(statusCode, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("postDeleteLandMark", url + "   >>>   " + statusCode + "  >>>>  ", throwable);
                callResponse.onFailure(statusCode, responseString);
            }
        });
    }
}

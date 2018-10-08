package com.managers;

import android.util.Log;

import com.utilities.constants.ApiConstants;
import com.utilities.constants.AppConstant;

import java.util.HashMap;
import java.util.Map;

public class BusinessManagers {


    public static void postAddLandMark(double longitude, double latitude, String Icon, String POIName, final ApiCallResponseString callResponse) {
        final String url = ApiConstants.ROOT_API + ApiConstants.ADD_LANDMARK_
                + "Longitude=" + longitude + "&Latitude=" + latitude + "&Icon=" + Icon + "&POIName=" + POIName;
        Map<String, String> params = new HashMap<>();
        ConnectionManagers.doRequestText(AppConstant.POST, url, params, new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                //                Gson gson = new Gson();
//                String json = responseObject.toString();
//                LandmarkModel parseObject = gson.fromJson(json, LandmarkModel.class);
                callResponse.onSuccess(statusCode, responseObject);
                Log.e("postAddLandMark", url + ">>> " + responseObject + "  >>>>\n   " + responseObject);
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                callResponse.onFailure(statusCode, errorResponse);
                Log.wtf("postAddLandMark", errorResponse + "");
            }
        });
    }
}

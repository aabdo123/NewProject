package com.managers;

import java.util.ArrayList;

public class ShortTermManager {
    static ShortTermManager mInstance;
    private static String authToken = "";
    private static boolean isFromGuest = false;
    private boolean isFirstShare = false;
    private String requestMapsExpendableList;
    private Object landMarkRequest;
    private Object geoFenceRequest;
    private ArrayList<String> fireBaseArray;
    private int mapsStyle;



    public static synchronized ShortTermManager getInstance() {
        if (mInstance == null) {
            mInstance = new ShortTermManager();
        }
        return mInstance;
    }

    public String getRequestMapsExpendableList() {
        return requestMapsExpendableList;
    }

    public void setRequestMapsExpendableList(String requestMapsExpendableList) {
        this.requestMapsExpendableList = requestMapsExpendableList;
    }

    public int getMapsStyle() {
        return mapsStyle;
    }

    public void setMapsStyle(int mapsStyle) {
        this.mapsStyle = mapsStyle;
    }

    public ArrayList<String> getFireBaseArray() {
        return fireBaseArray;
    }

    public void setFireBaseArray(ArrayList<String> fireBaseArray) {
        this.fireBaseArray = fireBaseArray;
    }

    public void setIsFirstShare(boolean isFirstShare) {
        this.isFirstShare = isFirstShare;
    }

    public boolean isFirstShare() {
        return isFirstShare;
    }

    public Object getLandMarkRequest() {
        return landMarkRequest;
    }

    public void setLandMarkRequest(Object landMarkRequest) {
        this.landMarkRequest = landMarkRequest;
    }

    public boolean isFromGuest() {
        return isFromGuest;
    }

    public void setIsFromGuest(boolean isFromGuest) {
        ShortTermManager.isFromGuest = isFromGuest;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        ShortTermManager.authToken = authToken;
    }

    public Object getGeoFenceRequest() {
        return geoFenceRequest;
    }

    public void setGeoFenceRequest(Object geoFenceRequest) {
        this.geoFenceRequest = geoFenceRequest;
    }

    public void clear(){
        this.requestMapsExpendableList = null;
        this.landMarkRequest = null;
        this.geoFenceRequest = null;
        this.fireBaseArray = null;
    }

}

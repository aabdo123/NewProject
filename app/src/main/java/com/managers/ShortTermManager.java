package com.managers;

public class ShortTermManager {
    static ShortTermManager mInstance;
    private static String authToken = "";
    private static boolean isFromGuest = false;
    private boolean isFirstShare = false;
    private String requestMapsExpendableList;



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

    public void setIsFirstShare(boolean isFirstShare) {
        this.isFirstShare = isFirstShare;
    }

    public boolean isFirstShare() {
        return isFirstShare;
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


}
package com.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.models.ListOfVehiclesModel;
import com.utilities.AppUtils;

public class PreferencesManager {

    private static final String PREF_ID_SAFEROAD = "com.saferoad.business.shared";
    private static final String PREF_ID_ASTRO = "com.astroGps.shared";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;
    Context context;

    public PreferencesManager(Context context) {
//        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (AppUtils.isAstroGps())
            mPref = context.getSharedPreferences(PREF_ID_ASTRO, Context.MODE_PRIVATE);
        else
            mPref = context.getSharedPreferences(PREF_ID_SAFEROAD, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " Is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;

    }

    public void setStringValue(String value, String key) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key, value)
                .apply();
    }

    public String getStringValue(String key) {

        return mPref.getString(key, "");
    }

    public void setIntegerValue(int value, String key) {
        mPref.edit()
                .putInt(key, value)
                .apply();
    }

    public int getIntegerValue(String key) {

        return mPref.getInt(key, -1);
    }

    public void setLongValue(long value, String key) {
        mPref.edit()
                .putLong(key, value)
                .apply();
    }

    public long getLongValue(String key) {
        return mPref.getLong(key, 00000000);
    }

    public void setBooleanValue(boolean value, String key) {
        mPref.edit()
                .putBoolean(key, value)
                .apply();
    }

    public boolean getBooleanValue(String key) {
        return mPref.getBoolean(key, false);
    }

    public void setVehicleModel(ListOfVehiclesModel.VehicleModel value, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        mPref.edit()
                .putString(key, json)
                .apply();
    }

    public ListOfVehiclesModel.VehicleModel getVehicleModel(String key) {
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        return gson.fromJson(json, ListOfVehiclesModel.VehicleModel.class);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

    public boolean getFirstRun() {
        return mPref.getBoolean("PREF_USER_FIRST_TIME", true);

    }

    public void setFirstRun(boolean firstRun) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("PREF_USER_FIRST_TIME", firstRun);
        editor.apply();
    }

}

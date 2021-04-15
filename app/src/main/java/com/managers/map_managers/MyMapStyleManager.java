package com.managers.map_managers;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.R;
import com.fragments.MapStyleDialogFragment;
import com.managers.PreferencesManager;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

public class MyMapStyleManager {

    private FragmentActivity activity;
    private GoogleMap googleMap;
    private Fragment targetFragment;

    public MyMapStyleManager(FragmentActivity activity, GoogleMap googleMap) {
        this.activity = activity;
        this.googleMap = googleMap;
    }

    public void setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
    }

    public void showDialog() {
        MapStyleDialogFragment fenceFragment = MapStyleDialogFragment.newInstance();
        if (targetFragment != null)
            fenceFragment.setTargetFragment(targetFragment, AppConstant.TARGET_FRAGMENT_MAP_STYLE);
        fenceFragment.show(activity.getSupportFragmentManager(), "MapStyleDialogFragment");
    }

    public void setSelectedStyle(int mSelectedStyleId) {
        saveUserMapStyle(mSelectedStyleId);
        MapStyleOptions style;
        switch (mSelectedStyleId) {
            case R.string.standard:
                style = null;
                break;

            case R.string.aubergine:
                style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_aubergine);
                break;

            case R.string.silver:
                style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_sliver);
                break;

            case R.string.retro:
                style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_retro);
                break;

            case R.string.night:
                style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_night);
                break;

            case R.string.dark:
                style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.mapstyle_dark);
                break;
            default:
                return;
        }
        googleMap.setMapStyle(style);
    }

    private void saveUserMapStyle(int id) {
        PreferencesManager.getInstance().setIntegerValue(id, SharesPrefConstants.MAP_STYLE_ID);
    }
}

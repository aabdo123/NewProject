package com.utilities;

import android.content.Context;
import android.content.Intent;

import com.activities.MainActivity;
import com.managers.ShortTermManager;
import com.models.ListOfVehiclesModel;


public class NavigationManager {

    static NavigationManager mInstance;
    public static final int MOVE_TO_ONLINE_FRAGMENT_NUMBER = 100;
    public static final String MOVE_TO_ONLINE_FRAGMENT = "move_to_online_fragment";
    public static final String EXTRA_VEHICLE_MODEL = "extra_vehicle_model";

    public static synchronized NavigationManager getInstance() {
        if (mInstance == null) {
            mInstance = new NavigationManager();
        }
        return mInstance;
    }

    public Intent navigateToSingleVehicle(Context context, ListOfVehiclesModel.VehicleModel vehicleModel) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MOVE_TO_ONLINE_FRAGMENT, MOVE_TO_ONLINE_FRAGMENT_NUMBER);
        intent.putExtra(EXTRA_VEHICLE_MODEL, vehicleModel);
        return intent;
    }


}

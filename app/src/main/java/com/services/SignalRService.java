package com.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.application.MyApplication;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.managers.ShortTermManager;
import com.models.AllVehiclesInHashModel;
import com.models.ListOfVehiclesModel;
import com.models.SignalRCommandModel;
import com.models.SignalRModel;
import com.utilities.Utils;
import com.utilities.constants.ApiConstants;
import com.utilities.constants.SharesPrefConstants;
import com.views.Progress;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by Saferoad-Dev1 on 9/13/2017.
 */

public class SignalRService extends Service {

    //    private SignalRModel signalRModel;
//    private SignalRCommandModel signalRCommandModel;
//    private HubConnection mHubConnection;
//    private HubProxy mHubProxy;
//     private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    private DatabaseReference mDatabase;
    ArrayList<String> mainTokenValues;
    private ValueEventListener valueEventListener;

    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        ///startSignalR();
        return result;
    }

    @Override
    public void onDestroy() {
        // mHubConnection.stop();
        stopListener();
        super.onDestroy();
    }


    private void stopListener() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        //startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */

    public interface FireBaseListener {
        void dataSnapShot(String dataSnapshot);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    public void invokeService(FireBaseListener signalR) {
        try {
            FirebaseApp.initializeApp(MyApplication.getAppContext());
            mDatabase = FirebaseDatabase.getInstance().getReference();
            try {
                mainTokenValues = ShortTermManager.getInstance().getFireBaseArray();
                if (mainTokenValues != null && mainTokenValues.size() > 0) {
                    for (String val : mainTokenValues) {
                        valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot != null) {
                                        HashMap<String, String> bullets = new HashMap<String, String>();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Object bullet = snapshot.getValue(Object.class);
                                            bullets.put(snapshot.getKey(), String.valueOf(bullet));
                                        }
                                        JSONObject jsonObject = new JSONObject(bullets);
                                        signalR.dataSnapShot(String.valueOf(jsonObject));
                                    }
                                } catch (Exception ex) {
                                    ex.getMessage();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(MyApplication.getContext(), "error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        };
                        mDatabase.child(val).addValueEventListener(valueEventListener);
                    }
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startSignalR() {
        try {
            Progress.showLoadingDialog((Activity) MyApplication.getAppContext());
            BusinessManager.postVehicles(new ApiCallResponseString() {
                @Override
                public void onSuccess(int statusCode, String responseObject) {
                    Progress.dismissLoadingDialog();
                    ListOfVehiclesModel.VehicleModel[] vehicleModel = new Gson().fromJson(responseObject, ListOfVehiclesModel.VehicleModel[].class);
                    List<ListOfVehiclesModel.VehicleModel> arrayListMain = Arrays.asList(vehicleModel);
                    ArrayList<ListOfVehiclesModel.VehicleModel> main = new ArrayList<>(arrayListMain);
                    mainTokenValues = new ArrayList<>();
                    for (ListOfVehiclesModel.VehicleModel allVehicleModel : main) {
                        if (allVehicleModel.getFbToken() != null) {
                            mainTokenValues.add(allVehicleModel.getFbToken());
                        }
                    }

                }

                @Override
                public void onFailure(int statusCode, String errorResponse) {
                    Progress.dismissLoadingDialog();

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
package com.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.managers.PreferencesManager;
import com.models.SignalRCommandModel;
import com.models.SignalRModel;
import com.utilities.Utils;
import com.utilities.constants.ApiConstants;
import com.utilities.constants.SharesPrefConstants;

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

    private SignalRModel signalRModel;
    private SignalRCommandModel signalRCommandModel;
    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients

    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }

    @Override
    public void onDestroy() {
        mHubConnection.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
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
    public void invokeService(SignalR signalR) {
        try {
//        mHubProxy.invoke("Signin", PreferencesManager.getInstance().getStringValue(SharesPrefConstants.USER_ID));
//        mHubProxy.invoke("Signin", "d0588782-bb49-4c8d-8e99-6073f47fd1f3");
            mHubConnection.received(new MessageReceivedHandler() {
                @Override
                public void onMessageReceived(final JsonElement json) {
                    Log.e("onMessageReceived ", json.toString());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            signalRModel = new Gson().fromJson(json, SignalRModel.class);
                            if (Utils.isNotEmptyList(signalRModel.getA()) && signalRModel.getA().get(0).getLatitude() != 0.0) {
//                            if (signalRModel.getA().get(0).getVehicleID() == PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.LAST_VIEW_VEHICLE_ID)) {
                                signalR.onMessageReceived(signalRModel);
//                                Log.v("onMessageReceived", json.toString());
//                            }
                            } else {
                                signalRCommandModel = new Gson().fromJson(json, SignalRCommandModel.class);
                                signalR.onCommandReceived(signalRCommandModel);
                            }
                        }
                    });
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startSignalR() {
        try {
            Platform.loadPlatformComponent(new AndroidPlatformComponent());

            Credentials credentials = new Credentials() {
                @Override
                public void prepareRequest(Request request) {
                    request.addHeader("User-Name", "BNK");
                }
            };

            mHubConnection = new HubConnection(ApiConstants.LOCATION_HUB);
            mHubConnection.setCredentials(credentials);

            String SERVER_HUB_CHAT = "LocationsHub";
            mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);
            ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
            SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);
            try {
                signalRFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
            mHubProxy.invoke("Signin", PreferencesManager.getInstance().getStringValue(SharesPrefConstants.USER_ID));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
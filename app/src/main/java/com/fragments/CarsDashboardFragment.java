package com.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.R;
import com.adapters.CarsGridAdapter;
import com.google.gson.Gson;
import com.managers.PreferencesManager;
import com.models.DashboardModel;
import com.models.SignalRModel;
import com.models._VehiclesModel;
import com.services.SignalRService;
import com.utilities.LogHelper;
import com.utilities.constants.SharesPrefConstants;
import com.views.ExpandableHeightGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static com.utilities.constants.AppConstant.CARS_DASHBOARD_MODEL_ARGS;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class CarsDashboardFragment extends Fragment {

    private static CarsDashboardFragment fragment;
    private Activity activity;
    private DashboardModel dashboardModel;
    private ExpandableHeightGridView carsGridView;


    public CarsDashboardFragment() {
        // Required empty public constructor
    }

    public static CarsDashboardFragment newInstance(DashboardModel dashboardModel) {
//        if (fragment == null) {
        fragment = new CarsDashboardFragment();
        Bundle args = new Bundle();
        args.putParcelable(CARS_DASHBOARD_MODEL_ARGS, dashboardModel);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        startFirebaseService();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
            dashboardModel = mBundle.getParcelable(CARS_DASHBOARD_MODEL_ARGS);
        }
    }

    private SignalRService mService;
    private boolean mBound = false;

    private void startFirebaseService() {
        startSignalRServiceIntent();

    }

    @Override
    public void onResume() {
        super.onResume();
        startSignalRServiceIntent();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopSignalRService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSignalRService();
    }

    private void stopSignalRService() {
        // Unbind from the service
        if (mBound) {
            activity.unbindService(mConnection);
            if (SignalRServiceIntent != null)
                mService.stopService(SignalRServiceIntent);
            mBound = false;
        }
    }


    private Intent SignalRServiceIntent;

    private void startSignalRServiceIntent() {

        Runnable r = new Runnable() {
            public void run() {
                SignalRServiceIntent = new Intent();
                SignalRServiceIntent.setClass(requireContext(), SignalRService.class);
                activity.bindService(SignalRServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                observeVehiclesData();
            }
        };
        new Thread(r).start();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            try {
                LogHelper.LOG_D("onServiceConnected", "" + service.getInterfaceDescriptor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            startSignalRServiceIntent();


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private List<SignalRModel.A> vehiclesModelList = new ArrayList<>();
    Set<SignalRModel.A> set = new HashSet<>(vehiclesModelList);



    public void observeVehiclesData() {
        try {
            if (mBound) {
                mService.invokeService(new SignalRService.FireBaseListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void dataSnapShot(String dataSnapshot) {
                        SignalRModel.A vehicle = new Gson().fromJson(dataSnapshot, SignalRModel.A.class);
                        if(vehicle != null){
                            if(vehicle.getSerialNumber() != null){
                                if (vehicle.getSerialNumber().isEmpty()) {
                                    Log.d("SignalRModel.A vehicle", "dataSnapShot: Vehicle serial number is empty");
                                    Long generatedSerialNumber = new Random().nextLong();
                                    vehicle.setSerialNumber(String.valueOf(generatedSerialNumber));
                                    vehicle.setDeviceTypeID("7");
                                    vehicle.setEngineStatus("false");
                                }
                            }else{
                                Long generatedSerialNumber = new Random().nextLong();
                                vehicle.setSerialNumber(String.valueOf(generatedSerialNumber));
                                vehicle.setDeviceTypeID("7");
                                vehicle.setEngineStatus("false");
                            }
                            vehiclesModelList.add(vehicle);
                        }else{
                            Log.d("SignalRModel.A vehicle", "dataSnapShot: Vehicle is null");
                        }
                        List<SignalRModel.A> unique = vehiclesModelList.stream()
                                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(SignalRModel.A::getSerialNumber))),
                                        ArrayList::new));
                        vehiclesModelList.clear();
                        vehiclesModelList.addAll(unique);
                        initAdapter();

//                        Log.d("datasnpshotTestA",dataSnapshot);
                    }

                });

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cars_dashboard, container, false);
        initView(rootView);
        initAdapter();
        return rootView;
    }

    private void initView(View rootView) {
        carsGridView = (ExpandableHeightGridView) rootView.findViewById(R.id.carsGridView);
        carsGridView.setExpanded(true);

    }
    private List<String> dashboardModelList() {
        List<String> modelList = new ArrayList<>();
        modelList.add(String.valueOf(dashboardModel.getTotalVehiclesNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalOfflineNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalOnlineNumber()));
        modelList.add(String.valueOf(dashboardModel.getTotalAlarmsCount()));
        updatedashboardModelList();
        return modelList;
    }

    //I/Choreographer: Skipped 66 frames!  The application may be doing too much work on its main thread.
    private List<String> updatedashboardModelList() {
        List<String> modelList = new ArrayList<>();
        List<SignalRModel.A> offlineList = new ArrayList<>();
        List<SignalRModel.A> onlineList = new ArrayList<>();
        List<SignalRModel.A> allList = new ArrayList<>();

        DashboardModel cloneDashboardModel = dashboardModel;
        cloneDashboardModel.setTotalVehiclesNumber(vehiclesModelList.size());
        for (SignalRModel.A vehicle : vehiclesModelList) {
            if(!allList.contains(vehicle))
                allList.add(vehicle);
            if (vehicle.getDeviceTypeID().equals("7") && isRecordDateOutdated(vehicle.getRecordDateTime())) {
                if(!offlineList.contains(vehicle))
                    offlineList.add(vehicle);
            } else if (vehicle.getEngineStatus().equals("false") && isRecordDate4HoursPassed(vehicle.getRecordDateTime())) {
                if(!offlineList.contains(vehicle))
                    offlineList.add(vehicle);
            } else if (vehicle.getEngineStatus().equals("true") && isRecordDate30MinutesPassed(vehicle.getRecordDateTime())) {
                if(!offlineList.contains(vehicle))
                    offlineList.add(vehicle);
            } else {
                if(!onlineList.contains(vehicle))
                    onlineList.add(vehicle);
            }
            Log.d("offlineList",offlineList.toString());
            Log.d("onlineList",onlineList.toString());

        }

        cloneDashboardModel.setTotalOfflineNumber(offlineList.size());
        cloneDashboardModel.setTotalOnlineNumber(onlineList.size());
        cloneDashboardModel.setTotalVehiclesNumber(allList.size());

        onlineList.clear();
        offlineList.clear();
        allList.clear();

        modelList.add(String.valueOf(cloneDashboardModel.getTotalVehiclesNumber()));
        modelList.add(String.valueOf(cloneDashboardModel.getTotalOfflineNumber()));
        modelList.add(String.valueOf(cloneDashboardModel.getTotalOnlineNumber()));
        modelList.add(String.valueOf(cloneDashboardModel.getTotalAlarmsCount()));

        CarsGridAdapter carsGridAdapter = new CarsGridAdapter();
        carsGridAdapter.notifyDataSetChanged();



        return modelList;
    }

    public final long MILLIS_PER_30_MINUTES = 30 * 60 * 1000;

    private boolean isRecordDate30MinutesPassed(String recordDateTime) {
        boolean moreThan30Minutes = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            if (recordDateTime != null) {
                try {
                    Date dtIn = inFormat.parse(recordDateTime);
                    Date utcDate = new Date(dtIn.getTime() + 3 * HOUR);
                    if (utcDate != null) {
                        moreThan30Minutes = Math.abs(utcDate.getTime() - new Date().getTime()) > MILLIS_PER_30_MINUTES;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//2021-05-21T16:40:57
        }
        return moreThan30Minutes;
    }

    public final long MILLIS_PER_4_HOURS = 4 * 60 * 60 * 1000;
    public static final long HOUR = 3600*1000; // in milli-seconds.

    private boolean isRecordDate4HoursPassed(String recordDateTime) {
        boolean moreThan4Hours = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            if (recordDateTime != null) {
                try {
                    Date dtIn = inFormat.parse(recordDateTime);
                    Date utcDate = new Date(dtIn.getTime() + 3 * HOUR);

                    if (utcDate != null) {
                        moreThan4Hours = Math.abs(utcDate.getTime() - new Date().getTime()) > MILLIS_PER_4_HOURS;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//2021-05-21T16:40:57
        }
        return moreThan4Hours;
    }

    public final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

    private boolean isRecordDateOutdated(String recordDateTime) {
        boolean moreThanDay = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            if (recordDateTime != null) {
                try {
                    Date dtIn = inFormat.parse(recordDateTime);
                    Date utcDate = new Date(dtIn.getTime() + 3 * HOUR);

                    if (utcDate != null) {
                        moreThanDay = Math.abs(utcDate.getTime() - new Date().getTime()) > MILLIS_PER_DAY;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//2021-05-21T16:40:57
        }
        return moreThanDay;
    }


    private void initAdapter() {

        CarsGridAdapter carsGridAdapter = new CarsGridAdapter(activity, dashboardModelList());
        carsGridView.setAdapter(carsGridAdapter);
        carsGridAdapter.notifyDataSetChanged();
    }

}
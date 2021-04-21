package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.adapters.AlarmNotificationsAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.AlarmNotificationModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.ToastHelper;
import com.utilities.constants.AppConstant;
import com.views.Progress;
import com.views.TextViewLight;

import java.util.ArrayList;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class AlarmNotificationsFragment extends Fragment {

    public static AlarmNotificationsFragment fragment;
    private final static String VEHICLE_ID_ARGS = "vehicle_id_args";
    private Activity activity;
    private AlarmNotificationModel[] alarmNotificationModels;
    private ArrayList<AlarmNotificationModel> arrayList;
    private RecyclerView dataRecyclerView;
    private AlarmNotificationsAdapter alarmNotificationsAdapter;
    private String vehicleId;
    private int page = 0;
    private Handler handler;
    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private TextViewLight alarm_noti;


    public AlarmNotificationsFragment() {
        // Required empty public constructor
    }

    public static AlarmNotificationsFragment newInstance(String vehicle_id) {
//        if (fragment == null) {
        fragment = new AlarmNotificationsFragment();
        Bundle args = new Bundle();
        args.putString(VEHICLE_ID_ARGS, vehicle_id);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("VEHICLE_ID_ARGS", "getArgs");
            vehicleId = mBundle.getString(VEHICLE_ID_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_analysis, container, false);
        initView(rootView);
        getPageOne();
        return rootView;
    }

    private void initView(View rootView) {
        dataRecyclerView = (RecyclerView) rootView.findViewById(R.id.dataRecyclerView);
        alarm_noti = (TextViewLight) rootView.findViewById(R.id.alarm_noti);
        layoutManager = new LinearLayoutManager(activity);
        dataRecyclerView.setLayoutManager(layoutManager);
        handler = new Handler();
    }

    private void pageOneApiCall() {
        BusinessManager.postAlarmNotification(vehicleId, String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                if (responseObject != null){
                alarmNotificationModels = (AlarmNotificationModel[]) responseObject;
                initAdapter(alarmNotificationModels);
                page = 1;}
                else {
                   alarm_noti.setText("No data available");
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }

    private void nextPageApiCall() {
        if (isLoadMore) {
            return;
        }
        BusinessManager.postAlarmNotification(vehicleId, String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                if (responseObject != null) {
                    page = page + 1;
                    Log.e("Page >>> ", " = " + page);
                    AlarmNotificationModel[] alarmModels = (AlarmNotificationModel[]) responseObject;
//                Collections.addAll(arrayList, dataAnalysisModel);
                    arrayList.addAll(arrayList.size(), getArrayList(alarmModels));
                    alarmNotificationsAdapter.setLoaded();
                    alarmNotificationsAdapter.notifyDataSetChanged();
                    if (alarmModels.length == 0) {
                        isLoadMore = true;
                    }
                }
                else {
                    alarm_noti.setText("No data available");

                }
                }


            @Override
            public void onFailure(int statusCode, String errorResponse) {
                ToastHelper.toastMessage(activity, getString(R.string.refresh_again));
            }
        });
    }

    private void initAdapter(AlarmNotificationModel[] models) {
        arrayList = new ArrayList<>();
        this.arrayList = getArrayList(models);
        alarmNotificationsAdapter = new AlarmNotificationsAdapter(arrayList, dataRecyclerView);
        dataRecyclerView.setAdapter(alarmNotificationsAdapter);
        alarmNotificationsAdapter.setOnLoadMoreListener(new AlarmNotificationsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                arrayList.add(null);
                alarmNotificationsAdapter.notifyItemInserted(arrayList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        arrayList.remove(arrayList.size() - 1);
                        alarmNotificationsAdapter.notifyItemRemoved(arrayList.size());
                        //add items one by one
                        //When you've added the items call the setLoaded()
                        //if you put all of the items at once call
//                        dataAnalysisAdapter.notifyDataSetChanged();
                        nextPageApiCall();
                    }
                }, AppConstant.LOADING_DELAY); //time 3 seconds
            }
        });
        Progress.dismissLoadingDialog();
        AnimationUtils.loadListAnimation(dataRecyclerView);
    }

    // TODO : test
    private ArrayList<AlarmNotificationModel> getArrayList(AlarmNotificationModel[] model) {
        ArrayList<AlarmNotificationModel> list = new ArrayList<>();
        for (AlarmNotificationModel aModel : model) {
            list.add(aModel);
        }
        return list;
    }

    public void getPageOne() {
//        Progress.showLoadingDialog(activity);
        if (alarmNotificationModels != null && alarmNotificationModels.length > 0) {
            initAdapter(alarmNotificationModels);
        } else {
            pageOneApiCall();
        }
    }
}

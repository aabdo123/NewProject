package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.R;
import com.adapters.DataAnalysisAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.DataAnalysisModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.ToastHelper;
import com.utilities.constants.AppConstant;
import com.views.Progress;

import java.util.ArrayList;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class DataAnalysisFragment extends Fragment {

    public static DataAnalysisFragment fragment;
    private final static String VEHICLE_ID_ARGS = "vehicle_id_key";
    private Activity activity;
    private ArrayList<DataAnalysisModel> arrayList;
    private RecyclerView dataRecyclerView;
    private DataAnalysisAdapter dataAnalysisAdapter;
    private String vehicleId;
    private int page = 0;
    private Handler handler;
    private boolean isLoadMore = false;


    public DataAnalysisFragment() {
        // Required empty public constructor
    }

    public static DataAnalysisFragment newInstance(String vehicle_id) {
//        if (fragment == null) {
        fragment = new DataAnalysisFragment();
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
        pageOneApiCall();
        return rootView;
    }

    private void initView(View rootView) {
        dataRecyclerView = (RecyclerView) rootView.findViewById(R.id.dataRecyclerView);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        handler = new Handler();
    }

    private void pageOneApiCall() {
        Progress.showLoadingDialog(activity);
        BusinessManager.postDataAnalysis(vehicleId, String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                DataAnalysisModel[] dataAnalysisModel = (DataAnalysisModel[]) responseObject;
                initAdapter(dataAnalysisModel);
                page = 1;
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
        BusinessManager.postDataAnalysis(vehicleId, String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                page = page + 1;
                Log.e("Page >>>", " = " + page);
                DataAnalysisModel[] dataAnalysisModel = (DataAnalysisModel[]) responseObject;
//                Collections.addAll(arrayList, dataAnalysisModel);
                arrayList.addAll(arrayList.size(), getArrayList(dataAnalysisModel));
                dataAnalysisAdapter.setLoaded();
                dataAnalysisAdapter.notifyDataSetChanged();
                if (dataAnalysisModel.length == 0) {
                    isLoadMore = true;
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                ToastHelper.toastMessage(activity, getString(R.string.refresh_again));
            }
        });
    }

    private void initAdapter(DataAnalysisModel[] dataAnalysisModel) {
        arrayList = new ArrayList<>();
        this.arrayList = getArrayList(dataAnalysisModel);
        dataAnalysisAdapter = new DataAnalysisAdapter(arrayList, dataRecyclerView);
        dataRecyclerView.setAdapter(dataAnalysisAdapter);
        dataAnalysisAdapter.setOnLoadMoreListener(new DataAnalysisAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                arrayList.add(null);
                dataAnalysisAdapter.notifyItemInserted(arrayList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        arrayList.remove(arrayList.size() - 1);
                        dataAnalysisAdapter.notifyItemRemoved(arrayList.size());
                        //add items one by one
                        //When you've added the items call the setLoaded()
                        //if you put all of the items at once call
//                        dataAnalysisAdapter.notifyDataSetChanged();
                        nextPageApiCall();
                    }
                }, AppConstant.LOADING_DELAY); //time 3 seconds

            }
        });
        AnimationUtils.loadListAnimation(dataRecyclerView);
    }

    // TODO : test
    private ArrayList<DataAnalysisModel> getArrayList(DataAnalysisModel[] model) {
        ArrayList<DataAnalysisModel> list = new ArrayList<>();
        for (DataAnalysisModel aModel : model) {
            list.add(aModel);
        }
        return list;
    }
}
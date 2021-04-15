package com.fragments;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.adapters.GeoFenceAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.GeoFenceModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class GeoFenceListFragment extends Fragment {

    public static GeoFenceListFragment fragment;
    private FragmentActivity activity;
    private GeoFenceModel[] geoFenceModel;
    private ArrayList<GeoFenceModel> arrayList;
    private RecyclerView recyclerView;
    private GeoFenceAdapter adapter;
    private int page = 0;
    private Handler handler;
    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private TextViewRegular emptyListTextView;


    public GeoFenceListFragment() {
        // Required empty public constructor
    }

    public static GeoFenceListFragment newInstance() {
        return new GeoFenceListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo_fence_list, container, false);
        initView(rootView);
        getPageOne();
        return rootView;
    }

    private void initView(View rootView) {
        emptyListTextView = (TextViewRegular) rootView.findViewById(R.id.emptyListTextView);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        handler = new Handler();

    }

    private void pageOneApiCall() {
        BusinessManager.postGeoFenceList(String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                geoFenceModel = (GeoFenceModel[]) responseObject;
                if (Utils.isNotEmptyList(Arrays.asList(geoFenceModel))) {
                    initAdapter(geoFenceModel);
                    page = 1;
                    isEmptyListTextVisible(false);
                } else {
                    isEmptyListTextVisible(true);
                }
                Progress.dismissLoadingDialog();
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
        BusinessManager.postGeoFenceList(String.valueOf(page), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                page = page + 1;
                Log.e("Page >>> ", " = " + page);
                GeoFenceModel[] geoFenceModel = (GeoFenceModel[]) responseObject;
//                Collections.addAll(arrayList, dataAnalysisModel);
                arrayList.addAll(arrayList.size(), getArrayList(geoFenceModel));
                adapter.setLoaded();
                adapter.notifyDataSetChanged();
                if (geoFenceModel.length == 0) {
                    isLoadMore = true;
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                ToastHelper.toastMessage(activity, getString(R.string.refresh_again));
            }
        });
    }

    private void initAdapter(GeoFenceModel[] models) {
        arrayList = new ArrayList<>();
        this.arrayList = getArrayList(models);
        adapter = new GeoFenceAdapter(activity, arrayList, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new GeoFenceAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                arrayList.add(null);
                adapter.notifyItemInserted(arrayList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        arrayList.remove(arrayList.size() - 1);
                        adapter.notifyItemRemoved(arrayList.size());
                        //add items one by one
                        //When you've added the items call the setLoaded()
                        //if you put all of the items at once call
//                        dataAnalysisAdapter.notifyDataSetChanged();
                        nextPageApiCall();
                    }
                }, AppConstant.LOADING_DELAY); //time 3 seconds
            }
        });
//        AnimationUtils.loadListAnimation(recyclerView);
        recyclerView.scrollToPosition(0);
    }

    // TODO : test
    private ArrayList<GeoFenceModel> getArrayList(GeoFenceModel[] model) {
        ArrayList<GeoFenceModel> list = new ArrayList<>();
        for (GeoFenceModel aModel : model) {
            list.add(aModel);
        }
        return list;
    }

    public void getPageOne() {
        Progress.showLoadingDialog(activity);
        if (geoFenceModel != null && geoFenceModel.length > 0) {
            initAdapter(geoFenceModel);
        } else {
            pageOneApiCall();
        }
    }

    private void isEmptyListTextVisible(boolean isVisible) {
        emptyListTextView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
}

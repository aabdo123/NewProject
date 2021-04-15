package com.fragments;

import android.app.Activity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.R;
import com.activities.MainActivity;
import com.adapters.ListOfVehiclesAdapter;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.AllVehiclesInHashModel;
import com.models.AllVehiclesInHashModel.AllVehicleModel;
import com.models.DashboardModel;
import com.models.ListOfVehiclesModel;
import com.utilities.AnimationUtils;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class ListOfVehiclesFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static ListOfVehiclesFragment fragment;
    private Activity activity;
    private final static String VIEW_TYPE_ARGS = "view_type_args";
    private final static String EXTRA_VEHICLE_MODEL = "extra_vehicle_model";
    private ListOfVehiclesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView listOfVehiclesRecyclerView;
    private RelativeLayout allCarsLayout;
    private RelativeLayout carsOnlineLayout;
    private RelativeLayout carsOfflineLayout;
    private TextViewRegular allCarsTextView;
    private TextViewRegular carsOnlineTextView;
    private TextViewRegular carsOfflineTextView;
    private ArrayList<ListOfVehiclesModel> listOfVehiclesModels;
    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> mapList;
    private String responseObj = null;
//    private String viewType = AppConstant.ALL_CARS;
    private String viewType;

    private SearchView simpleSearchView;
    private ListOfVehiclesModel.VehicleModel vehicleModel;


    public ListOfVehiclesFragment() {
        // Required empty public constructor
    }

    public static ListOfVehiclesFragment newInstance(String viewType) {
//        if (fragment == null) {
        fragment = new ListOfVehiclesFragment();
        Bundle args = new Bundle();
        args.putString(VIEW_TYPE_ARGS, viewType);
        fragment.setArguments(args);
//        }
        return fragment;
    }


    public static ListOfVehiclesFragment newInstance(String viewType,ListOfVehiclesModel.VehicleModel vehicleModel) {
//        if (fragment == null) {
        fragment = new ListOfVehiclesFragment();
        Bundle args = new Bundle();
        args.putString(VIEW_TYPE_ARGS, viewType);
        args.putParcelable(EXTRA_VEHICLE_MODEL, vehicleModel);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = this.getArguments();
        activity = getActivity();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
            viewType = mBundle.getString(VIEW_TYPE_ARGS);
            vehicleModel = mBundle.getParcelable(EXTRA_VEHICLE_MODEL);
            if (TextUtils.isEmpty(viewType)) {
                viewType = AppConstant.ALL_CARS;
            }
            if (vehicleModel!=null){
                Progress.dismissLoadingDialog();
                ((MainActivity) activity).call(MapOfVehicleFragment.newInstance(vehicleModel), activity.getString(R.string.real_time_tracking));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_of_vehicles, container, false);
        initView(rootView);
        initListeners();
        getVehicles();
        searchTextChange();
        return rootView;
    }

    private void initListeners() {
        allCarsLayout.setOnClickListener(this);
        carsOnlineLayout.setOnClickListener(this);
        carsOfflineLayout.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initView(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        listOfVehiclesRecyclerView = (RecyclerView) rootView.findViewById(R.id.listOfVehiclesRecyclerView);
        allCarsLayout = (RelativeLayout) rootView.findViewById(R.id.allCarsLayout);
        carsOnlineLayout = (RelativeLayout) rootView.findViewById(R.id.carsOnlineLayout);
        carsOfflineLayout = (RelativeLayout) rootView.findViewById(R.id.carsOfflineLayout);
        allCarsTextView = (TextViewRegular) rootView.findViewById(R.id.allCarsTextView);
        carsOnlineTextView = (TextViewRegular) rootView.findViewById(R.id.carsOnlineTextView);
        carsOfflineTextView = (TextViewRegular) rootView.findViewById(R.id.carsOfflineTextView);
        simpleSearchView = (SearchView) rootView.findViewById(R.id.simpleSearchView);
    }

    public void getVehicles() {
        Progress.showLoadingDialog(activity);
        if (responseObj != null && responseObj.length() > 0) {
            initListFromApi(responseObj);
        } else {
            getVehiclesApiCall();
        }
    }


    private void searchTextChange() {
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                if (viewType.equalsIgnoreCase(AppConstant.ALL_CARS)) {
                    initAdapter(filterSearchCarsList(newText, listOfVehiclesModels));
                } else if (viewType.equalsIgnoreCase(AppConstant.ONLINE_CARS)) {
                    initAdapter(filterSearchCarsList(newText, filterOnlineCarsList(listOfVehiclesModels)));
                } else if (viewType.equalsIgnoreCase(AppConstant.OFFLINE_CARS)) {
                    initAdapter(filterSearchCarsList(newText, filterOfflineCarsList(listOfVehiclesModels)));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (viewType.equalsIgnoreCase(AppConstant.ALL_CARS)) {
                    initAdapter(filterSearchCarsList(newText, listOfVehiclesModels));
                } else if (viewType.equalsIgnoreCase(AppConstant.ONLINE_CARS)) {
                    initAdapter(filterSearchCarsList(newText, filterOnlineCarsList(listOfVehiclesModels)));
                } else if (viewType.equalsIgnoreCase(AppConstant.OFFLINE_CARS)) {
                    initAdapter(filterSearchCarsList(newText, filterOfflineCarsList(listOfVehiclesModels)));
                }
                return false;
            }
        });

        simpleSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleSearchView.onActionViewExpanded();
            }
        });


    }


    private void getVehiclesApiCall() {
        BusinessManager.postVehicles(new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                responseObj = responseObject;
                initListFromApi(responseObj);
            }


            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initListFromApi(String responseObject) {
        ListOfVehiclesModel.VehicleModel[] vehicleModel = ((MainActivity) activity).gHelper().fromJson(responseObject, ListOfVehiclesModel.VehicleModel[].class);
        listOfVehiclesModels = new ArrayList<ListOfVehiclesModel>();
        ListOfVehiclesModel listOfVehiclesModel = new ListOfVehiclesModel();
        listOfVehiclesModel.setHeader(activity.getResources().getString(R.string.vehicles_list));
        listOfVehiclesModel.setVehicleModel(Arrays.asList(vehicleModel));
        listOfVehiclesModels.add(listOfVehiclesModel);
        initListFromApiWithoutHeader(responseObj);
        // SET UP DATA IN VIEWS
        if (listOfVehiclesModels != null) {
            setListOfVehicles();
            setUpCarCountersLayout();
        }
    }

    private void initListFromApiWithoutHeader(String responseObject) {
        mapList = new ArrayList<>();
        AllVehiclesInHashModel.AllVehicleModel[] vehicleModel = ((MainActivity) activity).gHelper().fromJson(responseObject, AllVehiclesInHashModel.AllVehicleModel[].class);
        Collections.addAll(mapList, vehicleModel);
    }

    private void setListOfVehicles() {
        switch (viewType) {
            case AppConstant.ALL_CARS:
                if (listOfVehiclesModels.get(0).getVehicleModel() != null) {
                    initAdapter(listOfVehiclesModels);
                }
                break;

            case AppConstant.ONLINE_CARS:
                if (listOfVehiclesModels.get(0).getVehicleModel() != null)
                    initAdapter(filterOnlineCarsList(listOfVehiclesModels));
                break;

            case AppConstant.OFFLINE_CARS:
                if (listOfVehiclesModels.get(0).getVehicleModel() != null)
                    initAdapter(filterOfflineCarsList(listOfVehiclesModels));
                break;

            default:
                initAdapter(listOfVehiclesModels);
                break;
        }
    }

    private void initAdapter(ArrayList<ListOfVehiclesModel> arrayList) {
        try {
            if (arrayList != null && arrayList.size() > 0) {
                if (arrayList.get(0) != null && arrayList.get(0).getVehicleModel().size() > 0) {
                    adapter = new ListOfVehiclesAdapter(activity, arrayList, new ListOfVehiclesAdapter.onClickSearched() {
                        @Override
                        public void clicked() {
                            clearAndRefresh();
                        }
                    });
                    listOfVehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    listOfVehiclesRecyclerView.setAdapter(adapter);
                    adapter.expandParent(arrayList.get(0));
                    AnimationUtils.loadListAnimation(listOfVehiclesRecyclerView);
                } else {
                    //arrayList.get(0).getVehicleModel().clear();
                    ListOfVehiclesModel listOfVehiclesModel = new ListOfVehiclesModel();
                    listOfVehiclesModel.setHeader("Vehicles list");
                    List<ListOfVehiclesModel.VehicleModel> vehicleModels = new ArrayList<>();
                    vehicleModels.add(new ListOfVehiclesModel.VehicleModel());
                    listOfVehiclesModel.setVehicleModel(vehicleModels);
                    arrayList.set(0,listOfVehiclesModel);
                    adapter = new ListOfVehiclesAdapter(activity, arrayList, new ListOfVehiclesAdapter.onClickSearched() {
                        @Override
                        public void clicked() {
                            clearAndRefresh();
                        }
                    });
                    listOfVehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    listOfVehiclesRecyclerView.setAdapter(adapter);
                   // adapter.expandParent(arrayList.get(0));
                   /// AnimationUtils.loadListAnimation(listOfVehiclesRecyclerView);
                }
            }
            Progress.dismissLoadingDialog();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setUpCarCountersLayout() {
        allCarsTextView.setText(carsCount());
        carsOnlineTextView.setText(carOnlineCount());
        carsOfflineTextView.setText(carOfflineCount());
    }

    private String carsCount() {
        if (listOfVehiclesModels == null || listOfVehiclesModels.size() < 0) {
            return "0";
        }
        if (listOfVehiclesModels.get(0).getVehicleModel() == null || listOfVehiclesModels.get(0).getVehicleModel().size() < 0) {
            return "0";
        }

        return String.valueOf(listOfVehiclesModels.get(0).getVehicleModel().size());
    }

    private String carOnlineCount() {
        if (listOfVehiclesModels == null || listOfVehiclesModels.size() < 0) {
            return "0";
        }
        if (listOfVehiclesModels.get(0).getVehicleModel() == null || listOfVehiclesModels.get(0).getVehicleModel().size() < 0) {
            return "0";
        }
        if (filterOnlineCarsList(listOfVehiclesModels) == null || filterOnlineCarsList(listOfVehiclesModels).size() < 0) {
            return "0";
        }
        if (filterOnlineCarsList(listOfVehiclesModels).get(0) == null || filterOnlineCarsList(listOfVehiclesModels).get(0).getVehicleModel().size() < 0) {
            return "0";
        }

        return String.valueOf(filterOnlineCarsList(listOfVehiclesModels).get(0).getVehicleModel().size());
    }

    private String carOfflineCount() {
        if (listOfVehiclesModels == null || listOfVehiclesModels.size() < 0) {
            return "0";
        }
        if (listOfVehiclesModels.get(0).getVehicleModel() == null || listOfVehiclesModels.get(0).getVehicleModel().size() < 0) {
            return "0";
        }
        if (filterOfflineCarsList(listOfVehiclesModels) == null || filterOfflineCarsList(listOfVehiclesModels).size() < 0) {
            return "0";
        }
        if (filterOfflineCarsList(listOfVehiclesModels).get(0) == null || filterOfflineCarsList(listOfVehiclesModels).get(0).getVehicleModel().size() < 0) {
            return "0";
        }
        return String.valueOf(filterOfflineCarsList(listOfVehiclesModels).get(0).getVehicleModel().size());
    }

    private ArrayList<ListOfVehiclesModel> filterOnlineCarsList(ArrayList<ListOfVehiclesModel> allList) {
        ArrayList<ListOfVehiclesModel> parentList = new ArrayList<>();
        try {
            ArrayList<ListOfVehiclesModel.VehicleModel> childList = new ArrayList<>();
            ListOfVehiclesModel model = null;
            int i, x;
            for (i = 0; i < allList.size(); i++) {
                for (x = i; x < allList.get(i).getVehicleModel().size(); x++) {
                    if (allList.get(i).getVehicleModel().get(x).getLastLocation().getIsOnline()) {
                        model = new ListOfVehiclesModel();
                        childList.add(allList.get(i).getVehicleModel().get(x));
                        model.setHeader(allList.get(i).getHeader());
                        model.setVehicleModel(childList);
                    }
                }
                parentList.add(model);
            }
//        while (parentList.remove("")) ;
//        Set<ListOfVehiclesModel> hashSet = new HashSet<>();
//        hashSet.addAll(parentList);
//        parentList.clear();
//        parentList.addAll(hashSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentList;
    }


    private ArrayList<ListOfVehiclesModel> filterSearchCarsList(String value, ArrayList<ListOfVehiclesModel> allList) {
        ArrayList<ListOfVehiclesModel> parentList = new ArrayList<>();
        try {
            ArrayList<ListOfVehiclesModel.VehicleModel> childList = new ArrayList<>();
            ListOfVehiclesModel model = null;
            int i, x;
            for (i = 0; i < allList.size(); i++) {
                for (x = i; x < allList.get(i).getVehicleModel().size(); x++) {
                    if (allList.get(i).getVehicleModel().get(x).getLabel().toLowerCase().contains(value.toLowerCase())) {
                        model = new ListOfVehiclesModel();
                        childList.add(allList.get(i).getVehicleModel().get(x));
                        model.setHeader(allList.get(i).getHeader());
                        model.setVehicleModel(childList);
                    } else {
                        Log.e("s", "s");
                    }
                }
                parentList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentList;
    }



    private ArrayList<ListOfVehiclesModel> filterOfflineCarsList(ArrayList<ListOfVehiclesModel> allList) {
        ArrayList<ListOfVehiclesModel> parentList = new ArrayList<>();
        try {
            ArrayList<ListOfVehiclesModel.VehicleModel> childList = new ArrayList<>();
            ListOfVehiclesModel model = null;
            int i, x;
            for (i = 0; i < allList.size(); i++) {
                for (x = i; x < allList.get(i).getVehicleModel().size(); x++) {
                    if (!allList.get(i).getVehicleModel().get(x).getLastLocation().getIsOnline()) {
                        model = new ListOfVehiclesModel();
                        childList.add(allList.get(i).getVehicleModel().get(x));
                        model.setHeader(allList.get(i).getHeader());
                        model.setVehicleModel(childList);
                    }
                }
                parentList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allCarsLayout:
                clearAndRefresh();
                viewType = AppConstant.ALL_CARS;
                initAdapter(listOfVehiclesModels);
                break;

            case R.id.carsOnlineLayout:
                clearAndRefresh();
                viewType = AppConstant.ONLINE_CARS;
                initAdapter(filterOnlineCarsList(listOfVehiclesModels));
                break;

            case R.id.carsOfflineLayout:
                clearAndRefresh();
                viewType = AppConstant.OFFLINE_CARS;
                initAdapter(filterOfflineCarsList(listOfVehiclesModels));
                break;
        }
    }


    private void clearAndRefresh() {
        try {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.hidKeyBoard(Objects.requireNonNull(getActivity()));
                    simpleSearchView.clearFocus();
                    simpleSearchView.setIconified(true);
                    simpleSearchView.onActionViewCollapsed();
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public void onRefresh() {
        getVehiclesApiCall();
    }
}
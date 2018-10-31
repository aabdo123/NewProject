package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.R;
import com.adapters.ExpandableAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.managers.ShortTermManager;
import com.managers.map_managers.MyGeoFenceManager;
import com.managers.map_managers.MyLandmarkManager;
import com.managers.map_managers.MyLocateManager;
import com.managers.map_managers.MyMapStyleManager;
import com.managers.map_managers.VehiclesClusterManager;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.models.AllVehiclesInHashModel;
import com.models.Item;
import com.models.ListOfVehiclesModel;
import com.models.SignalRCommandModel;
import com.models.SignalRModel;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
import com.services.SignalR;
import com.services.SignalRService;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.LogHelper;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.views.AlertDialogView;
import com.views.Click;
import com.views.Progress;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;

public class LisOfVehiclesMapFragment extends Fragment implements
        SlideUpFragment.OnChildFragmentInteractionListener,
        GeoFenceFragment.OnAddGeoFence,
        LandmarkFragment.OnAddLandmark,
        MapStyleDialogFragment.OnSelectMapStyle,
        OnMapReadyCallback, LocationSource, LocationListener, View.OnClickListener {

    public static LisOfVehiclesMapFragment fragment;

    private SignalRService mService;

    private boolean mBound = false;

    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng firstCarLatLng;
    private LatLng myCurrentLatLng;

    private OnLocationChangedListener mapLocationListener = null;
    private LocationManager locationManager = null;
    private Criteria criteria = new Criteria();
    private Context context;
    private Activity activity;
    private FragmentActivity fragmentActivity;
    private ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence = new ArrayList<>();
    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehiclesList = new ArrayList<>();
    private LinkedHashMap<Marker, AllVehiclesInHashModel> vehiclesHashMap = new LinkedHashMap<>();

    private com.github.clans.fab.FloatingActionButton mapStylingFab;
    //    private com.github.clans.fab.FloatingActionButton listStylingFab;
    public static SlideUp slideUp;
    public static SlideUp slideUpSingleCar;
    private FrameLayout sliderBgLayout;
    private LinearLayout sliderView;
    private LinearLayout singleSlideView;

    private CoordinatorLayout allViewSlideLayout;
    private RelativeLayout moreOptionsLayout;
    private LinearLayout singleCarMoreOptionsLayout;
    private TextViewRegular sliderTitleTextView;
    private TextViewRegular singleCarSliderTitleTextView;
    private TextViewRegular singleSliderCarSliderTitleTextView;
    private TextViewLight singleSliderUpTitleTextView;
    private TextViewLight singleCarSliderAddressTextView;
    private TextViewRegular kmTextView;
    private ImageView sliderArrowImageView;
    private ImageView singleSliderUpArrowImageView;
    private ImageView singleCarSliderArrowImageView;

    private RelativeLayout moreOptionsUpLayout;
    private TextViewRegular sliderUpTitleTextView;
    private TextViewRegular timeTextView;
    private TextViewRegular timerTextView;
    private TextViewRegular infoTextView;
    private TextViewRegular offTextView;
    private TextViewRegular nATextView;
    private TextViewRegular visaTextView;
    private TextViewRegular barCodeTextView;
    private TextViewRegular closedTextView;
    private TextViewRegular beltTextView;
    private TextViewRegular needlTextView;
    private TextViewRegular gasTextView;
    private TextViewRegular humanTextView;
    private TextViewRegular cardTextView;
    private TextViewRegular addressTextView;
    private TextViewRegular mileageTextView;
    private TextViewRegular workingTextView;
    private ImageView sliderUpArrowImageView;

    private GoogleApiClient googleApiClient;

    private View rootView;
    private MyLocateManager myLocateManager;
    private MyGeoFenceManager myGeoFenceManager;
    private MyLandmarkManager myLandmarkManager;
    private MyMapStyleManager myMapStyleManager;
    private VehiclesClusterManager vehiclesClusterManager;
    private ListView listView;
    private List<Item> list;
    private List<Item> mainArrayOfVehiclesList = new ArrayList<>();
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private LatLngBounds bounds;
    private boolean openFirstTime = true;
    private List<Item> mainChecks;
    private ExpandableAdapter expandableAdapter;
    private List<Item> itemLists;
    private boolean isRandomZoomChanged = false;
    private int randomZoom;
    private android.app.Dialog dialogAndroidAppCus;
    private static SlideUpFragment slideUpFragment;
    private MultiLevelRecyclerView multiLevelRecyclerView;
    private boolean markerClusterVisibility;
    private ArrayList<Item> itemArrayListCallas;
//    private Marker addMarker;

    public LisOfVehiclesMapFragment() {
        // Required empty public constructor
    }

    public static LisOfVehiclesMapFragment newInstance(ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehicleModel, ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence) {
//        if (fragment == null) {
        fragment = new LisOfVehiclesMapFragment();
        Bundle args = new Bundle();
//        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_MODEL_ARGS, vehicleModel);
        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS, listOfVehiclesForGeoFence);
        fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstant.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapView.getMapAsync(this);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        startSignalRServiceIntent();
        super.onResume();
        mMapView.onResume();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(0L, 0.0f, criteria, this, null);
        }

        if (googleMap != null) {
            googleMap.setLocationSource(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        stopSignalRService();
        try {
            googleMap.setLocationSource(null);
            locationManager.removeUpdates(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getContext();
            activity = getActivity();
            fragmentActivity = getActivity();
            startSignalRServiceIntent();
            Bundle mBundle = this.getArguments();
            if (mBundle != null) {
//                LogHelper.LOG_D("MAP", "getArgs");
                // vehiclesList = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_MODEL_ARGS);
                listOfVehiclesForGeoFence = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS);
//                firstCarLatLng = new LatLng(vehiclesList.get(0).getLastLocation().getLatitude(), vehiclesList.get(0).getLastLocation().getLongitude());
//                myCurrentLatLng = firstCarLatLng;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_of_vehicle_map, container, false);
        initViews();
        initListeners();
        if (!AppUtils.checkLocationPermissions(activity)) {
            openGpsLocation();
        }
        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU);
        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU);
        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(context.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        Progress.dismissLoadingDialog();

        return rootView;
    }

    private void initViews() {
        mapStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);
//        listStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.listStylingFab);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        sliderView = (LinearLayout) rootView.findViewById(R.id.slideView);
        singleSlideView = (LinearLayout) rootView.findViewById(R.id.singleSlideView);
        sliderBgLayout = (FrameLayout) rootView.findViewById(R.id.sliderBgLayout);
        allViewSlideLayout = (CoordinatorLayout) rootView.findViewById(R.id.allViewSlideLayout);

        moreOptionsLayout = (RelativeLayout) rootView.findViewById(R.id.moreOptionsLayout);
        singleCarMoreOptionsLayout = (LinearLayout) rootView.findViewById(R.id.singleCarMoreOptionsLayout);
        sliderTitleTextView = (TextViewRegular) rootView.findViewById(R.id.sliderTitleTextView);
        singleCarSliderTitleTextView = (TextViewRegular) rootView.findViewById(R.id.singleCarSliderTitleTextView);
        singleSliderCarSliderTitleTextView = (TextViewRegular) rootView.findViewById(R.id.singleSliderCarSliderTitleTextView);
        singleCarSliderAddressTextView = (TextViewLight) rootView.findViewById(R.id.singleCarSliderAddressTextView);
        kmTextView = (TextViewRegular) rootView.findViewById(R.id.kmTextView);
        singleSliderUpTitleTextView = (TextViewLight) rootView.findViewById(R.id.singleSliderUpTitleTextView);
        sliderArrowImageView = (ImageView) rootView.findViewById(R.id.sliderArrowImageView);
        singleSliderUpArrowImageView = (ImageView) rootView.findViewById(R.id.singleSliderUpArrowImageView);

        moreOptionsUpLayout = (RelativeLayout) rootView.findViewById(R.id.moreOptionsUpLayout);
        sliderUpTitleTextView = (TextViewRegular) rootView.findViewById(R.id.sliderUpTitleTextView);
        timeTextView = (TextViewRegular) rootView.findViewById(R.id.timeTextView);
        addressTextView = (TextViewRegular) rootView.findViewById(R.id.addressTextView);
        cardTextView = (TextViewRegular) rootView.findViewById(R.id.cardTextView);
        humanTextView = (TextViewRegular) rootView.findViewById(R.id.humanTextView);
        gasTextView = (TextViewRegular) rootView.findViewById(R.id.gasTextView);
        needlTextView = (TextViewRegular) rootView.findViewById(R.id.needlTextView);
        beltTextView = (TextViewRegular) rootView.findViewById(R.id.beltTextView);
        closedTextView = (TextViewRegular) rootView.findViewById(R.id.closedTextView);
        barCodeTextView = (TextViewRegular) rootView.findViewById(R.id.barCodeTextView);
        visaTextView = (TextViewRegular) rootView.findViewById(R.id.visaTextView);
        nATextView = (TextViewRegular) rootView.findViewById(R.id.nATextView);
        offTextView = (TextViewRegular) rootView.findViewById(R.id.offTextView);
        infoTextView = (TextViewRegular) rootView.findViewById(R.id.infoTextView);
        timerTextView = (TextViewRegular) rootView.findViewById(R.id.timerTextView);
        mileageTextView = (TextViewRegular) rootView.findViewById(R.id.mileageTextView);
        workingTextView = (TextViewRegular) rootView.findViewById(R.id.workingTextView);
        sliderUpArrowImageView = (ImageView) rootView.findViewById(R.id.sliderUpArrowImageView);
        singleCarSliderArrowImageView = (ImageView) rootView.findViewById(R.id.singleCarSliderArrowImageView);
        listView = (ListView) rootView.findViewById(R.id.listView);

        mMapView.setZ(0);
        mapStylingFab.setZ(1);
//        listStylingFab.setZ(1);
        sliderBgLayout.setZ(2);
        allViewSlideLayout.setZ(3);

        setSlideUpBuilder();
        setSingleSlideUpBuilder();
        addSlideUpFragment();

        // slide visibility
//        singleSlideView.setVisibility(View.GONE);
    }

    private void initListeners() {
        mapStylingFab.setOnClickListener(this);
//        listStylingFab.setOnClickListener(this);

        moreOptionsLayout.setOnClickListener(this);
        singleCarMoreOptionsLayout.setOnClickListener(this);
        sliderTitleTextView.setOnClickListener(this);
        singleCarSliderTitleTextView.setOnClickListener(this);
        sliderArrowImageView.setOnClickListener(this);
        singleSliderUpArrowImageView.setOnClickListener(this);
        singleCarSliderArrowImageView.setOnClickListener(this);

        sliderBgLayout.setOnClickListener(this);
        moreOptionsUpLayout.setOnClickListener(this);
        sliderUpArrowImageView.setOnClickListener(this);
        sliderUpTitleTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreOptionsLayout:
            case R.id.singleCarMoreOptionsLayout:
            case R.id.sliderTitleTextView:
            case R.id.sliderArrowImageView:
                slideUp.show();
                break;
            case R.id.singleCarSliderArrowImageView: // single car view
                slideUpSingleCar.show();
                break;
            case R.id.singleSliderUpArrowImageView:
                slideUpSingleCar.hide();
                break;
            case R.id.sliderBgLayout:
            case R.id.moreOptionsUpLayout:
            case R.id.sliderUpTitleTextView:
            case R.id.sliderUpArrowImageView:
                try {
                    if (slideUp != null)
                        slideUp.hide();
                    if (slideUpSingleCar != null)
                        slideUpSingleCar.hide();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.mapStylingFab:
                openMapStyleDialog();
                break;
            default:
                break;
        }
    }

    public void updateMapStyleOnBackPressed() {
        if (myMapStyleManager != null) {
            myMapStyleManager.setSelectedStyle(PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_ID));
            PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_MAP_STYLE_CHANGED);
        }
    }

    public void showExpandableList() {
        bottomViewVisibility();
        expandable();
    }


    private void addCarsOnMap() {
        try {
            if (mainArrayOfVehiclesList != null) {
                List<Item> mainItemsFull = new ArrayList<>();
                if (mainArrayOfVehiclesList.size() > 0) {
                    for (Item item : mainArrayOfVehiclesList) {
                        if (item.isChecked()) {
                            mainItemsFull.add(item);
                        }
                    }
                } else {
                    mainItemsFull.clear();
                }
                List<AllVehiclesInHashModel.AllVehicleModel.LastLocation> arrayFromApi = null;
                if (mainItemsFull.size() > 0) {
                    String request = new Gson().toJson(mainItemsFull);
                    AllVehiclesInHashModel.AllVehicleModel.LastLocation[] vehicleModel = new Gson().fromJson(request, AllVehiclesInHashModel.AllVehicleModel.LastLocation[].class);
                    arrayFromApi = Arrays.asList(vehicleModel);
                }
                if (vehiclesList != null) {
                    if (vehiclesList.size() > 0) {
                        vehiclesList.clear();
                    }
                    if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                        vehiclesHashMap.clear();
                    }
                    if (arrayFromApi != null) {
                        for (AllVehiclesInHashModel.AllVehicleModel.LastLocation allVehicleModel : arrayFromApi) {
                            AllVehiclesInHashModel.AllVehicleModel allVehicleModel1 = new AllVehiclesInHashModel.AllVehicleModel();
                            allVehicleModel1.setVehicleID(allVehicleModel.getVehicleID() != null ? allVehicleModel.getVehicleID() : 0);
                            allVehicleModel1.setLastLocation(allVehicleModel);
                            allVehicleModel1.setLabel(allVehicleModel.getVehicleDisplayName() != null ? allVehicleModel.getVehicleDisplayName() : "");
                            vehiclesList.add(allVehicleModel1);
                        }
                    }
                    openFirstTime = true;
                    if (googleMap != null)
                        googleMap.clear();
                    addVehiclesMarkers(true);
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showLandMark(true);
                    }
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showGeofence(true);
                    }
                    if (listOfVehiclesForGeoFence != null && listOfVehiclesForGeoFence.size() > 0 && listOfVehiclesForGeoFence.get(0) != null) {
                        String request = new Gson().toJson(vehiclesList);
                        ListOfVehiclesModel.VehicleModel[] vehicleModel = new Gson().fromJson(request, ListOfVehiclesModel.VehicleModel[].class);
                        List<ListOfVehiclesModel.VehicleModel> list = Arrays.asList(vehicleModel);
                        listOfVehiclesForGeoFence.get(0).setVehicleModel(list);
                    }
                    if (markerClusterVisibility)
                        setClusterManager(true, false, 1);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    private void expandable() {
        if (ShortTermManager.getInstance().getRequestMapsExpendableList() == null) {
            Progress.showLoadingDialog(activity);
            BusinessManager.getMainVehiclesList(new ApiCallResponseString() {
                @Override
                public void onSuccess(int statusCode, String responseObject) {
                    try {
                        if (responseObject != null) {
                            ShortTermManager.getInstance().setRequestMapsExpendableList(responseObject);
                        }
                        mainApiCall(responseObject);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, String errorResponse) {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mainApiCall(ShortTermManager.getInstance().getRequestMapsExpendableList());
        }
    }

    private void mainApiCall(String responseObject) {
        try {
            if (list == null)
                list = new ArrayList<>();
            if (mainChecks == null)
                mainChecks = new ArrayList<>();
//            List<Item> itemLists = (List<Item>) list;
            if (itemLists == null) {
                JSONObject jsonObject = new JSONObject(responseObject);
                JSONObject mainAdd = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                mainAdd.put("Childs", jsonArray);
                mainAdd.put("ID", "0");
                mainAdd.put("Name", "o0");
                mainAdd.put("VehicleStatus", "0");
                itemLists = (List<Item>) nestedLoop(mainAdd, 0);
                list = itemLists;
            }
            if (dialogAndroidAppCus == null) {
                dialogAndroidAppCus = new android.app.Dialog(context);
                Objects.requireNonNull(dialogAndroidAppCus.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAndroidAppCus.setContentView(R.layout.view_expandable_dialog);
            }
            TextViewRegular addReportButton = (TextViewRegular) dialogAndroidAppCus.findViewById(R.id.addReportButton);
            addReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAndroidAppCus.dismiss();
                    addCarsOnMap();
                }
            });
            dialogAndroidAppCus.setCancelable(true);
            dialogAndroidAppCus.show();
            multiLevelRecyclerView = (MultiLevelRecyclerView) dialogAndroidAppCus.findViewById(R.id.rv_list);
            Progress.dismissLoadingDialog();
            multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (expandableAdapter == null) {
                expandableAdapter = new ExpandableAdapter(context, itemLists, multiLevelRecyclerView, new ExpandableAdapter.ActionsInterface() {
                    @Override
                    public void ItemClicked(Item item, int position, boolean checkedState, String clickState) {
                        if (!checkedState) {
                            if (position == 0) {
                                if (itemLists != null) {
                                    if (item.isExpanded()) {
                                        for (int x = 0; x < itemLists.size(); x++) {
                                            itemLists.get(x).setChecked(false);
                                            itemLists.get(x).setGroupChecked(false);
                                        }
                                        for (int x = 0; x < list.size(); x++) {
                                            list.get(x).setChecked(false);
                                            list.get(x).setGroupChecked(false);
                                        }
                                    } else {
                                        if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
                                            itemArrayListCallas = new ArrayList<>();
                                            getLength(itemLists.get(0).getChildren(), false, false);
                                            Log.e("s", "s");
                                        }
                                    }
                                }
                                if (mainArrayOfVehiclesList.size() > 0)
                                    mainArrayOfVehiclesList.clear();
                                expandableAdapter.notifyDataSetChanged();
                            } else {
                                unChecked(item, itemLists, clickState);
                            }
                        } else {
                            if (position == 0) {
                                isCheckedValueAll(item, itemLists);
                            } else {
                                isCheckedValue(item, itemLists, position, clickState);
                            }
                        }
                    }
                });
                multiLevelRecyclerView.setAdapter(expandableAdapter);
                for (int x = 0; x < itemLists.size(); x++) {

                    String vehicle = itemLists.get(x).getID();
                    String firstOne = vehicle.substring(0, 1);
                    if (firstOne.equalsIgnoreCase("G")) {
                        multiLevelRecyclerView.openTill(x);
                    }
                }
            }
            multiLevelRecyclerView.setToggleItemOnClick(false);
            multiLevelRecyclerView.setAccordion(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void unChecked(Item item, List<Item> itemLists, String clickState) {
        try {
            if (item != null && item.getChildren() != null && clickState.equalsIgnoreCase("grope")) {
                List<?> arrayFromApiCasting = item.getChildren();
                List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                for (int x = 0; x < arrayFrom.size(); x++) {
                    arrayFrom.get(x).setChecked(false);
                }
                ArrayList<Item> mainArrayList = new ArrayList<>(arrayFrom);
                item.setChilds(mainArrayList);
                List<?> arrays = (List<Item>) mainArrayList;
                item.addChildren((List<RecyclerViewItem>) arrays);
                item.setChilds(mainArrayList);
                for (int x = 0; x < item.getChilds().size(); x++) {
                    for (int y = 0; y < itemLists.size(); y++) {
                        if (item.getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                            itemLists.set(y, item.getChilds().get(x));
                        }
                    }
                }

                if (item.getChildren() != null && item.getChildren().size() > 0) {
                    List<?> arrayFromApiCastings = item.getChildren();
                    List<Item> arrayFroms = (List<Item>) arrayFromApiCastings;
                    for (int x = 0; x < arrayFroms.size(); x++) {
                        for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                            if (arrayFroms.get(x).getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                                mainArrayOfVehiclesList.remove(y);
                            }
                        }
                    }
                }
                int isCheckedState = 0;
                for (int y = 1; y < itemLists.size(); y++) {
                    if (itemLists.get(y).isChecked()) {
                        isCheckedState = isCheckedState + 1;
                    }
                }
                if (isCheckedState == itemLists.size()) {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(true);
                } else if (isCheckedState > 0) {
                    itemLists.get(0).setGroupChecked(true);
                    itemLists.get(0).setChecked(false);
                } else {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(false);
                }
                expandableAdapter.notifyDataSetChanged();
            } else if (clickState.equalsIgnoreCase("vehicle")) {
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        itemLists.set(y, item);
                    }
                }
//                if (item != null && item.getParent() == null) {
//                    Item recyclerViewItem =  getParentOfItem(item);
//                    if (recyclerViewItem != null) {
//                        item.setParent(recyclerViewItem);
//                    }
//                }
                boolean state = false;
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getParent() != null && item.getParent().getID() != null && item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        if (item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                            for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                for (int v = 0; v < itemLists.size(); v++) {
                                    if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(v).getID())) {
                                        if (itemLists.get(v).isChecked()) {
                                            state = true;
                                            if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                itemLists.get(y).setChecked(false);
                                                itemLists.get(y).setGroupChecked(true);
                                            }
                                        } else {
                                            if (!state) {
                                                if (x == item.getParent().getChilds().size() - 1) {
                                                    if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                        itemLists.get(y).setChecked(false);
                                                        itemLists.get(y).setGroupChecked(false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int isCheckedState = 0;
                for (int y = 1; y < itemLists.size(); y++) {
                    if (itemLists.get(y).isChecked()) {
                        isCheckedState = isCheckedState + 1;
                    }
                }
                if (isCheckedState == itemLists.size()) {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(true);
                } else if (isCheckedState > 0) {
                    itemLists.get(0).setGroupChecked(true);
                    itemLists.get(0).setChecked(false);
                } else {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(false);
                }
                for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                    if (item != null)
                        if (item.getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                            mainArrayOfVehiclesList.remove(y);
                        }
                }

                if (item != null && item.getParent() != null) { // add the selected car with all its new data like selected and checked to ites parent by replasing its child
                    for (int y = 0; y < itemLists.size(); y++) {
                        if (item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                            if (itemLists.get(y) != null && itemLists.get(y).getChildren() != null && itemLists.get(y).getChildren().size() > 0) {
                                for (int x = 0; x < itemLists.get(y).getChildren().size(); x++) {
                                    List<?> array = itemLists.get(y).getChildren();
                                    List<Item> arrayItm = (List<Item>) array;
                                    if (item.getID().equalsIgnoreCase(arrayItm.get(x).getID())) {
                                        arrayItm.set(x, item);
                                    }
                                }
                            }
                        }
                    }
                }


                expandableAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void isCheckedValueAll(Item item, List<Item> itemLists) {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                if (item.isExpanded()) {
                    try {
                        //
                        Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                        List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                        for (int x = 0; x < arrayFromApi.size(); x++) {
                            for (int y = 0; y < itemLists.size(); y++) {
                                String vehicle = itemLists.get(y).getID();
                                int vehicleId = arrayFromApi.get(x).getVehicleID();
                                String firstOne = vehicle.substring(0, 1);
                                if (firstOne.equalsIgnoreCase("V")) {
                                    vehicle = vehicle.substring(2, vehicle.length());
                                    if (Integer.valueOf(vehicle) == vehicleId) {
                                        arrayFromApi.get(x).setLevel(itemLists.get(y).getLevel());
                                        arrayFromApi.get(x).setChecked(itemLists.get(y).isChecked());
                                        arrayFromApi.get(x).setClicked(itemLists.get(y).isClicked());
                                        arrayFromApi.get(x).setID(itemLists.get(y).getID());
                                        arrayFromApi.get(x).setName(itemLists.get(y).getName());
                                    }
                                }
                            }
                        }


                        for (int x = 0; x < arrayFromApi.size(); x++) {
                            for (int y = 0; y < itemLists.size(); y++) {
                                String vehicle = itemLists.get(y).getID();
                                int vehicleId = arrayFromApi.get(x).getVehicleID();
                                String firstOne = vehicle.substring(0, 1);
                                if (firstOne.equalsIgnoreCase("V")) {
                                    vehicle = vehicle.substring(2, vehicle.length());
                                    if (Integer.valueOf(vehicle) == vehicleId) {
                                        if (itemLists.get(y).getParent() != null) {
                                            arrayFromApi.get(x).setParent(itemLists.get(y).getParent());
                                        }

                                        Item items = arrayFromApi.get(x);
                                        itemLists.set(y, items);
                                    }
                                } else {
                                    if (itemLists.get(y).getChildren() != null) {
                                        List<?> array = itemLists.get(y).getChildren();
                                        ArrayList<?> arrayList = new ArrayList<>(array);
                                        Item items = itemLists.get(y);
                                        items.setChilds((ArrayList<Item>) arrayList);
                                        itemLists.set(y, items);
                                    }
                                }
                            }
                        }

                        for (int x = 0; x < itemLists.size(); x++) {
                            itemLists.get(x).setGroupChecked(false);
                            itemLists.get(x).setChecked(true);
                        }

                        List<?> newRepositoryArray = itemLists.get(0).getChildren();
                        List<Item> arrayList = (List<Item>) newRepositoryArray; // get all grupes
                        for (int x = 0; x < arrayList.size(); x++) {
                            List<?> children = arrayList.get(x).getChildren(); // get childe of the grupe
                            List<Item> mainChildren = (List<Item>) children;
                            if (mainChildren != null && mainChildren.size() > 0) {
                                for (int y = 0; y < mainChildren.size(); y++) {
                                    for (int z = 0; z < itemLists.size(); z++) {
                                        String vehicle = itemLists.get(z).getID();
                                        String vehicleId = mainChildren.get(y).getID();
                                        if (vehicle.equalsIgnoreCase(vehicleId)) {
                                            mainChildren.set(y, itemLists.get(z));
                                        }
                                    }
                                }
                            }
                        }
                        //
                        if (mainArrayOfVehiclesList.size() > 0)
                            mainArrayOfVehiclesList.clear();
                        mainArrayOfVehiclesList.addAll(arrayFromApi);
                        expandableAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                        List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                        if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
                            itemArrayListCallas = new ArrayList<>();
                            getLength(itemLists.get(0).getChildren(), true, false);
                            if (mainArrayOfVehiclesList.size() > 0) {
                                mainArrayOfVehiclesList.clear();
//                            mainArrayOfVehiclesList.addAll(arrayFromApi);
                            }
                            if (arrayFromApi.size() == itemArrayListCallas.size()) {
                                for (int x = 0; x < arrayFromApi.size(); x++) {
                                    Item mainItem = itemArrayListCallas.get(x);
                                    Item mainItemStyle = arrayFromApi.get(x);
                                    if (mainItem != null && mainItem.getID() != null)
                                        mainItemStyle.setID(mainItem.getID());
                                    if (mainItem != null && mainItem.getName() != null)
                                        mainItemStyle.setName(mainItem.getName());
                                    if (mainItem != null && mainItem.getParent() != null)
                                        mainItemStyle.setParent(mainItem.getParent());
                                    if (mainItem != null)
                                        mainItemStyle.setLevel(mainItem.getLevel());
                                    if (mainItem != null)
                                        mainItemStyle.setPosition(mainItem.getPosition());
                                    if (mainItem != null)
                                        mainItemStyle.setExpanded(mainItem.isExpanded());
                                    if (mainItem != null)
                                        mainItemStyle.setClicked(mainItem.isClicked());
                                    if (mainItem != null)
                                        mainItemStyle.setChecked(mainItem.isChecked());
                                    arrayFromApi.set(x, mainItemStyle);
                                    mainArrayOfVehiclesList.add(arrayFromApi.get(x));
                                }
                                Log.e("s", "s");
                            } else {
                                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                try {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getLength(List<RecyclerViewItem> items, boolean isChecked, boolean isGroupChecked) {
        try {
            for (int y = 0; y < items.size(); y++) {
                if (items.get(y).getChildren() != null && items.get(y).getChildren().size() > 0) {
                    getLength(items.get(y).getChildren(), isChecked, isGroupChecked);
                }
                List<?> newRepositoryArray = items;
                List<Item> arrayList = (List<Item>) newRepositoryArray;
                arrayList.get(y).setChecked(isChecked);
                arrayList.get(y).setGroupChecked(isGroupChecked);
                List<?> newArray = arrayList.get(y).getChildren();
                if (newArray != null && newArray.size() > 0) {
                    itemArrayListCallas.addAll((List<Item>) newArray);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void isCheckedValue(Item item, List<Item> itemLists, int position, String clickState) {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                try {
                    //
                    Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                    List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        arrayFromApi.get(x).setLevel(item.getLevel());
                    }

//                    if (item.getChildren() != null && item.getChildren().size() > 0) {
//                        for (int x = 0; x < item.getChildren().size(); x++) {
//                            String firstOne = item.getID().substring(0, 1);
//                            if (firstOne.equalsIgnoreCase("G")) {
//                                Item itemMain = (Item) item.getChildren().get(x);
//                                String firstOneValue = itemMain.getID().substring(0, 1);
//                                if (firstOneValue.equalsIgnoreCase("G")) {
//                                    int position = 0;
//                                    for (int c = 0; c < itemLists.size(); c++) {
//                                        if (itemMain.getID().equalsIgnoreCase(itemLists.get(c).getID())) {
//                                            position = c;
//                                        }
//                                    }
//                                    isCheckedValue(itemMain, itemLists, position, "grope");
//                                }
//                            }
//                        }
//                    }

                    //
                    Item mainItemsList = itemLists.get(position);
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {
                        List<?> arrayFromApiCasting = mainItemsList.getChildren();
                        List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                        if (arrayFrom.size() == arrayFromApi.size()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setName(arrayFrom.get(x).getName());
                                arrayFromApi.get(x).setID(arrayFrom.get(x).getID());
                                arrayFromApi.get(x).setChecked(arrayFrom.get(x).isChecked());
                                arrayFromApi.get(x).setClicked(arrayFrom.get(x).isClicked());
                                arrayFromApi.get(x).setParent(item);
//                                List<?> array = item.getChildren();
//                                arrayFromApi.get(x).setChilds((ArrayList<Item>) array);
//                                arrayFromApi.get(x).addChildren((List<RecyclerViewItem>) array);
                            }
                        }
                    } else if (mainItemsList != null) {
                        if (clickState.equalsIgnoreCase("vehicle")) {
                            if (arrayFromApi.size() > 0) {
                                arrayFromApi.get(0).setName(mainItemsList.getName());
                                arrayFromApi.get(0).setID(mainItemsList.getID());
                                arrayFromApi.get(0).setChecked(mainItemsList.isChecked());
                                arrayFromApi.get(0).setClicked(mainItemsList.isClicked());
                            }
                        }
                    }
                    if (clickState.equalsIgnoreCase("grope")) {
                        // state = "grope";
                        if (item.isChecked()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(true);
                            }
                        } else {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(false);
                            }
                        }
                    } else {
                        if (arrayFromApi.size() > 0)
                            if (item.isChecked()) {
                                arrayFromApi.get(0).setChecked(true);
                            } else {
                                arrayFromApi.get(0).setChecked(false);
                                item.setGroupChecked(true);
                            }
                    }
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {
                        List<?> newRepositoryArrayListFromChildrenToCHilde = arrayFromApi;
                        mainItemsList.addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                        ArrayList<Item> arrayList = new ArrayList<>(arrayFromApi);
                        mainItemsList.setChilds(arrayList);
                        itemLists.get(position).setChilds(arrayList);
                        itemLists.get(position).addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        for (int y = 0; y < itemLists.size(); y++) {
                            if (arrayFromApi.get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                if (itemLists.get(y).getParent() != null) {
                                    arrayFromApi.get(x).setParent(itemLists.get(y).getParent());
                                }
                                itemLists.set(y, arrayFromApi.get(x));
                            }
                        }
                    }

                    // sime check
                    if (clickState.equalsIgnoreCase("vehicle")) {
//                        if (item.getParent() == null) {
//                            Item recyclerViewItem = getParentOfItem(item);
//                            if (recyclerViewItem != null) {
//                                item.setParent(recyclerViewItem);
//                            }
//                        }
                        if (item.getParent() != null) {
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    if (item.getParent() != null && item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                                        int isCheckedState = 0;
                                        for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                            for (int z = 0; z < itemLists.size(); z++) {
                                                if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(z).getID())) {
                                                    if (itemLists.get(z).isChecked()) {
                                                        isCheckedState = isCheckedState + 1;
                                                    }
                                                }

                                            }
                                        }
                                        if (isCheckedState == item.getParent().getChilds().size()) {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(true);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(true);
                                                }
                                            }
                                        } else if (isCheckedState > 0) {
                                            item.getParent().setGroupChecked(true);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(true);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        } else {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else if (clickState.equalsIgnoreCase("grope")) {
                        if (item.isGroupChecked()) {
                            item.setGroupChecked(false);
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    itemLists.get(y).setGroupChecked(false);
                                }
                            }
                        }
                    }
                    boolean state = false;
                    for (int y = 1; y < itemLists.size(); y++) {
                        if (itemLists.get(y).isChecked()) {
                            state = true;
                        } else {
                            state = false;
                            break;
                        }
                    }
                    if (state) {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(true);
                    } else {
                        itemLists.get(0).setGroupChecked(true);
                        itemLists.get(0).setChecked(false);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        if (arrayFromApi.get(x).isChecked()) {
                            String firstOne = arrayFromApi.get(x).getID().substring(0, 1);
                            if (firstOne.equalsIgnoreCase("V")) {
                                mainArrayOfVehiclesList.add(arrayFromApi.get(x));
                            }
                        }
                    }
                    expandableAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                try {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private List<?> nestedLoop(JSONObject levelList, int level) {
        try {
            Item parentItems = null;
            List<RecyclerViewItem> itemList = new ArrayList<>();
            if (levelList != null)
                parentItems = new Gson().fromJson(String.valueOf(levelList), Item.class);
            JSONArray jsonArrayStringList = levelList.getJSONArray("Childs");
            int length = jsonArrayStringList.length();
            for (int i = 0; i < length; i++) {
                JSONObject itemObject = jsonArrayStringList.getJSONObject(i);
                if (itemObject.has("Childs") && !itemObject.isNull("Childs")) {
                    Item items = new Item(level);
                    int childrenSize = itemObject.getJSONArray("Childs").length();
                    if (childrenSize > 0) {
                        level = level + 1;
                        items.addChildren((List<RecyclerViewItem>) nestedLoop(itemObject, level));
                    }
                    items.setName(itemObject.getString("Name"));
                    items.setID(itemObject.getString("ID"));
                    items.setVehicleStatus(itemObject.getString("VehicleStatus"));
                    items.setClicked(true);
                    items.setParent(parentItems);
                    itemList.add(items);
                } else {
                    Item items = new Item(level);
                    items.setName(itemObject.getString("Name"));
                    items.setID(itemObject.getString("ID"));
                    items.setVehicleStatus(itemObject.getString("VehicleStatus"));
                    items.setClicked(true);
                    items.setParent(parentItems);
                    itemList.add(items);
                }
            }
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setMapStyleDialog() {
        myMapStyleManager = new MyMapStyleManager(fragmentActivity, googleMap);
        myMapStyleManager.setTargetFragment(this);
        myMapStyleManager.setSelectedStyle(PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_ID));
    }

    private void openMapStyleDialog() {
        if (myMapStyleManager != null)
            myMapStyleManager.showDialog();
    }

    @Override
    public void onSelectMapStyle(int selectedItemId) {
        if (myMapStyleManager != null) {
            myMapStyleManager.setSelectedStyle(selectedItemId);
        }
    }

    private void setSlideUpBuilder() {
        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        sliderBgLayout.setAlpha(1 - (percent / 100));
                        float rotateDegree = (percent / 100) * 180;
                        AnimationUtils.rotateAnimation(rotateDegree, sliderUpArrowImageView);
                        if (percent == 100) {
                            sliderBgLayout.setVisibility(View.GONE);
//                            mapStylingFab.show(true);
                        } else {
                            sliderBgLayout.setVisibility(View.VISIBLE);
//                            mapStylingFab.hide(true);
                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(moreOptionsLayout)
                .build();
    }

    private void viewSelected(boolean isSingle) {
        if (isSingle) {
            singleCarMoreOptionsLayout.setVisibility(View.VISIBLE);
            moreOptionsLayout.setVisibility(View.GONE);
        } else {
            singleCarMoreOptionsLayout.setVisibility(View.GONE);
            moreOptionsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setSingleSlideUpBuilder() {
        try {
            slideUpSingleCar = new SlideUpBuilder(singleSlideView)
                    .withListeners(new SlideUp.Listener.Events() {
                        @Override
                        public void onSlide(float percent) {
                            sliderBgLayout.setAlpha(1 - (percent / 100));
                            float rotateDegree = (percent / 100) * 180;
                            AnimationUtils.rotateAnimation(rotateDegree, singleSliderUpArrowImageView);
                            if (percent == 100) {
                                sliderBgLayout.setVisibility(View.GONE);
                            } else {
                                sliderBgLayout.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onVisibilityChanged(int visibility) {
                        }
                    })
                    .withStartGravity(Gravity.BOTTOM)
                    .withLoggingEnabled(true)
                    .withGesturesEnabled(true)
                    .withStartState(SlideUp.State.HIDDEN)
                    .withSlideFromOtherView(singleCarMoreOptionsLayout)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addHeaderTitle(AllVehiclesInHashModel markerModel) {
        try {
            if (markerModel != null) {
                if (markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLabel() != null)
                    singleCarSliderTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLabel()));
                singleSliderCarSliderTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLabel() != null ? markerModel.getAllVehicleModel().getLabel() : ""));
                if (markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getAddress() != null) {
                    singleCarSliderAddressTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress()));
                    singleSliderUpTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress()));
                    singleSliderUpTitleTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastHelper.toastMessage(activity, String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress()));
                        }
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void addSlideUpFragment() {
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            slideUpFragment = slideUpFragment.newInstance();
            transaction.replace(R.id.sliderContainer, slideUpFragment).commit();
        }
    }

    @Override
    public void onClickShow(boolean isShowCliched, boolean isAddClicked, int itemId) {
        switch (itemId) {
            case 0: // locate
                setLocateManager();
                break;

            case 1: //clustering
                markerClusterVisibility = isShowCliched;
                setClusterManager(isShowCliched, isAddClicked, itemId);
                break;

            case 2: // traffic
                setMapTraffic(isShowCliched);
                break;

            case 3: // landmark
                setLandmarkManager(isShowCliched, isAddClicked, itemId);
                break;

            case 4: // geo_fence
                setGeoFenceManager(isShowCliched, isAddClicked, itemId);
                break;
        }
    }

    private void setLocateManager() {
        googleMap.clear();
        if (myLocateManager == null) {
            myLocateManager = new MyLocateManager(context, rootView, googleMap, vehiclesHashMap, myCurrentLatLng);
            myLocateManager.setViews(mMapView);
            myLocateManager.setAfterOnDismissListeners(new Click() {
                @Override
                public void onClick() {
                    setVisibilityVehiclesMarkers(true);
                }

                @Override
                public void addMaps() {
                    if (googleMap != null) {
                        openFirstTime = true;
                        addVehiclesMarkers(false);
                        reLocate();
                    }
                }
            });
            myLocateManager.setLandMarkListeners(new MyLocateManager.LandMarkCheck() {
                @Override
                public void landMark() {
                    if (!PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                        addAndHideViews(-1, true);
                    } else {
                        addAndHideViewsAfterCluster(-1, true);
                    }
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showLandMark(true);
                    }
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showGeofence(true);
                    }
//                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
//                        slideUpFragment.showGeofence(true);
//                    }
                }
            });
        } else {
            myLocateManager.removeViewPopup();
            myLocateManager = null;
            myLocateManager = new MyLocateManager(context, rootView, googleMap, vehiclesHashMap, myCurrentLatLng);
            myLocateManager.setViews(mMapView);
            myLocateManager.setAfterOnDismissListeners(new Click() {
                @Override
                public void onClick() {
                    setVisibilityVehiclesMarkers(true);
                }

                @Override
                public void addMaps() {
                    if (googleMap != null) {
                        openFirstTime = true;
                        addVehiclesMarkers(false);
                        reLocate();
                    }
                }
            });
        }
        slideUp.hide();
        slideUpSingleCar.hide();
    }

    private void reLocate() {
        try {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setVisibilityAllMarkers(boolean show) {
        setVisibilityGeoFences(show);
        setVisibilityLandmarks(show);
    }

    private void setVisibilityGeoFences(boolean show) {
        if (myGeoFenceManager != null) {
            myGeoFenceManager.setCirclesVisibility(show);
        }
    }

    private void setVisibilityLandmarks(boolean show) {
        if (myLandmarkManager != null) {
            myLandmarkManager.setMarkersVisibility(show);
        }
    }

    private void setClusterManager(boolean isShowCliched, boolean isAddClicked, int itemId) {
        resetMapIfLocateOpenCluster();
        if (isShowCliched) {
            vehiclesClusterManager = new VehiclesClusterManager(context, googleMap, vehiclesHashMap);
            setMarkersVisibility(false);
            vehiclesClusterManager.startVehiclesClustering();
        } else {
            if (vehiclesClusterManager != null) {
                setMarkersVisibility(true);
                vehiclesClusterManager.removeVehiclesCluster();
                vehiclesClusterManager = null;
            }
            if (!isAddClicked)
                animateCameraAfterCluster();
        }
        slideUp.hide();
        slideUpSingleCar.hide();
    }

    private void setMapTraffic(boolean isShowCliched) {
        googleMap.setTrafficEnabled(isShowCliched);
        if (slideUp.isVisible()) {
            slideUp.hide();
            slideUpSingleCar.hide();
        } else {
            slideUp.show();
        }
    }

    private void setLandmarkManager(boolean isShowCliched, boolean isAddClicked, int itemId) {
        resetMapIfLocateOpen(isShowCliched, isAddClicked, itemId);
        myLandmarkManager = new MyLandmarkManager(activity, googleMap, myCurrentLatLng);
        myLandmarkManager.setTargetFragment(this);
        myLandmarkManager.setViews(isShowCliched, isAddClicked);
    }

    @Override
    public void onAddLandmark(LatLng geoLatLng, int landmarkId, String poiName) {
        if (myLandmarkManager != null)
            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                myLandmarkManager.afterSuccessAddLandMarkerMarker(geoLatLng, landmarkId, poiName);
                myLandmarkManager.newLandMarkerItemToList();
            }
    }

    private void setGeoFenceManager(boolean isShowCliched, boolean isAddClicked, int itemId) {
        resetMapIfLocateOpen(isShowCliched, isAddClicked, itemId);
        myGeoFenceManager = new MyGeoFenceManager(activity, googleMap, myCurrentLatLng, listOfVehiclesForGeoFence);
        myGeoFenceManager.setTargetFragment(this);
        myGeoFenceManager.setViews(isShowCliched, isAddClicked);
    }

    @Override
    public void onAddGeoFence(LatLng geoLatLng, String radius) {
        if (myGeoFenceManager != null)
            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU))
                myGeoFenceManager.addGeoFenceCircleOnMap(geoLatLng, Integer.parseInt(radius));
    }

    private void resetMapIfLocateOpenCluster() {
        if (myLocateManager != null) {
            myLocateManager.removeViewPopup();
            myLocateManager = null;
        }
        slideUp.hide();
    }

    private void resetMapIfLocateOpen(boolean isShowCliched, boolean isAddClicked, int itemId) {
        if (isShowCliched) {
            if (myLocateManager != null) {
                myLocateManager.removeViewPopup();
                myLocateManager = null;
//                setVisibilityVehiclesMarkers(true);
//                setVisibilityAllMarkers(true);
            }
        } else {
            if (!isAddClicked) {
                if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU) && itemId == 4) {
                    if (myGeoFenceManager != null) {
                        myGeoFenceManager.removeCircleFromMap();
                    }
                } else if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU) && itemId == 3) {
                    if (myLandmarkManager != null) {
                        myLandmarkManager.removeLandMarkerFromMap();
                    }
                } else {
                    setVisibilityVehiclesMarkers(false);
                    setVisibilityAllMarkers(false);
                }
            }
        }

        if (isAddClicked) {
            if (myLocateManager != null) {
                myLocateManager.removeViewPopup();
                myLocateManager = null;
                setVisibilityVehiclesMarkers(true);
                setVisibilityAllMarkers(true);
            }
        }
        slideUp.hide();
    }

    public void setMarkersVisibility(boolean visibility) {
        for (LinkedHashMap.Entry<Marker, AllVehiclesInHashModel> mapEntry : vehiclesHashMap.entrySet()) {
            AllVehiclesInHashModel vehiclesInHashModel = mapEntry.getValue();
            vehiclesInHashModel.getMarker().setVisible(visibility);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        setMapStyleDialog();
        addVehiclesMarkers(false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestLocationUpdates(0L, 0.0f, criteria, this, null);

        googleMap.setLocationSource(this);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setTrafficEnabled(false);
        googleMap.setOnMapClickListener(latLng -> {
            if (!PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                addAndHideViews(-1, true);
            } else {
                addAndHideViewsAfterCluster(-1, true);
            }
            bottomViewVisibility();


            //setClusterManager(markerClusterVisibility, true, 1);
//            if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
//                slideUpFragment.showLandMark(true);
//            }
//            if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
//                slideUpFragment.showGeofence(true);
//            }
        });
        new Handler().postDelayed(() -> {
            startSignalRSerivce();
            setLocationIfNeeded();
        }, 1000);
    }


    private void bottomViewVisibility() {
        try {
            if (singleCarMoreOptionsLayout.getVisibility() == View.VISIBLE) {
                viewSelected(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addVehiclesMarkers(boolean addCarsOnMap) {
        try {
            if (openFirstTime) {
                if (Utils.isNotEmptyList(vehiclesList))
                    vehiclesHashMap = new LinkedHashMap<>();
                for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                    AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
                    if (allVehicleModel.getVehicleID() != null)
                        inHashModel.setVehicleId(allVehicleModel.getVehicleID());
                    inHashModel.setAllVehicleModel(allVehicleModel);
                    LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());
                    Marker addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                            .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                            .anchor(0.5f, 0.5f)
                            .rotation((float) allVehicleModel.getLastLocation().getDirection())
                            .flat(true));
                    addMarker.setTag(allVehicleModel);
                    inHashModel.setMarker(addMarker);
                    vehiclesHashMap.put(addMarker, inHashModel);// note size is zero
                    builder.include(lng);
                }
                googleMap.setOnMarkerClickListener(marker -> {
                    try {
                        if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                            AllVehiclesInHashModel markerModel = vehiclesHashMap.get(marker);
                            if (markerModel != null) {
                                addHeaderTitle(markerModel);
                                addBodyView(markerModel);
                                viewSelected(true);
                                addAndHideViews(markerModel.getVehicleId(), false);
                            } else {
                                LatLng latLng = marker.getPosition();
                                List<AllVehiclesInHashModel> allVehiclesInHashModels = new ArrayList<AllVehiclesInHashModel>(vehiclesHashMap.values());
                                for (AllVehiclesInHashModel allVehiclesInHash : allVehiclesInHashModels) {
                                    if (latLng != null && allVehiclesInHash != null && allVehiclesInHash.getAllVehicleModel() != null && allVehiclesInHash.getAllVehicleModel().getLastLocation() != null)
                                        if (latLng.latitude == allVehiclesInHash.getAllVehicleModel().getLastLocation().getLatitude()) {
                                            addHeaderTitle(allVehiclesInHash);
                                            addBodyView(allVehiclesInHash);
                                            viewSelected(true);
                                            addAndHideViewsAfterCluster(allVehiclesInHash.getVehicleId(), false);
                                        }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                });
                if (addCarsOnMap) {
                    if (googleMap != null && vehiclesList != null && vehiclesList.size() > 0) {
                        if (vehiclesList.get(0) != null && vehiclesList.get(0).getLastLocation() != null) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(vehiclesList.get(0).getLastLocation().getLatitude(), vehiclesList.get(0).getLastLocation().getLongitude()), 8.0f));
                        }
                    }
                }
//                if (builder != null)
//                bounds = builder.build();
                openFirstTime = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAndHideViewsAfterCluster(int vehicleId, boolean returnAllWithoutFade) {
        try {
            Marker addMarker;
            googleMap.clear();
            if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                slideUpFragment.showLandMark(true);
            }
            if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
                slideUpFragment.showGeofence(true);
            }
            if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                for (Marker key : vehiclesHashMap.keySet()) {
                    key.remove();
                }
                vehiclesHashMap.clear();
                if (vehiclesClusterManager != null) {
                    vehiclesClusterManager.removeVehiclesCluster();
                    vehiclesClusterManager = null;
                }
                for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                    AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
                    inHashModel.setVehicleId(allVehicleModel.getVehicleID());
                    inHashModel.setAllVehicleModel(allVehicleModel);
                    LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());
                    if (!returnAllWithoutFade) {
                        if (vehicleId != allVehicleModel.getVehicleID()) {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIconAlpha(allVehicleModel.getLastLocation().getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                    .flat(true));
                            inHashModel.setFaded(true);
                        } else {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                    .flat(true));
                            inHashModel.setFaded(false);
                        }
                    } else {// if map clicked here
                        addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                                .anchor(0.5f, 0.5f)
                                .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                .flat(true));
                        inHashModel.setFaded(false);
                    }
                    inHashModel.setMarker(addMarker);
                    vehiclesHashMap.put(addMarker, inHashModel); // adding here
                    builder.include(lng);
                }
                // add cluster
                if (slideUpFragment != null)
                    slideUpFragment.notifyAdapterItemOneCluster(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void addAndHideViews(int vehicleId, boolean returnAllWithoutFade) {
        try {
            Marker addMarker;
            if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                for (Marker key : vehiclesHashMap.keySet()) {
                    key.remove();
                }
                vehiclesHashMap.clear();
                for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                    AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
                    inHashModel.setVehicleId(allVehicleModel.getVehicleID());
                    inHashModel.setAllVehicleModel(allVehicleModel);
                    LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());
                    if (!returnAllWithoutFade) {
                        if (vehicleId != allVehicleModel.getVehicleID()) {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIconAlpha(allVehicleModel.getLastLocation().getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                    .flat(true));
                        } else {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                    .flat(true));
                        }
                    } else {// if map clicked here
                        addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                                .anchor(0.5f, 0.5f)
                                .rotation((float) allVehicleModel.getLastLocation().getDirection())
                                .flat(true));
                    }
                    inHashModel.setMarker(addMarker);
                    vehiclesHashMap.put(addMarker, inHashModel); // adding here
                    builder.include(lng);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void addBodyView(AllVehiclesInHashModel markerModel) {
        try {
            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime() != null)
                timeTextView.setText(String.format(Locale.getDefault(), "%s", Utils.parseTime(markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime())));
            else
                timeTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                kmTextView.setText(String.format(Locale.getDefault(), "%s %s", markerModel.getAllVehicleModel().getLastLocation().getSpeed(), context.getString(R.string.km_h)));
            else
                kmTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null) {
                double value = markerModel.getAllVehicleModel().getLastLocation().getDirection() % 360;
                timerTextView.setText(String.format(Locale.getDefault(), "%s", value)); // fraction
            } else {
                timerTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));
            }

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus() != null)
                infoTextView.setText(String.format(Locale.getDefault(), "%s", AppUtils.getCarStatus(getActivity(), markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus()))); //
            else
                infoTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getEngineStatus() != null)
                offTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getEngineStatus() ? context.getString(R.string.on) : context.getString(R.string.off)));// engen statuds
            else
                offTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.off)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getPlateNumber() != null)
                visaTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getPlateNumber()));
            else
                visaTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getSerial() != null)
                barCodeTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getSerial()));
            else
                barCodeTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getDoorStatus() != null)
                closedTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getDoorStatus()));
            else
                closedTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getSimCardNumber() != null)
                cardTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getSimCardNumber())); // sim card view
            else
                cardTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getFuel() != null)
                gasTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getFuel()));
            else
                gasTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getSeatBeltStatus() != null)
                beltTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getSeatBeltStatus()));
            else
                beltTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getTemper() != null)
                needlTextView.setText(String.format(Locale.getDefault(), "%s", (markerModel.getAllVehicleModel().getLastLocation().getTemper().equalsIgnoreCase("0") || markerModel.getAllVehicleModel().getLastLocation().getTemper().equalsIgnoreCase("0.0")) ? context.getString(R.string.n_a) : markerModel.getAllVehicleModel().getLastLocation().getTemper()));
            else
                needlTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getDriverName() != null)
                nATextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getDriverName()));
            else
                nATextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getGroupName() != null)
                humanTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getGroupName()));
            else
                humanTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getAddress() != null)
                addressTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress())); //
            else
                addressTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getMileage() != null) {
                Double value = Double.valueOf(markerModel.getAllVehicleModel().getLastLocation().getMileage());
                mileageTextView.setText(String.format(Locale.getDefault(), "%s %.2f %s", context.getString(R.string.mileage), value, context.getString(R.string.km)));
                mileageTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastHelper.toastMessage(activity, String.format(Locale.getDefault(), "%s %s %s", context.getString(R.string.mileage), markerModel.getAllVehicleModel().getLastLocation().getMileage(), context.getString(R.string.km)));
                    }
                });
            } else if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                mileageTextView.setText(String.format(Locale.getDefault(), "%s %s %s", context.getString(R.string.mileage), markerModel.getAllVehicleModel().getLastLocation().getTotalMileage(), context.getString(R.string.km)));
            else
                mileageTextView.setText(String.format(Locale.getDefault(), "%s %s %s", context.getString(R.string.mileage), "0.00", context.getString(R.string.km)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getWorkingHours() != null) {
                Double valueCalc = Double.valueOf(markerModel.getAllVehicleModel().getLastLocation().getWorkingHours()) / 3600;
//                Long longValueCalc = Math.round(valueCalc);
                workingTextView.setText(String.format(Locale.getDefault(), "%s %.2f", context.getString(R.string.working_hours), Double.valueOf(valueCalc)));
                workingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastHelper.toastMessage(activity, String.format(Locale.getDefault(), "%s %s", context.getString(R.string.working_hours), valueCalc));
                    }
                });
            } else
                workingTextView.setText(String.format(Locale.getDefault(), "%s %s", context.getString(R.string.working_hours), "0.00"));

            if (markerModel != null)
                PreferencesManager.getInstance().setIntegerValue(markerModel.getVehicleId(), SharesPrefConstants.LAST_VIEW_VEHICLE_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setLocationIfNeeded() {
        //Setting the width and height of your screen
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        double random = Utils.getRandomNumber(0.28, 0.22);
//        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        int padding = (int) (width * random);

//        try {
//            //        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Utils.getRandomNumber(115, 105));
//            if (googleMap != null) {
//                CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
//                googleMap.animateCamera(cu);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void animateCameraAfterCluster() {
        try {
            if (isRandomZoomChanged) {
                randomZoom = (int) (googleMap.getCameraPosition().zoom - 2);
                isRandomZoomChanged = false;
            } else {
                randomZoom = (int) (googleMap.getCameraPosition().zoom + 2);
                isRandomZoomChanged = true;
            }
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(randomZoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVisibilityVehiclesMarkers(boolean show) {
        for (LinkedHashMap.Entry<Marker, AllVehiclesInHashModel> mapEntry : vehiclesHashMap.entrySet()) {
            mapEntry.getValue().getMarker().setVisible(show);
        }
    }

    private void animateCamera(LatLng locationCamera) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(locationCamera).zoom(AppConstant.ZOOM_VALUE_18).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mapLocationListener != null) {
            mapLocationListener.onLocationChanged(location);
            myCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            AlertDialogView.showGPSDisabledDialog(activity);
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        this.mapLocationListener = listener;
    }

    @Override
    public void deactivate() {
        this.mapLocationListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstant.GPS_ENABLE_REQUEST) {
            if (locationManager == null) {
                locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            }

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                openGpsLocation();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void markStartingLocationOnMap(final SignalRModel signalRModel) {
        SignalRModel.A aModel = signalRModel.getA().get(0);
        LatLng newLocation = new LatLng(aModel.getLatitude(), aModel.getLongitude());
        LogHelper.LOG_E("newLocation >>>> ", "Lat: " + newLocation.latitude + " Long: "
                + newLocation.longitude
                + "\nDirection: " + aModel.getDirection()
                + "\nVehicle ID: " + aModel.getVehicleID());

//        updateMarker(aModel);
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                googleMap.clear();
//                googleMap.addMarker(new MarkerOptions().position(newLocation)
//                        .icon(AppUtils.getCarIcon(aModel.getVehicleStatusText()))
//                        .anchor(0.5f, 0.5f)
//                        .rotation(aModel.getDirection().floatValue())
//                        .flat(true));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
//            }
//        });
    }

    private void updateMarker(SignalRModel.A aModel) {
        for (LinkedHashMap.Entry<Marker, AllVehiclesInHashModel> mapEntry : vehiclesHashMap.entrySet()) {
            AllVehiclesInHashModel vehiclesInHashModel = mapEntry.getValue();

            if (vehiclesInHashModel.getVehicleId() == aModel.getVehicleID()) {
                AllVehiclesInHashModel.AllVehicleModel allVehicleModel = vehiclesInHashModel.getAllVehicleModel();
                LatLng lng = new LatLng(aModel.getLatitude(), aModel.getLongitude());
//                Marker m = googleMap.addMarker(new MarkerOptions().position(lng)
//                        .icon(AppUtils.getCarIcon(String.valueOf(aModel.getVehicleStatus())))
//                        .anchor(0.5f, 0.5f)
//                        .rotation(aModel.getDirection().floatValue())
//                        .flat(true));

//                addMarker.setIcon(AppUtils.getCarIcon(String.valueOf(aModel.getVehicleStatus())));
//                addMarker.setAnchor(0.5f, 0.5f);
//                addMarker.setRotation(aModel.getDirection().floatValue());
//                addMarker.setFlat(true);
//                addMarker.setPosition(lng);
//
//                animateCamera(lng);
//                vehiclesInHashModel.setMarker(addMarker);
                vehiclesInHashModel.setAllVehicleModel(allVehicleModel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    vehiclesHashMap.replace(mapEntry.getKey(), mapEntry.getValue(), vehiclesInHashModel);
                } else {
                    vehiclesHashMap.remove(mapEntry.getKey());
                    vehiclesHashMap.put(mapEntry.getKey(), vehiclesInHashModel);
                }
            }
        }
    }

    public void startSignalRSerivce() {
        if (mBound) {
//             Call a method from the SignalRService.
//             However, if this call were something that might hang, then this request should
//             occur in a separate thread to avoid slowing down the activity performance.
            mService.invokeService(new SignalR() {
                @Override
                public void onMessageReceived(SignalRModel signalRModel) {
                    markStartingLocationOnMap(signalRModel);
                }

                @Override
                public void onCommandReceived(SignalRCommandModel signalRCommandModel) {

                }
            });
        }
    }

    private void startSignalRServiceIntent() {
        Intent intent = new Intent();
        intent.setClass(context, SignalRService.class);
        activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopSignalRService() {
        // Unbind from the service
        if (mBound) {
            activity.unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onStop() {
        stopSignalRService();
        super.onStop();
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void openGpsLocation() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(activity, AppConstant.GPS_ENABLE_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }
}
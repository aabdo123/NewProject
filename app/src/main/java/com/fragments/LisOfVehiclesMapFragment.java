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
import android.support.v7.widget.LinearLayoutManager;
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

import com.R;
import com.adapters.ExpandableAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import com.google.gson.Gson;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
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
    private TextViewLight singleCarSliderTitleTextView;
    private TextViewLight singleSliderCarSliderTitleTextView;
    private TextViewRegular singleSliderUpTitleTextView;
    private TextViewRegular singleCarSliderAddressTextView;
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
    private String jsonStringList = "[{\"title\":\"Main\",\"children\":[{\"title\":\"List\",\"children\":[{\"title\":\"Extended Child 111\",\"children\":[{\"title\":\"Super Extended Child 1111\",\"children\":[{\"VehicleID\":1968,\"Label\":\"123456789012\",\"PlateNumber\":\"1234567891\",\"SerialNumber\":\"12431231\",\"LastLocation\":{\"VehicleID\":1968,\"Speed\":0,\"TotalMileage\":0,\"TotalWorkingHours\":0,\"Direction\":0,\"Latitude\":0,\"Longitude\":0,\"Address\":\"Vehicle not connected yet\",\"StreetSpeed\":0,\"VehicleStatus\":\"5\",\"RecordDateTime\":\"2018-10-14T09:44:42.1590868+00:00\",\"IsOnline\":false,\"EngineStatus\":false,\"RecordTime\":null,\"Status\":null}},{\"VehicleID\":1968,\"Label\":\"123456789012\",\"PlateNumber\":\"1234567891\",\"SerialNumber\":\"12431231\",\"LastLocation\":{\"VehicleID\":1968,\"Speed\":0,\"TotalMileage\":0,\"TotalWorkingHours\":0,\"Direction\":0,\"Latitude\":0,\"Longitude\":0,\"Address\":\"Vehicle not connected yet\",\"StreetSpeed\":0,\"VehicleStatus\":\"5\",\"RecordDateTime\":\"2018-10-14T09:44:42.1590868+00:00\",\"IsOnline\":false,\"EngineStatus\":false,\"RecordTime\":null,\"Status\":null}}]}]},{\"title\":\"Extended Child 112\",\"children\":[]},{\"title\":\"Extended Child 113\",\"children\":[]}]},{\"title\":\"Child 12\",\"children\":[{\"title\":\"Extended Child 121\",\"children\":[]},{\"title\":\"Extended Child 122\",\"children\":[]}]},{\"title\":\"Child 13\",\"children\":[]}]},{\"title\":\"Root 2\",\"children\":[{\"title\":\"Child 21\",\"children\":[{\"title\":\"Extended Child 211\",\"children\":[]},{\"title\":\"Extended Child 212\",\"children\":[]},{\"title\":\"Extended Child 213\",\"children\":[]}]},{\"title\":\"Child 22\",\"children\":[{\"title\":\"Extended Child 221\",\"children\":[]},{\"title\":\"Extended Child 222\",\"children\":[]}]},{\"title\":\"Child 23\",\"children\":[]}]},{\"title\":\"Root 1\",\"children\":[]}]";
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private LatLngBounds bounds;
    private boolean openFirstTime = true;
    private List<Item> mainChecks;

    private boolean isRandomZoomChanged = false;
    private int randomZoom;
//    private Marker addMarker;

    public LisOfVehiclesMapFragment() {
        // Required empty public constructor
    }

    public static LisOfVehiclesMapFragment newInstance(ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehicleModel, ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence) {
//        if (fragment == null) {
        fragment = new LisOfVehiclesMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_MODEL_ARGS, vehicleModel);
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
        context = getContext();
        activity = getActivity();
        fragmentActivity = getActivity();
        startSignalRServiceIntent();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            LogHelper.LOG_D("MAP", "getArgs");
            vehiclesList = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_MODEL_ARGS);
            listOfVehiclesForGeoFence = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS);
            if (Utils.isNotEmptyList(vehiclesList)) {
                firstCarLatLng = new LatLng(vehiclesList.get(0).getLastLocation().getLatitude(), vehiclesList.get(0).getLastLocation().getLongitude());
                myCurrentLatLng = firstCarLatLng;
            }
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
        singleCarSliderTitleTextView = (TextViewLight) rootView.findViewById(R.id.singleCarSliderTitleTextView);
        singleSliderCarSliderTitleTextView = (TextViewLight) rootView.findViewById(R.id.singleSliderCarSliderTitleTextView);
        singleCarSliderAddressTextView = (TextViewRegular) rootView.findViewById(R.id.singleCarSliderAddressTextView);
        kmTextView = (TextViewRegular) rootView.findViewById(R.id.kmTextView);
        singleSliderUpTitleTextView = (TextViewRegular) rootView.findViewById(R.id.singleSliderUpTitleTextView);
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
        expandable();
    }


    private void addCarsOnMap() {
        try {
            if (mainChecks != null && mainChecks.size() > 0) {
                List<Item> mainItemsFull = new ArrayList<>();
                for (Item item : mainChecks) {
                    if (item.getID() == null) {
                        mainItemsFull.add(item);
                    }
                }
                String request = new Gson().toJson(mainItemsFull);
                AllVehiclesInHashModel.AllVehicleModel.LastLocation[] vehicleModel = new Gson().fromJson(request, AllVehiclesInHashModel.AllVehicleModel.LastLocation[].class);
                List<AllVehiclesInHashModel.AllVehicleModel.LastLocation> arrayFromApi = Arrays.asList(vehicleModel);
                if (vehiclesList != null) {
                    if (vehiclesList.size() > 0) {
                        vehiclesList.clear();
                    }
                    if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                        vehiclesHashMap.clear();
                    }
                    for (AllVehiclesInHashModel.AllVehicleModel.LastLocation allVehicleModel : arrayFromApi) {
                        AllVehiclesInHashModel.AllVehicleModel allVehicleModel1 = new AllVehiclesInHashModel.AllVehicleModel();
                        allVehicleModel1.setVehicleID(allVehicleModel.getVehicleID());
                        allVehicleModel1.setLastLocation(allVehicleModel);
                        vehiclesList.add(allVehicleModel1);
                    }
                    openFirstTime = true;
                    if (googleMap != null)
                        googleMap.clear();
                    addVehiclesMarkers();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void expandable() {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesList(new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                try {
                    list = new ArrayList<>();
                    mainChecks = new ArrayList<>();
                    final List<Item> itemLists = (List<Item>) list;
                    JSONObject jsonObject = new JSONObject(responseObject);
                    nestedLoop(jsonObject, 0);
                    final android.app.Dialog dialogAndroidAppCus = new android.app.Dialog(context);
                    dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAndroidAppCus.setContentView(R.layout.view_expandable_dialog);
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
                    final MultiLevelRecyclerView multiLevelRecyclerView = (MultiLevelRecyclerView) dialogAndroidAppCus.findViewById(R.id.rv_list);
                    Progress.dismissLoadingDialog();
                    multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    final ExpandableAdapter expandableAdapter = new ExpandableAdapter(context, itemLists, multiLevelRecyclerView, new ExpandableAdapter.ActionsInterface() {
                        @Override
                        public void ItemClicked(Item item, int position, boolean checkedState, String clickState) {
                            Progress.showLoadingDialog(activity);
                            BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
                                @Override
                                public void onSuccess(int statusCode, String responseObject) {
                                    Progress.dismissLoadingDialog();
                                    try {
                                        Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                                        List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                                        for (int x = 0; x < arrayFromApi.size(); x++) {
                                            arrayFromApi.get(x).setLevel(item.getLevel());
                                        }
                                        Item mainItemsList = itemLists.get(position);
                                        if (mainItemsList.getChildren() != null) {
                                            mainItemsList.getChildren().clear();
                                            List<?> newRepositoryArrayListFromChildrenToCHilde = mainItemsList.getChilds();
                                            mainItemsList.addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                                        }
                                        List<?> arrayFromApiCasting = arrayFromApi;
                                        List<RecyclerViewItem> castedArrayFromApiCasting = ((List<RecyclerViewItem>) arrayFromApiCasting);
                                        List<RecyclerViewItem> newRepositoryArrayList = new ArrayList<>();
                                        if (mainItemsList.getChildren() != null) {
                                            List<RecyclerViewItem> castedMainItemsList = ((List<RecyclerViewItem>) mainItemsList.getChildren());
                                            newRepositoryArrayList.addAll(castedMainItemsList);
                                        }
                                        newRepositoryArrayList.addAll(castedArrayFromApiCasting);
                                        // check box selection
                                        if (item != null && item.getChildren() != null && item.getChildren().size() > 0) {
                                            List<?> newIsChecked = newRepositoryArrayList;
                                            List<Item> casted = ((List<Item>) newIsChecked);
                                            for (int x = 0; x < casted.size(); x++) {
                                                Item itemMain = casted.get(x);
                                                itemMain.setChecked(checkedState);
                                            }
                                            checkCheckedVials(casted, clickState, "add");
                                        }
                                        //
                                        mainItemsList.addChildren(newRepositoryArrayList);
                                        multiLevelRecyclerView.toggleItemsGroup(position);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, String errorResponse) {
                                    try {
                                        Progress.dismissLoadingDialog();
                                        if (statusCode > 0 && responseObject != null) {
                                            List<Item> casted = new ArrayList<>();
                                            casted.add(item);
                                            checkCheckedVials(casted, clickState, "remove");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    multiLevelRecyclerView.setAdapter(expandableAdapter);
                    multiLevelRecyclerView.setToggleItemOnClick(false);
                    multiLevelRecyclerView.setAccordion(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }


    private void checkCheckedVials(List<Item> itemLists, String state, String addState) {
        try {
            int counter = 0;
            for (Item item : itemLists) {
                if (state.equalsIgnoreCase("checked")) {
                    if (!item.isChecked()) {
                        mainChecks.remove(counter);
                    } else {
                        mainChecks.add(item);
                    }
                }
                counter++;
            }
            if (addState.equalsIgnoreCase("add") && state.equalsIgnoreCase("checked")) {
                mainChecks.addAll(itemLists);
            }
            Log.e("s", "s");
            //nestedLoop(itemLists, 0);
            //Log.e("s", "s");
//            for (int x = 0; x < itemLists.size(); x++) {
//                if (itemLists.get(x) != null && itemLists.get(x).getVehicleID() > 0 && (itemLists.get(x).isChecked() || itemLists.get(x).isClicked())) {
//
//
//                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void nestedLoop(List<Item> itemList, int level) {
        try {
            int length = itemList.size();
            for (int i = 0; i < length; i++) {
                level = level + 1;
                if (itemList.get(i) != null && itemList.get(i).getChildren() != null) {
                    int childrenSize = itemList.get(i).getChildren().size();
                    List<?> itemMainLists = itemList.get(i).getChildren();
                    List<Item> castedArrayFromApiCasting = ((List<Item>) itemMainLists);
                    if (childrenSize > 0) {
                        nestedLoop(castedArrayFromApiCasting, level);
                    }
                    mainChecks.addAll(castedArrayFromApiCasting);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void nestedLoop(JSONObject levelList, int level) {
        try {
            JSONArray jsonArrayStringList = levelList.getJSONArray("Childs");
            int length = jsonArrayStringList.length();
            for (int i = 0; i < length; i++) {
                JSONObject itemObject = jsonArrayStringList.getJSONObject(i);
                if (itemObject.has("Childs") && !itemObject.isNull("Childs")) {
                    int childrenSize = itemObject.getJSONArray("Childs").length();
                    if (childrenSize > 0) {
                        level = level + 1;
                        nestedLoop(itemObject, level);
                    }
                    List<?> itemsList = Item.parseArray(itemObject.getJSONArray("Childs"));
                    Item items = Item.parse(itemObject);
                    List<RecyclerViewItem> children = ((List<RecyclerViewItem>) itemsList);
                    items.addChildren(children);
                    items.setLevel(level);
                    list.add(items);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                singleCarSliderTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLabel()));
                singleSliderCarSliderTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLabel()));
                if (markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getAddress() != null) {
                    singleCarSliderAddressTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress()));
                    singleSliderUpTitleTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void addSlideUpFragment() {
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.sliderContainer, SlideUpFragment.newInstance()).commit();
        }
    }

    @Override
    public void onClickShow(boolean isShowCliched, boolean isAddClicked, int itemId) {
        switch (itemId) {
            case 0: // locate
                setLocateManager();
                break;

            case 1: //clustering
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

    private void setLocateManager() { //TODO
        if (myLocateManager == null) {
//            setVisibilityVehiclesMarkers(false);
//            setVisibilityAllMarkers(false);
            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                if (vehiclesClusterManager != null) {
                    vehiclesClusterManager.removeVehiclesCluster();
                    vehiclesClusterManager = null;
                }
            }
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
                        addVehiclesMarkers();
                    }
                }
            });
        } else {
            myLocateManager.removeViewPopup();
            myLocateManager = null;
//            setVisibilityVehiclesMarkers(false);
//            setVisibilityAllMarkers(false);
            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                if (vehiclesClusterManager != null) {
                    vehiclesClusterManager.removeVehiclesCluster();
                    vehiclesClusterManager = null;
                }
            }
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
                        addVehiclesMarkers();
                    }
                }
            });
        }
        slideUp.hide();
        slideUpSingleCar.hide();
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
//            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
//                if (myLandmarkManager != null) {
//                    myLandmarkManager.setLandMarkClusterManager(true);
//                }
//            }
        } else {
            if (vehiclesClusterManager != null) {
                setMarkersVisibility(true);
                vehiclesClusterManager.removeVehiclesCluster();
                vehiclesClusterManager = null;
            }
//            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
//                if (myLandmarkManager != null) {
//                    myLandmarkManager.setLandMarkClusterManager(false);
//                }
//            }
            animateCameraAfterCluster();
        }
        slideUp.hide();
        slideUpSingleCar.hide();
    }

    private void setMapTraffic(boolean isShowCliched) {
//        if (googleMap.isTrafficEnabled()) {
//            googleMap.setTrafficEnabled(false);
//        } else {
//            googleMap.setTrafficEnabled(true);
//        }
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
        addVehiclesMarkers();
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
            if (singleCarMoreOptionsLayout.getVisibility() == View.VISIBLE) {
                addAndHideViews(0, true);
                viewSelected(false);
            }
        });
        new Handler().postDelayed(() -> {
            startSignalRSerivce();
            setLocationIfNeeded();
        }, 1000);
    }

    private void addVehiclesMarkers() {
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
                    inHashModel.setMarker(addMarker);
                    vehiclesHashMap.put(addMarker, inHashModel);
                    builder.include(lng);
                    googleMap.setOnMarkerClickListener(marker -> {
                        if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                            AllVehiclesInHashModel markerModel = vehiclesHashMap.get(marker);
                            if (markerModel != null) {
                                addHeaderTitle(markerModel);
                                addBodyView(markerModel);
                                viewSelected(true);
                                addAndHideViews(markerModel.getVehicleId(), false);
                            }
                        }
                        return false;
                    });
                }
                bounds = builder.build();
                openFirstTime = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAndHideViews(int vehicleId, boolean isReactedMap) {
        googleMap.clear();
        Marker addMarker;
        if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
            for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
                inHashModel.setVehicleId(allVehicleModel.getVehicleID());
                inHashModel.setAllVehicleModel(allVehicleModel);
                LatLng lng = new LatLng(allVehicleModel.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());
                if (isReactedMap) {
                    addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                            .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                            .anchor(0.5f, 0.5f)
                            .rotation((float) allVehicleModel.getLastLocation().getDirection())
                            .flat(true));
                } else if (vehicleId == allVehicleModel.getVehicleID()) {
                    addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                            .icon(AppUtils.getCarIcon(allVehicleModel.getLastLocation().getVehicleStatus()))
                            .anchor(0.5f, 0.5f)
                            .rotation((float) allVehicleModel.getLastLocation().getDirection())
                            .flat(true));
                } else {
                    addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                            .icon(AppUtils.getCarIconAlpha(allVehicleModel.getLastLocation().getVehicleStatus()))
                            .anchor(0.5f, 0.5f)
                            .rotation((float) allVehicleModel.getLastLocation().getDirection())
                            .flat(true));
                }
                inHashModel.setMarker(addMarker);
                vehiclesHashMap.put(addMarker, inHashModel);
                builder.include(lng);
            }
            bounds = builder.build();
        }
    }


    private void addBodyView(AllVehiclesInHashModel markerModel) {
        try {
            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime() != null)
                timeTextView.setText(String.format(Locale.getDefault(), "%s", Utils.parseTime(markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime())));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                kmTextView.setText(String.format(Locale.getDefault(), "%s km/h", markerModel.getAllVehicleModel().getLastLocation().getSpeed()));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                timerTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getDirection())); // fraction

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus() != null)
                infoTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus())); //

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus() != null)
                offTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getVehicleStatus()));// engen statuds

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getPlateNumber() != null)
                visaTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getPlateNumber()));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getSerialNumber() != null)
                barCodeTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getSerialNumber()));

            nATextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // capten view
            closedTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // vehicle OpenDoors view
            beltTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // seat bilt
            needlTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // needel view
            gasTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // gas view
            humanTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // persons view
            cardTextView.setText(String.format(Locale.getDefault(), "%s", "N/A")); // sim card view

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getAddress() != null)
                addressTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress())); //

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                mileageTextView.setText(String.format(Locale.getDefault(), "Mileage: %s", markerModel.getAllVehicleModel().getLastLocation().getTotalMileage()));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null)
                workingTextView.setText(String.format(Locale.getDefault(), "Working Hours: %s", markerModel.getAllVehicleModel().getLastLocation().getTotalWorkingHours()));
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

        try {
            //        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Utils.getRandomNumber(115, 105));
            CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
            googleMap.animateCamera(cu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateCameraAfterCluster() {
        if (isRandomZoomChanged) {
            randomZoom = (int) (googleMap.getCameraPosition().zoom - 2);
            isRandomZoomChanged = false;
        } else {
            randomZoom = (int) (googleMap.getCameraPosition().zoom + 2);
            isRandomZoomChanged = true;
        }

        try {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, randomZoom);
            googleMap.animateCamera(cu);
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
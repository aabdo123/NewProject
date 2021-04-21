package com.fragments;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
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
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.R;
import com.adapters.ExpandableAdapter;
import com.adapters.MainMapAdapter;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
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
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static android.content.Context.LOCATION_SERVICE;

public class LisOfVehiclesMapFragment extends Fragment implements
        SlideUpFragment.OnChildFragmentInteractionListener,
        GeoFenceFragment.OnAddGeoFence,
        LandmarkFragment.OnAddLandmark,
        MapStyleDialogFragment.OnSelectMapStyle,
        OnMapReadyCallback, LocationSource, LocationListener, View.OnClickListener {

    public static LisOfVehiclesMapFragment fragment;
    public static SlideUp slideUp;
    public static SlideUp slideUpSingleCar;
    private static SlideUpFragment slideUpFragment;
    int height = 0;
    int width = 0;
    List<AllVehiclesInHashModel.AllVehicleModel.LastLocation> arrayFromApi = null;
    Handler handler;
    private SignalRService mService;
    private boolean mBound = false;
    private Handler handler12 = new Handler();
    private Runnable refresh;
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
    private MapView mMapView;
    private GoogleMap googleMap;
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
    private com.github.clans.fab.FloatingActionButton searchFab;
    private RelativeLayout searchFieldRelativeLayout;
    private RelativeLayout shadowRelativeLayout;
    private EditText searchMapEditText;
    private RecyclerView listOfSearchedCars;
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
    private MainMapAdapter searchFieldAdapter;
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
    private MultiLevelRecyclerView multiLevelRecyclerView;
    private boolean markerClusterVisibility;
    private ArrayList<Item> itemArrayListCallas;
    private int selectedVehicleId;
    private Intent SignalRServiceIntent;
    private boolean isMoving;
    private boolean isSearchClicked;
    private RelativeLayout progressRelativeLayout;
    private ImageView shareLocationImageView;
    private ImageView shareLocationSliderImageView;

    public LisOfVehiclesMapFragment() {
    }

    public static LisOfVehiclesMapFragment newInstance(ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehicleModel, ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence) {
        fragment = new LisOfVehiclesMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS, listOfVehiclesForGeoFence);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static <K, T> Optional<K> getKey(Map<K, T> mapOrNull, T value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Optional.ofNullable(mapOrNull).flatMap(map -> map.entrySet()
                    .stream()
                    .filter(e -> Objects.equals(e.getValue(), value))
                    .map(Map.Entry::getKey)
                    .findAny());
        }
        return null;
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
                listOfVehiclesForGeoFence = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS);
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
        getScreenWidth();
        initSearchEdits();
        mMapView.getMapAsync(this);
        Progress.dismissLoadingDialog();

        return rootView;
    }

    private void getScreenWidth() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            }
        });
    }

    private void initSearchEdits() {
        searchMapEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initEmailsAdapter(s.toString());
            }
        });
    }

    private void showProgress() {
//        try {
//            if (progressRelativeLayout != null)
//                progressRelativeLayout.setVisibility(View.VISIBLE);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
    }

    private void hideProgress() {
//        try {
//            if (progressRelativeLayout != null)
//                progressRelativeLayout.setVisibility(View.GONE);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
    }

    private void initViews() {
        searchFieldRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.searchFieldRelativeLayout);
        shadowRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.shadowRelativeLayout);
        searchMapEditText = (EditText) rootView.findViewById(R.id.searchMapEditText);
        listOfSearchedCars = (RecyclerView) rootView.findViewById(R.id.listOfSearchedCars);
        listOfSearchedCars.setLayoutManager(new LinearLayoutManager(activity));
        mapStylingFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.mapStylingFab);
        searchFab = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.searchFab);
        searchFab.setVisibility(View.INVISIBLE);
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
        progressRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.progressRelativeLayout);
        listView = (ListView) rootView.findViewById(R.id.listView);
        shareLocationImageView = rootView.findViewById(R.id.shareLocationImageView);
        shareLocationSliderImageView = rootView.findViewById(R.id.shareLocationSliderImageView);

        mMapView.setZ(0);
        searchFieldRelativeLayout.setZ(5);
        shadowRelativeLayout.setZ(4);
        mapStylingFab.setZ(1);
        searchFab.setZ(6);
        sliderBgLayout.setZ(2);
        allViewSlideLayout.setZ(3);
        progressRelativeLayout.setZ(8);

        setSlideUpBuilder();
        setSingleSlideUpBuilder();
        addSlideUpFragment();
    }

    private void initListeners() {
        searchFieldRelativeLayout.setOnClickListener(this);
        mapStylingFab.setOnClickListener(this);
        searchFab.setOnClickListener(this);
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
        shadowRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreOptionsLayout:
                break;
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
                break;
            case R.id.moreOptionsUpLayout:
                break;
            case R.id.sliderUpTitleTextView:
                break;
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
            case R.id.searchFab:
                checkViewWidthAndroid();
                break;
            case R.id.searchFieldRelativeLayout:
                break;
            case R.id.shadowRelativeLayout:
                hideSearchView();
                break;
            default:
                break;
        }
    }

    private void checkViewWidthAndroid() {
        try {
            if (isSearchClicked) {
                Utils.hidKeyBoard(Objects.requireNonNull(getActivity()));
                isSearchClicked = false;
                searchMapEditText.getText().clear();
                shadowRelativeLayout.setVisibility(View.GONE);
                callasSearchColor(shadowRelativeLayout, context.getResources().getColor(R.color.trans_black),
                        context.getResources().getColor(R.color.transparent));
                callasSearchViewAndroid(searchFieldRelativeLayout, 0);
            } else {
                shadowRelativeLayout.setVisibility(View.VISIBLE);
                isSearchClicked = true;
                searchMapEditText.requestFocus();
                callasSearchColor(shadowRelativeLayout, context.getResources().getColor(R.color.transparent),
                        context.getResources().getColor(R.color.trans_black));
                callasSearchViewAndroid(searchFieldRelativeLayout, (int) Utils.convertDpToPixel(width, context));
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showKeyBoard(Objects.requireNonNull(getActivity()));
                    }
                });
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void addListMarin(int bottom, int top) {
        try {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) listOfSearchedCars.getLayoutParams();
            params.setMargins(params.leftMargin, top, params.rightMargin, bottom);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void callasSearchViewAndroid(View view, int endWidth) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(), endWidth);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(400);
        anim.start();
    }

    private void callasSearchColor(View view, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(400); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public void updateMapStyleOnBackPressed() {
        if (myMapStyleManager != null) {
            myMapStyleManager.setSelectedStyle(PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_ID));
            PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_MAP_STYLE_CHANGED);
        }
    }

    public boolean hideSearchView() {
        try {
            if (isSearchClicked) {
                isSearchClicked = false;
                Utils.hidKeyBoard(Objects.requireNonNull(getActivity()));
                searchMapEditText.getText().clear();
                callasSearchColor(shadowRelativeLayout, context.getResources().getColor(R.color.trans_black),
                        context.getResources().getColor(R.color.transparent));
                callasSearchViewAndroid(searchFieldRelativeLayout, 0);
                shadowRelativeLayout.setVisibility(View.GONE);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return false;
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

//                try {
//
//
//                    HashMap<String, Item> productMapAddItems = new HashMap<String, Item>();
//
//                    for (Item p : mainItemsFull) {
//                        if (p != null && p.getVehicleID() != null)
//                            productMapAddItems.put(String.valueOf(p.getVehicleID()), p);
//                    }
//
//
//                    if (mainItemsFull.size() > 0) {// for stack over flow and also take time
//                        // for (Item item : mainItemsFull) {
//                        for (Item itemChild : productMapAddItems.values()) {
//                            if (itemChild.getParent() != null) {
//                                itemChild.setParent(null);
//                                itemChild.addChildren(null);
//                                itemChild.setChilds(null);
//                            }
//
//                        }
//                        //  }
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
                try {
                    if (mainItemsFull.size() > 0) {// for stack over flow
                        for (Item item : mainItemsFull) {
                            if (item.getParent() != null) {
                                for (Item itemChild : item.getParent().getChilds()) {
                                    itemChild.setParent(null);
                                    itemChild.addChildren(null);
                                    itemChild.setChilds(null);
                                }

                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (arrayFromApi == null) {
                    arrayFromApi = new ArrayList<>();
                } else {
                    arrayFromApi.clear();
                }
                if (mainItemsFull.size() > 0) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (Item items : mainItemsFull) {
                                    AllVehiclesInHashModel.AllVehicleModel.LastLocation lastLocation = new AllVehiclesInHashModel.AllVehicleModel.LastLocation();
                                    lastLocation.setVehicleID(items.getVehicleID());
                                    lastLocation.setSpeed(items.getSpeed());
                                    lastLocation.setTotalMileage(items.getTotalMileage());
                                    lastLocation.setTotalWorkingHours(items.getTotalWorkingHours());
                                    lastLocation.setDirection(items.getDirection());
                                    lastLocation.setLatitude(items.getLatitude());
                                    lastLocation.setLongitude(items.getLongitude());
                                    lastLocation.setAddress(items.getAddress());
                                    lastLocation.setPlateNumber(items.getPlateNumber());
                                    lastLocation.setFuel(items.getFuel());
                                    lastLocation.setStreetSpeed(items.getStreetSpeed());
                                    lastLocation.setSimCardNumber(items.getSimCardNumber());
                                    lastLocation.setVehicleDisplayName(items.getVehicleDisplayName());
                                    lastLocation.setVehicleStatus(items.getVehicleStatus());
                                    lastLocation.setRecordDateTime(items.getRecordDateTime());
                                    lastLocation.setMileage(items.getMileage());
                                    lastLocation.setWorkingHours(items.getWorkingHours());
                                    lastLocation.setSerial(items.getSerial());
                                    lastLocation.setIsOnline(items.getOnline());
                                    lastLocation.setEngineStatus(items.getEngineStatus());
                                    lastLocation.setSeatBeltStatus(items.getSeatBeltStatus());
                                    lastLocation.setTemper(items.getTemper());
                                    lastLocation.setDriverName(items.getDriverName());
                                    lastLocation.setGroupName(items.getGroupName());
                                    lastLocation.setTemperature(items.getTemperature());
                                    lastLocation.setDoorStatus(items.getDoorStatus());
                                    arrayFromApi.add(lastLocation);
                                }
                            } catch (Exception ex) {
                                ex.getMessage();
                            }
                        }
                    });
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
                    addVehiclesMarkers(true);// take time
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showLandMark(true);
                    }
                    if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
                        slideUpFragment.showGeofence(true);
                    }
                    if (listOfVehiclesForGeoFence != null && listOfVehiclesForGeoFence.size() > 0 && listOfVehiclesForGeoFence.get(0) != null) {// take time
                        List<ListOfVehiclesModel.VehicleModel> list = new ArrayList<>();
                        for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                            ListOfVehiclesModel.VehicleModel vehicleModel = new ListOfVehiclesModel.VehicleModel();
                            vehicleModel.setPlateNumber(allVehicleModel.getPlateNumber());
                            vehicleModel.setVehicleID(allVehicleModel.getVehicleID());
                            vehicleModel.setLabel(allVehicleModel.getLabel());
                            vehicleModel.setSerialNumber(allVehicleModel.getSerialNumber());
                            vehicleModel.setFbToken(allVehicleModel.getFbToken());
                            ListOfVehiclesModel.VehicleModel.LastLocation lastLocation = new ListOfVehiclesModel.VehicleModel.LastLocation();
                            if (allVehicleModel.getLastLocation() != null) {
                                lastLocation.setVehicleID(allVehicleModel.getVehicleID());
                                lastLocation.setSpeed(allVehicleModel.getLastLocation().getSpeed());
                                lastLocation.setTotalMileage(allVehicleModel.getLastLocation().getTotalMileage());
                                lastLocation.setTotalWorkingHours(allVehicleModel.getLastLocation().getTotalWorkingHours());
                                lastLocation.setDirection(allVehicleModel.getLastLocation().getDirection());
                                lastLocation.setLatitude(allVehicleModel.getLastLocation().getLatitude());
                                lastLocation.setLongitude(allVehicleModel.getLastLocation().getLongitude());
                                lastLocation.setAddress(allVehicleModel.getLastLocation().getAddress());
                                lastLocation.setStreetSpeed(allVehicleModel.getLastLocation().getStreetSpeed());
                                lastLocation.setVehicleStatus(allVehicleModel.getLastLocation().getVehicleStatus());
                                lastLocation.setRecordDateTime(allVehicleModel.getLastLocation().getRecordDateTime());
                                lastLocation.setIsOnline(allVehicleModel.getLastLocation().getOnline());
                            }
                            vehicleModel.setLastLocation(lastLocation);
                            list.add(vehicleModel);
                        }

                        //  String request = new Gson().toJson(vehiclesList);
                        //   ListOfVehiclesModel.VehicleModel[] vehicleModel = new Gson().fromJson(request, ListOfVehiclesModel.VehicleModel[].class);
                        //  List<ListOfVehiclesModel.VehicleModel> list = Arrays.asList(vehicleModel);
                        listOfVehiclesForGeoFence.get(0).setVehicleModel(list);


                    }
                    if (markerClusterVisibility)
                        setClusterManager(true, false, 1);
                    checkFabButtonVisibly();
                }

            }
            new Handler().postDelayed(this::startLiveTarcking, 1000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startLiveTarcking() {
        try {
            if (mBound) {
                mService.invokeService(new SignalRService.FireBaseListener() {
                    @Override
                    public void dataSnapShot(String dataSnapshot) {
                        if (!PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                            if (!isMoving && !isSearchClicked)
                                markStartingLocationOnMap(dataSnapshot);
                        }
                    }
                });
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
                                        Log.d("s", "S");
//                                        if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
//                                            getLength(itemLists.get(0).getChildren(), false, false);
//                                        }
                                    } else {
                                        if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
                                            itemArrayListCallas = new ArrayList<>();
                                            getLength(itemLists.get(0).getChildren(), false, false);
                                        }
                                    }
                                }
                                if (mainArrayOfVehiclesList.size() > 0) {
                                    mainArrayOfVehiclesList.clear();
                                }
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
            if (item != null && clickState.equalsIgnoreCase("grope")) {
                if (item.getChildren() != null) {
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
                if (itemLists != null) {
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
                }
            } else if (clickState.equalsIgnoreCase("vehicle")) {
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        itemLists.set(y, item);
                    }
                }
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
                if (item.isExpanded()) {
                    try {
//                        handler = new Handler();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //
                                Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                                List<Item> arrayFromApis = Arrays.asList(vehicleModel);

                                HashMap<String, Item> productMap = new HashMap<String, Item>();
                                try {
                                    for (Item p : arrayFromApis) {
                                        if (p != null && p.getVehicleID() != null)
                                            productMap.put(String.valueOf(p.getVehicleID()), p);
                                    }
//                                        for (Item item1Main : arrayFromApi) {// long time
                                    for (Item itemListMain : itemLists) {
                                        String vehicle = itemListMain.getID();
                                        //int vehicleId = item1Main.getVehicleID();
                                        String firstOne = vehicle.substring(0, 1);
                                        if (firstOne.equalsIgnoreCase("V")) {
                                            vehicle = vehicle.substring(2, vehicle.length());
                                            Item itemsK = productMap.get(vehicle);
                                            itemsK.setLevel(itemListMain.getLevel());
                                            itemsK.setChecked(itemListMain.isChecked());
                                            itemsK.setClicked(itemListMain.isClicked());
                                            itemsK.setID(itemListMain.getID());
                                            itemsK.setName(itemListMain.getName());
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.getMessage();
                                }
                                List<Item> arrayFromApi = new ArrayList<>(productMap.values());
//                                        }

                                for (Item mainYItems : itemLists) {
                                    String grope = mainYItems.getID();
                                    String firstOne = grope.substring(0, 1);
                                    if (firstOne.equalsIgnoreCase("G")) {
                                        if (!mainYItems.isExpanded()) {
                                            getLength(mainYItems.getChildren(), true, false);
                                            for (Item itemsX : arrayFromApi) {
                                                if (mainYItems.getChildren() != null)
                                                    for (int h = 0; h < mainYItems.getChildren().size(); h++) {
                                                        List<?> array = mainYItems.getChildren();
                                                        List<Item> mainArray = (List<Item>) array;
                                                        String vehicle = mainArray.get(h).getID();
                                                        int vehicleId = itemsX.getVehicleID();
                                                        String firstOnes = vehicle.substring(0, 1);
                                                        if (firstOnes.equalsIgnoreCase("V")) {
                                                            vehicle = vehicle.substring(2, vehicle.length());
                                                            if (Integer.valueOf(vehicle) == vehicleId) {
                                                                itemsX.setLevel(mainArray.get(h).getLevel());
                                                                itemsX.setChecked(mainArray.get(h).isChecked());
                                                                itemsX.setClicked(mainArray.get(h).isClicked());
                                                                itemsX.setID(mainArray.get(h).getID());
                                                                itemsX.setName(mainArray.get(h).getName());
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                }

                                if (itemArrayListCallas != null && itemArrayListCallas.size() > 0)
                                    for (int x = 0; x < arrayFromApi.size(); x++) {
                                        for (int y = 0; y < itemArrayListCallas.size(); y++) {
                                            String vehicle = itemArrayListCallas.get(y).getID();
                                            int vehicleId = arrayFromApi.get(x).getVehicleID();
                                            String firstOne = vehicle.substring(0, 1);
                                            if (firstOne.equalsIgnoreCase("V")) {
                                                vehicle = vehicle.substring(2, vehicle.length());
                                                if (Integer.valueOf(vehicle) == vehicleId) {
                                                    arrayFromApi.get(x).setLevel(itemArrayListCallas.get(y).getLevel());
                                                    arrayFromApi.get(x).setChecked(itemArrayListCallas.get(y).isChecked());
                                                    arrayFromApi.get(x).setClicked(itemArrayListCallas.get(y).isClicked());
                                                    arrayFromApi.get(x).setID(itemArrayListCallas.get(y).getID());
                                                    arrayFromApi.get(x).setName(itemArrayListCallas.get(y).getName());
                                                }
                                            }
                                        }
                                    }

                                // int x = 0;
                                // for (Item itemX : arrayFromApi) {// long time

                                HashMap<String, Item> productMaps = new HashMap<String, Item>();

                                for (Item p : arrayFromApi) {
                                    if (p != null && p.getVehicleID() != null)
                                        productMaps.put(String.valueOf(p.getVehicleID()), p);
                                }

                                int y = 0;
                                for (Item itemY : itemLists) {
                                    String vehicle = itemY.getID();
                                    //int vehicleId = itemX.getVehicleID();
                                    String firstOne = vehicle.substring(0, 1);
                                    if (firstOne.equalsIgnoreCase("V")) {
                                        vehicle = vehicle.substring(2, vehicle.length());
//                                                    if (Integer.valueOf(vehicle) == vehicleId) {
                                        Item itemX = productMaps.get(vehicle);
                                        if (itemY.getParent() != null) {
                                            itemX.setParent(itemY.getParent());
                                        }
                                        Item items = itemX;
                                        itemLists.set(y, items);
//                                                    }
                                    } else {
                                        if (itemY.getChildren() != null) {
                                            List<?> array = itemY.getChildren();
                                            ArrayList<?> arrayList = new ArrayList<>(array);
                                            Item items = itemY;
                                            items.setChilds((ArrayList<Item>) arrayList);
                                            itemLists.set(y, items);
                                        }
                                    }
                                    y++;
                                }
                                // x++;
                                //}

                                for (Item itemX : itemLists) {
                                    itemX.setGroupChecked(false);
                                    itemX.setChecked(true);
                                }

                                List<?> newRepositoryArray = itemLists.get(0).getChildren();
                                List<Item> arrayList = (List<Item>) newRepositoryArray; // get all grupes

                                int x1 = 0;
                                for (Item itemX : arrayList) {
                                    List<?> children = itemX.getChildren(); // get childe of the grupe
                                    List<Item> mainChildren = (List<Item>) children;
                                    if (mainChildren != null && mainChildren.size() > 0) {
                                        int y1 = 0;
                                        for (Item itemY : mainChildren) {
                                            int z1 = 0;
                                            for (Item itemZ : itemLists) {
                                                String vehicle = itemZ.getID();
                                                String vehicleId = itemY.getID();
                                                if (vehicle.equalsIgnoreCase(vehicleId)) {
                                                    mainChildren.set(y1, itemZ);
                                                }
                                                z1++;
                                            }
                                            y1++;
                                        }
                                    }
                                    x1++;
                                }

                                if (mainArrayOfVehiclesList.size() > 0)
                                    mainArrayOfVehiclesList.clear();
                                mainArrayOfVehiclesList.addAll(arrayFromApi);
                                expandableAdapter.notifyDataSetChanged();
                                Progress.dismissLoadingDialog();
                            }
                        });
//                            }
//                        }).start();
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
                            } else {
                                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Progress.dismissLoadingDialog();
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
                if (newArray != null && newArray.size() > 0 && itemArrayListCallas != null) {
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

                    //
                    Item mainItemsList = itemLists.get(position);
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {

//                        if (mainArrayOfVehiclesList != null && mainArrayOfVehiclesList.size() > 0) {
//                            mainArrayOfVehiclesList.clear();
//                        }

                        List<?> arrayFromApiCasting = mainItemsList.getChildren();
                        List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                        if (arrayFrom.size() == arrayFromApi.size()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setName(arrayFrom.get(x).getName());
                                arrayFromApi.get(x).setID(arrayFrom.get(x).getID());
                                arrayFromApi.get(x).setChecked(arrayFrom.get(x).isChecked());
                                arrayFromApi.get(x).setClicked(arrayFrom.get(x).isClicked());
                                arrayFromApi.get(x).setParent(item);
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


                    HashMap<String, Item> productMap = new HashMap<String, Item>();
                    for (Item p : mainArrayOfVehiclesList) {
                        if (p != null && p.getVehicleID() != null)
                            productMap.put(String.valueOf(p.getVehicleID()), p);
                    }
                    mainArrayOfVehiclesList.clear();
                    mainArrayOfVehiclesList.addAll(productMap.values());


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
                    shareLocationImageView.setOnClickListener((v) -> {
                        double latitude = markerModel.getAllVehicleModel().getLastLocation().getLatitude();
                        double longitude = markerModel.getAllVehicleModel().getLastLocation().getLongitude();
                        shareLocationIntent(latitude, longitude);
                    });
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

    private void shareLocationIntent(double latitude, double longitude) {
        String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String ShareSub = "Here is my location";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
            default:
                break;
        }
    }

    private void setLocateManager() {
        try {
            searchFab.setVisibility(View.GONE);
            if (vehiclesClusterManager != null) {
                setMarkersVisibility(false);
                vehiclesClusterManager.removeVehiclesCluster();
                vehiclesClusterManager = null;
                animateCameraAfterCluster();
            } else {
                setMarkersVisibility(false);
                animateCameraAfterCluster();
            }
            if (myLocateManager == null) {
                myLocateManager = new MyLocateManager(context, rootView, googleMap, vehiclesHashMap, myCurrentLatLng);
                myLocateManager.setViews(mMapView);
                myLocateManager.setAfterOnDismissListeners(new Click() {
                    @Override
                    public void onClick() {
                        setVisibilityVehiclesMarkers(true);
                        checkFabButtonVisibly();
                    }

                    @Override
                    public void addMaps() {
                        if (googleMap != null) {
                            openFirstTime = true;
                            addVehiclesMarkers(false);
                            reLocate();
                            if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                                slideUpFragment.showLandMark(true);
                            }
                        }
                    }
                });
                myLocateManager.setLandMarkListeners(new MyLocateManager.LandMarkCheck() {
                    @Override
                    public void landMark() {
                        googleMap.clear();
                        if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU)) {
                            slideUpFragment.showLandMark(true);
                        }
                        if (slideUpFragment != null && PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU)) {
                            slideUpFragment.showGeofence(true);
                        }
                        if (!PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU)) {
                            addAndHideViews(-1, true);
                        } else {
                            addAndHideViewsAfterCluster(-1, true);
                        }
                    }
                });

            } else {
                myLocateManager.removeViewPopup();
                myLocateManager = null;
                myLocateManager = new MyLocateManager(context, rootView, googleMap, vehiclesHashMap, myCurrentLatLng);
                myLocateManager.setViews(mMapView);
                myLocateManager.setAfterOnDismissListeners(new Click() {
                    @Override
                    public void onClick() {// 3ok
                        setVisibilityVehiclesMarkers(true);
                        checkFabButtonVisibly();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkFabButtonVisibly() {
        if (vehiclesList != null && vehiclesList.size() > 0) {
            searchFab.setVisibility(View.VISIBLE);
        } else {
            searchFab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    private void initEmailsAdapter(String value) {
        try {
            if (value.length() > 0) {
                if (vehiclesList != null && vehiclesList.size() > 0) {
                    addListMarin(80, 30);
                    searchFieldAdapter = new MainMapAdapter(activity, filterSearchCarsList(value, vehiclesList), new MainMapAdapter.ClickOnListTap() {
                        @Override
                        public void onClick(int position, AllVehiclesInHashModel.AllVehicleModel model) {
                            selectedVehicle(model);
                        }
                    });
                    listOfSearchedCars.setAdapter(searchFieldAdapter);
                }
            } else {
                addListMarin(0, 0);
                searchFieldAdapter = new MainMapAdapter(activity, new ArrayList<>(), new MainMapAdapter.ClickOnListTap() {
                    @Override
                    public void onClick(int position, AllVehiclesInHashModel.AllVehicleModel model) {
                        selectedVehicle(model);
                    }
                });
                listOfSearchedCars.setAdapter(searchFieldAdapter);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void selectedVehicle(AllVehiclesInHashModel.AllVehicleModel model) {
        try {
            Progress.showLoadingDialog(activity);
            if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                Marker marker = null;
                for (AllVehiclesInHashModel allVehiclesInHashModel : vehiclesHashMap.values()) {
                    try {
                        if (allVehiclesInHashModel.getVehicleId() == model.getVehicleID()) {
                            // Object o = getKey(vehiclesHashMap, allVehiclesInHashModel);
                            // marker = (Marker) o;
                            addHeaderTitle(allVehiclesInHashModel);
                            addBodyView(allVehiclesInHashModel);
                            viewSelected(true);
                            addAndHideViews(allVehiclesInHashModel.getVehicleId(), false);
                            hideSearchView();
                            if (googleMap != null) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allVehiclesInHashModel.getAllVehicleModel().getLastLocation().getLatitude(),
                                        allVehiclesInHashModel.getAllVehicleModel().getLastLocation().getLongitude()), 18.0f));
                            }
                            Progress.dismissLoadingDialog();
                            break;
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> filterSearchCarsList(String value, ArrayList<AllVehiclesInHashModel.AllVehicleModel> allList) {
        ArrayList<AllVehiclesInHashModel.AllVehicleModel> childList = new ArrayList<>();
        try {
            for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : allList) {
                if (allVehicleModel.getLabel().toLowerCase().contains(value.toLowerCase())) {
                    childList.add(allVehicleModel);
                } else {
                    Log.e("s", "s");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childList;
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
        try {
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
            });

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setLocationIfNeeded() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Setting the width and height of your screen
                int width = context.getResources().getDisplayMetrics().widthPixels;
                int height = context.getResources().getDisplayMetrics().heightPixels;
                double random = Utils.getRandomNumber(0.28, 0.22);
//        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
                int padding = (int) (width * random);
//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(vehiclesList.get(0).getLastLocation().getLatitude(), vehiclesList.get(0).getLastLocation().getLongitude()), 8.0f));
                try {
                    if (builder != null)
                        bounds = builder.build();
                    if (googleMap != null && bounds != null) {
                        CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
                        googleMap.animateCamera(cu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bottomViewVisibility() {
        try {
            selectedVehicleId = 0;
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

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        isMoving = false;
                    }
                });

                googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int reason) {
                        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                            isMoving = true;
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(marker -> {
                    try {
                        if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {
                            showProgress();
                            AllVehiclesInHashModel markerModel = vehiclesHashMap.get(marker);
                            if (markerModel != null) {
                                addHeaderTitle(markerModel);
                                addBodyView(markerModel);
                                viewSelected(true);
                                addAndHideViews(markerModel.getVehicleId(), false);
                                hideProgress();
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
                                hideProgress();
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
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(vehiclesList.get(0).getLastLocation().getLatitude(), vehiclesList.get(0).getLastLocation().getLongitude()), 8.0f));
                            setLocationIfNeeded();
                        }
                    }
                }
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
            selectedVehicleId = vehicleId;
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
            if (markerModel != null && markerModel.getAllVehicleModel() != null) {
                shareLocationSliderImageView.setOnClickListener((v) -> {
                    double latitude = markerModel.getAllVehicleModel().getLastLocation().getLatitude();
                    double longitude = markerModel.getAllVehicleModel().getLastLocation().getLongitude();
                    shareLocationIntent(latitude, longitude);
                });
            }
            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime() != null)
                timeTextView.setText(String.format(Locale.getDefault(), "%s", Utils.parseTimeWithPlusZero(markerModel.getAllVehicleModel().getLastLocation().getRecordDateTime())));
            else
                timeTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null) {
                double d = markerModel.getAllVehicleModel().getLastLocation().getSpeed();
                int i = (int) d;
                kmTextView.setText(String.format(Locale.getDefault(), "%s %s", i, context.getString(R.string.km_h)));
            } else {
                kmTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));
            }

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null) {
                int value = (int) markerModel.getAllVehicleModel().getLastLocation().getDirection() % 360;
                timerTextView.setText(String.format(Locale.getDefault(), "%s°", value)); // fraction
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


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getSimCardNumber() != null)
                cardTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getSimCardNumber())); // sim card view
            else
                cardTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.n_a)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getDriverName() != null)
                nATextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getDriverName()));
            else
                nATextView.setText(context.getString(R.string.no_driver));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getGroupName() != null)
                humanTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getGroupName()));
            else
                humanTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.ungouped)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getAddress() != null)
                addressTextView.setText(String.format(Locale.getDefault(), "%s", markerModel.getAllVehicleModel().getLastLocation().getAddress())); //
            else
                addressTextView.setText(String.format(Locale.getDefault(), "%s", context.getString(R.string.address_not_found)));

            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getMileage() != null) {
                Double value = Double.valueOf(markerModel.getAllVehicleModel().getLastLocation().getMileage()) / 1000;
                mileageTextView.setText(String.format(Locale.CANADA, "%.2f %s", value, context.getString(R.string.km)));
                mileageTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Double value = Double.valueOf(markerModel.getAllVehicleModel().getLastLocation().getMileage()) / 1000;
//                        ToastHelper.toastMessage(activity, String.format(Locale.CANADA, "%s %s %s", context.getString(R.string.mileage), value, context.getString(R.string.km)));
                    }
                });
            } else if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null) {
                Double value = markerModel.getAllVehicleModel().getLastLocation().getTotalMileage() / 1000;
                mileageTextView.setText(String.format(Locale.CANADA, "%s %s", value, context.getString(R.string.km)));
            } else
                mileageTextView.setText(String.format(Locale.CANADA, "%s %s", "0.00", context.getString(R.string.km)));


            if (markerModel != null && markerModel.getAllVehicleModel() != null && markerModel.getAllVehicleModel().getLastLocation() != null && markerModel.getAllVehicleModel().getLastLocation().getWorkingHours() != null) {
                Double valueCalc = Double.valueOf(markerModel.getAllVehicleModel().getLastLocation().getWorkingHours()) / 3600;
                workingTextView.setText(String.format(Locale.CANADA, "%s %.2f", context.getString(R.string.working_hours), Double.valueOf(valueCalc)));
                workingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastHelper.toastMessage(activity, String.format(Locale.CANADA, "%s %s", context.getString(R.string.working_hours), valueCalc));
                    }
                });
            } else
                workingTextView.setText(String.format(Locale.CANADA, "%s %s", context.getString(R.string.working_hours), "0.00"));

            if (markerModel != null)
                PreferencesManager.getInstance().setIntegerValue(markerModel.getVehicleId(), SharesPrefConstants.LAST_VIEW_VEHICLE_ID);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        refresh = new Runnable() {
            public void run() {
                // Do something
                handler12.postDelayed(refresh, 1000);
            }
        };
        handler12.post(refresh);

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

    private void markStartingLocationOnMap(final String signalRModel) {
        try {
            if (signalRModel != null) {
                AllVehiclesInHashModel.AllVehicleModel.LastLocation vehicleModel = new Gson().fromJson(signalRModel, AllVehiclesInHashModel.AllVehicleModel.LastLocation.class);
                if (vehiclesHashMap != null && vehiclesHashMap.size() > 0 && !vehiclesHashMap.keySet().isEmpty()) {
                    for (Marker marker : vehiclesHashMap.keySet()) {
                        if (vehiclesHashMap.get(marker).getVehicleId() == vehicleModel.getVehicleID()) {
                            // vehiclesHashMap.get(marker).getAllVehicleModel().setLastLocation(vehicleModel);
                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addSignalRLocation(marker, vehicleModel);
                                }
                            });
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void addSignalRLocation(Marker marker, AllVehiclesInHashModel.AllVehicleModel.LastLocation allVehicleModels) {
        try {
            if (vehiclesList != null && vehiclesList.size() > 0) {
                AllVehiclesInHashModel.AllVehicleModel allVehicleModel1 = null;
                for (int x = 0; x < vehiclesList.size(); x++) {
                    if (vehiclesList.get(x).getVehicleID().equals(allVehicleModels.getVehicleID())) {
                        allVehicleModel1 = new AllVehiclesInHashModel.AllVehicleModel();
                        allVehicleModel1.setVehicleID(allVehicleModels.getVehicleID() != null ? allVehicleModels.getVehicleID() : 0);
                        String date = allVehicleModels.getRecordDateTime();
                        allVehicleModels.setRecordDateTime(date);
                        allVehicleModel1.setLastLocation(allVehicleModels);
                        allVehicleModel1.setPlateNumber(vehiclesList.get(x).getPlateNumber());
                        allVehicleModel1.setFbToken(vehiclesList.get(x).getFbToken());
                        allVehicleModel1.setSerialNumber(vehiclesList.get(x).getSerialNumber());
                        allVehicleModel1.setLabel(vehiclesList.get(x).getLabel());
                        vehiclesList.set(x, allVehicleModel1);
                    }
                }
                Marker addMarker = null;
                if (vehiclesHashMap != null && vehiclesHashMap.size() > 0) {

//                    for (Marker key : vehiclesHashMap.keySet()) {
//                        key.remove();
//                    }
//                    vehiclesHashMap.clear();

                    vehiclesHashMap.remove(marker);
//                    for (AllVehiclesInHashModel.AllVehicleModel allVehicleModel : vehiclesList) {
                    LatLng lng = new LatLng(allVehicleModels.getLatitude(), allVehicleModels.getLongitude());
                    marker.remove();
                    AllVehiclesInHashModel inHashModel = new AllVehiclesInHashModel();
                    inHashModel.setVehicleId(allVehicleModels.getVehicleID());
                    inHashModel.setAllVehicleModel(allVehicleModel1);
//                         LatLng lng = new LatLng(allVehicleModels.getLastLocation().getLatitude(), allVehicleModel.getLastLocation().getLongitude());

                    if (selectedVehicleId != allVehicleModels.getVehicleID()) {
                        if (selectedVehicleId == 0) {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIcon(allVehicleModels.getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModels.getDirection())
                                    .flat(true));
                            inHashModel.setMarker(addMarker);
                        } else {
                            addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                    .icon(AppUtils.getCarIconAlpha(allVehicleModels.getVehicleStatus()))
                                    .anchor(0.5f, 0.5f)
                                    .rotation((float) allVehicleModels.getDirection())
                                    .flat(true));
                            inHashModel.setMarker(addMarker);
                        }
                    } else {
                        addMarker = googleMap.addMarker(new MarkerOptions().position(lng)
                                .icon(AppUtils.getCarIcon(allVehicleModels.getVehicleStatus()))
                                .anchor(0.5f, 0.5f)
                                .rotation((float) allVehicleModels.getDirection())
                                .flat(true));
                        inHashModel.setMarker(addMarker);
                    }
                    vehiclesHashMap.put(addMarker, inHashModel); // adding here
                    builder.include(lng);
                    if (selectedVehicleId == allVehicleModels.getVehicleID()) {
                        addHeaderTitle(inHashModel);
                        addBodyView(inHashModel);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allVehicleModels.getLatitude(), allVehicleModels.getLongitude()), googleMap.getCameraPosition().zoom));
                    }
                }
            }

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void startSignalRServiceIntent() {
        SignalRServiceIntent = new Intent();
        SignalRServiceIntent.setClass(context, SignalRService.class);
        activity.bindService(SignalRServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
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

    @Override
    public void onStop() {
        stopSignalRService();
        super.onStop();
    }

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
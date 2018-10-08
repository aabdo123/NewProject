package com.managers.map_managers;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import com.models.LandmarkModel;
import com.models.MyClusterItem;
import com.utilities.LogHelper;
import com.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.utilities.AppUtils.getLandMarkerIcon;
import static com.utilities.constants.AppConstant.MARKER_PADDING_OFFSET;


public class LandmarkClusterManager implements ClusterManager.OnClusterClickListener<MyClusterItem>, ClusterManager.OnClusterInfoWindowClickListener<MyClusterItem>, ClusterManager.OnClusterItemClickListener<MyClusterItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItem> {

    private Context context;
    private GoogleMap googleMap;
    private List<LandmarkModel> landmarkList;
    private List<MyClusterItem> myClusterItems = new ArrayList<>();
    private ClusterManager<MyClusterItem> mClusterManager;

    public LandmarkClusterManager(Context context, GoogleMap googleMap, List<LandmarkModel> landmarkList) {
        this.context = context;
        this.googleMap = googleMap;
        this.landmarkList = landmarkList;
        addToClusterList();
    }

    private void addToClusterList() {
        for (LandmarkModel model : landmarkList) {
            LatLng lng = new LatLng(model.getLatitude(), model.getLongitude());
            MyClusterItem clusterItem = new MyClusterItem(lng, model.getPOIName(), 0.0f, getLandMarkerIcon(model.getPOIName()));
            myClusterItems.add(clusterItem);
        }
    }

    public void startClustering() {
        mClusterManager = new ClusterManager<>(context, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        mClusterManager.addItems(myClusterItems);

        MarkerClusterRenderer customRenderer = new MarkerClusterRenderer(context, googleMap, mClusterManager);
        mClusterManager.setRenderer(customRenderer);
        mClusterManager.onCameraIdle();

        // Mbdy2an no need elhom
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    public void removeLandMarkCluster() {
        for (MyClusterItem item : myClusterItems) {
            mClusterManager.removeItem(item);
        }
    }

    public class MarkerClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {


        public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {
            // use this to make your change to the marker option
            // for the marker before it gets render on the map
            markerOptions.icon(item.getMarkerIcon());
            LogHelper.LOG_E("onClusterItemRendered", "%%%%%%%%% " + item.getPosition());
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

        @Override
        protected int getColor(int clusterSize) {
            return Color.parseColor("#C91018");
        }

//        @Override
//        public void onRemove() {
//            super.onRemove();
//            removeCluster();
//        }

        //        private final IconGenerator mClusterIconGenerator = new IconGenerator(context);
//        @Override
//        protected void onBeforeClusterRendered(Cluster<MyClusterItem> cluster, MarkerOptions markerOptions){
//
//            final Drawable clusterIcon = context.getResources().getDrawable(R.drawable.ic_lens_black_24dp);
//            clusterIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
//
//            mClusterIconGenerator.setBackground(clusterIcon);
//
//            //modify padding for one or two digit numbers
//            if (cluster.getSize() < 10) {
//                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
//            }
//            else {
//                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
//            }
//
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        }
    }

    ///////////////////
    ///////////////////
    ///////////////////
    ///////////////////
    ///////////////////
    @Override
    public boolean onClusterClick(Cluster<MyClusterItem> cluster) {
        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        //Setting the width and height of your screen
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        double random = Utils.getRandomNumber(0.12, 0.10);
//        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        int padding = (int) (width * random);

        // Animate camera to the bounds
        try {
            CameraUpdate cu = (CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
            googleMap.animateCamera(cu);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyClusterItem> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(MyClusterItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyClusterItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }
}

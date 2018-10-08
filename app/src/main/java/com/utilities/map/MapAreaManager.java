package com.utilities.map;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.utilities.map.MapAreaWrapper.MarkerMoveResult;


/**
 * This class manages the map areas
 * <p>
 * On long click on any empty area of the map, a new area will be created
 * <p>
 * The areas can be moved or resized using markers
 *
 * @author ivanschuetz
 * <p>
 * Based on functionality of Google APIs v19, com.example.mapdemo.CircleDemoActivity
 */
public class MapAreaManager implements OnMarkerDragListener, OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private static int DEFAULT_FILL_COLOR = 0xff0000ff;
    private static int DEFAULT_STROKE_COLOR = 0xff000000;
    private static int DEFAULT_STROKE_WIDTH = 1;

    private List<MapAreaWrapper> areas = new ArrayList<MapAreaWrapper>(1);
    private GoogleMap map;

    private int fillColor = DEFAULT_FILL_COLOR;
    private int strokeWidth = DEFAULT_STROKE_WIDTH;
    private int strokeColor = DEFAULT_STROKE_COLOR;

    private int minRadiusMeters = 100;
    private int maxRadiusMeters = -1;

    private MapAreaMeasure initRadius;

    private CircleManagerListener circleManagerListener;

    private int moveDrawableId = -1;
    private int radiusDrawableId = -1;

    private float moveDrawableAnchorU;
    private float moveDrawableAnchorV;
    private float resizeDrawableAnchorU;
    private float resizeDrawableAnchorV;

    private boolean isSingleCircle = false;
    private LatLng geoLatLog;

    public interface CircleManagerListener {
        /**
         * Called when a circle was placed on the map
         *
         * @param draggableCircle created circle
         */
        void onCreateCircle(MapAreaWrapper draggableCircle);

        /**
         * Called when resizing gesture finishes (user lifts the finger)
         *
         * @param draggableCircle resized circle
         */
        void onResizeCircleEnd(MapAreaWrapper draggableCircle);

        /**
         * Called when move gesture finishes (user lifts the finger)
         *
         * @param draggableCircle move circle
         */
        void onMoveCircleEnd(MapAreaWrapper draggableCircle);

        /**
         * Called when move gesture starts (user long presses the position marker)
         *
         * @param draggableCircle circle about to be moved
         */
        void onMoveCircleStart(MapAreaWrapper draggableCircle);

        /**
         * Called when resize gesture starts (user long presses the resizing marker)
         *
         * @param draggableCircle circle about to be resized
         */
        void onResizeCircleStart(MapAreaWrapper draggableCircle);

        /**
         * Called when the circle reaches the min possible radius (meters), if it was initialized with a min radius value
         * This happens during resizing gesture
         * Reducing size is automatically blocked when reached this value - no extra action required for this
         *
         * @param draggableCircle circle
         */
        void onMinRadius(MapAreaWrapper draggableCircle);

        /**
         * Called when the circle reaches the max possible radius (meters), if it was initialized with a max radius value
         * This happens during resizing gesture
         * Increasing size is automatically blocked when reached this value - no extra action required for this
         *
         * @param draggableCircle circle
         */
        void onMaxRadius(MapAreaWrapper draggableCircle);
    }

    /**
     * Primary constructor
     *
     * @param map
     * @param strokeWidth           circle stroke with in pixels
     * @param strokeColor           circle stroke color
     * @param fillColor             circle fill color
     * @param moveDrawableId
     * @param moveDrawableId        drawable ressource id for positioning marker. If not set a default geomarker is used
     * @param resizeDrawableId      drawable ressource id for resizing marker. If not set a default geomarker is used
     * @param moveDrawableAnchorU   horizontal anchor for move drawable
     * @param moveDrawableAnchorV   vertical anchor for move drawable
     * @param resizeDrawableAnchorU horizontal anchor for resize drawable
     * @param resizeDrawableAnchorV vertical anchor for resize drawable
     * @param initRadius            init radius for all circles, currently supported pixels (constant in all zoom levels) or meters
     * @param circleManagerListener listener for circle events
     */
    public MapAreaManager(GoogleMap map, boolean isSingleCircle, LatLng geoLatLog, int strokeWidth, int strokeColor, int fillColor,
                          int moveDrawableId, int resizeDrawableId, float moveDrawableAnchorU, float moveDrawableAnchorV, float resizeDrawableAnchorU, float resizeDrawableAnchorV,
                          MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {

        this.map = map;
        this.isSingleCircle = isSingleCircle;
        this.geoLatLog = geoLatLog;
        this.circleManagerListener = circleManagerListener;

        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;

        this.moveDrawableId = moveDrawableId;
        this.radiusDrawableId = resizeDrawableId;

        this.moveDrawableAnchorU = moveDrawableAnchorU;
        this.moveDrawableAnchorV = moveDrawableAnchorV;
        this.resizeDrawableAnchorU = resizeDrawableAnchorU;
        this.resizeDrawableAnchorV = resizeDrawableAnchorV;

        this.initRadius = initRadius;

        map.setOnMarkerDragListener(this);

        // TODO : REMEMBER TO isLongClick ADD NEW FLAG , if isSingleCircle true small click is available
        if (isSingleCircle)
            map.setOnMapClickListener(this);
        else map.setOnMapLongClickListener(this);

        if (geoLatLog != null)
            setCircleManagerListener(geoLatLog);
    }

    /**
     * Convenience constructor
     * <p>
     * Will pass -1 as move and resize drawable resource id, with means we will use default geo markers
     *
     * @params see primary constructor
     */
    public MapAreaManager(GoogleMap map, boolean isSingleCircle, LatLng geoLatLog, int strokeWidth, int strokeColor, int circleColor,
                          MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {

        this(map, isSingleCircle, geoLatLog, strokeWidth, strokeColor, circleColor, -1, -1, initRadius, circleManagerListener);
    }

    /**
     * Convenience constructor
     * <p>
     * Uses default values for marker's drawable anchors
     *
     * @params see primary constructor
     */
    public MapAreaManager(GoogleMap map, boolean isSingleCircle, LatLng geoLatLog, int strokeWidth, int strokeColor, int circleColor,
                          int moveDrawableId, int radiusDrawableId,
                          MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {

        this(map, isSingleCircle, geoLatLog, strokeWidth, strokeColor, circleColor, moveDrawableId, radiusDrawableId, 0.5f, 1f, 0.5f, 1f, initRadius, circleManagerListener);
    }

    public List<MapAreaWrapper> getCircles() {
        return areas;
    }

    /**
     * Set min radius in meters. The circles will shrink bellow this, and onMinRadius will be called when reached
     *
     * @param minRadius
     */
    public void setMinRadius(int minRadius) {
        this.minRadiusMeters = minRadius;
    }

    /**
     * Set min radius in meters. The circles will expand above this, and onMaxRadius will be called when reached
     *
     * @param maxRadius
     */
    public void setMaxRadius(int maxRadius) {
        this.maxRadiusMeters = maxRadius;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
            case minRadius: {
                circleManagerListener.onMinRadius(result.draggableCircle);
                break;
            }
            case maxRadius: {
                circleManagerListener.onMaxRadius(result.draggableCircle);
                break;
            }
            case radiusChange: {
                circleManagerListener.onResizeCircleStart(result.draggableCircle);
                break;
            }
            case moved: {
                circleManagerListener.onMoveCircleStart(result.draggableCircle);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
            case minRadius: {
                circleManagerListener.onMinRadius(result.draggableCircle);
                break;
            }
            case maxRadius: {
                circleManagerListener.onMaxRadius(result.draggableCircle);
                break;
            }
            case radiusChange: {
                circleManagerListener.onResizeCircleEnd(result.draggableCircle);
                break;
            }
            case moved: {
                circleManagerListener.onMoveCircleEnd(result.draggableCircle);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
            case minRadius: {
                circleManagerListener.onMinRadius(result.draggableCircle);
                break;
            }
            case maxRadius: {
                circleManagerListener.onMaxRadius(result.draggableCircle);
                break;
            }
            default:
                break;
        }
    }

    public void add(MapAreaWrapper draggableCircle) {
        areas.add(draggableCircle);
    }

    /**
     * Wrapper for result of gesture with affected circle
     */
    private class MarkerMoveResultWithCircle {
        MarkerMoveResult markerMoveResult;
        MapAreaWrapper draggableCircle;

        public MarkerMoveResultWithCircle(MarkerMoveResult markerMoveResult, MapAreaWrapper draggableCircle) {
            this.markerMoveResult = markerMoveResult;
            this.draggableCircle = draggableCircle;
        }
    }

    /**
     * When marker is moved, notify circles
     * The circle containing the marker will execute necessary actions
     *
     * @param marker
     * @return
     */
    private MarkerMoveResultWithCircle onMarkerMoved(Marker marker) {
        MarkerMoveResult result = MarkerMoveResult.none;
        MapAreaWrapper affectedDraggableCircle = null;

        for (MapAreaWrapper draggableCircle : areas) {
            result = draggableCircle.onMarkerMoved(marker);
            if (result != MarkerMoveResult.none) {
                affectedDraggableCircle = draggableCircle;
                break;
            }
        }
        return new MarkerMoveResultWithCircle(result, affectedDraggableCircle);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        setCircleManagerListener(point);
    }


    @Override
    public void onMapClick(LatLng point) {
        setCircleManagerListener(point);
    }


    private void setCircleManagerListener(LatLng point) {
        double initRadiusMetersFinal;

        if (initRadius.unit == MapAreaMeasure.Unit.meters) { //init with meters radius

            initRadiusMetersFinal = initRadius.value;

        } else { //init with pixels radius
            Point screenCenterPoint = map.getProjection().toScreenLocation(point);
            LatLng radiusLatLng = map.getProjection().fromScreenLocation(new Point(screenCenterPoint.x + (int) initRadius.value, screenCenterPoint.y));
            initRadiusMetersFinal = MapUtils.toRadiusMeters(point, radiusLatLng);
        }

        if (isSingleCircle) {
            map.clear();
            MapAreaWrapper circle = new MapAreaWrapper(map, point, initRadiusMetersFinal, strokeWidth, strokeColor, fillColor, minRadiusMeters, maxRadiusMeters,
                    moveDrawableId, radiusDrawableId, moveDrawableAnchorU, moveDrawableAnchorV, resizeDrawableAnchorU, resizeDrawableAnchorV);

            areas.add(0, circle);

            circleManagerListener.onCreateCircle(circle);

            return;
        }
        MapAreaWrapper circle = new MapAreaWrapper(map, point, initRadiusMetersFinal, strokeWidth, strokeColor, fillColor, minRadiusMeters, maxRadiusMeters,
                moveDrawableId, radiusDrawableId, moveDrawableAnchorU, moveDrawableAnchorV, resizeDrawableAnchorU, resizeDrawableAnchorV);

        areas.add(circle);

        circleManagerListener.onCreateCircle(circle);
    }

    public void setBoundsAnimateCamera(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        LatLngBounds bounds = boundsWithCenterAndLatLngDistance(center, latDistanceInMeters, lngDistanceInMeters);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
    }

    private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float ACCURACY = 0.01f;

    private LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        latDistanceInMeters /= 2;
        lngDistanceInMeters /= 2;
        LatLngBounds.Builder builder = LatLngBounds.builder();
        float[] distance = new float[1];
        {
            boolean foundMax = false;
            double foundMinLngDiff = 0;
            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
                float distanceDiff = distance[0] - lngDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLngDiff = assumedLngDiff;
                        assumedLngDiff *= 2;
                    } else {
                        double tmp = assumedLngDiff;
                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
                        foundMinLngDiff = tmp;
                    }
                } else {
                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
            builder.include(east);
            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
            builder.include(west);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffNorth;
                        assumedLatDiffNorth *= 2;
                    } else {
                        double tmp = assumedLatDiffNorth;
                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
            builder.include(north);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffSouth;
                        assumedLatDiffSouth *= 2;
                    } else {
                        double tmp = assumedLatDiffSouth;
                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
            builder.include(south);
        }
        return builder.build();
    }
}

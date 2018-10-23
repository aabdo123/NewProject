package com.utilities.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.R;
import com.utilities.AppUtils;

import java.util.List;

import static com.activities.BaseActivity.mContext;

public class MapUtils {

    public static String[] getAddressByLatLng(Context context, LatLng position) {
        String[] strings = new String[0];
        Geocoder geocoder;
        List<Address> addresses;
//        geocoder = new Geocoder(context, Locale.getDefault());
        geocoder = new Geocoder(context, AppUtils.getLocale());
        try {
            addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Address returnedAddress = addresses.get(0);

            String city = returnedAddress.getSubLocality();
            if (city == null)
                city = returnedAddress.getLocality();
            else
                city = returnedAddress.getSubLocality();
            String state = returnedAddress.getAdminArea();
            String country = returnedAddress.getCountryName();
            String street = returnedAddress.getAddressLine(0);

//            StringBuilder strReturnedAddress = new StringBuilder();
//            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
//            }
//            String street = strReturnedAddress.toString();

            strings = new String[]{city != null ? city : state, state, country, street};
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings;
    }

    public static void openMap(Context context, double latitude, double longitude, String locationName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + latitude + ">,<" + longitude + ">?q=<" + latitude + ">,<" + longitude + ">(" + locationName + ")"));
//                        String uri = String.format(Locale.getDefault(), "geo:%f,%f", latitude, longitude);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static MarkerOptions createMarker(LatLng position, BitmapDescriptor bitmapDescriptor) {
        return new MarkerOptions()
                .position(position)
                .icon(bitmapDescriptor)
                .anchor(0.5f, 0.5f);
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(@DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(mContext, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / MapAreasConstants.RADIUS_OF_EARTH_METERS) / Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return result[0];
    }

    public static int getZoomLevel(Circle circle) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    public static int getZoomLevel(double radius) {
        double scale = radius / 500;
        return (int) (16 - Math.log(scale) / Math.log(2)) - 1;
    }

//    public static MapStyleOptions setSelectedStyle(){
//        MapStyleOptions style;
//        switch (mSelectedStyleId) {
//            case R.string.style_label_retro:
//                // Sets the retro style via raw resource JSON.
//                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro);
//                break;
//            case R.string.style_label_night:
//                // Sets the night style via raw resource JSON.
//                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
//                break;
//            case R.string.style_label_grayscale:
//                // Sets the grayscale style via raw resource JSON.
//                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_grayscale);
//                break;
//            case R.string.style_label_default:
//                // Removes previously set style, by setting it to null.
//                style = null;
//                break;
//            default:
//                return;
//        }
//    }


    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = googleMap.getProjection();
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarker) {
//                        marker.setVisible(false);
//                    } else {
//                        marker.setVisible(true);
//                    }
//                }
//            }
//        });
    }

}

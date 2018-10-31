package com.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.application.MyApplication;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.BuildConfig;
import com.R;
import com.activities.LoginActivity;
import com.managers.PreferencesManager;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

import java.util.Locale;

/**
 * Created by malikabuqauod on 5/16/17.
 */

public class AppUtils {

    public static boolean isAstroGps() {
        return BuildConfig.FLAVOR.equals("astroGps");
    }

    public static boolean isArabic() {
        return PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR);
    }

    public static Locale getLocale() {
        try {
            if (PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR)) {
                return new Locale("ar");
            } else if (PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR)) {
                return Locale.ENGLISH;
            } else {
                return Locale.getDefault();
            }
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }

    public static String getRouteLanguage() {
        if (PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR)) {
            return AppConstant.LANGUAGE_AR;
        } else {
            return AppConstant.LANGUAGE_EN;
        }
    }

    public static String getDirectionDegree(Context context, double degrees) {
//        String[] directions = {"North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest", "North"};
        String[] directions = context.getResources().getStringArray(R.array.direction);
        return directions[(int) Math.round(((degrees % 360) / 45)) % 8];
    }

    public static double secondsToHours(double value) {
        return value / (60 * 60);
    }

    public static double meterToKilometer(double value) {
        return value / 1000;
    }

    public static String dateForm(int year, int month, int dayOfMonth) {
        return year + "/" + month + "/" + dayOfMonth;
    }

    public static String dateForm(int month, int dayOfMonth) {
        return month + "/" + dayOfMonth;
    }

    public static BitmapDescriptor getLandMarkerIcon(String iconName) {
        if (iconName.contains("Bank")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);
        } else if (iconName.contains("Building")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_building);
        } else if (iconName.contains("Home")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_home_landmark);
        } else if (iconName.contains("Office")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_office);
        } else if (iconName.contains("School")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_school);
        } else if (iconName.contains("University")) {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_university);
        } else {
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);
        }
    }

//    public static BitmapDescriptor getLandMarkerIcon(String iconName) {
//        switch (iconName) {
//            case "Bank":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_bank);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);
//
//            case "Building":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_building);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_building);
//
//            case "Home":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_home);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_home_landmark);
//
//            case "Office":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_office);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_office);
//
//            case "School":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_school);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_school);
//
//            case "University":
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_university);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_university);
//
//            default:
////                return BitmapDescriptorFactory.fromResource(R.drawable.marker_bank);
//                return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);
//        }
//    }

    public static BitmapDescriptor getLandMarkerIcon(int landmarkId) {
        switch (landmarkId) {
            case R.string.bank:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_bank);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);

            case R.string.building:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_building);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_building);

            case R.string.home:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_home);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_home_landmark);

            case R.string.office:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_office);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_office);

            case R.string.school:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_school);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_school);

            case R.string.university:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_university);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_university);

            default:
//                return BitmapDescriptorFactory.fromResource(R.drawable.marker_bank);
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_bank);
        }
    }

    public static BitmapDescriptor getCarIcon(String carStatus) {
        switch (carStatus) {
            case "600":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_0);
            case "5":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_0);
            case "1":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_2);
            case "0":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_600);
            case "2":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_1);
            case "101":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_101);
            case "100":
                return BitmapDescriptorFactory.fromResource(R.drawable.car_100);
            default:
                return BitmapDescriptorFactory.fromResource(R.drawable.car_0);
        }
    }

    public static BitmapDescriptor getCarIconAlpha(String carStatus) {
        switch (carStatus) {
            case "600":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_0)));
            case "5":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_0)));
            case "1":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_2)));
            case "0":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_600)));
            case "2":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_1)));
            case "101":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_101)));
            case "100":
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_100)));
            default:
                return BitmapDescriptorFactory.fromBitmap(drawAlpha(BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(),R.drawable.car_0)));
        }
    }

    public static Bitmap drawAlpha(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(50);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }

    public static Drawable getCarIconDrawable(Context context, String carStatus) {
        switch (carStatus) {
            case "600":
                return context.getResources().getDrawable(R.drawable.car_0);
            case "5":
                return context.getResources().getDrawable(R.drawable.car_0);
            case "1":
                return context.getResources().getDrawable(R.drawable.car_2);
            case "0":
                return context.getResources().getDrawable(R.drawable.car_600);
            case "2":
                return context.getResources().getDrawable(R.drawable.car_1);
            case "101":
                return context.getResources().getDrawable(R.drawable.car_101);
            case "100":
                return context.getResources().getDrawable(R.drawable.car_100);
            default:
                return context.getResources().getDrawable(R.drawable.car_0);
        }
    }

    public static int getCarIconDrawableID(String carStatus) {
        switch (carStatus) {
            case "600":
                return R.drawable.car_0;
            case "5":
                return R.drawable.car_0;
            case "1":
                return R.drawable.car_2;
            case "0":
                return R.drawable.car_600;
            case "2":
                return R.drawable.car_1;
            case "101":
                return R.drawable.car_101;
            case "100":
                return R.drawable.car_100;
            default:
                return R.drawable.car_0;
        }
    }


    public static String getCarStatus(Activity activity, String carStatus) {
        String status = carStatus;
        switch (carStatus) {
            case "600":
                status = activity.getString(R.string.offline);
                break;
            case "5":
                status = activity.getString(R.string.offline);
                break;
            case "1":
                status = activity.getString(R.string.running);
                break;
            case "0":
                status = activity.getString(R.string.stopped);
                break;
            case "2":
                status = activity.getString(R.string.idle);
                break;
            case "101":
                status = activity.getString(R.string.over_speed);
                break;
            case "100":
                status = activity.getString(R.string.over_street_speed);
                break;
            default:
                status = activity.getString(R.string.stopped);
                break;
        }
        return status;
    }

    public static boolean checkLocationPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = activity.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermission(activity);
                return false;
            }
        } else {
            return true;
        }
    }

    private static void requestPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstant.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstant.PERMISSION_REQUEST_CODE);
        }
    }

    public static void endSession(Activity activity) {
        PreferencesManager.getInstance().clear();
//        PreferencesManager.getInstance().setStringValue("", SharesPrefConstants.TOKEN_TYPE);
//        PreferencesManager.getInstance().setStringValue("", SharesPrefConstants.ACCESS_TOKEN);
//        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_LOGIN);
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
//        Intent i = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.startActivity(i);
    }

    public static String getYoutubeVideoImage(Context context, String youtubeUrl) {
//        String url = "https://img.youtube.com/vi/bTjJSGGEkF8/mqdefault.jpg";
        String imageUrl = "";
        try {
            if (youtubeUrl.contains("youtu.be")) {
                String[] separated = youtubeUrl.split("youtu.be/");
                String youtubeId = separated[1].trim();
                imageUrl = "https://img.youtube.com/vi/" + youtubeId + "/default.jpg";
            } else if (youtubeUrl.contains("watch?v=")) {
                String[] separated = youtubeUrl.split("v=");
                String youtubeId = separated[1].trim();
                imageUrl = "https://img.youtube.com/vi/" + youtubeId + "/default.jpg";
            } else if (youtubeUrl.contains("embed/")) {
                String[] separated = youtubeUrl.split("embed/");
                String youtubeId = separated[1].trim();
                imageUrl = "https://img.youtube.com/vi/" + youtubeId + "/default.jpg";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    public static void openYoutubeVideo(Context context, String youtubeUrl) {
        String url = "";
        try {
            if (youtubeUrl.contains("watch?v=")) {
                String[] separated = youtubeUrl.split("watch?v=");
                String youtubeId = separated[1];
                url = "https://youtu.be/" + youtubeId;
            } else if (youtubeUrl.contains("embed/")) {
                String[] separated = youtubeUrl.split("embed/");
                String youtubeId = separated[1];
                url = "https://youtu.be/" + youtubeId;
            } else {
                url = youtubeUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static String youtubeVideoUrl(Context context, String youtubeUrl) {
        String youtubeId = "";
        try {
            if (youtubeUrl.contains("youtu.be/")) {
                String[] separated = youtubeUrl.split("https://youtu.be/");
                youtubeId = separated[1].trim();
            } else if (youtubeUrl.contains("embed/")) {
                String[] separated = youtubeUrl.split("embed/");
                youtubeId = separated[1].trim();
            } else if (youtubeUrl.contains("watch?v=")) {
                String[] separated = youtubeUrl.split("v=");
                youtubeId = separated[1].trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return youtubeId;
    }
}
package com.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.R;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.utilities.constants.ApiConstants;
import com.utilities.constants.AppConstant;
import com.views.ClickWithTwoParam;

import static com.activities.BaseActivity.mContext;
import static com.utilities.constants.ApiConstants.GOOGLE_API_KEY;

/**
 * Created by Saferoad-Dev1 on 9/6/2017.
 */

public class Route {

    GoogleMap mMap;
    Context context;
    String lang;

//    public static String LANGUAGE_ARABIC = "ar";
//    public static String LANGUAGE_ENGLISH = "en";

    static String TRANSPORT_DRIVING = "driving";
    static String TRANSPORT_WALKING = "walking";
    static String TRANSPORT_BIKE = "bicycling";
    static String TRANSPORT_TRANSIT = "transit";
    public Polyline line;
    List<Polyline> polylines = new ArrayList<Polyline>();
    private String distance;
    private String duration;
    private String distanceValue;
    private String durationValue;
    private ClickWithTwoParam clickWithTwoParam;

    public void setDistanceListener(ClickWithTwoParam clickWithTwoParam) {
        this.clickWithTwoParam = clickWithTwoParam;
    }

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, boolean withIndications, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
//            new connectAsyncTask(url, withIndications).execute();
            drawPathApiCall(url, withIndications);
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, "driving", optimize);
//            new connectAsyncTask(url, withIndications).execute();
            drawPathApiCall(url, withIndications);
            return true;
        }
        return false;
    }

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, "driving");
//            new connectAsyncTask(url, false).execute();
            drawPathApiCall(url, false);
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, "driving", optimize);
//            new connectAsyncTask(url, false).execute();
            drawPathApiCall(url, false);
            return true;
        }
        return false;
    }

    public boolean drawRoute(GoogleMap map, Context c, ArrayList<LatLng> points, String mode, boolean withIndications, String language, boolean optimize) {
        mMap = map;
        context = c;
        lang = language;
        if (points.size() == 2) {
            String url = makeURL(points.get(0).latitude, points.get(0).longitude, points.get(1).latitude, points.get(1).longitude, mode);
//            new connectAsyncTask(url, withIndications).execute();
            drawPathApiCall(url, withIndications);
            return true;
        } else if (points.size() > 2) {
            String url = makeURL(points, mode, optimize);
//            new connectAsyncTask(url, withIndications).execute();
            drawPathApiCall(url, withIndications);
            return true;
        }
        return false;
    }

    public void drawMultiRoute(GoogleMap map, Context c, LatLng source, LatLng dest, boolean withIndications, String language) {
        try {
            mMap = map;
            context = c;
            if (line != null) {
                line.remove();
            }

            lang = language;
            String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
//        new connectAsyncTask(url, withIndications).execute();
            drawMultiPathApiCall(url, withIndications);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, boolean withIndications, String language) {
        mMap = map;
        context = c;
        if (line != null) {
            line.remove();
        }

        lang = language;
        String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
//        new connectAsyncTask(url, withIndications).execute();
        drawPathApiCall(url, withIndications);
    }

    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, String language) {
        mMap = map;
        context = c;

        lang = language;
        String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, "driving");
//        new connectAsyncTask(url, false).execute();
        drawPathApiCall(url, false);

    }


    public void drawRoute(GoogleMap map, Context c, LatLng source, LatLng dest, String mode, boolean withIndications, String language) {
        mMap = map;
        context = c;

        lang = language;
        String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude, mode);
//        new connectAsyncTask(url, withIndications).execute();
        drawPathApiCall(url, withIndications);

    }

    private String makeURL(ArrayList<LatLng> points, String mode, boolean optimize) {
        StringBuilder urlString = new StringBuilder();

        if (mode == null)
            mode = "driving";

        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(points.get(0).latitude);
        urlString.append(',');
        urlString.append(points.get(0).longitude);
        urlString.append("&destination=");
        urlString.append(points.get(points.size() - 1).latitude);
        urlString.append(',');
        urlString.append(points.get(points.size() - 1).longitude);

        urlString.append("&waypoints=");
        if (optimize)
            urlString.append("optimize:true|");
        urlString.append(points.get(1).latitude);
        urlString.append(',');
        urlString.append(points.get(1).longitude);

        for (int i = 2; i < points.size() - 1; i++) {
            urlString.append('|');
            urlString.append(points.get(i).latitude);
            urlString.append(',');
            urlString.append(points.get(i).longitude);
        }
        urlString.append("&sensor=true&mode=" + mode);
        return urlString.toString();
    }

    private String makeURL(double sourcelat, double sourcelog, double destlat, double destlog, String mode) {
        StringBuilder urlString = new StringBuilder();

        if (mode == null)
            mode = "driving";

        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=" + mode + "&alternatives=true&language=" + lang + "&key=" + GOOGLE_API_KEY);
        return urlString.toString();
    }

    public void clearRoute() {
        if (!Utils.isNotEmptyList(polylines)) {
            return;
        }
        for (Polyline line1 : polylines) {
            line1.remove();
        }
        polylines.clear();
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawMultiPathApiCall(String urlPass, final boolean withSteps) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.fetching_route_please_wait));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Utils.checkConnectingToInternet(mContext);
        BusinessManager.getJsonFromUrl(urlPass, new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                progressDialog.dismiss();
                if (responseObject != null) {
                    drawMultiPath(responseObject, withSteps);
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                progressDialog.dismiss();
            }
        });
    }


    private void drawPathApiCall(String urlPass, final boolean withSteps) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.fetching_route_please_wait));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Utils.checkConnectingToInternet(mContext);
        BusinessManager.getJsonFromUrl(urlPass, new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                progressDialog.dismiss();
                if (responseObject != null) {
                    drawPath(responseObject, withSteps);
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                progressDialog.dismiss();
            }
        });
    }

//    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
//        private ProgressDialog progressDialog;
//        String url;
//        boolean steps;
//
//        connectAsyncTask(String urlPass, boolean withSteps) {
//            url = urlPass;
//            steps = withSteps;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Fetching route, Please wait...");
//            progressDialog.setIndeterminate(true);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            JSONParser jParser = new JSONParser();
//            String json = jParser.getJSONFromUrl(url);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.hide();
//            if (result != null) {
//                drawPath(result, steps);
//            }
//        }
//    }


    private void drawMultiPath(String result, boolean withSteps) {
        try {
//            clearRoute();
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            if (routeArray.length() == 0) {
                clickWithTwoParam.onClick("error", "error", "error", "error");
                return;
            }
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            if (line != null) {
                line.remove();
            }

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(AppConstant.POLY_THICKNESS)
                        .color(ContextCompat.getColor(context, R.color.route))
                        .geodesic(true));
                polylines.add(line);
            }

            JSONArray arrayLegs = routes.getJSONArray("legs");
            JSONObject legs = arrayLegs.getJSONObject(0);

            JSONObject distanceObj = legs.getJSONObject("distance");
            distance = distanceObj.getString("text");
            distanceValue = distanceObj.getString("value");

            JSONObject durationObj = legs.getJSONObject("duration");
            duration = durationObj.getString("text");
            durationValue = durationObj.getString("value");

            if (clickWithTwoParam != null)
                clickWithTwoParam.onClick(distance, duration, distanceValue, durationValue);
            if (withSteps) {
//                JSONArray arrayLegs = routes.getJSONArray("legs");
//                JSONObject legs = arrayLegs.getJSONObject(0);
                JSONArray stepsArray = legs.getJSONArray("steps");
                //put initial point

                for (int i = 0; i < stepsArray.length(); i++) {
                    Step step = new Step(stepsArray.getJSONObject(i));
                    mMap.addMarker(new MarkerOptions()
                            .position(step.location)
                            .title(step.distance)
                            .snippet(step.instructions)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            clickWithTwoParam.onClick("error", "error", "error", "error");
        }
    }

    private void drawPath(String result, boolean withSteps) {
        try {
            clearRoute();
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");

            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            if (line != null) {
                line.remove();
            }

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(AppConstant.POLY_THICKNESS)
                        .color(ContextCompat.getColor(context, R.color.route))
                        .geodesic(true));
                polylines.add(line);
            }

            JSONArray arrayLegs = routes.getJSONArray("legs");
            JSONObject legs = arrayLegs.getJSONObject(0);

            JSONObject distanceObj = legs.getJSONObject("distance");
            distance = distanceObj.getString("text");
            distanceValue = distanceObj.getString("value");

            JSONObject durationObj = legs.getJSONObject("duration");
            duration = durationObj.getString("text");
            durationValue = durationObj.getString("value");

            if (clickWithTwoParam != null)
                clickWithTwoParam.onClick(distance, duration, distanceValue, durationValue);
            if (withSteps) {
//                JSONArray arrayLegs = routes.getJSONArray("legs");
//                JSONObject legs = arrayLegs.getJSONObject(0);
                JSONArray stepsArray = legs.getJSONArray("steps");
                //put initial point

                for (int i = 0; i < stepsArray.length(); i++) {
                    Step step = new Step(stepsArray.getJSONObject(i));
                    mMap.addMarker(new MarkerOptions()
                            .position(step.location)
                            .title(step.distance)
                            .snippet(step.instructions)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            clickWithTwoParam.onClick("error", "error", "error", "error");
        }
    }

    /**
     * Class that represent every step of the directions. It store distance, location and instructions
     */

    private class Step {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON) {
            JSONObject startLocation;
            try {
                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
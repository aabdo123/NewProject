package com.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.R;
import com.application.MyApplication;
import com.views.AlertDialogView;
import com.views.Click;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// GENRAL ////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static void checkConnectingToInternet(Context _context) {
        if (!Utils.isConnectingToInternet(_context)) {
            AlertDialogView.oneButtonDialog(_context, _context.getString(R.string.please_check_your_internet_connection), _context.getString(R.string.connection_error), _context.getString(R.string.done), new Click() {
                @Override
                public void onClick() {
                }
            });
        }
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isNotEmptyList(List<?> list) {
        return list != null && list.size() != 0;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getSimSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerial = tm.getSimSerialNumber();
            return simSerial;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDeviceId(Context context) {
        String devceId = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            devceId = tm.getDeviceId();
            return devceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devceId;
    }

    public static String getUniqueDeviceId(Context context) {
        String android_id = "";
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            android_id = getDeviceId(context);
        }
        return android_id;
    }

    public static void hideKeyboardOnSubmit(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            Log.e("HIDE_KEY", "hideKeyboardOnSubmit: ");
        }
    }

    public static void hidKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static double getRandomNumber(double max, double min){
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static int getRandomNumber(int max, int min){
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// INTENT /////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////


    public static void openUrl(AppCompatActivity activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static void openActivity(AppCompatActivity activity, Class className) {
        activity.startActivity(new Intent(activity, className));
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void finishAndOpenActivity(AppCompatActivity activity, Class className) {
        activity.finish();
        activity.startActivity(new Intent(activity, className));
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void openGPSSettings(Context context) {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(gpsOptionsIntent);
    }

    public static void openPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// TIME AND DATE /////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static String time24H(int selectedHour, int selectedMinute) {
        String hourString = selectedHour < 10 ? "0" + selectedHour : "" + selectedHour;
        String minuteString = selectedMinute < 10 ? "0" + selectedMinute : "" + selectedMinute;
        return hourString + ":" + minuteString;
    }

    public static String getTimeNow() throws Exception {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public static Date getTimeNowDATE() throws Exception {
        Date currentDate = new Date(System.currentTimeMillis());
        return currentDate;
    }

    public static long convertDayToMilliseconds(int days) throws Exception {
        int milliseconds = (int) (days * (1000 * 60 * 60 * 24));
        return milliseconds;
    }

    public static long getCurrentDate() throws Exception {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        return seconds;
    }

    public static CharSequence createDate(long timestamp) {
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        c.setTimeInMillis(timestamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        return sdf.format(d);
    }

    public static String formattedTime(long min, long sec) {
        String minStr = "";
        String secStr = "";
        if (min < 10) {
            minStr = "0";
        }
        if (sec < 10) {
            secStr = "0";
        }
        minStr += min;
        secStr += sec;
        return minStr + ":" + secStr;
    }

    public static String getStartMonth() {
        Calendar startTime = Calendar.getInstance();
        startTime.add(Calendar.MONTH, -1);
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        return "" + (startTime.getTimeInMillis() - 10000) / 1000;
    }

    public static String getEndMonth() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        return "" + startTime.getTimeInMillis() / 1000;
    }

    public static String getStringTime(String str) {
        long millisecond = Long.parseLong(str + "000");
        Date now = new Date(millisecond);
        String dateString = Utils.formattedTime(now.getHours(), now.getMinutes());
        return dateString;
    }

    public static String getStringTimeNow(String str) {
        long millisecond = Long.parseLong(str);
        Date now = new Date(millisecond);
        String dateString = Utils.formattedTime(now.getHours(), now.getMinutes());
        return dateString;
    }

    public static String getDateUtcToTimeStamp(String stringDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }

    public static String getDateUtcToSameFormat(String myUtc) {
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            Date getDate = existingUTCFormat.parse(myUtc);
            return requiredFormat.format(utcTtoLocalDate(getDate));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return myUtc;
//            return "";
        }
    }

    public static String getDateUpUtcToNormalFormat(String myUtc) {
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date getDate = existingUTCFormat.parse(myUtc);
            return requiredFormat.format(utcTtoLocalDate(getDate));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return myUtc;
        }
    }

    public static String getDateUpUtcToNormalFormatAM_PM(String myUtc) {
        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date getDate = existingUTCFormat.parse(myUtc);
            return requiredFormat.format(utcTtoLocalDate(getDate));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return myUtc;
        }
    }

    private static Date utcTtoLocalDate(Date date) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        return new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// CONVERT TO ////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static float getDistanceBetweenToLocations(String pointA, double latA, double logA, String pointB, double latB, double logB) {
        Location locationA = new Location(pointA);

        locationA.setLatitude(latA);
        locationA.setLongitude(logA);

        Location locationB = new Location(pointB);

        locationB.setLatitude(latB);
        locationB.setLongitude(logB);

        return locationA.distanceTo(locationB);
//        float d = distance/1000;      // to km
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static String convertListToArray(List<String> selectedList) {
        String selected = "";
        String array;
        if (selectedList.size() > 0) {
            for (int x = 0; x < selectedList.size(); x++) {
                selected = selected + selectedList.get(x) + ",";
            }
            array = "[" + selected.substring(0, selected.length() - 1) + "]";
            Log.d("selected", selected);
            Log.d("array", array);
            return array;
        } else {
            array = "[" + "]";
            Log.d("array", array);
            return array;
        }
    }

    public static String convertListToStringWithComaSpreader(List<String> selectedList) {
        String selectedService = "";
        if (selectedList.size() > 0) {
            for (int x = 0; x < selectedList.size(); x++) {
                selectedService = selectedService + selectedList.get(x) + ",";
            }
            selectedService = selectedService.substring(0, selectedService.length() - 1);
            return selectedService;
        } else {
            selectedService = selectedList.get(0);
            return selectedService;
        }
    }

    public static String audioToBase64(String uri) {
        byte[] bytes = new byte[0];
        try {
            bytes = Utils.readFile(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes, 0);
        Log.e("~~~~~~~~ Encoded  ", encoded);
        return encoded;
    }

    public static String toBase64(String uri) {
        File imagefile = new File(uri);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("~~~~~~BASE64", encImage);
        return encImage;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = MyApplication.getAppContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = MyApplication.getAppContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static Uri convertToUri(String uri) {
        Uri myUri = null;
        try {
            myUri = Uri.parse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myUri;
    }

//    public static String doubleToString(double value) {
//        return String.valueOf(value);
//    }
//

    public static String doubleToString(double value) {
        return String.format(Locale.getDefault(), "%.3f", value);
    }

//    public static String doubleToString(double value){
//        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
//        return REAL_FORMATTER.format(value);
//    }

    public static int colorInt(String colorHex){
        return Color.parseColor(colorHex);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// IMAGES, BITMAP, and READ FILES///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static String loadJSONFile(Context context, String name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static Bitmap getImageBitmapFromURI(Uri uri, Context context) {

        InputStream inputStream = null;
        Bitmap bm = null;
        try {
            inputStream = context.getContentResolver()
                    .openInputStream(uri);
            bm = decodeSampledBitmapFromResource(inputStream, 200, 200);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static Bitmap decodeSampledBitmapFromResource(InputStream inputStream, int reqWidth, int reqHeight) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is1, null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(is2, null, options);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }


        return inSampleSize;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap imageBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return b;
    }

    public static void imageFromView(Context context, View view, String imageName) {
        if (context == null) {
            context = MyApplication.getAppContext();
        }
        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + imageName + "_" + createDate(System.currentTimeMillis()) + ".jpg";

            view.setDrawingCacheEnabled(true);
            Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            b.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static Bitmap fastBlur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static Bitmap createReflectedImage(Bitmap originalImage, int reflectionHeight) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        // Realize image flip 90 degrees
        matrix.preScale(1, -1);
        if (reflectionHeight > height)
            reflectionHeight = height;

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height - reflectionHeight, width, reflectionHeight, matrix, false);

        Bitmap finalReflection = Bitmap.createBitmap(width, reflectionHeight, Bitmap.Config.ARGB_8888);
        // Create canvas
        Canvas canvas = new Canvas(finalReflection);

        canvas.drawBitmap(reflectionImage, 0, 0, null);
        Paint shaderPaint = new Paint();
        // Create a linear gradient LinearGradient object
        LinearGradient shader = new LinearGradient(0, 0, 0, finalReflection.getHeight() + 1, 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
        shaderPaint.setShader(shader);
        shaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Canvas painting by inverted image size region, and then the gradient effect to the picture, appeared the reflection effect.
        canvas.drawRect(0, 0, width, finalReflection.getHeight(), shaderPaint);
        return finalReflection;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////     DOWNLOAD    /////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

//    public static void downloadFile(Context context, String title, String url) {
//        if ((title == null) || (title.equalsIgnoreCase("null"))) {
//            title = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
//
//        }
//        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + title + url.substring(url.lastIndexOf(".")));
//        try {
//            f.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        final ProgressDialog progress = new ProgressDialog(context);
//
//        //  progress.setTitle(title);
//        progress.setMessage("تنزيل " + title + " " + "الى المحفوظات");
//        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progress.setIndeterminate(false);
//        progress.setProgress(0);
//        progress.show();
//        Ion.with(context)
//                .load(url)
//
//                .progressDialog(progress)
//
//                .progress(new ProgressCallback() {
//                    @Override
//                    public void onProgress(long downloaded, long total) {
//                        int cc = (int) ((downloaded * 100 / total));
//                        progress.setProgress(cc);
//
//                    }
//
//
//                })
//                .write(f)
//                .setCallback(new FutureCallback<File>() {
//                    @Override
//                    public void onCompleted(Exception e, File file) {
//                        // download done...
//                        // do stuff with the File or error
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(2000);
//                                    progress.dismiss();
//                                } catch (Exception e) {
//                                }
//                            }
//                        }).start();
//
//
//                    }
//                });
//    }

}

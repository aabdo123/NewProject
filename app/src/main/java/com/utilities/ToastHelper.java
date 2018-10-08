package com.utilities;

import android.content.Context;
import android.widget.Toast;

import com.valdesekamdem.library.mdtoast.MDToast;

public class ToastHelper {

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessageLong(Context context, String message) {
        // long is int = -1
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toastInfo(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show();
    }

    public static void toastInfoLong(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_INFO).show();
    }

    public static void toastError(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
    }

    public static void toastErrorLong(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
    }

    public static void toastWarning(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING).show();
    }

    public static void toastWarningLong(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_WARNING).show();
    }

    public static void toastSuccess(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS).show();
    }

    public static void toastSuccessLong(Context context, String message) {
        MDToast.makeText(context, message, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
    }
}

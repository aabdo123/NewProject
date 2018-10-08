package com.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.R;
import com.wang.avi.AVLoadingIndicatorView;

public class Progress {

    private static ProgressDialog progresses;
    private static AVLoadingIndicatorView avLoadingIndicatorView;
    private static AlertDialog alertDialog = null;

    public Progress() {
    }

    public static void showProgressDialog (final Context context, String title, String message) throws Exception {
        progresses = new ProgressDialog(context);
        if (title != null)
            progresses.setTitle(title);
        progresses.setMessage(message);
        progresses.setCancelable(false);
        progresses.show();
    }

    public static void showProgressDialogSmall (final Context context, String message) throws Exception {
        progresses = new ProgressDialog(context);
        if (message != null)
            progresses.setMessage(message);
        progresses.setCancelable(true);
        progresses.show();
    }

    public static void dismissProgressDialog() throws Exception{
        if (progresses != null && progresses.isShowing())
            progresses.dismiss();
    }

    ////////////////////////////////////

    public static void showLoadingDialog(Activity activity){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_loading_dialog, null);
        avLoadingIndicatorView = (AVLoadingIndicatorView) dialogView.findViewById(R.id.avi);

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        alertDialog.setCancelable(false);
        alertDialog.show();
        avLoadingIndicatorView.show();
        // avi.smoothToShow();
    }

    public static void dismissLoadingDialog(){
        if (alertDialog != null && alertDialog.isShowing()) {
            avLoadingIndicatorView.hide();
            alertDialog.dismiss();
        }
        alertDialog = null;
    }

}

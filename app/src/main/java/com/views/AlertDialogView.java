package com.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.R;
import com.utilities.constants.AppConstant;

import java.util.Arrays;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class AlertDialogView {

//    private static AlertDialog alertDialog = null;

    public static void yesNoButtonDialog(Activity activity, Context context, String message, String title, Click clickListener) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.layout_two_button_dailog, null);
            ButtonBold yesButton = (ButtonBold) dialogView.findViewById(R.id.yesButton);
            ButtonBold noButton = (ButtonBold) dialogView.findViewById(R.id.noButton);
            TextViewRegular titleTextView = (TextViewRegular) dialogView.findViewById(R.id.titleTextView);
            TextViewRegular messageTextView = (TextViewRegular) dialogView.findViewById(R.id.messageTextView);
            if (title.equals("")) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setText(title);
            }
            if (message.equals("")) {
                messageTextView.setVisibility(View.GONE);
            } else {
                messageTextView.setText(message);
            }
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            yesButton.setOnClickListener(v -> {
                clickListener.onClick();
                alertDialog.dismiss();
            });
            noButton.setOnClickListener(v -> alertDialog.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void twoButtonDialog(Activity activity, Context context, String message, String title, String posButton, String nogButton, Click clickListener) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.layout_two_button_dailog, null);
            ButtonBold yesButton = (ButtonBold) dialogView.findViewById(R.id.yesButton);
            ButtonBold noButton = (ButtonBold) dialogView.findViewById(R.id.noButton);
            TextViewRegular titleTextView = (TextViewRegular) dialogView.findViewById(R.id.titleTextView);
            TextViewRegular messageTextView = (TextViewRegular) dialogView.findViewById(R.id.messageTextView);
            if (title.equals("")) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setText(title);
            }
            if (message.equals("")) {
                messageTextView.setVisibility(View.GONE);
            } else {
                messageTextView.setText(message);
            }
            yesButton.setText(posButton);
            noButton.setText(nogButton);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            yesButton.setOnClickListener(v -> {
                clickListener.onClick();
                alertDialog.dismiss();
            });
            noButton.setOnClickListener(v -> alertDialog.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void oneButtonDialog(Context context,String message, String title, String posButton, Click clickListener) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.layout_two_button_dailog, null);
            ButtonBold yesButton = (ButtonBold) dialogView.findViewById(R.id.yesButton);
            ButtonBold noButton = (ButtonBold) dialogView.findViewById(R.id.noButton);
            TextViewRegular titleTextView = (TextViewRegular) dialogView.findViewById(R.id.titleTextView);
            TextViewRegular messageTextView = (TextViewRegular) dialogView.findViewById(R.id.messageTextView);
            if (title.equals("")) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setText(title);
            }
            if (message.equals("")) {
                messageTextView.setVisibility(View.GONE);
            } else {
                messageTextView.setText(message);
            }
            yesButton.setText(posButton);
            noButton.setVisibility(View.GONE);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            yesButton.setOnClickListener(v -> {
                clickListener.onClick();
                alertDialog.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void yesNoButtonDialog() {
//        try {
//            if ((alertDialog != null) && (!alertDialog.isShowing())) {
//                alertDialog.dismiss();
//            }
//            alertDialog = null;
//        } catch (Exception e) {
//            alertDialog = null;
//        }
//    }


    public static void showGPSDisabledDialog(final Activity activity) {
        AlertDialog mGPSDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.gps_disabled));
        builder.setMessage("Gps is disabled, in order to use the application properly you need to enable GPS of your device");
        builder.setPositiveButton(activity.getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstant.GPS_ENABLE_REQUEST);
            }
        }).setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mGPSDialog = builder.create();
        mGPSDialog.show();
    }


    public static void showWheelView(Activity activity, String title, int selectedIndex, String[] array, ClickWithParam clickWithParam) {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = LayoutInflater.from(activity);
            final View dialogView = inflater.inflate(R.layout.layout_wheel_view, null);
            ButtonBold yesButton = (ButtonBold) dialogView.findViewById(R.id.yesButton);
            ButtonBold noButton = (ButtonBold) dialogView.findViewById(R.id.noButton);
            TextViewRegular titleTextView = (TextViewRegular) dialogView.findViewById(R.id.titleTextView);
            if (title.equals("")) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setText(title);
            }
            WheelView wv = (WheelView) dialogView.findViewById(R.id.wheel_view_wv);
            wv.setOffset(2);
            wv.setItems(Arrays.asList(array));
            wv.setSeletion(selectedIndex);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.e("[Dialog]", "selectedIndex: " + selectedIndex + ", item: " + item);
                }
            });
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            yesButton.setOnClickListener(v -> {
                clickWithParam.onClick(wv.getSeletedItem());
                alertDialog.dismiss();
            });
            noButton.setOnClickListener(v -> alertDialog.dismiss());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void monthDatePickerDialog() {
//        Date firstDayOfMonth, lastDayOfMonth;
//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR), 0, 1);
//        calendar1.set(calendar1.get(Calendar.YEAR), 11, 31);
//        lastDayOfMonth = calendar1.getTime();
//        firstDayOfMonth = calendar.getTime();
//
//        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(activity, R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
//                monthTextView.setText(AppUtils.dateForm(year1, month1 + 1, dayOfMonth));
//            }
//        }, yearNow, monthNow, dayNow){
//            @Override
//            protected void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
////                getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
//            }
//        };
//        monthDatePickerDialog.setTitle(getString(R.string.select_month));
//        monthDatePickerDialog.getDatePicker().setCalendarViewShown(false);
//        monthDatePickerDialog.getDatePicker().setMinDate(firstDayOfMonth.getTime());
//        monthDatePickerDialog.getDatePicker().setMaxDate(lastDayOfMonth.getTime());
//        monthDatePickerDialog.show();
//    }
//
//
//    private void monthDatePickerDialog() {
//        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(activity, android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
//                monthTextView.setText(AppUtils.dateForm(year1, month1 + 1, dayOfMonth));
//            }
//        }, yearNow, monthNow, dayNow){
//            @Override
//            protected void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
//            }
//        };
//        monthDatePickerDialog.setTitle(getString(R.string.select_month));
//        monthDatePickerDialog.show();
//    }
}

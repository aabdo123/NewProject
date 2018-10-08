//package com.fragments;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.res.Resources;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.DatePicker;
//
//import com.R;
//
//import java.lang.reflect.Field;
//import java.util.Calendar;
//
//public class DatePickerFragment extends DialogFragment{
//
//    private FragmentActivity activity;
//    private Calendar c = Calendar.getInstance();
//    private int year = c.get(Calendar.YEAR);
//    private int month_i = c.get(Calendar.MONTH);
//    private int day = c.get(Calendar.DAY_OF_MONTH);
//
//    public DatePickerFragment() {
//        // Required empty public constructor
//    }
//
//    public static DatePickerFragment newInstance() {
//        return new DatePickerFragment();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = getActivity();
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_dailog_picker, container, false);
//        initMonthPicker(view);
//        return view;
//    }
//    public void initMonthPicker(View view){
////        DatePicker dp_mes = (DatePicker) view.findViewById(R.id.datePicker);
//        DatePicker dp_mes = new DatePicker(activity);
//        int year    = dp_mes.getYear();
//        int month   = dp_mes.getMonth();
//        int day     = dp_mes.getDayOfMonth();
//
//        dp_mes.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                month_i = monthOfYear + 1;
//                Log.e("selected month:", Integer.toString(month_i));
//                //Add whatever you need to handle Date changes
//            }
//        });
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
//            if (daySpinnerId != 0)
//            {
//                View daySpinner = dp_mes.findViewById(daySpinnerId);
//                if (daySpinner != null)
//                {
//                    daySpinner.setVisibility(View.VISIBLE);
//                }
//            }
//
//            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
//            if (monthSpinnerId != 0)
//            {
//                View monthSpinner = dp_mes.findViewById(monthSpinnerId);
//                if (monthSpinner != null)
//                {
//                    monthSpinner.setVisibility(View.VISIBLE);
//                }
//            }
//
//            int yearSpinnerId = activity.getResources().getIdentifier("year", "id", "android");
//
//            if (yearSpinnerId != 0)
//            {
//                View yearSpinner = dp_mes.findViewById(yearSpinnerId);
//                if (yearSpinner != null)
//                {
//                    yearSpinner.setVisibility(View.GONE);
//                }
//            }
//        } else {
//            Field f[] = dp_mes.getClass().getDeclaredFields();
//            for (Field field : f)
//            {
//                if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner"))
//                {
//                    field.setAccessible(true);
//                    Object dayPicker = null;
//                    try {
//                        dayPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) dayPicker).setVisibility(View.GONE);
//                }
//
//                if(field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object monthPicker = null;
//                    try {
//                        monthPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) monthPicker).setVisibility(View.VISIBLE);
//                }
//
//                if(field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object yearPicker = null;
//                    try {
//                        yearPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) yearPicker).setVisibility(View.GONE);
//                }
//            }
//        }
//    }
//}
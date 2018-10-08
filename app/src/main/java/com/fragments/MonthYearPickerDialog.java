package com.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.R;
import com.views.ButtonBold;
import com.views.TextViewRegular;

import java.util.Calendar;

public class MonthYearPickerDialog extends DialogFragment {

    private final static String ARGS_IS_BOOLEAN = "args_is_boolean";
    private DatePickerDialog.OnDateSetListener listener;
    private FragmentActivity activity;
    private String[] MONTHS;
    private boolean isDayOnly;

    public static MonthYearPickerDialog newInstance(boolean isDayOnly) {
        MonthYearPickerDialog fragment = new MonthYearPickerDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_IS_BOOLEAN, isDayOnly);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            isDayOnly = mBundle.getBoolean(ARGS_IS_BOOLEAN);
        }
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (isDayOnly) {
            return onlyDayView();
        } else {
            return monthView();
        }
    }

    private AlertDialog monthView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        MONTHS = activity.getResources().getStringArray(R.array.months);

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.fragment_date_picker_dialog, null);

        ButtonBold yesButton = (ButtonBold) dialog.findViewById(R.id.yesButton);
        ButtonBold noButton = (ButtonBold) dialog.findViewById(R.id.noButton);

        TextViewRegular titleTextView = (TextViewRegular) dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(getString(R.string.select_month));

        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker daysPicker = (NumberPicker) dialog.findViewById(R.id.picker_days);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
        monthPicker.setDisplayedValues(MONTHS);
        monthPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                daysPicker.setMaxValue(getMonthCountDays(newVal));
            }
        });

        int year = cal.get(Calendar.DAY_OF_MONTH);
        daysPicker.setMinValue(1);
        daysPicker.setMaxValue(getMonthCountDays(cal.get(Calendar.MONTH)));
        daysPicker.setValue(year);
        daysPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        yesButton.setOnClickListener(v -> {
            listener.onDateSet(null, Calendar.YEAR, monthPicker.getValue(), daysPicker.getValue());
            dismiss();
        });
        noButton.setOnClickListener(v -> MonthYearPickerDialog.this.getDialog().cancel());
        builder.setView(dialog);
        return builder.create();
    }

    private AlertDialog onlyDayView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        MONTHS = activity.getResources().getStringArray(R.array.months);

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.fragment_date_picker_dialog, null);

        ButtonBold yesButton = (ButtonBold) dialog.findViewById(R.id.yesButton);
        ButtonBold noButton = (ButtonBold) dialog.findViewById(R.id.noButton);

        TextViewRegular titleTextView = (TextViewRegular) dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(getString(R.string.select_day_of_month));

        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        monthPicker.setVisibility(View.GONE);
        final NumberPicker daysPicker = (NumberPicker) dialog.findViewById(R.id.picker_days);

        int year = cal.get(Calendar.DAY_OF_MONTH);
        daysPicker.setMinValue(1);
//        daysPicker.setMaxValue(getMonthCountDays(cal.get(Calendar.MONTH)));
        daysPicker.setMaxValue(31);
        daysPicker.setValue(year);
        daysPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        yesButton.setOnClickListener(v -> {
            listener.onDateSet(null, Calendar.YEAR, Calendar.MONTH + 1, daysPicker.getValue());
            dismiss();
        });
        noButton.setOnClickListener(v -> MonthYearPickerDialog.this.getDialog().cancel());
        builder.setView(dialog);
        return builder.create();
    }

    private int getMonthCountDays(int scrollState) {
        switch (scrollState) {
            case 2:
                return 28;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            default:
                return 30;
        }
    }
}

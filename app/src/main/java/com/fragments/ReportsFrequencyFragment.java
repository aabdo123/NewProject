package com.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.R;
import com.activities.AddReportsActivity;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.Utils;
import com.views.AlertDialogView;
import com.views.ClickWithParam;
import com.views.TextViewRegular;
import com.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportsFrequencyFragment extends Fragment implements View.OnClickListener {

    public static ReportsFrequencyFragment frequencyFragment;
    private FragmentActivity activity;

    private RelativeLayout dayLayout;
    private RelativeLayout dayMonthLayout;
    private RelativeLayout hourLayout;
    private RelativeLayout monthLayout;
    private LinearLayout quarterLayout;
    private LinearLayout halfLayout;

    private TextViewRegular hourTextView;
    private TextViewRegular dayTextView;
    private TextViewRegular dayMonthTextView;
    private TextViewRegular monthTextView;

    private Spinner frequencyTypeSpinner;
    private Spinner quarterSpinner;
    private Spinner halfSpinner;

    private final Calendar mCurrentDate = Calendar.getInstance();
    //    private final int yearNow = mCurrentDate.get(Calendar.YEAR);
//    private final int monthNow = mCurrentDate.get(Calendar.MONTH);
//    private final int dayOfMonthNow = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private final int dayOfWeekNow = mCurrentDate.get(Calendar.DAY_OF_WEEK) - 1;
    private final int hourNow = mCurrentDate.get(Calendar.HOUR_OF_DAY);
    private final int minuteNow = mCurrentDate.get(Calendar.MINUTE);

    private String[] MONTHS;
    private String[] DAYS_WEEK;
    private String[] MONTHS_ENG = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private String frequency = null;
    private String freqHour = null;
    private String freqDay = null;
    private String freqDayOfMonth = null;
    private String freqMonth = null;
    private String freqHalf = null;

    public ReportsFrequencyFragment() {
        // Required empty public constructor
    }

    public static ReportsFrequencyFragment newInstance() {
        if (frequencyFragment == null)
            frequencyFragment = new ReportsFrequencyFragment();
        return frequencyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        MONTHS = activity.getResources().getStringArray(R.array.months);
        DAYS_WEEK = activity.getResources().getStringArray(R.array.week_days);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_reports_frequency, container, false);
        View rootView = getLayoutInflater().inflate(R.layout.fragment_reports_frequency, container, false);

        initView(rootView);
        initListeners();
        setFrequencySpinner();
        setQuarterlySpinner();
        setHalfYearlySpinner();
        return rootView;
    }

    private void initListeners() {
        hourLayout.setOnClickListener(this);
        dayLayout.setOnClickListener(this);
        dayMonthLayout.setOnClickListener(this);
        monthLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hourLayout:
                timePickerDialog();
                break;

            case R.id.dayMonthLayout:
                selectDayOfMonth();
                break;

            case R.id.dayLayout:
                selectDayOfTheWeek();
                break;

            case R.id.monthLayout:
                monthDatePickerDialog();
                break;
        }
    }

    private void initView(View rootView) {
        hourLayout = (RelativeLayout) rootView.findViewById(R.id.hourLayout);
        dayLayout = (RelativeLayout) rootView.findViewById(R.id.dayLayout);
        dayMonthLayout = (RelativeLayout) rootView.findViewById(R.id.dayMonthLayout);
        monthLayout = (RelativeLayout) rootView.findViewById(R.id.monthLayout);
        quarterLayout = (LinearLayout) rootView.findViewById(R.id.quarterLayout);
        halfLayout = (LinearLayout) rootView.findViewById(R.id.halfLayout);

        hourTextView = (TextViewRegular) rootView.findViewById(R.id.hourTextView);
        dayTextView = (TextViewRegular) rootView.findViewById(R.id.dayTextView);
        dayMonthTextView = (TextViewRegular) rootView.findViewById(R.id.dayMonthTextView);
        monthTextView = (TextViewRegular) rootView.findViewById(R.id.monthTextView);

        quarterSpinner = (Spinner) rootView.findViewById(R.id.quarterSpinner);
        halfSpinner = (Spinner) rootView.findViewById(R.id.halfSpinner);
        frequencyTypeSpinner = (Spinner) rootView.findViewById(R.id.frequencyTypeSpinner);
    }

    private void timePickerDialog() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                if (freqHour != null) {
                    AddReportsActivity.wizardModel.removeScheduleFrequency(freqHour);
//                    AddReportsActivity.wizardModel.setHourSelected(false);
                }
                hourTextView.setText(Utils.time24H(selectedHour, selectedMinute));
                freqHour = "hour," + Utils.time24H(selectedHour, selectedMinute);
                AddReportsActivity.wizardModel.addScheduleFrequency(freqHour);
                AddReportsActivity.wizardModel.setHourSelected(true);
            }
        }, hourNow, minuteNow, true);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_hour));
        mTimePicker.setCancelable(false);
        mTimePicker.show();
    }

    private void selectDayOfTheWeek() {
        AlertDialogView.showWheelView(
                activity, getString(R.string.select_day_of_week), dayOfWeekNow, DAYS_WEEK, new ClickWithParam() {
                    @Override
                    public void onClick(String text) {
                        if (freqDay != null) {
                            AddReportsActivity.wizardModel.removeScheduleFrequency(freqDay);
                        }
                        dayTextView.setText(text);
                        freqDay = "weeklyday," + text.toLowerCase();
                        AddReportsActivity.wizardModel.addScheduleFrequency(freqDay);
                        AddReportsActivity.wizardModel.setTypeSelected(true);
                    }
                });
    }

    // hour - hour,18:33
    // day - weeklyday,saturday
    // month - year, May/10
    // qurterYear - firstlast,first
    // halfYear - firstlast,first
    // dayMonth - day,12
    private void selectDayOfMonth() {
        MonthYearPickerDialog monthYearPickerDialog = MonthYearPickerDialog.newInstance(true);
        monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (freqDayOfMonth != null) {
                    AddReportsActivity.wizardModel.removeScheduleFrequency(freqDayOfMonth);
                }
                dayMonthTextView.setText(String.valueOf(dayOfMonth));
                freqDayOfMonth = "day," + dayOfMonth;
                AddReportsActivity.wizardModel.addScheduleFrequency(freqDayOfMonth);
                AddReportsActivity.wizardModel.setTypeSelected(true);
            }
        });
        monthYearPickerDialog.show(getChildFragmentManager(), "MonthYearPickerDialog");
    }

    private void monthDatePickerDialog() {
        MonthYearPickerDialog monthYearPickerDialog = MonthYearPickerDialog.newInstance(false);
        monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (freqMonth != null) {
                    AddReportsActivity.wizardModel.removeScheduleFrequency(freqMonth);
                }
                monthTextView.setText(dayOfMonth + " - " + MONTHS[month - 1]);
                freqMonth = "year," + dayOfMonth + "/" + MONTHS_ENG[month - 1];
                AddReportsActivity.wizardModel.addScheduleFrequency(freqMonth);
                AddReportsActivity.wizardModel.setTypeSelected(true);
            }
        });
        monthYearPickerDialog.show(getChildFragmentManager(), "MonthYearPickerDialog");
    }

    private void setFrequencySpinner() {
        List<String> timeList = new ArrayList<>();
        timeList.add(getString(R.string.select_scheduled_frequency));
        timeList.add(getString(R.string.daily));
        timeList.add(getString(R.string.weekly));
        timeList.add(getString(R.string.monthly));
        timeList.add(getString(R.string.quarterly));
        timeList.add(getString(R.string.half_year));
        timeList.add(getString(R.string.yearly));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, timeList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_row);
        frequencyTypeSpinner.setAdapter(arrayAdapter);
        frequencyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                visibility(position);
                if (position == 0) {
                    AddReportsActivity.wizardModel.clearReportsFrequency();
                    AddReportsActivity.wizardModel.setReportsFrequencyID(null);
                    frequency = null;
                } else {
                    frequency = timeList.get(position);
                    AddReportsActivity.wizardModel.setReportsFrequencyID(String.valueOf(position - 1));
                    AddReportsActivity.wizardModel.addReportsFrequency(frequency);
                }
                // to remove validation from daily
                if (position == 1) {
                    AddReportsActivity.wizardModel.setTypeSelected(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setQuarterlySpinner() {
        List<String> timeList = new ArrayList<>();
        timeList.add(getString(R.string.select_quarter_year));
        timeList.add(getString(R.string.first_day));
        timeList.add(getString(R.string.last_day));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, timeList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_row);
        quarterSpinner.setAdapter(arrayAdapter);
        quarterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    freqHalf = null;
                    AddReportsActivity.wizardModel.setTypeSelected(false);
                } else if (position == 1) {
                    freqHalf = "firstlast,firstday";
                    AddReportsActivity.wizardModel.addScheduleFrequency(freqHalf);
                    AddReportsActivity.wizardModel.setTypeSelected(true);
                } else {
                    freqHalf = "firstlast,lastday";
                    AddReportsActivity.wizardModel.addScheduleFrequency(freqHalf);
                    AddReportsActivity.wizardModel.setTypeSelected(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setHalfYearlySpinner() {
        List<String> timeList = new ArrayList<>();
        timeList.add(getString(R.string.select_half_year));
        timeList.add(getString(R.string.first_day));
        timeList.add(getString(R.string.last_day));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, timeList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_row);
        halfSpinner.setAdapter(arrayAdapter);
        halfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    freqHalf = null;
                    AddReportsActivity.wizardModel.setTypeSelected(false);
                } else if (position == 1) {
                    freqHalf = "firstlast,firstday";
                    AddReportsActivity.wizardModel.addScheduleFrequency(freqHalf);
                    AddReportsActivity.wizardModel.setTypeSelected(true);
                } else {
                    freqHalf = "firstlast,lastday";
                    AddReportsActivity.wizardModel.addScheduleFrequency(freqHalf);
                    AddReportsActivity.wizardModel.setTypeSelected(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void visibility(int position) {
        switch (position) {
            case 0:
                nonSelected();
                break;

            case 1:
                ifDaily();
                break;

            case 2:
                ifWeekly();
                break;

            case 3:
                ifMonthly();
                break;

            case 4:
                ifQuarterly();
                break;

            case 5:
                ifHalfYearly();
                break;

            case 6:
                ifYearly();
                break;
        }
        checkObjects();
    }

    private void nonSelected() {
        AnimationUtils.collapse(hourLayout);
        AnimationUtils.collapse(dayLayout);
        AnimationUtils.collapse(dayMonthLayout);
        AnimationUtils.collapse(monthLayout);
        AnimationUtils.collapse(quarterLayout);
        AnimationUtils.collapse(halfLayout);
    }

    private void ifDaily() {
        AnimationUtils.expand(hourLayout);
        dayLayout.setVisibility(View.GONE);
        dayMonthLayout.setVisibility(View.GONE);
        monthLayout.setVisibility(View.GONE);
        quarterLayout.setVisibility(View.GONE);
        halfLayout.setVisibility(View.GONE);
    }

    private void ifWeekly() {
        AnimationUtils.expand(dayLayout);
        AnimationUtils.expand(hourLayout);
        dayMonthLayout.setVisibility(View.GONE);
        monthLayout.setVisibility(View.GONE);
        quarterLayout.setVisibility(View.GONE);
        halfLayout.setVisibility(View.GONE);
    }

    private void ifMonthly() {
        AnimationUtils.expand(hourLayout);
        AnimationUtils.expand(dayMonthLayout);
        dayLayout.setVisibility(View.GONE);
        monthLayout.setVisibility(View.GONE);
        quarterLayout.setVisibility(View.GONE);
        halfLayout.setVisibility(View.GONE);
    }

    private void ifQuarterly() {
        AnimationUtils.expand(hourLayout);
        AnimationUtils.expand(quarterLayout);
        dayLayout.setVisibility(View.GONE);
        dayMonthLayout.setVisibility(View.GONE);
        monthLayout.setVisibility(View.GONE);
        halfLayout.setVisibility(View.GONE);
    }

    private void ifHalfYearly() {
        AnimationUtils.expand(hourLayout);
        AnimationUtils.expand(halfLayout);
        dayLayout.setVisibility(View.GONE);
        dayMonthLayout.setVisibility(View.GONE);
        monthLayout.setVisibility(View.GONE);
        quarterLayout.setVisibility(View.GONE);
    }

    private void ifYearly() {
        AnimationUtils.expand(hourLayout);
        AnimationUtils.expand(monthLayout);
        dayLayout.setVisibility(View.GONE);
        dayMonthLayout.setVisibility(View.GONE);
        quarterLayout.setVisibility(View.GONE);
        halfLayout.setVisibility(View.GONE);
    }

    private void checkObjects() {
        if (hourLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqHour);
            AddReportsActivity.wizardModel.setHourSelected(false);
            freqHour = null;
            hourTextView.setText(getString(R.string.select_hour));
        }

        if (dayLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqDay);
            AddReportsActivity.wizardModel.setTypeSelected(false);
            freqDay = null;
            dayTextView.setText(getString(R.string.select_day_of_week));
        }

        if (dayMonthLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqDayOfMonth);
            AddReportsActivity.wizardModel.setTypeSelected(false);
            freqDayOfMonth = null;
            dayMonthTextView.setText(getString(R.string.select_day_of_month));
        }

        if (monthLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqMonth);
            AddReportsActivity.wizardModel.setTypeSelected(false);
            freqMonth = null;
            monthTextView.setText(getString(R.string.select_month));
        }

        if (quarterLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqHalf);
            AddReportsActivity.wizardModel.setTypeSelected(false);
            freqHalf = null;
        }

        if (halfLayout.getVisibility() == View.GONE) {
            AddReportsActivity.wizardModel.removeScheduleFrequency(freqHalf);
            AddReportsActivity.wizardModel.setTypeSelected(false);
            freqHalf = null;
        }
    }
}

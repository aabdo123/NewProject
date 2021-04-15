package com.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsSingleActivity;
import com.adapters.RealtyCityAdapter;
import com.models.ReportsTypeModel;
import com.utilities.AppUtils;
import com.utilities.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.activities.AddReportsSingleActivity.wizardModel;

public class ReportsDurationFragment extends Fragment implements View.OnClickListener {

    public ReportsDurationFragment includedFragment;
    private TextView fromSingleDateTextView;
    private TextView fromDateTextView;
    private TextView fromDateNoTimeTextView;
    private TextView toDateTextView;
    private TextView toDateNoTimeTextView;
    private Date dateFrom;
    private ReportsTypeModel reportsTypeModel;
    private static final String TYPE_ONE = "TYPE_ONE";
    private static final String TYPE_TOW = "TYPE_TOW";
    private static final String TYPE_THREE = "TYPE_THREE";
    private static final String TYPE_FUR = "TYPE_FUR";
    private static final String TYPE_FIVE = "TYPE_FIVE";
    private static final String TYPE_SIX = "TYPE_SIX";
    private static final String TYPE_SEVEN = "TYPE_SEVEN";
    private String selectedType;
    private LinearLayout viewOneLinearLayout;
    private LinearLayout multiSelectionLinearLayout;
    private LinearLayout multiSelectionTimeLinearLayout;
    private LinearLayout multiSelectionWithoutTimeLinearLayout;
    private LinearLayout minimumSpeedLinearLayout;
    private LinearLayout durationInnerLinearLayout;
    private LinearLayout selectTypeLinearLayout;
    private EditText minimumSpeedEditText;
    private EditText durationEditText;
    private LinearLayout durationLinearLayout;
    private TextView fromTimeTextView;
    private TextView toTimeTextView;
    private TextView selectTypeTextView;
    private TextView selectedDateTextView;
    private int selectedYear1 = -1;
    private int selectedMonth1 = -1;
    private int selectedDayOfMonth = -1;

    public void changeViewInside() {
        try {
            if (AddReportsSingleActivity.wizardModel != null && AddReportsSingleActivity.wizardModel.getReportsType() != null && AddReportsSingleActivity.wizardModel.getReportsType().size() > 0) {
                List<ReportsTypeModel> getReportsType = AddReportsSingleActivity.wizardModel.getReportsType();
                reportsTypeModel = getReportsType.get(0);
            } else {
                reportsTypeModel = new ReportsTypeModel();
                reportsTypeModel.setID(10);
            }
            validationTypes();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void validationTypes() {
        switch (viewTypes(reportsTypeModel)) {
            case TYPE_ONE:
                methodOne();
                break;
            case TYPE_TOW:
                methodTwo();
                break;
            case TYPE_THREE:
                methodThree();
                break;
            case TYPE_FUR:
                methodFur();
                break;
            case TYPE_FIVE:
                methodFive();
                break;
            case TYPE_SIX:
                methodSix();
                break;
            case TYPE_SEVEN:
                methodSeven();
                break;
            default:
                break;
        }
    }


    private void methodOne() {
        try {
            viewOneLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionLinearLayout.setVisibility(View.GONE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            durationLinearLayout.setVisibility(View.GONE);
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            minimumSpeedLinearLayout.setVisibility(View.GONE);
            durationInnerLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            fromDateSetUp();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodTwo() {
        try {
            multiSelectionLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            durationLinearLayout.setVisibility(View.GONE);
            viewOneLinearLayout.setVisibility(View.GONE);
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            minimumSpeedLinearLayout.setVisibility(View.GONE);
            durationInnerLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            setDateToLayout();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodThree() {
        try {
            multiSelectionTimeLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionLinearLayout.setVisibility(View.GONE);
            durationLinearLayout.setVisibility(View.GONE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.VISIBLE);
            viewOneLinearLayout.setVisibility(View.GONE);
            minimumSpeedLinearLayout.setVisibility(View.GONE);
            durationInnerLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            setDateToLayoutWithNoTime();
            setTimeFromTo();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodFur() {
        try {
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            multiSelectionLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            durationLinearLayout.setVisibility(View.VISIBLE);
            minimumSpeedLinearLayout.setVisibility(View.GONE);
            durationInnerLinearLayout.setVisibility(View.VISIBLE);
            viewOneLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            setDateToLayout();
            durationTextListener();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodFive() {
        try {
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            multiSelectionLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            //
            durationLinearLayout.setVisibility(View.VISIBLE);
            minimumSpeedLinearLayout.setVisibility(View.VISIBLE);
            durationInnerLinearLayout.setVisibility(View.VISIBLE);
            //
            viewOneLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            setDateToLayout();
            minimumSpeedTextListener();
            durationTextListener();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodSix() {
        try {
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            multiSelectionLinearLayout.setVisibility(View.VISIBLE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            //
            durationLinearLayout.setVisibility(View.VISIBLE);
            minimumSpeedLinearLayout.setVisibility(View.VISIBLE);
            durationInnerLinearLayout.setVisibility(View.GONE);
            //
            viewOneLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.GONE);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            setDateToLayout();
            minimumSpeedTextListener();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void methodSeven() {
        try {
            multiSelectionTimeLinearLayout.setVisibility(View.GONE);
            multiSelectionLinearLayout.setVisibility(View.GONE);
            multiSelectionWithoutTimeLinearLayout.setVisibility(View.GONE);
            //
            durationLinearLayout.setVisibility(View.GONE);
            minimumSpeedLinearLayout.setVisibility(View.GONE);
            durationInnerLinearLayout.setVisibility(View.GONE);
            //
            viewOneLinearLayout.setVisibility(View.GONE);
            selectTypeLinearLayout.setVisibility(View.VISIBLE);
            selectedDateTextView.setText(getContext().getString(R.string.filter_by));
            selectTypeTextView.setText(wizardModel.getSelectedType() != null ? wizardModel.getSelectedType() : getContext().getString(R.string.all));
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getLayoutInflater().inflate(R.layout.fragment_duration, container, false);
        initView(rootView);
        return rootView;
    }


    // IDS: {2,WorkingHoursAndMileageDailyBases},{6, AlarmandNotificationsReport} = TYPE_1
    // IDS: {3,WorkingHoursAndMileagePeriod}, {10, FuelSummaryReport} , {13,DriverLogging}, {9,OverStreetSpeedReport} , {5,OfflineVehicleReport}, {11,VehicleIdllingandParkingReport} = TYPE_2
    // IDS: {12,CustomRunningTime} = TYPE_3
    // IDS: {14,TripReport} = TYPE_4
    // IDS: {4,SpeedOverDurationReport} = TYPE_5
    // IDS: {7,OverSpeedReport} = TYPE_6
    // IDS: {8,UserVehicles} = TYPE_7


    private String viewTypes(ReportsTypeModel reportsTypeModel) {
        switch (reportsTypeModel.getID()) {
            case 2:
                return selectedType = TYPE_ONE;
            case 6:
                return selectedType = TYPE_ONE;
            case 3:
                return selectedType = TYPE_TOW;
            case 10:
                return selectedType = TYPE_TOW;
            case 13:
                return selectedType = TYPE_TOW;
            case 9:
                return selectedType = TYPE_TOW;
            case 5:
                return selectedType = TYPE_TOW;
            case 11:
                return selectedType = TYPE_TOW;
            case 12:
                return selectedType = TYPE_THREE;
            case 14:
                return selectedType = TYPE_FUR;
            case 4:
                return selectedType = TYPE_FIVE;
            case 7:
                return selectedType = TYPE_SIX;
            case 8:
                return selectedType = TYPE_SEVEN;
            default:
                return selectedType = TYPE_TOW;
        }
    }


    private void fromDateSetUp() {
        Calendar dateNow = Calendar.getInstance();
        int hour = dateNow.get(Calendar.HOUR_OF_DAY);
        int minute = dateNow.get(Calendar.MINUTE);
        int year = dateNow.get(Calendar.YEAR);
        int month = dateNow.get(Calendar.MONTH) + 1;
        int dayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
        int dayBefore = dateNow.get(Calendar.DAY_OF_MONTH) - 1 == 0 ? dateNow.get(Calendar.DAY_OF_MONTH) : dateNow.get(Calendar.DAY_OF_MONTH) - 1;
        fromSingleDateTextView.setText(AppUtils.dateForm(year, month, dayBefore));
        dateFrom = new Date(System.currentTimeMillis());
        wizardModel.setFromDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
        wizardModel.setToDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
    }


    private void setTimeFromTo() {
        Calendar dateNow = Calendar.getInstance();
        int hour = dateNow.get(Calendar.HOUR_OF_DAY);
        int minute = dateNow.get(Calendar.MINUTE);
        fromTimeTextView.setText(Utils.time24H(hour, minute));
        toTimeTextView.setText(Utils.time24H(hour, minute));
        wizardModel.setFromTime(Utils.time24H(hour, minute));
        wizardModel.setToTime(Utils.time24H(hour, minute));
    }

    private void timeSelectionFrom() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
            fromTimeTextView.setText(Utils.time24H(selectedHour, selectedMinute));
            wizardModel.setFromTime(Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_from));
//        mTimePicker.getDatePicker().setMaxDate(new Date().getTime());
        mTimePicker.show();
    }

    private void typeFilterSelection() {

    }


    private void timeSelectionTo() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
            toTimeTextView.setText(Utils.time24H(selectedHour, selectedMinute));
            wizardModel.setToTime(Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_from));
//        mTimePicker.getDatePicker().setMaxDate(new Date().getTime());
        mTimePicker.show();
    }

    private void setDateToLayoutWithNoTime() {
        Calendar dateNow = Calendar.getInstance();
        int hour = dateNow.get(Calendar.HOUR_OF_DAY);
        int minute = dateNow.get(Calendar.MINUTE);
        int year = dateNow.get(Calendar.YEAR);
        int month = dateNow.get(Calendar.MONTH) + 1;
        int dayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
        int dayBefore = dateNow.get(Calendar.DAY_OF_MONTH) - 1 == 0 ? dateNow.get(Calendar.DAY_OF_MONTH) : dateNow.get(Calendar.DAY_OF_MONTH) - 1;

//        fromDate = AppUtils.dateForm(year, month, dayBefore) + "%20" + Utils.time24H(hour, minute);
//        toDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(hour, minute);

        fromDateNoTimeTextView.setText(AppUtils.dateForm(year, month, dayBefore));
        toDateNoTimeTextView.setText(AppUtils.dateForm(year, month, dayOfMonth));

        dateFrom = new Date(System.currentTimeMillis());
        wizardModel.setFromDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(0, 0));
        wizardModel.setToDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(0, 0));
    }

    private void durationTextListener() {
        try {
            durationEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null)
                        wizardModel.setDuration(s.toString());
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void minimumSpeedTextListener() {
        try {
            minimumSpeedEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null)
                        wizardModel.setMinimumSpeed(s.toString());
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setDateToLayout() {
        Calendar dateNow = Calendar.getInstance();
        int hour = dateNow.get(Calendar.HOUR_OF_DAY);
        int minute = dateNow.get(Calendar.MINUTE);
        int year = dateNow.get(Calendar.YEAR);
        int month = dateNow.get(Calendar.MONTH) + 1;
        int dayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
        int dayBefore = dateNow.get(Calendar.DAY_OF_MONTH) - 1 == 0 ? dateNow.get(Calendar.DAY_OF_MONTH) : dateNow.get(Calendar.DAY_OF_MONTH) - 1;

//        fromDate = AppUtils.dateForm(year, month, dayBefore) + "%20" + Utils.time24H(hour, minute);
//        toDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(hour, minute);

        fromDateTextView.setText(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
        toDateTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(hour, minute));

        dateFrom = new Date(System.currentTimeMillis());
        wizardModel.setFromDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
        wizardModel.setToDate(AppUtils.dateForm(year, month, dayBefore) + "-" + Utils.time24H(hour, minute));
    }

    private void initView(View viewHolder) {
        try {
            fromDateTextView = (TextView) viewHolder.findViewById(R.id.fromDateTextView);
            selectTypeTextView = (TextView) viewHolder.findViewById(R.id.selectTypeTextView);
            fromDateNoTimeTextView = (TextView) viewHolder.findViewById(R.id.fromDateNoTimeTextView);
            fromSingleDateTextView = (TextView) viewHolder.findViewById(R.id.fromSingleDateTextView);
            toDateTextView = (TextView) viewHolder.findViewById(R.id.toDateTextView);
            toDateNoTimeTextView = (TextView) viewHolder.findViewById(R.id.toDateNoTimeTextView);
            viewOneLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.viewOneLinearLayout);
            multiSelectionLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.multiSelectionLinearLayout);
            multiSelectionWithoutTimeLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.multiSelectionWithoutTimeLinearLayout);
            minimumSpeedEditText = (EditText) viewHolder.findViewById(R.id.minimumSpeedEditText);
            durationEditText = (EditText) viewHolder.findViewById(R.id.durationEditText);
            durationInnerLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.durationInnerLinearLayout);
            selectTypeLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.selectTypeLinearLayout);
            minimumSpeedLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.minimumSpeedLinearLayout);
            durationLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.durationLinearLayout);
            multiSelectionTimeLinearLayout = (LinearLayout) viewHolder.findViewById(R.id.multiSelectionTimeLinearLayout);
            fromTimeTextView = (TextView) viewHolder.findViewById(R.id.fromTimeTextView);
            toTimeTextView = (TextView) viewHolder.findViewById(R.id.toTimeTextView);
            selectedDateTextView = (TextView) viewHolder.findViewById(R.id.selectedDateTextView);
            selectedDateTextView.setText(getContext().getString(R.string.select_date));
            //
            fromDateTextView.setOnClickListener(this);
            selectTypeLinearLayout.setOnClickListener(this);
            fromDateNoTimeTextView.setOnClickListener(this);
            fromSingleDateTextView.setOnClickListener(this);
            toDateTextView.setOnClickListener(this);
            toDateNoTimeTextView.setOnClickListener(this);
            fromTimeTextView.setOnClickListener(this);
            toTimeTextView.setOnClickListener(this);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromDateTextView:
                fromDatePickerDialog();
                break;
            case R.id.fromDateNoTimeTextView:
                fromDatePickerDialogWithNoTime();
                break;
            case R.id.fromSingleDateTextView:
                singleDatePickerDialog();
                break;
            case R.id.toDateTextView:
                toDatePickerDialog();
                break;
            case R.id.toDateNoTimeTextView:
                toDatePickerDialogWithNoTime();
                break;
            case R.id.fromTimeTextView:
                timeSelectionFrom();
                break;
            case R.id.toTimeTextView:
                timeSelectionTo();
                break;
            case R.id.selectTypeLinearLayout:
                showCityList();
                break;
            default:
                break;
        }

    }

    protected void showCityList() {
        final android.app.Dialog dialogAndroidAppCus = new android.app.Dialog(getActivity());
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAndroidAppCus.setContentView(R.layout.view_list_dialog);
        RecyclerView recyclerView = (RecyclerView) dialogAndroidAppCus.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RealtyCityAdapter realtyStringAdapter = new RealtyCityAdapter(getActivity(), AppUtils.getCarStatesString(getActivity()), new RealtyCityAdapter.Clicks() {
            @Override
            public void click(String city, int position) {
                try {
                    dialogAndroidAppCus.dismiss();
                    selectTypeTextView.setText(city);
                    wizardModel.setSelectedType(city);
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });
        recyclerView.setAdapter(realtyStringAdapter);
        dialogAndroidAppCus.setCancelable(true);
        dialogAndroidAppCus.show();
    }


    private void toDatePickerDialogWithNoTime() {
        Calendar mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                dateFrom = new Date(Utils.componentTimeToTimestamp(year1, month1, dayOfMonth, 0, 0));
                toDateNoTimeTextView.setText(AppUtils.dateForm(year, month, dayOfMonth));
                wizardModel.setToDate(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(0, 0));
            }
        }, year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_to));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(dateFrom.getTime());
        datePickerDialog.show();
    }

    private void fromDatePickerDialogWithNoTime() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> {
            dateFrom = new Date(Utils.componentTimeToTimestamp(year1, month1, dayOfMonth, 0, 0));
            fromDateNoTimeTextView.setText(AppUtils.dateForm(year, month, dayOfMonth));
            wizardModel.setFromDate(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(0, 0));
        }, year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_from));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void toDatePickerDialog() {
        Calendar mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> toTimePickerDialog(year1, month1 + 1, dayOfMonth), year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_to));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setMinDate(dateFrom.getTime());
        datePickerDialog.show();
    }

    private void toTimePickerDialog(int year, int month, int dayOfMonth) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
//            toDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(selectedHour, selectedMinute);
            toDateTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
            wizardModel.setToDate(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_to));
        mTimePicker.show();
    }

    private void singleDatePickerDialog() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> {
            dateFrom = new Date(Utils.componentTimeToTimestamp(year1, month1 + 1, dayOfMonth, 0, 0));
            fromSingleDateTextView.setText(AppUtils.dateForm(year1, month1 + 1, dayOfMonth));
            selectedYear1 = year1;
            selectedMonth1 = month1;
            selectedDayOfMonth = dayOfMonth;
        }, year, month, day);
        if (selectedYear1 > -1)
            datePickerDialog.updateDate(selectedYear1, selectedMonth1, selectedDayOfMonth);//
//        }else {
//            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
//        }
//        datePickerDialog.
        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }


    private void fromDatePickerDialog() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerTheme, (view, year1, month1, dayOfMonth) -> {
            dateFrom = new Date(Utils.componentTimeToTimestamp(year1, month1, dayOfMonth, 0, 0));
            fromTimePickerDialog(year1, month1 + 1, dayOfMonth);
        }, year, month, day);
        datePickerDialog.setTitle(getString(R.string.select_date_from));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void fromTimePickerDialog(int year, int month, int dayOfMonth) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
//            fromDate = AppUtils.dateForm(year, month, dayOfMonth) + "%20" + Utils.time24H(selectedHour, selectedMinute);
            fromDateTextView.setText(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
            wizardModel.setFromDate(AppUtils.dateForm(year, month, dayOfMonth) + "-" + Utils.time24H(selectedHour, selectedMinute));
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time_from));
//        mTimePicker.getDatePicker().setMaxDate(new Date().getTime());
        mTimePicker.show();
    }


}

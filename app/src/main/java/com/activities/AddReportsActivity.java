package com.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.R;
import com.fragments.ListOfUsersFragment;
import com.fragments.ReportsContactFragment;
import com.fragments.ReportsFrequencyFragment;
import com.fragments.ReportsIncludedFragment;
import com.fragments.ReportsVehiclesFragment;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.ListOfUsersModel;
import com.models.ListOfVehiclesSmallModel;
import com.models.ReportsTypeModel;
import com.models.WizardModel;
import com.utilities.CustomViewPager;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.views.AlertDialogView;
import com.views.ButtonBold;
import com.views.Click;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

public class AddReportsActivity extends BaseActivity implements View.OnClickListener {

    public static WizardModel wizardModel;
    private AppCompatActivity ADD_REPORTS_ACTIVITY;
    private CustomViewPager mViewPager;
    private Toolbar toolbar;
    private TextViewRegular toolbarTitleTextView;
    private ButtonBold finishButton;
    private ButtonBold preButton;
    private ButtonBold nextButton;
    private ImageView indicatorZero;
    private ImageView indicatorOne;
    private ImageView indicatorTwo;
    private ImageView indicatorThree;
    private ImageView indicatorFour;
    private ImageView[] indicators;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reports);
        ADD_REPORTS_ACTIVITY = AddReportsActivity.this;
        initViews();
        setUpData();
        initListeners();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitleTextView = (TextViewRegular) findViewById(R.id.toolbarTitleTextView);

        preButton = (ButtonBold) findViewById(R.id.preButton);
        nextButton = (ButtonBold) findViewById(R.id.nextButton);

        finishButton = (ButtonBold) findViewById(R.id.finishButton);

        indicatorZero = (ImageView) findViewById(R.id.intro_indicator_0);
        indicatorOne = (ImageView) findViewById(R.id.intro_indicator_1);
        indicatorTwo = (ImageView) findViewById(R.id.intro_indicator_2);
        indicatorThree = (ImageView) findViewById(R.id.intro_indicator_3);
        indicatorFour = (ImageView) findViewById(R.id.intro_indicator_4);

        mViewPager = (CustomViewPager) findViewById(R.id.container);
        changeTitle(0);
        wizardModel = new WizardModel();
    }


    private void changeTitle(int position) {
        switch (position) {
            case 0:
                setToolbarTitleTextView(getString(R.string.reports_type));
                break;

            case 1:
                setToolbarTitleTextView(getString(R.string.scheduled_frequency));
                break;

            case 2:
                setToolbarTitleTextView(getString(R.string.list_of_vehicles));
                break;

            case 3:
                setToolbarTitleTextView(getString(R.string.list_of_users));
                break;

            case 4:
                setToolbarTitleTextView(getString(R.string.submit));
                break;
        }
    }

    private void setToolbarTitleTextView(String titleTextView) {
        toolbarTitleTextView.setText(titleTextView);
    }

    private void setUpData() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        indicators = new ImageView[]{indicatorZero, indicatorOne, indicatorTwo, indicatorThree, indicatorFour};

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);

        int limit = (mSectionsPagerAdapter.getCount() > 1 ? mSectionsPagerAdapter.getCount() - 1 : 1);
        mViewPager.setOffscreenPageLimit(limit);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);
    }

    private void initListeners() {
        nextButton.setOnClickListener(this);
        preButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
                preButton.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
                nextButton.setVisibility(position == 4 ? View.GONE : View.VISIBLE);
                finishButton.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
                changeTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                if (validation()) {
                    page += 1;
                    setCurrentItemViewPager(page);
                }
                break;

            case R.id.preButton:
                page -= 1;
                setCurrentItemViewPager(page);
                break;

            case R.id.finishButton:
                if (validation()) {
                    submitApiCall();
                }
                break;
        }
    }

    public void setCurrentItemViewPager(int pageNumber) {
        mViewPager.setCurrentItem(pageNumber, true);
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == position ? R.drawable.selected_dot : R.drawable.un_selected_dot);
        }
    }

    private boolean validation() {
        if ((!Utils.isNotEmptyList(wizardModel.getReportsType())) && page == 0) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_select_report_first));
            return false;
        }

        if ((!Utils.isNotEmptyList(wizardModel.getScheduleFrequency())) && page == 1) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_fill_all_frequency_components));
            return false;
        }
        if (!wizardModel.isTypeSelected() && page == 1) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_fill_all_frequency_components));
            return false;
        }

        if (!wizardModel.isHourSelected() && page == 1) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.select_hour));
            return false;
        }

        if ((!Utils.isNotEmptyList(wizardModel.getVehicleList())) && page == 2) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_select_atleast_one_vehicle));
            return false;
        }

        if ((!Utils.isNotEmptyList(wizardModel.getUsersList())) && page == 3) {
            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_select_atleast_one_username));
            return false;
        }

//        if ((!Utils.isNotEmptyList(wizardModel.getUsersList())) && page == 4) {
//            ToastHelper.toastInfo(ADD_REPORTS_ACTIVITY, getString(R.string.please_select_atleast_one_vehicle));
//            return false;
//        }
        return true;
    }

    private void submitApiCall() {
        Progress.showLoadingDialog(ADD_REPORTS_ACTIVITY);
        BusinessManager.postSubmitScheduledReports("0",
                getReportsFrequencyID(),
                getDescription(),
                getItemsComma(getScheduleVehicleID()),
                getItemsComma(getScheduleUserID()),
                getItemsComma(getReportScheduleID()),
                getItemsComma(getAdditionalEmails()),
                getItemsComma(getAdditionalNumbers()),
                getFrequencyTitle(),
                getItemsComma(getScheduleFrequency()),
                new ApiCallResponseString() {
                    @Override
                    public void onSuccess(int statusCode, String responseObject) {
                        Progress.dismissLoadingDialog();
                        if (responseObject.contains("true")) {
                            ToastHelper.toastSuccessLong(ADD_REPORTS_ACTIVITY, getString(R.string.added_successfully));
                            ADD_REPORTS_ACTIVITY.finish();
                        } else {
                            ToastHelper.toastErrorLong(ADD_REPORTS_ACTIVITY, getString(R.string.something_went_worng));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                        Progress.dismissLoadingDialog();
                        ToastHelper.toastErrorLong(ADD_REPORTS_ACTIVITY, getString(R.string.something_went_worng));
                    }
                });
    }

    private String getDescription() {
        return AddReportsActivity.wizardModel.getDescription();
    }

    private String getReportsFrequencyID() {
        return AddReportsActivity.wizardModel.getReportsFrequencyID();
    }

    private List<String> getScheduleVehicleID() {
        List<String> list = new ArrayList<>();
        for (ListOfVehiclesSmallModel.Vehicles model : wizardModel.getVehicleList()) {
            list.add(String.valueOf(model.getID()));
        }
        return list;
    }

    private List<String> getReportScheduleID() {
        List<String> list = new ArrayList<>();
        for (ReportsTypeModel model : wizardModel.getReportsType()) {
            list.add(String.valueOf(model.getID()));
        }
        return list;
    }

    private List<String> getScheduleUserID() {
        List<String> list = new ArrayList<>();
        for (ListOfUsersModel model : wizardModel.getUsersList()) {
            list.add(String.valueOf(model.getID()));
        }
        return list;
    }

    private List<String> getAdditionalEmails() {
        List<String> list = new ArrayList<>();
        for (String model : wizardModel.getEmails()) {
            list.add(model);
        }
        return list;
    }

    private List<String> getAdditionalNumbers() {
        List<String> list = new ArrayList<>();
        if (wizardModel.isNotifyBySms()) {
            for (String model : wizardModel.getPhones()) {
                list.add(model);
            }
        }
        return list;
    }

    private List<String> getScheduleFrequency() {
        List<String> list = new ArrayList<>();
        for (String model : wizardModel.getScheduleFrequency()) {
            list.add(model);
        }
        return list;
    }

    private String getItemsComma(List<?> list) {
        if (Utils.isNotEmptyList(list)) {
            return android.text.TextUtils.join(",", list);
        } else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialogView.yesNoButtonDialog(ADD_REPORTS_ACTIVITY, ADD_REPORTS_ACTIVITY, getString(R.string.are_you_sure_do_want_to_exit), getString(R.string.nav_scheduled_reports), new Click() {
            @Override
            public void onClick() {
                ADD_REPORTS_ACTIVITY.finish();
            }
        });
//        super.onBackPressed();
    }

    private String getFrequencyTitle() {
        List<String> timeList = new ArrayList<>();
        timeList.add("daily");
        timeList.add("weekly");
        timeList.add("monthly");
        timeList.add("quarterly");
        timeList.add("halfyear");
        timeList.add("yearly");
        try {
            int pos = Integer.parseInt(AddReportsActivity.wizardModel.getReportsFrequencyID());
            return timeList.get(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ReportsIncludedFragment.newInstance();

                case 1:
                    return ReportsFrequencyFragment.newInstance();

                case 2:
                    return ReportsVehiclesFragment.newInstance();

                case 3:
                    return ListOfUsersFragment.newInstance();

                case 4:
                    return ReportsContactFragment.newInstance();

                default:
                    return ReportsIncludedFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.checkConnectingToInternet(ADD_REPORTS_ACTIVITY);
    }
}

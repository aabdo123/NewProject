package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.R;
import com.fragments.AboutUsFragment;
import com.fragments.AlarmNotificationsFragment;
import com.fragments.DataAnalysisFragment;
import com.fragments.FragmentDrawer;
import com.fragments.GeoFenceFragment;
import com.fragments.GeoFenceListFragment;
import com.fragments.HelpFragment;
import com.fragments.HistoricalRouteFragment;
import com.fragments.HomeFragment;
import com.fragments.LandmarkListFragment;
import com.fragments.LisOfVehiclesMapFragment;
import com.fragments.ListOfVehiclesFragment;
import com.fragments.ScheduledReportsFragment;
import com.managers.map_managers.MyLocateManager;
import com.managers.PreferencesManager;
import com.managers.map_managers.MyStartMapViewManager;
import com.utilities.AppUtils;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.views.AlertDialogView;
import com.views.Click;
import com.views.TextViewRegular;

import java.util.Stack;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    public static AppCompatActivity MAIN_ACTIVITY;
    private static FragmentDrawer drawerFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextViewRegular toolbarTitleTextView;
    private ImageView addReportButton;
    private ImageView expandableListButton;
    private Stack<String> pageTitleList = new Stack<>();
    private boolean isReplacer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN_ACTIVITY = MainActivity.this;
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        displayView(R.string.nav_home, getString(R.string.nav_home));
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbarTitleTextView = (TextViewRegular) findViewById(R.id.toolbarTitleTextView);
        addReportButton = (ImageView) findViewById(R.id.addReportButton);
        expandableListButton = (ImageView) findViewById(R.id.expandableListButton);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawer, toolbar);
        drawerFragment.setDrawerListener(this);
    }

    private void initListeners() {
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                displayView(R.string.nav_scheduled_reports, getString(R.string.nav_scheduled_reports));
//                call(ReportsIncludedFragment.newInstance(), "");
                Utils.openActivity(MAIN_ACTIVITY, AddReportsActivity.class);
            }
        });
        expandableListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_body);
                LisOfVehiclesMapFragment lisOfVehiclesMapFragment = (LisOfVehiclesMapFragment) fragment;
                lisOfVehiclesMapFragment.showExpandableList();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            backDialog();
        } else {
            if (LisOfVehiclesMapFragment.slideUp != null && LisOfVehiclesMapFragment.slideUp.isVisible()) {
                LisOfVehiclesMapFragment.slideUp.hide();
                return;
            }

            if (MyLocateManager.popupView != null && MyLocateManager.popupView.getVisibility() == View.VISIBLE) {
                if (MyLocateManager.popupView.getParent() != null) {
                    ((ViewGroup) MyLocateManager.popupView.getParent()).removeView(MyLocateManager.popupView);
                    return;
                }
            }

            // TODO to be removed
            if (isReplacer) {
                pageTitleList.pop();
                isReplacer = false;
            }

            pageTitleList.pop();
            toolbarTitleTextView.setText(pageTitleList.get(pageTitleList.size() - 1));
            //            ConnectionManager.cancelAllRequests();
            super.onBackPressed();
            if (toolbarTitleTextView.getText().equals(getString(R.string.nav_scheduled_reports))) {
                showImageToolbar();
            } else {
                hideImageToolbar();
            }

            if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_MAP_STYLE_CHANGED)) {
//                LisOfVehiclesMapFragment lisOfVehiclesMapFragment = (LisOfVehiclesMapFragment) getSupportFragmentManager().findFragmentById(R.id.container_body);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(MAIN_ACTIVITY.getString(R.string.nav_map));
                if (fragment instanceof LisOfVehiclesMapFragment) {
                    LisOfVehiclesMapFragment lisOfVehiclesMapFragment = (LisOfVehiclesMapFragment) fragment;
                    lisOfVehiclesMapFragment.updateMapStyleOnBackPressed();
                }
            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int titleId) {
        displayView(titleId, getString(titleId));
    }

    public void displayView(int titleId, String title) {
        switch (titleId) {
            case R.string.nav_user_management:
//                new FragmentCaller().call(MAIN_ACTIVITY, null);
                break;

            case R.string.nav_map://0795258181
                new MyStartMapViewManager(MAIN_ACTIVITY);
                break;

            case R.string.nav_list_of_vehicles:
                call(ListOfVehiclesFragment.newInstance(""), title);
                break;

            case R.string.nav_geo_fence_list:
                call(GeoFenceListFragment.newInstance(), title);
                break;

            case R.string.nav_landmark_list:
                call(LandmarkListFragment.newInstance(), title);
                break;

            case R.string.nav_alarm_notification:
                call(AlarmNotificationsFragment.newInstance(""), title);
                break;

            case R.string.nav_scheduled_reports:
                call(ScheduledReportsFragment.newInstance(), title);
                break;

            case R.string.nav_about:
                call(AboutUsFragment.newInstance(), title);
                break;

            case R.string.nav_help:
                call(HelpFragment.newInstance(), title);
                break;

            case R.string.nav_logout:
                logout();
                break;

            default:
                callRemoveAllThenReplace(HomeFragment.newInstance(), title);
                break;
        }
    }

    public void displayView(int titleId, String title, String stringValue) {
        switch (titleId) {
            case R.string.nav_map:
                new MyStartMapViewManager(MAIN_ACTIVITY);
                break;

            case R.string.nav_list_of_vehicles:
                call(ListOfVehiclesFragment.newInstance(stringValue), title);
                break;

            case R.string.geo_fence:
                callReplacer(GeoFenceFragment.newInstance(stringValue, null, 0.0, 0.0), title);
                break;

            case R.string.historical_route_playback:
                callReplacer(HistoricalRouteFragment.newInstance(stringValue), title);
                break;

            case R.string.alarm_notification:
                call(AlarmNotificationsFragment.newInstance(stringValue), title);
                break;

            case R.string.data_analysis:
                call(DataAnalysisFragment.newInstance(stringValue), title);
                break;

            default:
                callRemoveAllThenReplace(HomeFragment.newInstance(), title);
                break;
        }
    }

    public void call(Fragment fragment, String title) {
        try {
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in, R.anim.hyperspace_out, R.anim.hyperspace_in, R.anim.slide_out);
                ft.add(R.id.container_body, fragment, title);
                ft.addToBackStack("backStack");
                ft.commit();
                pageTitleList.push(title);
                toolbarTitleTextView.setText(title);
                if (title.equals(getString(R.string.nav_scheduled_reports))) {
                    showImageToolbar();
                } else {
                    hideImageToolbar();
                }
                if (title.equals(getString(R.string.nav_map))) {
                    showExpandableImageToolbar();
                } else {
                    hideExpandableImageToolbar();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callUpAnimation(Fragment fragment, String title) {
        try {
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_up, R.anim.slide_out_down);
                ft.add(R.id.container_body, fragment, title);
                ft.addToBackStack("backStack");
                ft.commit();
                pageTitleList.push(title);
                toolbarTitleTextView.setText(title);
                if (title.equals(getString(R.string.nav_scheduled_reports))) {
                    showImageToolbar();
                } else {
                    hideImageToolbar();
                }
                if (title.equals(getString(R.string.nav_map))) {
                    showExpandableImageToolbar();
                } else {
                    hideExpandableImageToolbar();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callRemoveAllThenReplace(Fragment fragment, String title) {
        try {
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                ft.replace(R.id.container_body, fragment, title);
                ft.addToBackStack(null);
                ft.commit();
                pageTitleList.push(title);
                toolbarTitleTextView.setText(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callReplacer(Fragment fragment, String title) {
        try {
            if (fragment != null) {
                fragmentManager = MAIN_ACTIVITY.getSupportFragmentManager();
                fragmentManager.popBackStack();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                ft.replace(R.id.container_body, fragment, title);
                ft.addToBackStack(null);
                ft.commit();
                pageTitleList.push(title);
                toolbarTitleTextView.setText(title);
                isReplacer = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideImageToolbar() {
        addReportButton.setVisibility(View.GONE);
    }

    public void showImageToolbar() {
        addReportButton.setVisibility(View.VISIBLE);
    }

    public void hideExpandableImageToolbar() {
        expandableListButton.setVisibility(View.GONE);
    }

    public void showExpandableImageToolbar() {
        expandableListButton.setVisibility(View.VISIBLE);
    }

    private void backDialog() {
        AlertDialogView.yesNoButtonDialog(MAIN_ACTIVITY, MAIN_ACTIVITY, getString(R.string.are_you_sure_do_want_to_exit_saferoad), getString(R.string.app_name), new Click() {
            @Override
            public void onClick() {
                MAIN_ACTIVITY.finishAffinity();
            }

            @Override
            public void addMaps() {

            }
        });
    }

    public void logout() {
        AlertDialogView.yesNoButtonDialog(MAIN_ACTIVITY, MAIN_ACTIVITY, getString(R.string.are_you_sure_do_want_to_logout), getString(R.string.logout), new Click() {
            @Override
            public void onClick() {
                AppUtils.endSession(MAIN_ACTIVITY);
                PreferencesManager.getInstance().clear();
                Intent intent = new Intent(MAIN_ACTIVITY, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void addMaps() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.checkConnectingToInternet(MAIN_ACTIVITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AlertDialogView.yesNoButtonDialog();
    }


    public void showChangeLanguageDialog() {
        String title;
        String message;
        String yButton;
        String nButton;
        if (AppUtils.isArabic()) {
            title = getString(R.string.change_langauge_en);
            message = getString(R.string.are_you_sure_do_want_to_change_language_to_english);
            yButton = getString(R.string.yes_en);
            nButton = getString(R.string.no_en);
        } else {
            title = getString(R.string.change_langauge_ar);
            message = getString(R.string.are_you_sure_do_want_to_change_language_to_arabic);
            yButton = getString(R.string.yes_ar);
            nButton = getString(R.string.no_ar);
        }

        AlertDialogView.twoButtonDialog(MAIN_ACTIVITY, MAIN_ACTIVITY, message, title, yButton, nButton, new Click() {
            @Override
            public void onClick() {
                if (PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR)) {
                    PreferencesManager.getInstance().setStringValue(AppConstant.LANGUAGE_EN, SharesPrefConstants.LANGUAGE);
                } else {
                    PreferencesManager.getInstance().setStringValue(AppConstant.LANGUAGE_AR, SharesPrefConstants.LANGUAGE);
                }
                changeLanguage();
            }

            @Override
            public void addMaps() {

            }
        });
    }

    private void changeLanguage() {
        setLocale();
        pageTitleList.empty();
        Intent i = new Intent(MAIN_ACTIVITY, SplashActivity.class);
        i.putExtra(AppConstant.SETTINGS, true);
        finish();
        startActivity(i);
    }
}

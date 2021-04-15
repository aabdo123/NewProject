package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.R;
import com.activities.MainActivity;
import com.models.DashboardModel;
import com.utilities.constants.AppConstant;
import com.views.TextViewRegular;

import static com.utilities.constants.AppConstant.PROGRESS_DASHBOARD_MODEL_ARGS;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class ProgressFragment extends Fragment {

    private static ProgressFragment fragment;
    private Activity activity;
    private DashboardModel dashboardModel;
    private int animationDuration = 1000;
    private TextViewRegular numberOfCarsTextView;
    private TextViewRegular percentageTextView;
    private ImageView carGreenImageView;
    private CircularProgressBar circularProgressbar;
    private RelativeLayout dashboardMainLayout;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance(DashboardModel dashboardModel) {
//        if (fragment == null) {
            fragment = new ProgressFragment();
            Bundle args = new Bundle();
            args.putParcelable(PROGRESS_DASHBOARD_MODEL_ARGS, dashboardModel);
            fragment.setArguments(args);
//        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            Log.d("tag", "getArgs");
            dashboardModel = mBundle.getParcelable(PROGRESS_DASHBOARD_MODEL_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        initView(rootView);
        initListeners();
        setUpData();
        return rootView;
    }

    private void initView(View rootView) {
        dashboardMainLayout = (RelativeLayout) rootView.findViewById(R.id.dashboardMainLayout);
        numberOfCarsTextView = (TextViewRegular) rootView.findViewById(R.id.numberOfCarsTextView);
        percentageTextView = (TextViewRegular) rootView.findViewById(R.id.percentageTextView);
        carGreenImageView = (ImageView) rootView.findViewById(R.id.carGreenImageView);
        circularProgressbar = (CircularProgressBar) rootView.findViewById(R.id.circularProgressbar);
    }

    private void initListeners() {
        dashboardMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).displayView(R.string.nav_list_of_vehicles, getString(R.string.nav_list_of_vehicles),  AppConstant.ONLINE_CARS);
            }
        });
    }

    private void setUpData() {
        try {
            circularProgressbar.setProgressWithAnimation(getPercentage(dashboardModel.getTotalVehiclesOnlinePercentage()), animationDuration);
            percentageTextView.setText("" + getPercentage(dashboardModel.getTotalVehiclesOnlinePercentage()));
        } catch (Exception e) {
            circularProgressbar.setProgressWithAnimation(0, animationDuration);
            Toast.makeText(getContext(), getString(R.string.something_went_worng), Toast.LENGTH_SHORT).show();
        }
    }

    private int getPercentage(double per) {
        return (int) (per * 100);
    }
}
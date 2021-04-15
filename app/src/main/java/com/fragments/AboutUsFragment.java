package com.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.R;
import com.utilities.AppUtils;
import com.views.TextViewRegular;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class AboutUsFragment extends Fragment {

    private TextViewRegular webSiteTextView;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        webSiteTextView = (TextViewRegular) rootView.findViewById(R.id.webSiteTextView);
        webSiteTextView.setOnClickListener(v -> {
            Intent browserIntent;
            if (AppUtils.isAstroGps()) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.astrogps.com"));
            } else {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.saferoad.com.sa"));
            }
            startActivity(browserIntent);
        });
    }
}
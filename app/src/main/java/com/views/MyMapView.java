package com.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Created by Saferoad-Dev1 on 10/24/2017.
 */

public class MyMapView extends MapView {

    private ViewParent mViewParent;

    public MyMapView(Context context) {
        super(context);
    }

    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }

    public void setViewParent(@Nullable final ViewParent viewParent) { //any ViewGroup
        mViewParent = viewParent;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null == mViewParent) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    mViewParent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (null == mViewParent) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    mViewParent.requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}

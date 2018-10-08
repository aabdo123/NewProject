package com.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.R;
import com.utilities.constants.AppConstant;

public class AnimationUtils {


    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(AppConstant.ANIMATION_DURATION);//(int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(AppConstant.ANIMATION_DURATION);//(int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void loadListAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = android.view.animation.AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    public static void showToolBar(View toolbar) {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public static void hideToolBar(View toolbar) {
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
    }

    public static void slideDownToolbar(View toolbar) {
        // instagram open activity
        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.setTranslationY(-actionbarSize);
        toolbar.animate()
                .translationY(0)
                .setDuration(AppConstant.ANIMATION_DURATION)
                .setStartDelay(AppConstant.ANIMATION_DELAY);
    }

    public static void slideUpToolbar(View view) {
        // instagram open activity
        int actionbarSize = Utils.dpToPx(56);
        view.setTranslationY(actionbarSize);
        view.setTranslationY(actionbarSize);
        view.setTranslationY(actionbarSize);
        view.animate()
                .translationY(0)
                .setDuration(AppConstant.ANIMATION_DURATION)
                .setStartDelay(AppConstant.ANIMATION_DELAY).start();
    }

    private void setupWindowAnimations(Activity activity) {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.END);
        slideTransition.setDuration(AppConstant.ANIMATION_DURATION);
        activity.getWindow().setReenterTransition(slideTransition);
        activity.getWindow().setExitTransition(slideTransition);
    }

    public static void rotateAnimation(boolean rotate, View view) {
        // TODO : You Will Found this in AppConstants
        final float INITIAL_POSITION = 0.0f;
        final float ROTATED_POSITION = 180f;

        RotateAnimation rotateAnimation;
        if (rotate) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
    }


    public static void rotateAnimation(float rotate, ImageView view) {
        RotateAnimation rotateAnimation = new RotateAnimation(-rotate,
                0.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        view.startAnimation(rotateAnimation);
    }
}

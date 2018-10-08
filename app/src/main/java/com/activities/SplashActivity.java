package com.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.R;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.utilities.AppUtils;
import com.utilities.Utils;
import com.utilities.constants.SharesPrefConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class SplashActivity extends BaseActivity {

    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;
    public static final int ZOOM_IN_DELAY = 11000;
    public static final int START_ACTIVITY_DELAY = 5000;
    private Animation zoomIn;

    private AppCompatActivity SPLASH_ACTIVITY;

    private ProgressBar progressBar;
    private RelativeLayout backGroundSplashRelativeLayout;
    private ImageView logoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPLASH_ACTIVITY = SplashActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        iniViews();
        animate();
        startZoomInAnimation();
        setStartupDelay();
//        getHashKey();
    }

    private void iniViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        backGroundSplashRelativeLayout = (RelativeLayout) findViewById(R.id.backGroundSplashRelativeLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        if (AppUtils.isAstroGps()) {
            logoImageView.setPadding(Utils.dpToPx(34), 0, Utils.dpToPx(34), 0);
        }
    }

    private void setStartupDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.SAVE_LOGIN)) {
                    getTokenApiCall();
                } else {
                    Utils.finishAndOpenActivity(SPLASH_ACTIVITY, LoginActivity.class);
                }
            }
        }, START_ACTIVITY_DELAY);
    }

    private void startZoomInAnimation() {
        zoomIn = AnimationUtils.loadAnimation(SPLASH_ACTIVITY, R.anim.zoomin);
        backGroundSplashRelativeLayout.startAnimation(zoomIn);
        animationImplement(ZOOM_IN_DELAY);
    }

    private void animationImplement(long duration) {
        zoomIn.setDuration(duration);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                setStartupDelay();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void animate() {
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-150)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.4f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    private void getTokenApiCall() {
        progressBar.setVisibility(View.VISIBLE);
        BusinessManager.postLogin(
                PreferencesManager.getInstance().getStringValue(SharesPrefConstants.USERNAME),
                PreferencesManager.getInstance().getStringValue(SharesPrefConstants.PASSWORD),
                new ApiCallResponse() {
                    @Override
                    public void onSuccess(int statusCode, Object responseObject) {
                        progressBar.setVisibility(View.GONE);
                        Utils.finishAndOpenActivity(SPLASH_ACTIVITY, MainActivity.class);
                    }

                    @Override
                    public void onFailure(int statusCode, String errorResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (statusCode == 400) {
                            Utils.finishAndOpenActivity(SPLASH_ACTIVITY, LoginActivity.class);
                        }
                    }
                });
    }


    void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.saferoad",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                KeyHash.length();
                Log.e("hash >>> ", KeyHash);

//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("hash >>> ", KeyHash);
//                clipboard.setPrimaryClip(clip);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
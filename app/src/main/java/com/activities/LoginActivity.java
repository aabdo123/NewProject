package com.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.R;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.managers.PreferencesManager;
import com.models.TokenModel;
import com.utilities.AppUtils;
import com.utilities.ToastHelper;
import com.utilities.constants.SharesPrefConstants;
import com.views.Progress;
import com.utilities.Utils;
import com.views.ButtonBold;
import com.views.TextViewRegular;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

    private static AppCompatActivity LOGIN_ACTIVITY;
    private AppCompatEditText userNameEditText;
    private AppCompatEditText passwordEditText;
    private ButtonBold loginButton;
    private TextViewRegular forgetPasswordTextView;
    private AppCompatCheckBox savePasswordCheckBox;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGIN_ACTIVITY = LoginActivity.this;
        LOGIN_ACTIVITY.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LOGIN_ACTIVITY.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
    }

    private void initViews() {
        userNameEditText = (AppCompatEditText) findViewById(R.id.userNameEditText);
        passwordEditText = (AppCompatEditText) findViewById(R.id.passwordEditText);
        loginButton = (ButtonBold) findViewById(R.id.loginButton);
        forgetPasswordTextView = (TextViewRegular) findViewById(R.id.forgetPasswordTextView);
        savePasswordCheckBox = (AppCompatCheckBox) findViewById(R.id.savePasswordCheckBox);
        savePasswordCheckBox.setChecked(true);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
    }

    private void initListeners() {
        loginButton.setOnClickListener(this);
        forgetPasswordTextView.setOnClickListener(this);
        passwordEditText.setOnEditorActionListener(this);
        savePasswordCheckBox.setOnCheckedChangeListener(this);
        userNameEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (userNameEditText.getCompoundDrawables()[2] != null) {
                        if (event.getX() >= (userNameEditText.getRight() - userNameEditText.getLeft() - userNameEditText.getCompoundDrawables()[2].getBounds().width())) {
                            userNameEditText.setText("");
                        }
                    }
                }
                return false;
            }
        });
    }


    private void validation() {
        if (userNameEditText.getText().toString().isEmpty()) {
            userNameEditText.setError(getString(R.string.filed_is_required));
            return;
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError(getString(R.string.filed_is_required));
            return;
        }

//        if (!Utils.isEmailValid(userNameEditText.getText())) {
//            userNameEditText.setError(getString(R.string.invalid_email));
//            return;
//        }
        saveUserData();
        getTokenApiCall();
    }

    private void saveUserData() {
        PreferencesManager.getInstance().setStringValue(userNameEditText.getText().toString(), SharesPrefConstants.USERNAME);
        PreferencesManager.getInstance().setStringValue(passwordEditText.getText().toString(), SharesPrefConstants.PASSWORD);
        if (savePasswordCheckBox.isChecked()) {
            PreferencesManager.getInstance().setBooleanValue(true, SharesPrefConstants.SAVE_LOGIN);
        } else {
            PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.SAVE_LOGIN);
        }
    }

    private void getTokenApiCall() {
        Progress.showLoadingDialog(LOGIN_ACTIVITY);
        BusinessManager.postLogin(userNameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                Progress.dismissLoadingDialog();
                TokenModel tokenModel = (TokenModel) responseObject;
                PreferencesManager.getInstance().setStringValue(tokenModel.getTokenType(), SharesPrefConstants.TOKEN_TYPE);
                PreferencesManager.getInstance().setStringValue(tokenModel.getAccessToken(), SharesPrefConstants.ACCESS_TOKEN);
                PreferencesManager.getInstance().setStringValue(tokenModel.getUserID(), SharesPrefConstants.USER_ID);
                startActivity(new Intent(LOGIN_ACTIVITY, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
                if (statusCode == 400) {
                    ToastHelper.toastMessageLong(LOGIN_ACTIVITY, getString(R.string.wrong_username_or_password));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                validation();
                break;

            case R.id.forgetPasswordTextView:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            validation();
            Utils.hideKeyboardOnSubmit(LOGIN_ACTIVITY);
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
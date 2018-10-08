package com.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.application.MyApplication;
import com.managers.PreferencesManager;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by malikabuqaoud on 12/6/16.
 */

public class BaseActivity extends AppCompatActivity {

    public static Context mContext = MyApplication.getAppContext();

    AppCompatActivity BASE_ACTIVITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ACTIVITY = BaseActivity.this;
        Fabric.with(BASE_ACTIVITY, new Crashlytics());
        PreferencesManager.initializeInstance(BASE_ACTIVITY);
        setLocale();
    }

    public Gson gHelper(){
        return new Gson();
    }

    public void setLocale() {
        if (PreferencesManager.getInstance().getStringValue(SharesPrefConstants.LANGUAGE).equals(AppConstant.LANGUAGE_AR)) {
            PreferencesManager.getInstance().setStringValue(AppConstant.LANGUAGE_AR, SharesPrefConstants.LANGUAGE);
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            PreferencesManager.getInstance().setStringValue(AppConstant.LANGUAGE_EN, SharesPrefConstants.LANGUAGE);
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}










//keytool -list -v -keystore C:\Users\Saferoad-Dev1\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
//
//
//        C:\Program Files\Java\jdk1.8.0_151\bin
//
//        keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000
//
//
//        androiddebugkey -storepass android -keypass android
//        Alias name: androiddebugkey
//        Creation date: Oct 26, 2017
//        Entry type: PrivateKeyEntry
//        Certificate chain length: 1
//        Certificate[1]:
//        Owner: C=US, O=Android, CN=Android Debug
//        Issuer: C=US, O=Android, CN=Android Debug
//        Serial number: 1
//        Valid from: Thu Oct 26 09:45:12 EEST 2017 until: Sat Oct 19 09:45:12 EEST 2047
//        Certificate fingerprints:
//        MD5:  BF:1B:FC:46:0A:D5:BB:3E:29:08:D8:71:94:C1:92:19
//        SHA1: 62:61:99:11:14:80:C3:5A:EC:B3:1D:B8:A3:73:96:A7:59:55:35:11
//        SHA256: 8D:DF:66:1B:A7:46:87:24:C3:E6:8F:49:7B:80:C0:C4:BA:EA:5F:90:22:C1:DD:F4:51:B2:51:CC:5D:0B:5D:89
//        Signature algorithm name: SHA1withRSA
//        Subject Public Key Algorithm: 1024-bit RSA key
//        Version: 1

package com.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.FirebaseApp;

/**
 * Created by malikabuqaoud on 3/26/17.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    public static Context getAppContext() {
        String val = MyApplication.context.getClass().getSimpleName();
        return MyApplication.context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        super.attachBaseContext(MyContextWrapper.wrap(base,"en"));
        MultiDex.install(this);

    }
}


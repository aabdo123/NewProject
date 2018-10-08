package com.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.R;

public class FragmentCaller {

    public void call(AppCompatActivity a, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = a.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.hyperspace_out, R.anim.hyperspace_in, R.anim.slide_out);
            ft.add(R.id.container_body, fragment);
            ft.addToBackStack("backStack");
            ft.commit();
        }
    }

    public void callReplacer(AppCompatActivity a, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = a.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            ft.replace(R.id.container_body, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void callRemoveAllThenReplace(AppCompatActivity a, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = a.getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            ft.replace(R.id.container_body, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void callHomeFragment(AppCompatActivity a, Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = a.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.hyperspace_out, R.anim.hyperspace_in, R.anim.slide_out);
            ft.add(R.id.container_body, fragment);
            ft.commit();
        }
    }
}
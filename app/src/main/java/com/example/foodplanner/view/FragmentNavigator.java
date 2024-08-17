package com.example.foodplanner.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentNavigator {
    public static void addFragment(Fragment fragment, FragmentActivity appCompatActivity,int containerViewId) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}

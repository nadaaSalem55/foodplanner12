package com.example.foodplanner.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodplanner.R;
import com.example.foodplanner.view.FragmentNavigator;
import com.example.foodplanner.view.SplashScreenFragment;

public class MainActivity extends AppCompatActivity {
    Fragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashFragment=new SplashScreenFragment();
        hideActionBar();
        FragmentNavigator.addFragment(splashFragment,this,R.id.fragment_container);

    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
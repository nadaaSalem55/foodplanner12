package com.example.foodplanner.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplanner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity{
    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        initViews();

    }

    private void initViews() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);

    }
}
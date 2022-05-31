package com.utkarsh.wificonnect;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.utkarsh.wificonnect.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;
BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        


        bottomNav = binding.bottomNavigation;
        bottomNav.setBackgroundColor(getResources().getColor(R.color.bottom_menu));


        bottomNav.setSelectedItemId(R.id.nav_connect);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectFragment())
              .commit();
        Log.d(TAG, "onCreate: HI");


        



        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_tools:
                        selectedFragment = new ToolsFragment();
                        break;

                    case R.id.nav_connect:
                        selectedFragment = new ConnectFragment();
                        break;

                    case R.id.nav_creds:
                        selectedFragment = new CredentialsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();


                return true;
            }});



    }

    public void changeFragment(Fragment fragment, int id){

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
        bottomNav.setSelectedItemId(id);

    }

}
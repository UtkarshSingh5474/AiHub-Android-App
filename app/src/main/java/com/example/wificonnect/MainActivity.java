package com.example.wificonnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.wificonnect.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;
BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNav = binding.bottomNavigation;


        bottomNav.setSelectedItemId(R.id.nav_connect);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectFragment())
                .commit();

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
package com.utkarsh.wificonnect;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        bottomNav = binding.bottomNavigation;
        bottomNav.setBackgroundColor(ContextCompat.getColor(this, R.color.bottom_menu));


        bottomNav.setSelectedItemId(R.id.nav_connect);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectFragment())
              .commit();
        Log.d(TAG, "onCreate: HI");


        



        bottomNav.setOnItemSelectedListener(item -> {
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
                default:
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();


            return true;
        });



    }

    public void changeFragment(Fragment fragment, int id){

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
        bottomNav.setSelectedItemId(id);

    }

}
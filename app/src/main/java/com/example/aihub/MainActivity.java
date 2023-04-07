package com.example.aihub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.aihub.databinding.ActivityMainBinding;
import com.example.aihub.fragments.HubFragment;
import com.example.aihub.fragments.LearnFragment;
import com.example.aihub.fragments.ProfileFragment;
import com.example.aihub.services.CheckNetwork;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth mAuth;
    AnimatedBottomBar bottomNav;

    //If you are using ProGuard, add following rule to proguard config file:
    //
    //-keep class com.shockwave.**
    //pdfviewer error


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(v);

        CheckNetwork.isInternetAvailable(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        bottomNav = findViewById(R.id.bottom_navigation);
        try {
            if (getIntent().getExtras().getInt("change") == 1) {
                bottomNav.selectTabAt(0, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearnFragment())
                        .commit();
            }else if (getIntent().getExtras().getInt("change") == 2) {
                bottomNav.selectTabAt(1, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HubFragment())
                        .commit();
            }
        }catch (Exception e){
            bottomNav.selectTabAt(1, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HubFragment())
                    .commit();
        }



        bottomNav.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                Fragment selectedFragment = null;
                switch (i1) {
                    case 0:
                        if (currentFragment instanceof HubFragment|| currentFragment instanceof ProfileFragment) {
                            ft.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                        }else{
                            ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                        }
                        selectedFragment = new LearnFragment();
                        break;

                    case 1:
                        if (currentFragment instanceof ProfileFragment) {
                            ft.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                        }else{
                            ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                        }
                        selectedFragment = new HubFragment();
                        break;

                    case 2:
                        ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                        selectedFragment = new ProfileFragment();
                        break;
                }


                ft.replace(R.id.fragment_container,
                        selectedFragment).commit();
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    //change fragment
    public void changeFragment() {
        bottomNav.selectTabAt(1, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HubFragment())
                .commit();
    }

}

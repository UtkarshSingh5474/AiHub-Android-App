package com.utkarsh.wificonnect;

import android.animation.Animator;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.utkarsh.wificonnect.databinding.FragmentConnectBinding;

public class ConnectFragment extends Fragment {

    private FragmentConnectBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConnectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "App Opened!");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        binding.secretText.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "Developed By Utkarsh Singh", Toast.LENGTH_SHORT).show();
        });

        binding.menu.setOnClickListener(view1 -> {

            //https://docs.google.com/forms/d/e/1FAIpQLSfEBBnnTgGj4cnQs1STirvXNte4uINYp0CpTnVkHq-UaLw4gg/viewform?usp=pp_url&entry.966055269=ANDROIDAPIVERSION


            PopupMenu popup = new PopupMenu(getContext(), binding.menu);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.options_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    if ("Feedback".equals(item.getTitle())) {
                        openForms();
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        });

        binding.widgetLL.setOnClickListener(view1 -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppWidgetManager mAppWidgetManager = (AppWidgetManager) getActivity().getSystemService(AppWidgetManager.class);

                ComponentName myProvider = new ComponentName(getContext(), ConnectWidget.class);
                Intent pinnedWidgetCallbackIntent = new Intent(getContext(), ConnectWidget.class);
                PendingIntent successCallback = PendingIntent.getBroadcast(getContext(), 0,
                        pinnedWidgetCallbackIntent, 0);
                mAppWidgetManager.requestPinAppWidget(myProvider, null, successCallback);
            }


        });


        PushDownAnim.setPushDownAnimTo(binding.connectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        binding.connectBtn.setOnClickListener(view1 -> {


            WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);


            if(!wifiManager.isWifiEnabled()){

                wifiManager.setWifiEnabled(true);

            }

            // Retrieving the value using its keys the file name
            // must be same in both saving and retrieving the data
            SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

            // The value will be default as empty string because for
            // the very first time when the app is opened, there is nothing to show
            String username = sh.getString("username", "");
            String password = sh.getString("password", "");

            if (username.isEmpty() || password.isEmpty()){
                Snackbar snackbar = Snackbar.make(view,"Please update your AdmNo and Password in the " +
                        "credentials tab.",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                MainActivity mainActivity = (MainActivity)getActivity();
                                mainActivity.changeFragment(new CredentialsFragment(),R.id.nav_creds);

                            }
                        });
                snackbar.setActionTextColor(ContextCompat.getColor(getContext(),R.color.green_accent));
                snackbar.show();
            }else {
                if (!wifiManager.isWifiEnabled()){
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    Toast.makeText(getActivity(), "Please Connect To ABESEC WIFI", Toast.LENGTH_LONG).show();
                }else {

                    binding.connectBtn.setVisibility(View.INVISIBLE);
                    binding.animView.setVisibility(View.VISIBLE);

                    binding.animView.playAnimation();

                    binding.animView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                            Intent intent = new Intent(getActivity(),Login.class);
                            startActivity(intent);
                            binding.connectBtn.setVisibility(View.VISIBLE);
                            binding.animView.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });


                }


            }
        });

        return view;
    }

    private void openForms(){


        String url = "https://docs.google.com/forms/d/e/1FAIpQLSfEBBnnTgGj4cnQs1STirvXNte4uINYp0CpTnVkHq-UaLw4gg/viewform?usp=pp_url&entry.966055269=" + Build.VERSION.SDK_INT;

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);




    }
}
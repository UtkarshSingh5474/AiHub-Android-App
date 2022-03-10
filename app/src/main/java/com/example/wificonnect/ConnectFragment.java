package com.example.wificonnect;

import static android.content.Context.MODE_APPEND;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.wificonnect.databinding.FragmentConnectBinding;
import com.example.wificonnect.databinding.FragmentCredentialsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

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

        binding.secretText.setOnClickListener(view1 -> {
            //binding.secretText.setTextColor(ContextCompat.getColor(getContext(),R.color.grey_subtext));
            Toast.makeText(getContext(), "Developed By Utkarsh Singh", Toast.LENGTH_SHORT).show();
        });

        binding.menu.setOnClickListener(view1 -> {
            PopupMenu popup = new PopupMenu(getContext(), binding.menu);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.options_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    if ("Feedback".equals(item.getTitle())) {
                        sendEmail();
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
                    Intent intent = new Intent(getActivity(),Login.class);
                    startActivity(intent);
                }


            }
        });

        return view;
    }

    private void sendEmail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"utkarshsingh.5474@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "WifiConnect App FeedBack");
        email.putExtra(Intent.EXTRA_TEXT, "Android API Version: " + Build.VERSION.SDK_INT);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
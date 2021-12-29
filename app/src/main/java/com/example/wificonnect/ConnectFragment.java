package com.example.wificonnect;

import static android.content.Context.MODE_APPEND;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wificonnect.databinding.FragmentConnectBinding;
import com.example.wificonnect.databinding.FragmentCredentialsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ConnectFragment extends Fragment {

    private FragmentConnectBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConnectBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
                        "credentials tab",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                MainActivity mainActivity = (MainActivity)getActivity();
                                mainActivity.changeFragment(new CredentialsFragment(),R.id.nav_creds);

                            }
                        });
                snackbar.setActionTextColor(Color.RED);
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
}
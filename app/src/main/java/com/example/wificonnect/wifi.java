package com.example.wificonnect;

import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class wifi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        String networkPass = "justcause";

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            WifiNetworkSuggestion.Builder builder = new WifiNetworkSuggestion.Builder()
                    .setSsid("archeva")
                    .setWpa2Passphrase("justcause");
            WifiNetworkSuggestion suggestion = builder.build();

            ArrayList<WifiNetworkSuggestion> list = new ArrayList<>();
            list.add(suggestion);

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int status = manager.addNetworkSuggestions(list);

            if (status == STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                WifiNetworkSpecifier.Builder builder1 = new WifiNetworkSpecifier.Builder();
                builder1.setSsid("archeva");
                builder1.setWpa2Passphrase("justcause");

                WifiNetworkSpecifier wifiNetworkSpecifier = builder1.build();
                NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
                networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
                networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED);
                networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);
                NetworkRequest networkRequest = networkRequestBuilder.build();
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    cm.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(@NonNull Network network) {
                            //Use this network object to Send request.
                            //eg - Using OkHttp library to create a service request
                            Toast.makeText(wifi.this, "done", Toast.LENGTH_SHORT).show();
                            super.onAvailable(network);
                        }
                    });
            }
        }


    }
}
}
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

        WifiNetworkSpecifier.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder = new WifiNetworkSpecifier.Builder();
            builder.setSsid("archeva");
            builder.setWpa2Passphrase("justcause");

            WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

            NetworkRequest.Builder networkRequestBuilder1 = new NetworkRequest.Builder();
            networkRequestBuilder1.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            networkRequestBuilder1.setNetworkSpecifier(wifiNetworkSpecifier);

            NetworkRequest nr = networkRequestBuilder1.build();
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            ConnectivityManager.NetworkCallback networkCallback = new
                    ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(@NonNull Network network) {
                            super.onAvailable(network);
                            Log.d("wifi_conn", "onAvailable:" + network);
                            assert cm != null;
                            cm.bindProcessToNetwork(network);
                        }

                    };
            assert cm != null;
            cm.requestNetwork(nr, networkCallback);
        }


//        WifiConfiguration wifiConfig = new WifiConfiguration();
//        if (wifi.isWifiEnabled() == false) {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
//        }
//        wifiConfig.SSID = String.format("\"%s\"", "archeva");
//        wifiConfig.preSharedKey = String.format("\"%s\"", "justcause");
//        //wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//
//        wifiConfig.status = WifiConfiguration.Status.ENABLED;
//        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//        //remember id
//        int netId = wifi.addNetwork(wifiConfig);
//        wifi.enableNetwork(netId, true);

    }
}
package com.example.wificonnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){

            wifiManager.setWifiEnabled(true);

        }

        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });


        String url = "https://192.168.1.254:8090";

        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String username = sh.getString("username", "");
        String password = sh.getString("password", "");
        if (username.isEmpty() || password.isEmpty()){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout),"Please update your AdmNo and Password in the " +
                    "credentials tab",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }else {
            if (!wifiManager.isWifiEnabled()){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                Toast.makeText(this, "Please Connect To ABESEC WIFI", Toast.LENGTH_LONG).show();

            }
        }
        Log.d("Credentials", "Credentials " + username + password);

        WebView myWebView = (WebView) findViewById(R.id.webview);


        final String js = "javascript:document.getElementById('username').value='"+username+"';"
                + "document.getElementById('password').value='"+password+"';"
                +"document.getElementById('loginbutton').click()";

        myWebView.loadUrl(url);
        myWebView.getSettings().setJavaScriptEnabled(true);


        myWebView.setWebViewClient(new WebViewClient(){


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // before: handler.cancel();
                handler.proceed();
            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                view.evaluateJavascript(js, new ValueCallback<String>() {



                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("ReceivedValue", "onReceiveValue: " +s);

                        TextView textView = findViewById(R.id.toolbarVal);
                        textView.setText(s);

                        //Toast.makeText(getApplicationContext(), "Connected Successfully!" +username+" "+ password,
                          //      Toast.LENGTH_SHORT).show();
                    }

                });

                if (url.contains("google")){
                    Toast.makeText(getApplicationContext(), "Connected Successfully", Toast.LENGTH_LONG).show();
                }

            }});



    }

}
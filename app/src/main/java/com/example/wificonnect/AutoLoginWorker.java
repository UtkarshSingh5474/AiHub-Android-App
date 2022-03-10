package com.example.wificonnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.material.snackbar.Snackbar;

public class AutoLoginWorker extends Worker {
    public AutoLoginWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {


        String url = "https://192.168.1.254:8090";

        SharedPreferences sh = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String username = sh.getString("username", "");
        String password = sh.getString("password", "");
        if (username.isEmpty() || password.isEmpty()){
            //Stop The Worker Service
        }else {

        }
        Log.d("Credentials", "Credentials " + username + password);

        WebView myWebView = new WebView(getApplicationContext());


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

                        //Toast.makeText(getApplicationContext(), "Connected Successfully!" +username+" "+ password,
                        //      Toast.LENGTH_SHORT).show();
                    }

                });

                if (url.contains("google")){
                    Toast.makeText(getApplicationContext(), "Connected Successfully", Toast.LENGTH_LONG).show();
                }

            }});


        return Result.success();

        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)
    }
}

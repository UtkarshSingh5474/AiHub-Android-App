package com.utkarsh.wificonnect;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.crashlytics.internal.Logger.TAG;

import android.animation.Animator;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.utkarsh.wificonnect.databinding.FragmentConnectBinding;

public class ConnectFragment extends Fragment {

    private AppUpdateManager appUpdateManager;
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
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if(sh.getBoolean("rateBtnClicked",false) == false){

            binding.rateLL.setVisibility(View.VISIBLE);

        }
        binding.rateLL.setOnClickListener(view1 -> {

            Bundle bundle2 = new Bundle();
            bundle2.putString("rated", "Rated With App!");
            mFirebaseAnalytics.logEvent("rate_button", bundle2);

            askRating();
            binding.rateLL.setVisibility(View.GONE);
            SharedPreferences.Editor editor = sh.edit();
            editor.putBoolean("rateBtnClicked",true)
                    .commit();

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


            WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);


            if (!wifiManager.isWifiEnabled()) {

                wifiManager.setWifiEnabled(true);

            }



            // The value will be default as empty string because for
            // the very first time when the app is opened, there is nothing to show
            String username = sh.getString("username", "");
            String password = sh.getString("password", "");

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Please update your AdmNo and Password in the " +
                                "credentials tab.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.changeFragment(new CredentialsFragment(), R.id.nav_creds);

                            }
                        });
                snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.green_accent));
                snackbar.show();
            } else {
                if (!wifiManager.isWifiEnabled()) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    Toast.makeText(getActivity(), "Please Connect To ABESEC WIFI", Toast.LENGTH_LONG).show();
                } else {

                    binding.connectBtn.setVisibility(View.INVISIBLE);
                    binding.animView.setVisibility(View.VISIBLE);

                    binding.animView.playAnimation();


                }


            }
        });

        binding.animView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                Intent intent = new Intent(getActivity(), Login.class);
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

        //In-App-Update



        appUpdateManager = AppUpdateManagerFactory.create(getContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.FLEXIBLE, getActivity(),100);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });





        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Checks that the platform will allow the specified type of update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE, getActivity(),100);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void openForms() {


        String url = "https://docs.google.com/forms/d/e/1FAIpQLSfEBBnnTgGj4cnQs1STirvXNte4uINYp0CpTnVkHq-UaLw4gg/viewform?usp=pp_url&entry.966055269=" + Build.VERSION.SDK_INT;

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void askRating() {

        ReviewManager manager = ReviewManagerFactory.create(getContext());


        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(task2 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.\

                    Log.d(TAG, "askRating: Launched!");
                });
            } else {
                // There was some problem, log or handle the error code.

            }
        });


    }
}
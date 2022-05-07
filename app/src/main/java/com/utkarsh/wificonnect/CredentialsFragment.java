package com.utkarsh.wificonnect;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.utkarsh.wificonnect.databinding.FragmentCredentialsBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class CredentialsFragment extends Fragment {

    private FragmentCredentialsBinding binding;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCredentialsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String username = sh.getString("username", null);
        String password = sh.getString("password", null);
        binding.admissionNumber.setText(username);
        binding.password.setText(password);

        binding.mainLayout.setOnClickListener(view1 -> {
            closeKeyboard(view1);
        });

        if(!binding.admissionNumber.getText().toString().isEmpty()){
            binding.admissionNumber.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldAdmNo.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldAdmNo.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
            binding.textfieldAdmNo.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
        }

        if(!binding.password.getText().toString().isEmpty()){
            binding.password.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldPass.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldPass.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
            binding.textfieldPass.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
        }



        binding.admissionNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length()!=0){
                    binding.admissionNumber.setBackgroundResource(R.drawable.rounded_corners_selected);
                    binding.textfieldAdmNo.setBackgroundResource(R.drawable.rounded_corners_selected);
                    binding.textfieldAdmNo.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
                    binding.textfieldAdmNo.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
                }else {
                    binding.textfieldAdmNo.setBackgroundResource(R.drawable.rounded_corners_unselected);
                    binding.textfieldAdmNo.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
                    binding.admissionNumber.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_purple_background));
                }

            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length()!=0){
                    binding.password.setBackgroundResource(R.drawable.rounded_corners_selected);
                    binding.textfieldPass.setBackgroundResource(R.drawable.rounded_corners_selected);
                    binding.textfieldPass.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
                    binding.textfieldPass.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
                }else {
                    binding.textfieldPass.setBackgroundResource(R.drawable.rounded_corners_unselected);
                    binding.textfieldPass.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.grey_subtext)));
                    binding.password.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_purple_background));
                }

            }
        });


        PushDownAnim.setPushDownAnimTo(binding.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.btnSave.setOnClickListener(view1 -> {

            if(binding.admissionNumber.getText().toString().isEmpty()){
                binding.admissionNumber.setError("Cannot be empty!");
                binding.admissionNumber.requestFocus();
                return;
            }

            if(binding.password.getText().toString().isEmpty()){
                binding.password.setError("Cannot be empty!");
                binding.password.requestFocus();
                return;
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

            // Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("username", binding.admissionNumber.getText().toString());
            myEdit.putString("password", binding.password.getText().toString());

            // Once the changes have been made,
            // we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();

            Toast.makeText(getActivity(), "Credentials Saved!", Toast.LENGTH_SHORT).show();
            closeKeyboard(view1);

        });



        return view;
    }



    public void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view1 = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view1 == null) {
            view1 = new View(view.getContext());
        }else {
            getActivity().getCurrentFocus().clearFocus();
        }

        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
    }
}
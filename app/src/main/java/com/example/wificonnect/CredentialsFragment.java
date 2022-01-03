package com.example.wificonnect;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.example.wificonnect.databinding.FragmentCredentialsBinding;

public class CredentialsFragment extends Fragment {

    private FragmentCredentialsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCredentialsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String username = sh.getString("username",null);
        String password = sh.getString("password", null);
        binding.admissionNumber.setText(username);
        binding.password.setText(password);


        binding.password.setOnClickListener(view1 -> {
            binding.password.setFocusableInTouchMode(true);
            binding.password.requestFocus();
            binding.textfieldPass.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldPass.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
            binding.password.setBackgroundResource(R.drawable.rounded_corners_selected);

            binding.admissionNumber.setFocusableInTouchMode(false);
            binding.textfieldAdmNo.setBackgroundResource(R.drawable.rounded_corners_unselected);
            binding.textfieldAdmNo.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.grey_subtext)));
            binding.admissionNumber.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.dark_purple_background));
        });

        binding.admissionNumber.setOnClickListener(view1 -> {
            binding.admissionNumber.setFocusableInTouchMode(true);
            binding.admissionNumber.requestFocus();
            binding.textfieldAdmNo.setBackgroundResource(R.drawable.rounded_corners_selected);
            binding.textfieldAdmNo.setStartIconTintList(ColorStateList.valueOf(Color.WHITE));
            binding.admissionNumber.setBackgroundResource(R.drawable.rounded_corners_selected);

            binding.password.setFocusableInTouchMode(false);
            binding.textfieldPass.setBackgroundResource(R.drawable.rounded_corners_unselected);
            binding.textfieldPass.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.grey_subtext)));
            binding.password.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.dark_purple_background));
        });


        binding.btnSave.setOnClickListener(view1 -> {

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

    private void closeKeyboard(View view) { InputMethodManager imm =
            (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
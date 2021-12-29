package com.example.wificonnect;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
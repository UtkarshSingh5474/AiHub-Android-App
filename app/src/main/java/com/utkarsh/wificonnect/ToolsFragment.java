package com.utkarsh.wificonnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.utkarsh.wificonnect.databinding.FragmentCredentialsBinding;
import com.utkarsh.wificonnect.databinding.FragmentToolsBinding;

public class ToolsFragment extends Fragment {

    private FragmentToolsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentToolsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.version.setText("Version: "+BuildConfig.VERSION_CODE + "("+BuildConfig.VERSION_NAME+")");

        binding.version.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "Developed By Utkarsh Singh", Toast.LENGTH_SHORT).show();
        });



        return view;
    }


}
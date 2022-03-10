package com.example.wificonnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.wificonnect.databinding.FragmentCredentialsBinding;
import com.example.wificonnect.databinding.FragmentToolsBinding;

import java.util.concurrent.TimeUnit;

public class ToolsFragment extends Fragment {

    private FragmentToolsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentToolsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //Spinner hrSpinner = (Spinner) binding.hrSpinner;
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
        //        R.array.Hours, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //hrSpinner.setAdapter(adapter);
        //hrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //    @Override
        //    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //        hrSpinner.getItemAtPosition(i);
        //    }
//
        //    @Override
        //    public void onNothingSelected(AdapterView<?> adapterView) {
        //        hrSpinner.setSelection(0);
        //    }
        //});



        return view;
    }


}
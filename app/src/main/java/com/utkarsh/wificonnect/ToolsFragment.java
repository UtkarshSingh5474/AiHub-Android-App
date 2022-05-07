package com.utkarsh.wificonnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
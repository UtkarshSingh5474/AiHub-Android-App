package com.utkarsh.wificonnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.utkarsh.wificonnect.databinding.FragmentToolsBinding;


public class ToolsFragment extends Fragment {

    private FragmentToolsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentToolsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.version.setText("Version: "+ BuildConfig.VERSION_CODE + "("+BuildConfig.VERSION_NAME+")");

        binding.version.setOnClickListener(view1 -> Toast.makeText(getContext(), "Developed By Utkarsh Singh", Toast.LENGTH_SHORT).show());


        binding.github.setOnClickListener(view1 -> {
            String githubUrl = "https://github.com/UtkarshSingh5474";
            Uri githubUri = Uri.parse(githubUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, githubUri);
            startActivity(intent);
        });

        binding.insta.setOnClickListener(view1 -> {
            String instaUrl = "https://www.instagram.com/utkarsh.5474/";
            Uri instaUri = Uri.parse(instaUrl);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, instaUri);
            startActivity(intent2);
        });

        binding.linkedin.setOnClickListener(view1 -> {
            String instaUrl = "https://www.linkedin.com/in/utkarsh5474/";
            Uri instaUri = Uri.parse(instaUrl);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, instaUri);
            startActivity(intent2);
        });

        return view;
    }


}
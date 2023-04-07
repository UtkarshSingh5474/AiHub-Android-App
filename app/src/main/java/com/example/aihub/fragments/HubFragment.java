package com.example.aihub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aihub.ImageClassifierActivity;
import com.example.aihub.FruitClassificationActivity;
import com.example.aihub.PredictionActivity;
import com.example.aihub.databinding.FragmentHubBinding;


public class HubFragment extends Fragment {

    private FragmentHubBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHubBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnFruit.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ImageClassifierActivity.class);
            intent.putExtra("url", "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/fruit-vision/?");
            intent.putExtra("title", "Fruit Vision");
            intent.putExtra("imageSize", 64);
            intent.putExtra("notebookUrl", "https://colab.research.google.com/drive/1stjCjPO26wJ6cODXYi0UXkvgSJTiG_Wy?usp=sharing");
            startActivity(intent);
        });

        binding.btnIndianFood.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ImageClassifierActivity.class);
            intent.putExtra("url", "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/indian-food-vision/?");
            intent.putExtra("title", "Indian Food Vision");
            intent.putExtra("imageSize", 224);
            intent.putExtra("notebookUrl", "https://colab.research.google.com/drive/1EltZVEpIWMMX0-NKj965UGitDiCVA87C?usp=sharing");
            startActivity(intent);
        });

        binding.btnFood.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ImageClassifierActivity.class);
            intent.putExtra("url", "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/food-vision/?");
            intent.putExtra("title", "Food Vision");
            intent.putExtra("imageSize", 224);
            intent.putExtra("notebookUrl", "https://colab.research.google.com/drive/1O_wzmV3K-lxa6wZ_8ioindgl5-wavQEy");
            startActivity(intent);
        });



        binding.btnUsedCarPrice.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PredictionActivity.class);
            intent.putExtra("url", "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/used-car-price/?");
            intent.putExtra("title", "Used Car Price");
            intent.putExtra("notebookUrl", "https://colab.research.google.com/drive/15AUbSmYhz0OSMXzTaVcIuzmdABbwaqpK?usp=sharing");
            startActivity(intent);
        });

        binding.btnSign.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ImageClassifierActivity.class);
            intent.putExtra("url", "https://models-api-zfdfcwnvrq-em.a.run.app/prediction/sign-language/?");
            intent.putExtra("title", "Sign Language");
            intent.putExtra("imageSize", 64);
            intent.putExtra("notebookUrl", "https://colab.research.google.com/drive/1jwUtbGjp8nWegPifB5GMw6PFmel1h-_r?usp=sharing");
            startActivity(intent);
        });


        // Inflate the layout for this fragment
        return view;
    }
}
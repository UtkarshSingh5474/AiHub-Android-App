package com.example.aihub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.aihub.databinding.ActivitySublessonsBinding;
import com.example.aihub.dataobjects.SubLessons;
import com.example.aihub.lessons.SubLessonRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SublessonsActivity extends AppCompatActivity {

    private ActivitySublessonsBinding binding;
    List<SubLessons> subLessonsList = new ArrayList<>();
    private static SubLessonRecyclerViewAdapter subLessonRecyclerViewAdapter;
    private static Map<String,String> progressMap = Collections.emptyMap();;
    private static String lessonIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySublessonsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        lessonIndex = (String) getIntent().getExtras().getString("lessonIndex");
        subLessonsList = (List<SubLessons>) getIntent().getSerializableExtra("subLessonsList");
        progressMap = (Map<String, String>) getIntent().getSerializableExtra("progressMap");


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.subLessonsRecyclerView.setLayoutManager(horizontalLayoutManager);
        subLessonRecyclerViewAdapter = new SubLessonRecyclerViewAdapter(this, subLessonsList, progressMap, lessonIndex);
        binding.subLessonsRecyclerView.setAdapter(subLessonRecyclerViewAdapter);


    }
}
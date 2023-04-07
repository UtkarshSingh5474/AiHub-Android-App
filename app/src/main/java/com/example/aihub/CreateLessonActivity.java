package com.example.aihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aihub.databinding.ActivityCreateLessonBinding;
import com.example.aihub.databinding.ActivityLoginBinding;
import com.example.aihub.dataobjects.Lessons;
import com.example.aihub.dataobjects.SubLessons;
import com.example.aihub.lessons.LessonRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateLessonActivity extends AppCompatActivity {

    private ActivityCreateLessonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateLessonBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.isNewLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.isNewLesson.isChecked()) {
                    binding.lessonNameTextView.setVisibility(View.VISIBLE);
                    binding.lessonIndexTextView.setVisibility(View.VISIBLE);
                }else{
                    binding.lessonNameTextView.setVisibility(View.GONE);
                }
            }
        });



        binding.btnCreateLesson.setOnClickListener(v1 -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            SubLessons subLessons = new SubLessons(binding.subLessonIndexTextView.getText().toString(), binding.subLessonNameTextView.getText().toString(), binding.subLessonCardsTextView.getText().toString());

            if (binding.isNewLesson.isChecked()) {
                List<SubLessons> subLessonsList = new ArrayList<>();
                subLessonsList.add(subLessons);
                Lessons lesson = new Lessons(subLessonsList, binding.lessonIndexTextView.getText().toString(), binding.lessonNameTextView.getText().toString());
                db.collection("Lessons").add(lesson)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d("CreateLessonActivity", task.getException().getMessage());
                                //Toast.makeText(PropertyAds.this, "error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "unknown error (re030)", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else {
                db.collection("Lessons")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                        if (document.exists()) {

                                            if (document.get("index").toString().equals(binding.lessonIndexTextView.getText().toString())) {
                                                db.collection("Lessons").document(document.getId()).update("subLessons", FieldValue.arrayUnion(subLessons));
                                            }
                                        }
                                    }



                                } else {

                                    Log.d("LearnFragment", task.getException().getMessage());
                                    //Toast.makeText(PropertyAds.this, "error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });
    }
}

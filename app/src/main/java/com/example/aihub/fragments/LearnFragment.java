package com.example.aihub.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aihub.databinding.FragmentLearnBinding;
import com.example.aihub.dataobjects.Lessons;
import com.example.aihub.dataobjects.SubLessons;
import com.example.aihub.lessons.LessonRecyclerViewAdapter;
import com.example.aihub.services.CheckNetwork;
import com.example.aihub.services.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class LearnFragment extends Fragment {

    //If you are using ProGuard, add following rule to proguard config file:
    //
    //-keep class com.shockwave.**
    //pdfviewer error

    private FragmentLearnBinding binding;
    private static List<Lessons> lessonsObject = new ArrayList<>();
    private static List<SubLessons> subLessonsList = new ArrayList<>();
    private static Map<String,String> progressMap = Collections.emptyMap();;
    private static LessonRecyclerViewAdapter lessonRecyclerViewAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLearnBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //get data from firebase firestore
        getDataFromFirestore();


        return view;


    }

    private void getDataFromFirestore() {

        if (!CheckNetwork.isInternetAvailable(getContext())) {

            return;
        }

        //binding.emptyTextView.setVisibility(View.GONE);

        LoadingBar loadingBar = new LoadingBar(getContext(), "Loading Lessons...");
        loadingBar.show();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lessons")
                .orderBy("index", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            lessonsObject = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                if (document.exists()) {


                                    Lessons pfo = document.toObject(Lessons.class);
                                    lessonsObject.add(new Lessons(pfo.subLessons, pfo.getIndex(), pfo.getName()));
                                }
                            }

                            db.collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc = task.getResult();
                                                if(doc.exists()){
                                                    progressMap = (Map<String, String>) doc.get("progress");
                                                    Log.d("progressMap", "onComplete: "+ progressMap);

                                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                                    binding.lessonsRecyclerView.setLayoutManager(horizontalLayoutManager);
                                                    lessonRecyclerViewAdapter = new LessonRecyclerViewAdapter(getContext(), lessonsObject, progressMap);
                                                    binding.lessonsRecyclerView.setAdapter(lessonRecyclerViewAdapter);
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        }
                                    });



                        } else {

                            Log.d("LearnFragment", task.getException().getMessage());
                            //Toast.makeText(PropertyAds.this, "error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "unknown error (re030)", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
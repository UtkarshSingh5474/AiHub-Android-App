package com.example.aihub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aihub.CreateLessonActivity;
import com.example.aihub.LoginActivity;
import com.example.aihub.R;
import com.example.aihub.WelcomeActivity;
import com.example.aihub.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getContext(), WelcomeActivity.class));
        }
        DocumentReference db = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());


        db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String firstName = documentSnapshot.getString("name");
                if(firstName.contains(" ")){
                    firstName= firstName.substring(0, firstName.indexOf(" "));
                    System.out.println(firstName);
                }
                binding.nameTV.setText("Hi, " + firstName);
                binding.emailTV.setText(documentSnapshot.getString("email"));
                binding.phoneTV.setText(documentSnapshot.getString("phone"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error " + e, Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnSignOut.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        binding.myAds.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "This Page is Underconstruction!", Toast.LENGTH_SHORT).show();

        });
        binding.settings.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "This Page is Underconstruction!", Toast.LENGTH_SHORT).show();

        });
        binding.help.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "This Page is Underconstruction!", Toast.LENGTH_SHORT).show();

        });
        binding.feedback.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "This Page is Underconstruction!", Toast.LENGTH_SHORT).show();

        });
        binding.terms.setOnClickListener(view1 -> {

            Toast.makeText(getContext(), "This Page is Underconstruction!", Toast.LENGTH_SHORT).show();

        });

        if(user.getEmail().equals("utkarshsingh.5474@gmail.com")){
            binding.btnCreateLesson.setVisibility(View.VISIBLE);
        }
        binding.btnCreateLesson.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), CreateLessonActivity.class));
        });

        return view;
    }


}
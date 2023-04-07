package com.example.aihub.lessons;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.aihub.MainActivity;
import com.example.aihub.R;
import com.example.aihub.databinding.ActivityLessonExampleBinding;
import com.example.aihub.fragments.LearnFragment;
import com.example.aihub.fragments.ProfileFragment;
import com.example.aihub.services.CheckNetwork;
import com.example.aihub.services.LoadingBar;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LessonExampleActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();

        if(progress!=0){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String key = "progress." + lessonIndex+":"+subLessonIndex;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                    .update(""+key,""+String.valueOf(progress));
        }

    }

    private ActivityLessonExampleBinding binding;
    final int[] pageReached = {0};
    private int progress =0;
    private String lessonIndex;
    private String subLessonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonExampleBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        if (!CheckNetwork.isInternetAvailable(this)) {
            onBackPressed();

        }

        progress = getIntent().getExtras().getInt("progress");

        lessonIndex = getIntent().getExtras().getString("lessonIndex");
        subLessonIndex = getIntent().getExtras().getString("subLessonIndex");

        binding.lessonTextView.setText("Lesson "+subLessonIndex);
        Log.d("LessonExample", "onCreate: "+progress);

        loadPdf();
        if (progress==0){
            binding.progressBar.setProgress(0);
            binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.red));
            binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.red));
        }else{
            binding.progressBar.setProgress(progress);
            if (progress <= 25) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.red));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.red));
            } else if (progress <= 50) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.orange));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.orange));
            } else if (progress <= 75) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.yellow));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.yellow));
            } else if (progress <= 100) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.green));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.green));
            }
        }


        binding.nextButton.setOnClickListener(view -> {
            binding.pdfView.jumpTo(binding.pdfView.getCurrentPage() + 1, true);
            binding.currentPageTextView.setText(String.valueOf(binding.pdfView.getCurrentPage() + 1));
        });
        binding.prevButton.setOnClickListener(view -> {
            binding.pdfView.jumpTo(binding.pdfView.getCurrentPage() - 1, true);
            binding.currentPageTextView.setText(String.valueOf(binding.pdfView.getCurrentPage() + 1));
        });

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    int round(float f) {
        return (int) (f + 0.5f);
    }

    void setProgress(int page, int pageCount) {
        binding.pageCountTextView.setText(String.valueOf(pageCount));
        int progressLocal = (int) (((page + 1) / (float) pageCount) * 100);
        if (page+1> pageReached[0] && progressLocal>progress){
            pageReached[0] = page;
            progress = progressLocal;
            binding.progressBar.setProgress((int)progress);
            if (progress <= 25) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.red));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.red));
            } else if (progress <= 50) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.orange));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.orange));
            } else if (progress <= 75) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.yellow));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.yellow));
            } else if (progress <= 100) {
                binding.progressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.green));
                binding.progressBar.setReachedBarColor(ContextCompat.getColor(this, R.color.green));
            }
        }
    }

    void loadPdf(){
        LoadingBar loadingBar = new LoadingBar(this, "Loading...");
        loadingBar.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("Lesson Pdf's/"+lessonIndex+"-"+subLessonIndex+".pdf");
        pathReference.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                binding.pdfView.fromBytes(bytes)
                        .swipeHorizontal(true)
                        .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                        .fitEachPage(false)
                        .enableSwipe(false)
                        .enableDoubletap(false)
                        .pageSnap(true)
                        .autoSpacing(false)
                        .pageFling(true)
                        .onPageChange((page, pageCount) -> {
                            setProgress(page, pageCount);
                        })

                        .load();
                loadingBar.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(LessonExampleActivity.this, "Error Loading Lesson\nTry Again", Toast.LENGTH_SHORT).show();
                onBackPressed();
                loadingBar.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("change",1);
        startActivity(intent);
        finish();
    }
}
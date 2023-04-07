package com.example.aihub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.aihub.databinding.ActivityNotebookViewerBinding;

public class NotebookViewerActivity extends AppCompatActivity {

    private ActivityNotebookViewerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotebookViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the notebook name from the intent
        String notebookName = getIntent().getStringExtra("notebookName");
        binding.title.setText(notebookName + " Notebook");
        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(getIntent().getStringExtra("notebookUrl"));
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

}
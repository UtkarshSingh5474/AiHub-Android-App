package com.example.aihub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aihub.databinding.ActivityImageClassifierBinding;
import com.example.aihub.dataobjects.FoodVisionResponse;
import com.example.aihub.services.CheckNetwork;
import com.example.aihub.services.LoadingBar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Charsets;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ImageClassifierActivity extends AppCompatActivity {

    private ActivityImageClassifierBinding binding;
    Button camera, gallery;
    ImageView imageView;
    TextView result;
    int imageSize = 224;
    String title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageClassifierBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        if (!CheckNetwork.isInternetAvailable(this)) {
            onBackPressed();
        }
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        imageSize = getIntent().getIntExtra("imageSize", 224);
        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);
        String notebookUrl = getIntent().getStringExtra("notebookUrl");

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        binding.title.setText(title);

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
        binding.btnNotebook.setOnClickListener(view -> {
            Intent intent = new Intent(ImageClassifierActivity.this, NotebookViewerActivity.class);
            intent.putExtra("notebookName", title);
            intent.putExtra("notebookUrl", notebookUrl);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                uploadFile(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                uploadFile(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //upload image to firebase storage
    private void uploadFile(Bitmap bitmap) {
        LoadingBar loadingBar = new LoadingBar(this, "Uploading Image");
        loadingBar.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        int randomName = (int) (Math.random() * 100000);
        StorageReference mountainImagesRef = storageRef.child("Temp Images/" + randomName + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String image_link = uri.toString();
                        //classifyImage
                        loadingBar.dismiss();
                        postRequest(image_link, String.valueOf(randomName));
                    }
                });

            }
        });

    }

    //classify image
    private void postRequest(String image_link, String ImageName) {
        LoadingBar loadingBar = new LoadingBar(this, "Classifying Image");
        loadingBar.show();
        OkHttpClient client = new OkHttpClient();
        String query = null;
        try {
            query = URLEncoder.encode(image_link, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url+"image_link="+query)
                .get()
                .build();

        client.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.d("response", "FAIL: "+e.getMessage());
                loadingBar.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String res = response.body().string();
                    FoodVisionResponse responseObject = parseJson(res);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.classified.setText("Classified as:");
                            result.setText(responseObject.getModel_prediction());
                            loadingBar.dismiss();
                        }
                    });
                    deleteFileFromFirebase(ImageName);

                    Log.d("response", "onResponse: "+res);
                }
            }
        });

    }

    //json string to object
    private FoodVisionResponse parseJson(String json) {
        Gson gson = new Gson();
        FoodVisionResponse response = gson.fromJson(json, FoodVisionResponse.class);
        return response;
    }

    private void deleteFileFromFirebase(String imageName){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("Temp Images/"+imageName+".jpg");
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("response", "onSuccess: deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("response", "onFailure: "+exception.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("change",2);
        startActivity(intent);
        finish();
    }

}
/*package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView resultIv;
    Button camera;
    Button files;

    ActivityResultLauncher<Void> cameraResultLauncher;
    ActivityResultLauncher<Uri> cameraFullSizeResultLauncher;
    ActivityResultLauncher<String> pickContentResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.camera);
        files = findViewById(R.id.file);
        resultIv = findViewById(R.id.imageView);

        initLaunchers();

        camera.setOnClickListener(v -> cameraFun());
        files.setOnClickListener(v -> fileFun());

    }

    public void cameraFun() {

        cameraFullSizeResultLauncher.launch(Uri.parse(""));
    }

    public void fileFun() {

        pickContentResultLauncher.launch("image/*");
    }

    private void initLaunchers() {

        cameraFullSizeResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),//חוזה
                new ActivityResultCallback<Boolean>() {//call back
                    @Override
                    public void onActivityResult(Boolean result) {
                        //true if the image saved to the uri given in the launch function
                    }

                });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                Log.d("onActivityResult", "Result: " + result);
                resultIv.setImageBitmap(result);
            }
        });


        *//*pickContentResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Log.d("onActivityResult", "Result: " + result);
                resultIv.setImageURI(result);
            }
        });*//*
    }
}*/


package com.example.myapplication;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final String AUTHORITY_OF_A_FILEPROVIDER = "com.example.myapplication.android.fileprovider"; //todo change it and in  android:authorities="com.example.myapplication.fileprovider"

    static final int REQUEST_IMAGE_CAPTURE = 1;
    TextView resultTv;
    ImageView resultIv;
    String currentPhotoPath;
    Uri photoURI;

    ActivityResultLauncher<Uri> cameraFullSizeResultLauncher; //big img
    ActivityResultLauncher<String> pickContentResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultIv = findViewById(R.id.result_image);
        resultTv = findViewById(R.id.test_result);

        initLaunchers();

        Button cameraBtn = findViewById(R.id.pic_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFullSizeResultLauncher.launch(dispatchTakePictureIntent());

            }
        });


        Button pickPicBtn = findViewById(R.id.pick_picture);
        pickPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickContentResultLauncher.launch("image/*");
            }
        });

    }

    private void initLaunchers() {
        // camera
        cameraFullSizeResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            //true if the image saved to the uri given in the launch function
            Log.d("initLaunchers", "result: " + result);
            if (result) {
                resultIv.setImageURI(photoURI);
                FirebaseStorgeRepository.getInstance().UploadFile(photoURI);
            }

        });

        // gallery
        pickContentResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Log.d("pickContentResultLauncher", "result: " + result);
            if (result != null) {
                resultIv.setImageURI(result);
                FirebaseStorgeRepository.getInstance().UploadFile(result);

            }
        });
    }

    public Uri dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoURI = null;
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("dispatchTakePictureIntent", "" + "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, AUTHORITY_OF_A_FILEPROVIDER, photoFile);
            }
        }

        return photoURI;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("currentPhotoPath", "createImageFile: " + currentPhotoPath);
        return image;
    }


}


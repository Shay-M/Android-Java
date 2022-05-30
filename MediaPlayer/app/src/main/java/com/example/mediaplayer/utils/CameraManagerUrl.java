package com.example.mediaplayer.utils;//package com.example.mediaplayer.utils;/* Created by Shay Mualem 30/07/2021 */
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContract;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.FileProvider;
//
//import com.example.mediaplayer.MainActivity;
//
//import java.io.File;
//
//public class CameraManagerUrl extends AppCompatActivity {
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private static CameraManagerUrl instead;
//    File file;
//
//    ActivityResultLauncher<Uri> cameraFullSizeResultLauncher;
//
////    ActivityResultLauncher<Uri> cameraFullSizeResultLauncher;
//
//
//    private MainActivity context;
//
//
//    public CameraManagerUrl(MainActivity context) {
//        this.context = context;
//
//        cameraFullSizeResultLauncher = registerForActivityResult(new ActivityResultContract<Uri, Object>() {
//            @NonNull
//            @Override
//            public Intent createIntent(@NonNull Context context, Uri input) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
////                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                return intent;
//            }
//
//            @Override
//            public Object parseResult(int resultCode, @Nullable Intent intent) {
//                return intent;
//            }
//        });
////        cameraFullSizeResultLauncher = registerForActivityResult(
////                new ActivityResultContracts.StartActivityForResult(),
////                result -> {
////                    if (result.getResultCode() == Activity.RESULT_OK) {
////                        Log.d("someActivityResultLauncher", file.getAbsolutePath());
////                    }
////                });
//
//        file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pic1.jpg");
//
//
//    }
//
//    //singleton
//    public static CameraManagerUrl getInstance() {
//        if (instead == null)
//            throw new AssertionError("You have to call init first");
//        return instead;
//
//    }
//
//    public synchronized static CameraManagerUrl init(MainActivity context) {
//        if (instead != null) {
//            throw new AssertionError("You already initialized me");
//        }
//
//        instead = new CameraManagerUrl(context);
//        return instead;
//    }
//
//    private ActivityResultLauncher<Uri> registerForActivityResult(ActivityResultContract<Uri, Object> uriObjectActivityResultContract) {
//        return null;
//    }
//
//    public void TakePicFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//        Toast.makeText(context, fileUri.toString(), Toast.LENGTH_LONG).show();
//        Log.d("TakePicFromCamera", fileUri.toString());
//
//        cameraFullSizeResultLauncher.launch(fileUri);
//        //return fileUri.toString();
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//
//}
//


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.mediaplayer.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CameraManagerUrl {
    static final String AUTHORITY_OF_A_FILEPROVIDER = "com.retro_player.android.fileprovider";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static CameraManagerUrl instead;
    String currentPhotoPath;
    Uri photoURI;
    private MainActivity context;


    public CameraManagerUrl(MainActivity context) {
        this.context = context;
    }

    //singleton
    public static CameraManagerUrl getInstance() {
        if (instead == null)
            throw new AssertionError("You have to call init first");
        return instead;

    }

    public synchronized static CameraManagerUrl init(MainActivity context) {
        if (instead == null) {
            instead = new CameraManagerUrl(context);
            //throw new AssertionError("You already initialized me");
        }

        instead = new CameraManagerUrl(context);
        return instead;
    }

    public Uri dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("dispatchTakePictureIntent", "" + "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(context, AUTHORITY_OF_A_FILEPROVIDER, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                context.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        return photoURI;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

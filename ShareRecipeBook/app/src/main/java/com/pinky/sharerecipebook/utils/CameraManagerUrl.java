package com.pinky.sharerecipebook.utils;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.pinky.sharerecipebook.view.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CameraManagerUrl {
    static final String AUTHORITY_OF_A_FILEPROVIDER = "com.pinky.sharerecipebook.android.fileprovider";
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
            photoURI = null;
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("dispatchTakePictureIntent", "" + "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(context, AUTHORITY_OF_A_FILEPROVIDER, photoFile);
            } else
                Log.d("dispatchTakePictureIntent", "" + "Error, photoFile = null");

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

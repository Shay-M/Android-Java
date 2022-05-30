package com.example.myapplication;/* Created by Shay Mualem 22/09/2021 */

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Date;
import java.util.UUID;

// https://www.youtube.com/watch?v=MfCiiTEwt3g
//https://firebase.google.com/docs/storage/android/create-reference?authuser=0#java


public class FirebaseStorgeRepository {
    //private  FirebaseStorge

    static FirebaseStorgeRepository INSTANCE;
    // Create a Cloud Storage reference from the app
    private FirebaseStorage storage;
    private StorageReference mStorageRef;

    public FirebaseStorgeRepository() {
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        Log.d("FirebaseStorgeRepository", "");

    }

    public static FirebaseStorgeRepository getInstance() {
        Log.d("getInstance", "");
        if (INSTANCE == null)
            INSTANCE = new FirebaseStorgeRepository();

        return INSTANCE;
    }

    public void UploadFile(Uri imageUri) {
        Log.d("UploadFile", "imageUri:" + imageUri);

        String userID = "AuthAppRepository.getInstance().getCurrentUser().getUid()";
//        String fileName = (new File(String.valueOf(imageUri))).getName();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String uniqueString = UUID.randomUUID().toString();

        String fileName = "" + timeStamp + "_" + uniqueString + ".jpg ";

        // Create a reference
        StorageReference storageReference = mStorageRef.child("images/recipe/" + userID + "/" + fileName);
//        StorageReference storageReference = mStorageRef.child("images/recipe" + "/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d("UploadFile", "onSuccess");
//            Snackbar.make(findViewById(android.R.id.content),"Image Upload!")
                }).addOnFailureListener(e -> Log.d("UploadFile", "onFailure"))
        ;
    }

    public void LoadFile(StorageReference storage) {

       /* StorageReference pathReference = storageRef.child("images/stars.jpg");

        // Create a reference to a file from a Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
    */
    }

    private void loadImageFromStorage(Uri imageUri) {
        String path = imageUri.toString();
        File f = new File(path, "");
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//            image.setImageBitmap(b);

    }

}

package com.pinky.sharerecipebook.repositories;/* Created by Shay Mualem 22/09/2021 */

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

// https://www.youtube.com/watch?v=MfCiiTEwt3g
// https://firebase.google.com/docs/storage/android/create-reference?authuser=0#java
// https://firebase.google.com/docs/storage/android/upload-files?authuser=0


public class FirebaseStorgeRepository {

    static FirebaseStorgeRepository INSTANCE;
    public StorageReference storageReference;
    // public UploadTask uploadTask;
    public Uri downloadUri = null;
    // Create a Cloud Storage reference from the app
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;

    public FirebaseStorgeRepository() {
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference();
        Log.d("FirebaseStorgeRepository", "");

    }

    public static FirebaseStorgeRepository getInstance() {
        Log.d("getInstance", "");
        if (INSTANCE == null)
            INSTANCE = new FirebaseStorgeRepository();

        return INSTANCE;
    }

    public void UploadFile(Uri imageUri, String folder, OnTaskDownloadUri onTaskDownloadUri) {
        Log.d("UploadFile", "imageUri:" + imageUri);

        String userID = AuthRepository.getInstance().getCurrentUser().getUid(); // userID for name folder in db
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSS").format(new Date());

        // String fileName = (new File(String.valueOf(imageUri))).getName();
        // String uniqueString = UUID.randomUUID().toString();
        // String fileName = "" + timeStamp + "_" + uniqueString + ".jpg ";

        String fileName = "" + timeStamp + "" + ".jpg ";

        // Create a reference
        storageReference = mStorageRef.child("images/" + folder + "/" + userID + "/" + fileName);

        UploadTask uploadTask;
        uploadTask = storageReference.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("UploadFile", "onFailure");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("UploadFile", "onSuccess");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        //Log.d("UploadFile", "progress: " + progress + "% done");
                        onTaskDownloadUri.onProgress(progress);
                    }
                }) // for get file url upload
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d("@onComplete", "downloadUri: " + downloadUri);
                            onTaskDownloadUri.onSuccess(downloadUri);
                        } else {
                            // Handle failures
                            Log.d("onComplete", "uploadTask: Failure");
                            onTaskDownloadUri.onFailure();
                        }
                    }
                });


    }

         /*
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //
                    }
                })
                .addOnFailureListener(e -> Log.d("UploadFile", "onFailure"))
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    Log.d("UploadFile", "progress: " + progress + "% done");
                    //mProgressBar.setProgress((int) progress);
                });
                */


    //            Snackbar.make(findViewById(android.R.id.content),"Image Upload!")

               /* public void getFileDownloadUri() {
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.d("@onComplete", "downloadUri: " + downloadUri);

                            } else {
                                // Handle failures
                                Log.d("onComplete", "uploadTask: Failure");
                            }
                        }
                    });


                }*/

    public interface OnTaskDownloadUri {
        void onSuccess(Uri downloadUri);

        void onFailure();

        void onProgress(double progressNum);

    }
}
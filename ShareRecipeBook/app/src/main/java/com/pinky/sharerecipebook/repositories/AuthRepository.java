package com.pinky.sharerecipebook.repositories;

// https://www.youtube.com/watch?v=FuAz-ahdk0E

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinky.sharerecipebook.models.User;

public class AuthRepository {
    // Database
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference databaseReferenceUsers;
    private Application application;
    // auth
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    //log out
    private MutableLiveData<Boolean> loggedOutLiveData;

    public AuthRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        // Database
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReferenceUsers = firebaseDatabase.getReference("users");

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);

        }
    }

    public static FirebaseAuth getInstance() {
        return FirebaseAuth.getInstance();
    }

    public void login(String email, String password, OnTaskLoginAuth onTaskAuth) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(),
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userLiveData.postValue(firebaseAuth.getCurrentUser());
                                    loggedOutLiveData.postValue(false); // update live data - login user
                                    onTaskAuth.onSuccessLogin();
                                    //UserLoginHelper.getInstance().setUser(email);

                                } else {
                                    //Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    // Snackbar.make(getWindow().getDecorView(), "fdf", Snackbar.LENGTH_SHORT).show();
                                    onTaskAuth.onFailureLogin("Login Failure: " + task.getException().getMessage());
                                    loggedOutLiveData.postValue(true); // update live data - login user
                                }
                            }
                        });
    }

    public void register(String email, String password, String name, String deviceTokenId) { // create New User

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(firebaseAuth.getCurrentUser());

                        // better to send user
                        User newUser = new User(
                                name,
                                "", // todo
                                email,
                                firebaseAuth.getCurrentUser().getUid(),
                                deviceTokenId
                        );

                        String key = firebaseAuth.getCurrentUser().getUid(); //??? DatabaseReferenceUsers.push().getKey();
                        //String key = DatabaseReferenceUsers.push().getKey();

                        databaseReferenceUsers
                                .child(key)
                                .setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    Log.d("DatabaseReferenceUsers", "onComplete: ");
                                    loggedOutLiveData.postValue(false); // update live data

                                });

                    } else {
                        Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true); // update live data
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public void resatPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email);

    }

    public interface OnTaskLoginAuth {
        void onSuccessLogin();

        void onFailureLogin(String s);


    }


   /* void onSuccesslogOut();

    void onFailurelogOut();

    void onSuccessregister();

    void onFailureregister();*/
}
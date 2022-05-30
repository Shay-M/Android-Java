package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 17/09/2021 */

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.pinky.sharerecipebook.repositories.AuthRepository;

public class LogOutViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public LogOutViewModel(@NonNull Application application) {
        super(application);

        authRepository = new AuthRepository(application);
        userLiveData = authRepository.getUserLiveData();
        loggedOutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authRepository.logOut();
    }


    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}

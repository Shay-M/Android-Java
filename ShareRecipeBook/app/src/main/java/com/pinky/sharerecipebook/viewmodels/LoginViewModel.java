package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 17/09/2021 */

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.pinky.sharerecipebook.repositories.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> userLiveData;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        userLiveData = authRepository.getUserLiveData();

    }

    public void login(String email, String password, AuthRepository.OnTaskLoginAuth onTaskLoginAuth) {

        authRepository.login(email, password,onTaskLoginAuth );

    }

    public void register(String email, String password, String name, String deviceTokenId) {

        authRepository.register(email, password, name, deviceTokenId);

    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void resatPassword(String email) {
        authRepository.resatPassword(email);

    }
}

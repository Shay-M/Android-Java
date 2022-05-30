package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 17/09/2021 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.repositories.FirebaseDatabaseRepository;

import java.util.ArrayList;


public class HomeViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Recipe>> liveData;
    public MutableLiveData<User> userDbLoginliveData;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //    public LiveData<User> userliveData;
    private String userId = null;

    public void init() {
        if (liveData != null) {
            return;
        }
        liveData = FirebaseDatabaseRepository.getInstance().getRecipes();
    }

    public LiveData<ArrayList<Recipe>> getRecipeLiveData() {
        return liveData;
    }

    //////////////////////////
    public void initUserLogin() {
        if (userDbLoginliveData != null) {
            return;
        }


        if (AuthRepository.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else userId = null;

        userDbLoginliveData = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userId);
//        userliveData = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userId);

    }

    public LiveData<User> getUserLiveData() {

        /*if (userDbLoginliveData.getValue().getName() !="Guest" ) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userDbLoginliveData = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userId);
        }
*/      if(userDbLoginliveData != null){
            return userDbLoginliveData;
        }

        return FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userId);
    }



}
package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 19/09/2021 */

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;

import java.util.ArrayList;

public class AddRecipeViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Recipe>> liveData;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRecipeDBRef = database.getReference("recipe");
    private String mRecipeName;
    private String mPhotoURI;
    private User mLoginUserGet;


    public LiveData<ArrayList<Recipe>> getRecipeLiveData() {
        return liveData;
    }

    public void AttachNewRecipe(Recipe newRecipe) {
        String key = myRecipeDBRef.push().getKey();
        newRecipe.setRecipeId(key);
        myRecipeDBRef.child(key).setValue(newRecipe);
    }


    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }


    public void setRecipephotoURI(String photoURI) {
        mPhotoURI = photoURI;
    }

    public void setLoginUserGet(User loginUserGet) {
        mLoginUserGet = loginUserGet;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public String getmPhotoURI() {
        return mPhotoURI;
    }

    public User getmLoginUserGet() {
        return mLoginUserGet;
    }
}



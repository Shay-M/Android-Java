package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 01/10/2021 */
// https://www.youtube.com/watch?v=qWyzvkySKaU&list=PLs1bCj3TvmWmM-qN3FsCuPTTX-29I8Gh7&index=27

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.FirebaseDatabaseRepository;

import java.util.ArrayList;

public class RecipeDetailsViewModel extends ViewModel {

    public MutableLiveData<User> liveDataUserRecipeCreated;
    public MutableLiveData<User> liveDataCurrentUser;
    private String userMakeId;

    public void init() {
        if (liveDataUserRecipeCreated != null)
            return;
        liveDataUserRecipeCreated = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userMakeId);

    }

    public LiveData<User> getUserLiveData(String userId) {
        userMakeId = userId;
        liveDataUserRecipeCreated = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userId);
        return liveDataUserRecipeCreated;
    }

    public void setRecipe(Recipe recipeGet) {

    }

    public void changeLikeToRecipe(String IdTofind, int newValue) {
        String folder = "recipe";
        String fildeToChange = "rank";

        // change like in recipe
        FirebaseDatabaseRepository.getInstance().changeDataFirebase(folder, IdTofind, fildeToChange, Integer.toString(newValue), 0);

    }

    public void addIdLikeToUser(String IdTofind, ArrayList<String> newValue) {
        String folder = "users";
        String fildeToChange = "favoriteRecipe";

        // change like in recipe
        FirebaseDatabaseRepository.getInstance().changeDataFirebaseArrayList(folder, IdTofind, fildeToChange, newValue);

    }
}

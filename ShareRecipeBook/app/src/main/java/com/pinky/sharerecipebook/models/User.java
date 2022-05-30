package com.pinky.sharerecipebook.models;/* Created by Shay Mualem 31/08/2021 */

import java.util.ArrayList;

// https://stackoverflow.com/questions/52718594/what-is-the-best-way-to-add-data-to-a-list-in-firebase-realtime-database
public class User extends Guest {

    //Map<String, String> favoriteRecipeHashMap;
    //private Recipe myRecipe;
    private String firebaseUserId;
    private String deviceTokenId;
    private String emailAddress;
    private ArrayList<String> favoriteRecipe;
    // new user
    public User(String name, String userImagePath, String emailAddress, String firebaseUserId, String deviceTokenId) {
        super(name, 0, userImagePath);
        this.emailAddress = emailAddress;
        this.firebaseUserId = firebaseUserId;
        this.favoriteRecipe = new ArrayList<>();
        this.favoriteRecipe.add("NO_FAVORITE_RECIPE");
        this.deviceTokenId = deviceTokenId;
        //this.favoriteRecipeHashMap = new HashMap<>();
    }
    public User() {
        //need for fire base
    }

    public String getDeviceTokenId() {
        return deviceTokenId;
    }

    public void setDeviceTokenId(String deviceTokenId) {
        this.deviceTokenId = deviceTokenId;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public void setFirebaseUserId(String firebaseUserId) {
        this.firebaseUserId = firebaseUserId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ArrayList<String> getFavoriteRecipe() {
        return favoriteRecipe;
    }

    public void setFavoriteRecipe(ArrayList<String> favoriteRecipe) {
        this.favoriteRecipe = favoriteRecipe;
    }

    public void addFavoriteRecipe(String favoriteRecipe) {
        this.favoriteRecipe.add(favoriteRecipe);
    }

    public void removeFavoriteRecipe(String favoriteRecipeId) {
        this.favoriteRecipe.remove(favoriteRecipeId);
    }


}

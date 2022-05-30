package com.pinky.sharerecipebook.models;/* Created by Shay Mualem 31/08/2021 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable

public class Recipe implements Serializable {
    private String recipeId;
    private String firebaseUserIdMade;    //add userId
    private String firebaseDeviceTokenMade;
    private String title;
    private String preparation;
    private String ingredients; // convert to class
    private int rank;
    private String imagePath;
    private Map<String, Comment> commentArrayListHashMap;

    public Recipe(String firebaseUserIdMade, String firebaseDeviceTokenMade, String title, String preparation, String ingredients, String imagePath, String recipeId) {
        this.firebaseUserIdMade = firebaseUserIdMade;
        this.firebaseDeviceTokenMade = firebaseDeviceTokenMade;
        this.recipeId = recipeId;
        this.title = title;
        this.ingredients = ingredients;
        this.preparation = preparation;
        this.rank = 0;
        this.imagePath = imagePath;
        this.commentArrayListHashMap = new HashMap<>();

        //        this.commentArrayList = new ArrayList<>();
//        commentArrayListHashMap.put("try", new Comment("fd", "123"));
        /*Comment c = new Comment("sdfsdf", "sdfsdf");
        commentArrayList.add(c);*/
    }

    public Recipe() {
    } // needed

    public Map<String, Comment> getCommentArrayListHashMap() {
        return commentArrayListHashMap;
    }

    public void setCommentArrayListHashMap(Map<String, Comment> commentArrayListHashMap) {
        this.commentArrayListHashMap = commentArrayListHashMap;
    }


    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public String getFirebaseUserIdMade() {
        return firebaseUserIdMade;
    }

    public void setFirebaseUserIdMade(String firebaseUserIdMade) {
        this.firebaseUserIdMade = firebaseUserIdMade;
    }

    public String getFirebaseDeviceTokenMade() {
        return firebaseDeviceTokenMade;
    }

    public void setFirebaseDeviceTokenMade(String firebaseDeviceTokenMade) {
        this.firebaseDeviceTokenMade = firebaseDeviceTokenMade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}

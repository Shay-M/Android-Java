package com.pinky.sharerecipebook.models;/* Created by Shay Mualem 31/08/2021 */

import java.io.Serializable;

public class Guest implements Serializable {
    protected String name = "Guest";
    protected int ratingOfUser = 0;
    private String userImagePath = "";

    public Guest(String name, int ratingOfUser, String userImagePath) {
        this.name = name;
        this.ratingOfUser = ratingOfUser;
        this.userImagePath = userImagePath;
    }

    public Guest() {
        //need for fire base
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRatingOfUser() {
        return ratingOfUser;
    }

    public void setRatingOfUser(int ratingOfUser) {
        this.ratingOfUser = ratingOfUser;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

}

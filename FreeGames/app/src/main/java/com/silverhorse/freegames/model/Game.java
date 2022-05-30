package com.silverhorse.freegames.model;
/* Created by Shay Mualem on 09/12/2021 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Game implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("imageurl")
    private String imgUrl;
    @SerializedName("bio")
    private String info;

    public Game(String name, String imgUrl, String info) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}


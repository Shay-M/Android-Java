package com.silverhorse.retrofit.model;
/* Created by Shay Mualem on 18/12/2021 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class animl implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private Integer id;
    @SerializedName("age")
    private Integer age;
    @SerializedName("url")
    private String url;
    @SerializedName("imag")
    private String imageUrl;
    @SerializedName("kind")
    private String kind;

    public animl(String name, Integer id, Integer age, String url, String imageUrl, String kind) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.url = url;
        this.imageUrl = imageUrl;
        this.kind = kind;
    }


    public animl getAnimals() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}

package com.silverhorse.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    private Animals animals;
    @SerializedName("array")

    private List<Animals> match = null;

    public Post(List<Animals> match) {
        this.match = match;
    }
    public Post(Animals animals) {
        this.animals = animals;
    }

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    public List<Animals> getMatch() {
        return match;
    }

    public void setMatch(List<Animals> match) {
        this.match = match;
    }

    public static class Animals {

        @SerializedName("name")
        private String name;

        @SerializedName("id")
        private Integer id;

        @SerializedName("age")
        private Integer age;

        @SerializedName("url")
        private String url;

        public Animals(String name, Integer id, Integer age, String url) {
            this.name = name;
            this.id = id;
            this.age = age;
            this.url = url;
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
    }
}


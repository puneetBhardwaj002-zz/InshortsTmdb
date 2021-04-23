package com.example.inshortsmovie.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieGenre {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieGenre(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private int id;
}

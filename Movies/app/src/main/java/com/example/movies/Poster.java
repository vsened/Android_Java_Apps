package com.example.movies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Poster implements Serializable {

    @SerializedName("url")
    private String URL;

    public Poster(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "URL='" + URL + '\'' +
                '}';
    }
}

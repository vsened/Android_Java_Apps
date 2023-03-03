package com.example.movies;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("url")
    private final String url;
    @SerializedName("name")
    private final String name;

    public Trailer(String URL, String NAME) {
        this.url = URL;
        this.name = NAME;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

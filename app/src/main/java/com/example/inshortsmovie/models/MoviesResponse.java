package com.example.inshortsmovie.models;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    private boolean isError;

    @SerializedName("results")
    @Expose
    private List<Movies> results;

    public MoviesResponse(List<Movies> results) {
        this.results = results;
    }

    @Ignore
    public MoviesResponse() {
    }

    public List<Movies> getResults() {
        return results;
    }

    public void setResults(List<Movies> results) {
        this.results = results;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}

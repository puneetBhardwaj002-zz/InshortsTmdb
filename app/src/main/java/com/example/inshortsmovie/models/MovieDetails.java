package com.example.inshortsmovie.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "moviesDetails",inheritSuperIndices = true)
public class MovieDetails extends Movies {

    @SerializedName("budget")
    @Expose
    private long budget;

    @SerializedName("revenue")
    @Expose
    private long revenue;

    @SerializedName("runtime")
    @Expose
    private int runtime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("genres")
    @Expose
    @TypeConverters(MovieGenreTypeConverter.class)
    private List<MovieGenre> genres;

    public  boolean isError;

    @Ignore
    public MovieDetails(){

    }

    public MovieDetails(boolean adult, String posterPath, String overview, String releaseDate, int id, String originalTitle, String originalLanguage, String title, String backdropPath, double popularity, int voteCount, boolean video, double voteAverage, long budget, long revenue, int runtime, String status, String tagline,List<MovieGenre> genres) {
        super(adult, posterPath, overview, releaseDate, id, originalTitle, originalLanguage, title, backdropPath, popularity, voteCount, video, voteAverage);
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.genres = genres;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<MovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieGenre> genres) {
        this.genres = genres;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovieDetails movies = (MovieDetails) o;
        return this.getId() == movies.getId();
    }
}

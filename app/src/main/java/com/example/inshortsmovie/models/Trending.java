package com.example.inshortsmovie.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "trending",foreignKeys = {@ForeignKey(entity = Movies.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = ForeignKey.CASCADE)},indices = {@Index(value = "movie_id")})
public class Trending {
    @PrimaryKey(autoGenerate = true)
    public int trendingId;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}

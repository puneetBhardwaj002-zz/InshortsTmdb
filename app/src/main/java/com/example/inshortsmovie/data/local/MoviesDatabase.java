package com.example.inshortsmovie.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.inshortsmovie.models.Favourites;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.NowPlaying;
import com.example.inshortsmovie.models.Trending;

@Database(entities = {Movies.class, MovieDetails.class, Favourites.class, Trending.class, NowPlaying.class},version = 1,exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MoviesDao getMoviesDao();
}

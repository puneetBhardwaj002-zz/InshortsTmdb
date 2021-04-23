package com.example.inshortsmovie.data.local;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.MoviesResponse;

import io.reactivex.Flowable;

public interface MoviesLocalDataSource {
    Flowable<MoviesResponse> getNowPlayingMoviesCache();
    Flowable<MoviesResponse> getTrendingMoviesCache();
    Flowable<MoviesResponse> getFavouriteMoviesCache();
    Flowable<MovieDetails> getMovieDetails(int movieId);
    void insertMovie(Movies movies);
    void insertMovieDetails(MovieDetails movieDetails);
    void markMovieAsFavourite(int movieId);
    void removeMovieAsFavourite(int movieId);
    void insertTrendingMovie(int movieId);
    void insertNowPLayingMovie(int movieId);
}

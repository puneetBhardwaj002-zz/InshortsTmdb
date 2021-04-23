package com.example.inshortsmovie.data;

import android.net.Uri;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.MoviesResponse;

import io.reactivex.Flowable;

public interface MoviesRepository {
    Flowable<MoviesResponse> getTrendingMovies();
    Flowable<MoviesResponse> getLocalTrendingMovies();
    Flowable<MoviesResponse> getRemoteTrendingMovies();

    Flowable<MoviesResponse> getNowPlayingMovies();
    Flowable<MoviesResponse> getLocalNowPlayingMovies();
    Flowable<MoviesResponse> getRemoteNowPlayingMovies();

    Flowable<MoviesResponse> searchMoviesWithText(Uri queryText);
    Flowable<MoviesResponse> searchRemoteMoviesWithText(Uri queryText);

    Flowable<MovieDetails> getMovieDetails(int movieId);
    Flowable<MovieDetails> getLocalMovieDetails(int movieId);
    Flowable<MovieDetails> getRemoteMovieDetails(int movieId);

    Flowable<MoviesResponse> fetchFavouriteMovies();
    Flowable<MoviesResponse> fetchLocalFavouriteMovies();
    void markMovieAsFavourite(int movieId);
    void markMovieAsFavouriteLocal(int movieId);
    void removeMovieFromFavourites(int movieId);
    void removeMovieFromFavouritesLocal(int movieId);
}

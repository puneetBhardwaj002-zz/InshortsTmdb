package com.example.inshortsmovie.data.remote;

import android.net.Uri;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.MoviesResponse;

import io.reactivex.Flowable;

public interface MoviesRemoteDataSource {
    Flowable<MoviesResponse> getTrendingMovies(String mediaType, String timeWindow, String api_key);
    Flowable<MoviesResponse> getNowPlayingMovies(String api_key);
    Flowable<MovieDetails> getMoviesDetails(int movie_id, String api_key);
    Flowable<MoviesResponse> searchMovies(Uri query, String api_key);
}

package com.example.inshortsmovie.data.remote;

import android.net.Uri;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.MoviesResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    /*
        media_type -> all,movies, tv, person
        time_window -> day, week
     */
    @GET("trending/{media_type}/{time_window}")
    Flowable<MoviesResponse> getTrendingMovies(
            @Path("media_type") String mediaType,
            @Path("time_window") String timeWindow,
            @Query("api_key")String api_key
    );

    @GET("movie/now_playing")
    Flowable<MoviesResponse> getNowPlayingMovies(@Query("api_key")String api_key);

    @GET("movie/{movie_id}")
    Flowable<MovieDetails> getMoviesDetails(
            @Path("movie_id") int movie_id,
            @Query("api_key")String api_key
    );

    @GET("search/movie")
    Flowable<MoviesResponse> searchMovies(
            @Query("query") Uri query,
            @Query("api_key")String api_key);

}

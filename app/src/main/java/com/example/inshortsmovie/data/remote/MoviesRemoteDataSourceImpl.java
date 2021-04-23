package com.example.inshortsmovie.data.remote;

import android.net.Uri;
import android.util.Log;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.MoviesResponse;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class MoviesRemoteDataSourceImpl implements MoviesRemoteDataSource {

    private final MoviesApi moviesApi;

    @Inject
    public MoviesRemoteDataSourceImpl(MoviesApi moviesApi){
        this.moviesApi = moviesApi;
    }

    @Override
    public Flowable<MoviesResponse> getTrendingMovies(String mediaType, String timeWindow, String api_key) {
        return moviesApi.getTrendingMovies(mediaType, timeWindow, api_key)
                .onErrorReturn(
                        throwable -> {
                            MoviesResponse response = new MoviesResponse();
                            response.setError(true);
                            return response;
                        }
                )
                .map(
                        moviesResponse -> {
                            if(moviesResponse.getResults()!=null && moviesResponse.getResults().size() > 0){
                                Log.d(this.getClass().getSimpleName(),"Trending movies response received: "+moviesResponse.getResults().size());
                            }else{
                                Log.d(this.getClass().getSimpleName(),"Trending movies response received: Null or empty list");
                            }
                            return moviesResponse;
                        }
                );
    }

    @Override
    public Flowable<MoviesResponse> getNowPlayingMovies(String api_key) {
        return moviesApi.getNowPlayingMovies(api_key)
                .onErrorReturn(
                        throwable -> {
                            MoviesResponse response = new MoviesResponse();
                            response.setError(true);
                            return response;
                        }
                ).map(
                        moviesResponse -> {
                            if(moviesResponse.getResults()!=null && moviesResponse.getResults().size() > 0){
                                Log.d(this.getClass().getSimpleName(),"Now Playing movies response received: "+moviesResponse.getResults().size());
                            }else{
                                Log.d(this.getClass().getSimpleName(),"Now Playing movies response received: Null or empty list");
                            }
                            return moviesResponse;
                        }
                );
    }

    @Override
    public Flowable<MovieDetails> getMoviesDetails(int movie_id, String api_key) {
        return moviesApi.getMoviesDetails(movie_id, api_key)
                .onErrorReturn(
                        throwable -> {
                            MovieDetails response = new MovieDetails();
                            response.isError = true;
                            return response;
                        }
                );
    }

    @Override
    public Flowable<MoviesResponse> searchMovies(Uri query, String api_key) {
        return moviesApi.searchMovies(query, api_key)
                .onErrorReturn(
                        throwable -> {
                            MoviesResponse response = new MoviesResponse();
                            response.setError(true);
                            return response;
                        }
                );
    }
}

package com.example.inshortsmovie.data.local;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class MoviesLocalDataSourceImpl implements MoviesLocalDataSource {
    private final MoviesDatabase database;

    @Inject
    public MoviesLocalDataSourceImpl(MoviesDatabase moviesDatabase){
        this.database = moviesDatabase;
    }

    @Override
    public Flowable<MoviesResponse> getNowPlayingMoviesCache() {
        return database.getMoviesDao().getNowPlayingMovies().map(
                moviesList -> {
                    MoviesResponse response = new MoviesResponse();
                    if(moviesList!=null && moviesList.size() > 0){
                        response.setResults(moviesList);
                        return response;
                    }
                    response.setError(true);
                    return response;
                }
        );
    }

    @Override
    public Flowable<MoviesResponse> getTrendingMoviesCache() {
        return database.getMoviesDao().getTrendingMovies().map(
                moviesList -> {
                    MoviesResponse response = new MoviesResponse();
                    if(moviesList!=null && moviesList.size() > 0){
                        response.setResults(moviesList);
                        return response;
                    }
                    response.setError(true);
                    return response;
                });
    }

    @Override
    public Flowable<MoviesResponse> getFavouriteMoviesCache() {
        return database.getMoviesDao().getFavouriteMovies().map(
                moviesDetailsList -> {
                    MoviesResponse response = new MoviesResponse();
                    if(moviesDetailsList!=null && moviesDetailsList.size() > 0){
                        List<Movies> moviesList = new ArrayList<>();
                        for(MovieDetails details:moviesDetailsList){
                            moviesList.add((Movies) details);
                        }
                        response.setResults(moviesList);
                        return response;
                    }
                    response.setError(true);
                    return response;
                });
    }

    @Override
    public Flowable<MovieDetails> getMovieDetails(int movieId) {
        return database.getMoviesDao().getMovieDetails(movieId);
    }

    @Override
    public void insertMovie(Movies movies) {
        database.getMoviesDao().insertMovie(movies);
    }

    @Override
    public void insertMovieDetails(MovieDetails movieDetails) {
        database.getMoviesDao().insertMovieDetails(movieDetails);
    }

    @Override
    public void markMovieAsFavourite(int movieId) {
        database.getMoviesDao().insertFavouriteMovie(movieId);
    }

    @Override
    public void removeMovieAsFavourite(int movieId) {
        database.getMoviesDao().deleteFavourites(movieId);
    }

    @Override
    public void insertTrendingMovie(int movieId) {
        database.getMoviesDao().insertTrendingMovie(movieId);
    }

    @Override
    public void insertNowPLayingMovie(int movieId) {
        database.getMoviesDao().insertNowPlayingMovie(movieId);
    }

}

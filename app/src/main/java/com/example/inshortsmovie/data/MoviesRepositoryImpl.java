package com.example.inshortsmovie.data;

import android.net.Uri;

import com.example.inshortsmovie.data.local.MoviesLocalDataSourceImpl;
import com.example.inshortsmovie.data.remote.MoviesRemoteDataSourceImpl;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.MoviesResponse;
import com.example.inshortsmovie.utils.Constants;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class MoviesRepositoryImpl implements MoviesRepository {
    private final MoviesRemoteDataSourceImpl remoteDataSource;
    private final MoviesLocalDataSourceImpl localDataSource;

    @Inject
    public MoviesRepositoryImpl(MoviesRemoteDataSourceImpl remoteDataSource, MoviesLocalDataSourceImpl localDataSource){
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Flowable<MoviesResponse> getNowPlayingMovies() {
        Flowable<MoviesResponse> localResponse = getLocalNowPlayingMovies();
        return Flowable.merge(localResponse,getRemoteNowPlayingMovies());
    }

    @Override
    public Flowable<MoviesResponse> getLocalNowPlayingMovies() {
        return localDataSource.getNowPlayingMoviesCache();
    }

    @Override
    public Flowable<MoviesResponse> getRemoteNowPlayingMovies() {
        return remoteDataSource.getNowPlayingMovies(Constants.API_KEY)
                .doOnNext(
                moviesResponse -> {
                    if(!moviesResponse.isError()){
                        for (Movies movie:moviesResponse.getResults()) {
                                localDataSource.insertMovie(movie);
                                localDataSource.insertNowPLayingMovie(movie.getId());
                            }
                        }
                    }
        );
    }

    @Override
    public Flowable<MoviesResponse> getTrendingMovies() {
        Flowable<MoviesResponse> localResponse = getLocalTrendingMovies();
        return Flowable.merge(localResponse,getRemoteTrendingMovies());
    }

    @Override
    public Flowable<MoviesResponse> getLocalTrendingMovies() {
        return localDataSource.getTrendingMoviesCache();
    }

    @Override
    public Flowable<MoviesResponse> getRemoteTrendingMovies() {
        return remoteDataSource.getTrendingMovies(Constants.MEDIA_TYPE,Constants.MEDIA_FREQUENCY,Constants.API_KEY)
                .doOnNext(
                        moviesResponse -> {
                            if(!moviesResponse.isError()){
                                for (Movies movie:moviesResponse.getResults()) {
                                    localDataSource.insertMovie(movie);
                                    localDataSource.insertTrendingMovie(movie.getId());
                                }
                            }
                        }
                );
    }

    @Override
    public Flowable<MovieDetails> getLocalMovieDetails(int movieId){
        return localDataSource.getMovieDetails(movieId);
    }

    @Override
    public Flowable<MovieDetails> getRemoteMovieDetails(int movieId) {
        return remoteDataSource.getMoviesDetails(movieId,Constants.API_KEY).doOnNext(
                movieDetails -> {
                    if(!movieDetails.isError){
                        localDataSource.insertMovieDetails(movieDetails);
                    }
                }
        );
    }

    @Override
    public Flowable<MoviesResponse> fetchFavouriteMovies() {
        return fetchLocalFavouriteMovies();
    }

    @Override
    public Flowable<MoviesResponse> fetchLocalFavouriteMovies() {
        return localDataSource.getFavouriteMoviesCache();
    }

    @Override
    public void markMovieAsFavourite(int movieId) {
        markMovieAsFavouriteLocal(movieId);
    }

    @Override
    public void markMovieAsFavouriteLocal(int movieId) {
        Completable.fromAction(()->localDataSource.markMovieAsFavourite(movieId)).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void removeMovieFromFavourites(int movieId) {
        removeMovieFromFavouritesLocal(movieId);
    }

    @Override
    public void removeMovieFromFavouritesLocal(int movieId) {
        Completable.fromAction(()->localDataSource.removeMovieAsFavourite(movieId)).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public Flowable<MoviesResponse> searchMoviesWithText(Uri queryText){
        return searchRemoteMoviesWithText(queryText);
    }

    @Override
    public Flowable<MoviesResponse> searchRemoteMoviesWithText(Uri queryText) {
        return remoteDataSource.searchMovies(queryText,Constants.API_KEY);
    }

    @Override
    public Flowable<MovieDetails> getMovieDetails(int movieId) {
        Flowable<MovieDetails> localResponse = getLocalMovieDetails(movieId);
        return Flowable.mergeDelayError(localResponse,getRemoteMovieDetails(movieId));
    }


}

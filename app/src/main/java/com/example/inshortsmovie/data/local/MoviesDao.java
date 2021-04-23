package com.example.inshortsmovie.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movies movie);

    @Query("SELECT movies.* from movies INNER JOIN trending ON movies.id == trending.movie_id")
    Flowable<List<Movies>> getTrendingMovies();

    @Query("SELECT movies.* from movies INNER JOIN nowPlaying ON movies.id == nowPlaying.movie_id")
    Flowable<List<Movies>> getNowPlayingMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieDetails(MovieDetails details);

    @Query("SELECT * from moviesDetails where id = :movieId")
    Flowable<MovieDetails> getMovieDetails(int movieId);

    @Query("SELECT moviesDetails.* from moviesDetails INNER JOIN favourites ON moviesDetails.id == favourites.movie_id")
    Flowable<List<MovieDetails>> getFavouriteMovies();

    @Query("INSERT INTO favourites (movie_id) VALUES (:movieId)")
    void insertFavouriteMovie(int movieId);

    @Query("DELETE from favourites where movie_id = :movieId")
    void deleteFavourites(int movieId);

    @Query("INSERT INTO trending (movie_id) VALUES (:movieId)")
    void insertTrendingMovie(int movieId);

    @Query("INSERT INTO nowPlaying (movie_id) VALUES (:movieId)")
    void insertNowPlayingMovie(int movieId);
}

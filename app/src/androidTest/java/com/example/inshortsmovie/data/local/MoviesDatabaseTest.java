package com.example.inshortsmovie.data.local;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.inshortsmovie.data.RxImmediateSchedulerRule;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class MoviesDatabaseTest {
    MoviesDatabase database;
    MoviesDao dao;
    @Rule
    public RxImmediateSchedulerRule testRule = new RxImmediateSchedulerRule();
    @Before
    public void setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        Context context = ApplicationProvider.getApplicationContext();
        // initialize the db and dao variable
        database = Room.inMemoryDatabaseBuilder(context, MoviesDatabase.class).build();
        dao = database.getMoviesDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void fetchTrendingMovies(){
        Movies testMovie = new Movies();
        testMovie.setId(1234);
        ArrayList<Movies> testList = new ArrayList<>();
        testList.add(testMovie);
        dao.insertMovie(testMovie);
        dao.insertTrendingMovie(1234);
        TestSubscriber<List<Movies>> testFlowable = new TestSubscriber<>();
        dao.getTrendingMovies().subscribeOn(Schedulers.trampoline()).subscribe(testFlowable);
        testFlowable.awaitTerminalEvent(3,TimeUnit.SECONDS);
        testFlowable.assertValue(testList);
    }

    @Test
    public void fetchNowPLayingMovies(){
        Movies testMovie = new Movies();
        testMovie.setId(1234);
        ArrayList<Movies> testList = new ArrayList<>();
        testList.add(testMovie);
        dao.insertMovie(testMovie);
        dao.insertNowPlayingMovie(1234);
        TestSubscriber<List<Movies>> testFlowable = new TestSubscriber<>();
        dao.getNowPlayingMovies().subscribeOn(Schedulers.trampoline()).subscribe(testFlowable);
        testFlowable.awaitTerminalEvent(3,TimeUnit.SECONDS);
        testFlowable.assertValue(testList);
    }

    @Test
    public void fetchFavouriteMovies(){
        MovieDetails testMovie = new MovieDetails();
        testMovie.setId(1234);
        //Add dummy movie into both movies list and movieDetails list, else FOREIGN_KEY constraint will fail
        dao.insertMovie(testMovie);
        dao.insertMovieDetails(testMovie);
        dao.insertFavouriteMovie(1234);
        TestSubscriber<List<MovieDetails>> testFlowable = new TestSubscriber<>();
        dao.getFavouriteMovies().subscribeOn(Schedulers.trampoline()).subscribe(testFlowable);
        testFlowable.awaitTerminalEvent(3,TimeUnit.SECONDS);
        testFlowable.assertValue(
                movieDetails -> movieDetails!=null && movieDetails.size() > 0 && movieDetails.get(0).getId() == 1234
        );
    }

    @Test
    public void removeFavouriteMovies(){
        MovieDetails testMovie = new MovieDetails();
        testMovie.setId(1234);
        //Add dummy movie into both movies list and movieDetails list, else FOREIGN_KEY constraint will fail
        dao.insertMovie(testMovie);
        dao.insertMovieDetails(testMovie);
        dao.insertFavouriteMovie(1234);
        TestSubscriber<List<MovieDetails>> testFlowable = new TestSubscriber<>();
        dao.deleteFavourites(1234);
        dao.getFavouriteMovies().subscribeOn(Schedulers.trampoline()).subscribe(testFlowable);
        testFlowable.awaitTerminalEvent(3,TimeUnit.SECONDS);
        testFlowable.assertValue(
                movieDetails -> {
                    if(movieDetails == null || movieDetails.size() == 0){
                        return true;
                    }
                    for(MovieDetails details:movieDetails){
                        return details.getId() == 1234;
                    }
                    return false;
                }
        );
    }

}
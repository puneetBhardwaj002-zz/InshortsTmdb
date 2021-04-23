package com.example.inshortsmovie;

import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static MoviesResponse getDummyMovieResponse(int size){
        MoviesResponse response = new MoviesResponse();
        List<Movies> moviesList = new ArrayList<>();
        for(int i=0;i<size;i++){
            Movies movies = new Movies();
            movies.setId(i+1);
            moviesList.add(movies);
        }
        response.setResults(moviesList);
        return response;
    }

    public static MoviesResponse getErrorMovieResponse(){
        MoviesResponse response = new MoviesResponse();
        response.setError(true);
        return response;
    }
}

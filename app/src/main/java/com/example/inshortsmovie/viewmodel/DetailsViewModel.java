package com.example.inshortsmovie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inshortsmovie.data.MoviesRepositoryImpl;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Movies;
import com.example.inshortsmovie.models.Resource;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends ViewModel {

    @Inject
    MoviesRepositoryImpl moviesRepositoryImpl;

    @Inject
    public DetailsViewModel(){

    }

    private MutableLiveData<Resource<MovieDetails>> _detailedMovieResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isFavouriteMovie = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public LiveData<Resource<MovieDetails>> getDetailedMovieResponse() {
        return _detailedMovieResponse;
    }

    public LiveData<Boolean> getIsFavouriteMovieOrNot(int movieId) {
        return _isFavouriteMovie;
    }

    public void fetchMovieDetails(int movieId){
        _detailedMovieResponse.postValue(Resource.loading(null));
        Disposable nowPlayingDisposable = moviesRepositoryImpl.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(
                        moviesResponse -> {
                           if(moviesResponse.isError){
                               _detailedMovieResponse.postValue(Resource.error("Something went wrong!!",null));
                           }
                           _detailedMovieResponse.postValue(Resource.success(moviesResponse));
                        },
                        error -> _detailedMovieResponse.postValue(Resource.error("Something went wrong!!",null))
                );

        compositeDisposable.add(nowPlayingDisposable);
    }

    public void markMovieAsFavourite(int movieId){
        moviesRepositoryImpl.markMovieAsFavourite(movieId);
    }

    public void removeMovieFromFavourite(int movieId){
        moviesRepositoryImpl.removeMovieFromFavourites(movieId);
    }

    public void getFavouriteMovieDetails(int movieId){
        _isFavouriteMovie.postValue(false);
        Disposable getFavouriteDisposable = moviesRepositoryImpl.fetchFavouriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movieDetails -> {
                            if(movieDetails!=null && movieDetails.getResults()!=null && movieDetails.getResults().size() > 0){
                                boolean isFavourite = false;
                                for(Movies movies:movieDetails.getResults()){
                                    isFavourite = isFavourite || movies.getId() == movieId;
                                }
                                _isFavouriteMovie.postValue(false);
                            }else{
                                _isFavouriteMovie.postValue(true);
                            }
                        }
                );
        compositeDisposable.add(getFavouriteDisposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

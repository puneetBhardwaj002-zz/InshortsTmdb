package com.example.inshortsmovie.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inshortsmovie.data.MoviesRepositoryImpl;
import com.example.inshortsmovie.models.MoviesResponse;
import com.example.inshortsmovie.models.Resource;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentViewModel extends ViewModel {

    @Inject
    MoviesRepositoryImpl moviesRepositoryImpl;

    @Inject
    public MainFragmentViewModel(){

    }

    private MutableLiveData<Resource<MoviesResponse>> _nowPlayingMoviesResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<MoviesResponse>> _trendingMoviesResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<MoviesResponse>> _searchMoviesResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<MoviesResponse>> _favouriteMoviesResponse = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public void fetchNowPlayingMovies(){
        _nowPlayingMoviesResponse.postValue(Resource.loading(null));
        Disposable nowPlayingDisposable = moviesRepositoryImpl.getNowPlayingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(
                        moviesResponse -> {
                            if(moviesResponse.isError()){
                                _nowPlayingMoviesResponse.postValue(Resource.error("Something went wrong!!",null));
                            }
                            _nowPlayingMoviesResponse.postValue(Resource.success(moviesResponse));
                        },
                        error -> _nowPlayingMoviesResponse.postValue(Resource.error("Something went wrong!!",null))
                );

        compositeDisposable.add(nowPlayingDisposable);
    }

    public void fetchTrendingMovies(){
        _trendingMoviesResponse.postValue(Resource.loading(null));
        Disposable trendingDisposable = moviesRepositoryImpl.getTrendingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(
                        moviesResponse -> {
                            if(moviesResponse.isError()){
                                _trendingMoviesResponse.postValue(Resource.error("Something went wrong!!",null));
                            }
                            _trendingMoviesResponse.postValue(Resource.success(moviesResponse));
                        },
                        error -> _trendingMoviesResponse.postValue(Resource.error("Something went wrong!!",null))
                );

        compositeDisposable.add(trendingDisposable);
    }

    public void searchMovies(String query){
        _trendingMoviesResponse.postValue(Resource.loading(null));
        Disposable trendingDisposable = moviesRepositoryImpl.searchMoviesWithText(Uri.parse(query))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(
                        moviesResponse -> {
                            if(moviesResponse.isError()){
                                _searchMoviesResponse.postValue(Resource.error("Something went wrong!!",null));
                            }
                            _searchMoviesResponse.postValue(Resource.success(moviesResponse));
                        },
                        error -> _searchMoviesResponse.postValue(Resource.error("Something went wrong!!",null))
                );

        compositeDisposable.add(trendingDisposable);
    }

    public void fetchFavouriteMovies(){
        _favouriteMoviesResponse.postValue(Resource.loading(null));
        Disposable trendingDisposable = moviesRepositoryImpl.fetchFavouriteMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .subscribe(
                        moviesResponse -> _favouriteMoviesResponse.postValue(Resource.success(moviesResponse)),
                        error -> _favouriteMoviesResponse.postValue(Resource.error("Something went wrong!!",null))
                );

        compositeDisposable.add(trendingDisposable);
    }

    public LiveData<Resource<MoviesResponse>> getSearchMoviesResponse() {
        return _searchMoviesResponse;
    }

    public LiveData<Resource<MoviesResponse>> getFavouriteMoviesResponse() {
        return _favouriteMoviesResponse;
    }

    public LiveData<Resource<MoviesResponse>> getNowPlayingMoviesResponse() {
        return _nowPlayingMoviesResponse;
    }

    public LiveData<Resource<MoviesResponse>> getTrendingMoviesResponse() {
        return _trendingMoviesResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

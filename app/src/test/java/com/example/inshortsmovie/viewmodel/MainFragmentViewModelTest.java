package com.example.inshortsmovie.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.inshortsmovie.RxImmediateSchedulerRule;
import com.example.inshortsmovie.TestUtils;
import com.example.inshortsmovie.data.MoviesRepositoryImpl;
import com.example.inshortsmovie.data.local.MoviesLocalDataSourceImpl;
import com.example.inshortsmovie.data.remote.MoviesRemoteDataSourceImpl;
import com.example.inshortsmovie.models.MoviesResponse;
import com.example.inshortsmovie.models.Resource;
import com.example.inshortsmovie.utils.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainFragmentViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule public RxImmediateSchedulerRule timeoutRule = new RxImmediateSchedulerRule();

    @Mock
    Observer<Resource<MoviesResponse>> movieResponseObserver;

    @Captor
    ArgumentCaptor<Resource<MoviesResponse>> movieResponseCaptor;

    MoviesRepositoryImpl repository;
    @Mock
    MoviesRemoteDataSourceImpl remoteDataSource;
    @Mock
    MoviesLocalDataSourceImpl localDataSource;
    private MainFragmentViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new MainFragmentViewModel();
        repository = new MoviesRepositoryImpl(remoteDataSource,localDataSource);
        viewModel.moviesRepositoryImpl = repository;
    }

    /**
     * Testing function related to fetching trending movies and now playing movies will be similar
     */

    @Test
    public void getTrendingMovieResponse_whenLocalReturnsDataAndRemoteReturnsData() {
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getDummyMovieResponse(2);
        MoviesResponse remoteMovieResponse = TestUtils.getDummyMovieResponse(4);

        when(localDataSource.getTrendingMoviesCache()).thenReturn(Flowable.just(localMovieResponse));
        when(remoteDataSource.getTrendingMovies(Constants.MEDIA_TYPE,Constants.MEDIA_FREQUENCY,
                Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieResponse));

        //Act
        viewModel.getTrendingMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchTrendingMovies();

        //Verify
        verify(movieResponseObserver,times(3)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,localMovieResponse);
        assertEquals(movieResponseCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(2).data,remoteMovieResponse);
    }

    @Test
    public void getTrendingMovieResponse_whenLocalReturnsErrorAndRemoteReturnsData() {
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getErrorMovieResponse();
        MoviesResponse remoteMovieResponse = TestUtils.getDummyMovieResponse(4);

        when(localDataSource.getTrendingMoviesCache()).thenReturn(Flowable.just(localMovieResponse));
        when(remoteDataSource.getTrendingMovies(Constants.MEDIA_TYPE,Constants.MEDIA_FREQUENCY,
                Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieResponse));

        //Act
        viewModel.getTrendingMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchTrendingMovies();

        //Verify
        verify(movieResponseObserver,times(4)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.ERROR);
        assertNull(movieResponseCaptor.getAllValues().get(1).data);
        assertEquals(movieResponseCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(2).data,localMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(2).data.isError());
        assertEquals(movieResponseCaptor.getAllValues().get(3).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(3).data,remoteMovieResponse);
    }

    @Test
    public void getTrendingMovieResponse_whenLocalReturnsDataAndRemoteReturnsError() {
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getDummyMovieResponse(2);
        MoviesResponse remoteMovieResponse = TestUtils.getErrorMovieResponse();

        when(localDataSource.getTrendingMoviesCache()).thenReturn(Flowable.just(localMovieResponse));
        when(remoteDataSource.getTrendingMovies(Constants.MEDIA_TYPE,Constants.MEDIA_FREQUENCY,
                Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieResponse));

        //Act
        viewModel.getTrendingMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchTrendingMovies();

        //Verify
        verify(movieResponseObserver,times(4)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,localMovieResponse);
        assertEquals(movieResponseCaptor.getAllValues().get(2).status, Resource.Status.ERROR);
        assertNull(movieResponseCaptor.getAllValues().get(2).data);
        assertEquals(movieResponseCaptor.getAllValues().get(3).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(3).data,remoteMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(3).data.isError());
    }

    @Test
    public void getTrendingMovieResponse_whenLocalReturnsErrorAndRemoteReturnsError() {
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getErrorMovieResponse();
        MoviesResponse remoteMovieResponse = TestUtils.getErrorMovieResponse();

        when(localDataSource.getTrendingMoviesCache()).thenReturn(Flowable.just(localMovieResponse));
        when(remoteDataSource.getTrendingMovies(Constants.MEDIA_TYPE,Constants.MEDIA_FREQUENCY,
                Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieResponse));

        //Act
        viewModel.getTrendingMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchTrendingMovies();

        //Verify
        verify(movieResponseObserver,times(5)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.ERROR);
        assertNull(movieResponseCaptor.getAllValues().get(1).data);
        assertEquals(movieResponseCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(2).data,localMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(2).data.isError());
        assertEquals(movieResponseCaptor.getAllValues().get(3).status, Resource.Status.ERROR);
        assertNull(movieResponseCaptor.getAllValues().get(3).data);
        assertEquals(movieResponseCaptor.getAllValues().get(4).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(4).data,remoteMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(4).data.isError());
    }

    @Test
    public void fetchFavouriteMovies_localReturnsError(){
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getErrorMovieResponse();

        when(localDataSource.getFavouriteMoviesCache()).thenReturn(Flowable.just(localMovieResponse));

        //Act
        viewModel.getFavouriteMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchFavouriteMovies();

        //Verify
        verify(movieResponseObserver,times(2)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,localMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(1).data.isError());
    }

    @Test
    public void fetchFavouriteMovies_localReturnsData(){
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getDummyMovieResponse(4);

        when(localDataSource.getFavouriteMoviesCache()).thenReturn(Flowable.just(localMovieResponse));

        //Act
        viewModel.getFavouriteMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchFavouriteMovies();

        //Verify
        verify(movieResponseObserver,times(2)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,localMovieResponse);
    }

    @Test
    public void searchMovies_remoteReturnsData(){
        //Assemble
        MoviesResponse remoteMovieResponse = TestUtils.getDummyMovieResponse(4);

        when(localDataSource.getFavouriteMoviesCache()).thenReturn(Flowable.just(remoteMovieResponse));

        //Act
        viewModel.getFavouriteMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchFavouriteMovies();

        //Verify
        verify(movieResponseObserver,times(2)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,remoteMovieResponse);
    }

    @Test
    public void searchMovies_remoteReturnsError(){
        //Assemble
        MoviesResponse localMovieResponse = TestUtils.getErrorMovieResponse();

        when(localDataSource.getFavouriteMoviesCache()).thenReturn(Flowable.just(localMovieResponse));

        //Act
        viewModel.getFavouriteMoviesResponse().observeForever(movieResponseObserver);
        viewModel.fetchFavouriteMovies();

        //Verify
        verify(movieResponseObserver,times(2)).onChanged(movieResponseCaptor.capture());
        assertEquals(movieResponseCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieResponseCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieResponseCaptor.getAllValues().get(1).data,localMovieResponse);
        assertTrue(movieResponseCaptor.getAllValues().get(1).data.isError());
    }

}
package com.example.inshortsmovie.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.inshortsmovie.RxImmediateSchedulerRule;
import com.example.inshortsmovie.data.MoviesRepositoryImpl;
import com.example.inshortsmovie.data.local.MoviesLocalDataSourceImpl;
import com.example.inshortsmovie.data.remote.MoviesRemoteDataSourceImpl;
import com.example.inshortsmovie.models.MovieDetails;
import com.example.inshortsmovie.models.Resource;
import com.example.inshortsmovie.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class DetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule public RxImmediateSchedulerRule timeoutRule = new RxImmediateSchedulerRule();

    @Mock
    Observer<Resource<MovieDetails>> movieDetailsObserver;

    @Captor
    ArgumentCaptor<Resource<MovieDetails>> movieDetailsCaptor;

    MoviesRepositoryImpl repository;
    @Mock
    MoviesRemoteDataSourceImpl remoteDataSource;
    @Mock
    MoviesLocalDataSourceImpl localDataSource;
    private DetailsViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new DetailsViewModel();
        repository = new MoviesRepositoryImpl(remoteDataSource,localDataSource);
        viewModel.moviesRepositoryImpl = repository;
    }

    @Test
    public void getDetailedMovieResponse_whenLocalReturnsDataAndRemoteReturnsData() {
        int movieId = 12345;
        //Assemble
        MovieDetails localMovieDetails = new MovieDetails();
        localMovieDetails.setId(movieId);
        localMovieDetails.setRuntime(100);
        MovieDetails remoteMovieDetails = new MovieDetails();
        remoteMovieDetails.setId(movieId);
        remoteMovieDetails.setRuntime(300);
        when(localDataSource.getMovieDetails(movieId)).thenReturn(Flowable.just(localMovieDetails));
        when(remoteDataSource.getMoviesDetails(movieId, Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieDetails));

        //Act
        viewModel.getDetailedMovieResponse().observeForever(movieDetailsObserver);
        viewModel.fetchMovieDetails(movieId);

        //Verify
        verify(movieDetailsObserver,times(3)).onChanged(movieDetailsCaptor.capture());
        assertEquals(movieDetailsCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).data,localMovieDetails);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).data,remoteMovieDetails);
    }

    @Test
    public void getDetailedMovieResponse_whenLocalReturnsErrorRemoteReturnsData() {
        int movieId = 12345;
        //Assemble
        MovieDetails localMovieDetails = new MovieDetails();
        localMovieDetails.isError = true;
        MovieDetails remoteMovieDetails = new MovieDetails();
        remoteMovieDetails.setId(movieId);
        remoteMovieDetails.setRuntime(300);
        when(localDataSource.getMovieDetails(movieId)).thenReturn(Flowable.just(localMovieDetails));
        when(remoteDataSource.getMoviesDetails(movieId, Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieDetails));

        //Act
        viewModel.getDetailedMovieResponse().observeForever(movieDetailsObserver);
        viewModel.fetchMovieDetails(movieId);

        //Verify
        verify(movieDetailsObserver,times(4)).onChanged(movieDetailsCaptor.capture());
        assertEquals(movieDetailsCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).status, Resource.Status.ERROR);
        assertNull(movieDetailsCaptor.getAllValues().get(1).data);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).data,localMovieDetails);
        assertEquals(movieDetailsCaptor.getAllValues().get(3).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(3).data,remoteMovieDetails);
    }

    @Test
    public void getDetailedMovieResponse_whenLocalReturnsDataRemoteReturnsError() {
        int movieId = 12345;
        //Assemble
        MovieDetails localMovieDetails = new MovieDetails();
        localMovieDetails.setId(movieId);
        localMovieDetails.setRuntime(100);
        MovieDetails remoteMovieDetails = new MovieDetails();
        remoteMovieDetails.isError = true;
        when(localDataSource.getMovieDetails(movieId)).thenReturn(Flowable.just(localMovieDetails));
        when(remoteDataSource.getMoviesDetails(movieId, Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieDetails));

        //Act
        viewModel.getDetailedMovieResponse().observeForever(movieDetailsObserver);
        viewModel.fetchMovieDetails(movieId);

        //Verify
        verify(movieDetailsObserver,times(4)).onChanged(movieDetailsCaptor.capture());
        assertEquals(movieDetailsCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).data,localMovieDetails);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).status, Resource.Status.ERROR);
        assertNull(movieDetailsCaptor.getAllValues().get(2).data);
        assertEquals(movieDetailsCaptor.getAllValues().get(3).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(3).data,remoteMovieDetails);
    }

    @Test
    public void getDetailedMovieResponse_whenLocalReturnsErrorRemoteReturnsError() {
        int movieId = 12345;
        //Assemble
        MovieDetails localMovieDetails = new MovieDetails();
        localMovieDetails.isError = true;
        MovieDetails remoteMovieDetails = new MovieDetails();
        remoteMovieDetails.isError = true;
        when(localDataSource.getMovieDetails(movieId)).thenReturn(Flowable.just(localMovieDetails));
        when(remoteDataSource.getMoviesDetails(movieId, Constants.API_KEY)).thenReturn(Flowable.just(remoteMovieDetails));

        //Act
        viewModel.getDetailedMovieResponse().observeForever(movieDetailsObserver);
        viewModel.fetchMovieDetails(movieId);

        //Verify
        verify(movieDetailsObserver,times(5)).onChanged(movieDetailsCaptor.capture());
        assertEquals(movieDetailsCaptor.getAllValues().get(0).status, Resource.Status.LOADING);
        assertEquals(movieDetailsCaptor.getAllValues().get(1).status, Resource.Status.ERROR);
        assertNull(movieDetailsCaptor.getAllValues().get(1).data);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(2).data,localMovieDetails);
        assertEquals(movieDetailsCaptor.getAllValues().get(3).status, Resource.Status.ERROR);
        assertNull(movieDetailsCaptor.getAllValues().get(3).data);
        assertEquals(movieDetailsCaptor.getAllValues().get(4).status, Resource.Status.SUCCESS);
        assertEquals(movieDetailsCaptor.getAllValues().get(4).data,remoteMovieDetails);
    }

    @After
    public void tearDown() {
        repository = null;
        viewModel = null;
    }
}
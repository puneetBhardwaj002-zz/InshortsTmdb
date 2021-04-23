package com.example.inshortsmovie.di.modules;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.inshortsmovie.R;
import com.example.inshortsmovie.data.MoviesRepositoryImpl;
import com.example.inshortsmovie.data.local.MoviesDatabase;
import com.example.inshortsmovie.data.local.MoviesLocalDataSourceImpl;
import com.example.inshortsmovie.data.remote.MoviesApi;
import com.example.inshortsmovie.data.remote.MoviesRemoteDataSourceImpl;
import com.example.inshortsmovie.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.bg_placeholder_background)
                .error(R.drawable.bg_placeholder_background);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application).applyDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient(){
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();
            Log.d("---Request body-----:",request.toString());
            return chain.proceed(chain.request());
        }).build();
    }


    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(OkHttpClient client){
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    static MoviesApi provideMoviesApi(Retrofit retrofit){
        return retrofit.create(MoviesApi.class);
    }

    @Singleton
    @Provides
    static MoviesRepositoryImpl provideMoviesRepository(MoviesRemoteDataSourceImpl remoteDataSource, MoviesLocalDataSourceImpl moviesLocalDataSource){
        return new MoviesRepositoryImpl(remoteDataSource,moviesLocalDataSource);
    }

    @Singleton
    @Provides
    static MoviesRemoteDataSourceImpl provideMoviesRemoteDataSource(MoviesApi moviesApi){
        return new MoviesRemoteDataSourceImpl(moviesApi);
    }

    @Singleton
    @Provides
    static MoviesLocalDataSourceImpl provideMoviesLocalDataSource(MoviesDatabase database){
        return new MoviesLocalDataSourceImpl(database);
    }

    @Singleton
    @Provides
    static MoviesDatabase providesMovieDatabase(Application application){
        return Room.databaseBuilder(application.getApplicationContext(), MoviesDatabase.class,Constants.MOVIES_DB_NAME).build();
    }
}

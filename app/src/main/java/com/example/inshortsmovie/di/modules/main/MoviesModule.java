package com.example.inshortsmovie.di.modules.main;

import com.bumptech.glide.RequestManager;
import com.example.inshortsmovie.di.scopes.main.MoviesScope;
import com.example.inshortsmovie.models.RecyclerViewItemClickListener;
import com.example.inshortsmovie.view.MainActivity;
import com.example.inshortsmovie.view.adapter.MovieAdapter;
import com.example.inshortsmovie.view.adapter.MyFragmentPagerAdapter;
import com.example.inshortsmovie.utils.SingleScrollDirectionEnforcer;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesModule {

    @MoviesScope
    @Provides
    static MovieAdapter provideAdapter(RequestManager manager, MainActivity activity){
        return new MovieAdapter(manager,(RecyclerViewItemClickListener) activity);
    }

    @Provides
    @MoviesScope
    static MyFragmentPagerAdapter providesMyFragmentPagerAdapter(MainActivity activity){
        return new MyFragmentPagerAdapter(activity);
    }

    @Provides
    @MoviesScope
    static SingleScrollDirectionEnforcer providesSingleScrollDirectionEnforcer(){
        return new SingleScrollDirectionEnforcer();
    }

}

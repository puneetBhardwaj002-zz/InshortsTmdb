package com.example.inshortsmovie.di.modules;

import com.example.inshortsmovie.di.modules.details.DetailsModule;
import com.example.inshortsmovie.di.modules.details.DetailsViewModelModule;
import com.example.inshortsmovie.di.modules.main.MainFragmentBuilderModule;
import com.example.inshortsmovie.di.modules.main.MainViewModelModule;
import com.example.inshortsmovie.di.modules.main.MoviesModule;
import com.example.inshortsmovie.di.scopes.details.DetailsScope;
import com.example.inshortsmovie.di.scopes.main.MoviesScope;
import com.example.inshortsmovie.view.DetailsActivity;
import com.example.inshortsmovie.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @MoviesScope
    @ContributesAndroidInjector(
            modules = {
                    MainViewModelModule.class,
                    MoviesModule.class,
                    MainFragmentBuilderModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

    @DetailsScope
    @ContributesAndroidInjector(
            modules = {
                    DetailsViewModelModule.class,
                    DetailsModule.class
            }
    )
    abstract DetailsActivity contributeDetailsActivity();
}

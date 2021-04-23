package com.example.inshortsmovie.di.modules.main;

import com.example.inshortsmovie.view.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();
}

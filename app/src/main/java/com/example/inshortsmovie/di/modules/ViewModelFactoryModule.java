package com.example.inshortsmovie.di.modules;

import androidx.lifecycle.ViewModelProvider;

import com.example.inshortsmovie.viewmodel.ViewModelProvidersFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProvidersFactory viewModelProvidersFactory);
}

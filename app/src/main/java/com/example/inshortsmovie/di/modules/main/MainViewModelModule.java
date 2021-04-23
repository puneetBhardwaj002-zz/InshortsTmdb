package com.example.inshortsmovie.di.modules.main;

import androidx.lifecycle.ViewModel;

import com.example.inshortsmovie.di.ViewModelKey;
import com.example.inshortsmovie.viewmodel.DetailsViewModel;
import com.example.inshortsmovie.viewmodel.MainFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel.class)
    public abstract ViewModel bindMainFragmentViewModel(MainFragmentViewModel viewModel);
}

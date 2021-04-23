package com.example.inshortsmovie.di.modules.details;

import androidx.lifecycle.ViewModel;

import com.example.inshortsmovie.di.ViewModelKey;
import com.example.inshortsmovie.viewmodel.DetailsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DetailsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    public abstract ViewModel bindDetailsViewModel(DetailsViewModel viewModel);
}

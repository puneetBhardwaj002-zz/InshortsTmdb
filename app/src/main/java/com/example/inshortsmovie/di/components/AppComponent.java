package com.example.inshortsmovie.di.components;

import android.app.Application;

import com.example.inshortsmovie.BaseApplication;
import com.example.inshortsmovie.di.modules.ActivityBuildersModule;
import com.example.inshortsmovie.di.modules.AppModule;
import com.example.inshortsmovie.di.modules.ViewModelFactoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

package com.willowtreeapps.doggogame.core;

import com.willowtreeapps.doggogame.network.NetworkModule;
import com.willowtreeapps.doggogame.ui.DoggoActivity;
import com.willowtreeapps.doggogame.ui.DoggoFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class
})
public interface ApplicationComponent {
    void inject(DoggoGameApplication application);
    void inject(DoggoActivity activity);
    void inject(DoggoFragment fragment);
}
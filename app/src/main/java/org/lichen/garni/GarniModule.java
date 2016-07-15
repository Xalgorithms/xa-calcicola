package org.lichen.garni;

import android.app.Application;

import org.lichen.garni.activities.ActivitiesModule;
import org.lichen.garni.data.DataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = { DataModule.class, ActivitiesModule.class }
)
public final class GarniModule {
    private final Application _app;

    GarniModule(Application app) {
        _app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return _app;
    }
}

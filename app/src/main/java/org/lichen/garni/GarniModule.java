package org.lichen.garni;

import android.app.Application;

import org.lichen.garni.activities.ActivitiesModule;
import org.lichen.garni.adapters.AdaptersModule;
import org.lichen.garni.data.DataModule;
import org.lichen.garni.services.ServicesModule;
import org.lichen.geghard.api.Client;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = { DataModule.class, ActivitiesModule.class, ServicesModule.class, AdaptersModule.class }
)
public final class GarniModule {
    private final Application _app;

    GarniModule(Application app) {
        _app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return _app;
    }
    @Provides @Singleton Client provideClient() {
        return new Client("https://xa-lichen.herokuapp.com");
    }
}

package org.lichen.garni.activities;

import dagger.Module;

@Module(
        injects = {
                MainActivity.class
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

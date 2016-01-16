package org.lichen.garni.activities;

import dagger.Module;

@Module(
        injects = {
                LoginActivity.class,
                InvoicesActivity.class,
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

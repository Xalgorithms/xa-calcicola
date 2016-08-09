package org.lichen.garni.activities;

import org.lichen.geghard.api.Client;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                LoginActivity.class,
                MainActivity.class,
                TransactionActivity.class,
                InvoicesActivity.class,
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

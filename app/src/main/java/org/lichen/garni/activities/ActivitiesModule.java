package org.lichen.garni.activities;

import dagger.Module;

@Module(
        injects = {
                LoginActivity.class,
                AccountsActivity.class,
                InvoicesActivity.class,
                MainActivity.class
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

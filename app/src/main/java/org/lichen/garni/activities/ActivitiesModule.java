package org.lichen.garni.activities;

import dagger.Module;

@Module(
        injects = {
                MainActivity.class,
                AccountsActivity.class,
                InvoicesActivity.class
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

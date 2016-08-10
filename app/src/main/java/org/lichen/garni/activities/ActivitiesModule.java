package org.lichen.garni.activities;

import dagger.Module;

@Module(
        injects = {
                LoginActivity.class,
                UserInvoicesActivity.class,
                AffectedInvoiceActivity.class,
                UserInvoicesAdapter.class
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

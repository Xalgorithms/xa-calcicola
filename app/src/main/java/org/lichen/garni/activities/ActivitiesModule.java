package org.lichen.garni.activities;

import org.lichen.garni.adapters.UserInvoicesAdapter;

import dagger.Module;

@Module(
        injects = {
                LoginActivity.class,
                UserInvoicesActivity.class,
                AffectedInvoiceActivity.class,
                InvoiceChangesActivity.class,
        },
        complete = false,
        library = true
)
public final class ActivitiesModule {
}

package org.lichen.garni.adapters;

import dagger.Module;

@Module(
        injects = {
                UserInvoicesAdapter.class
        },
        complete = false,
        library = true
)

public class AdaptersModule {
}

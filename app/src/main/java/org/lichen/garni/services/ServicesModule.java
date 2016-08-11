package org.lichen.garni.services;

import dagger.Module;

@Module(
        injects = {
            RegistrationIntentService.class,
            GcmListenerService.class
        },
        complete = false,
        library = true
)

public final class ServicesModule {
}

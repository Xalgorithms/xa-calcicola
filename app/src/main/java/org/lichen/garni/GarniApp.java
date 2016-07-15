package org.lichen.garni;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import timber.log.Timber;

public class GarniApp extends Application {
    private ObjectGraph _graph;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        _graph = ObjectGraph.create(new GarniModule(this));
    }

    public static ObjectGraph object_graph(Context ctx) {
        return ((GarniApp) ctx.getApplicationContext())._graph;
    }
}

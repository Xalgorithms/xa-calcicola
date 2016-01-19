package org.lichen.garni.activities;

import android.view.View;

public interface Receiver<T> {
    void receive(View v, T it);
}

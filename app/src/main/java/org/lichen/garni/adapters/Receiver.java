package org.lichen.garni.adapters;

import android.view.View;

public interface Receiver<T> {
    void receive(View v, T it);
}

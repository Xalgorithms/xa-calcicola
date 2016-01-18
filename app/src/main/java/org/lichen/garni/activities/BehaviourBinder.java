package org.lichen.garni.activities;

import android.view.View;

import rx.functions.Action1;

public interface BehaviourBinder<T> {
    void bind(View v, Action1<T> act);
}

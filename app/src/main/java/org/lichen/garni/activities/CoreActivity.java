package org.lichen.garni.activities;

import org.lichen.garni.GarniApp;

import butterknife.ButterKnife;

public class CoreActivity extends RxActivity {
    public void init(int resId) {
        setContentView(resId);
        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }
}

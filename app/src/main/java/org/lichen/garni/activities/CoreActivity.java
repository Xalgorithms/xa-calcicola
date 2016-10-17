package org.lichen.garni.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoreActivity extends RxActivity {
    ProgressBar _progress_toolbar;
    private Toolbar _toolbar;
    private int _shows;

    public void init(int resId) {
        setContentView(resId);
        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _toolbar = (Toolbar) findViewById(R.id.toolbar_general);
        if (null != _toolbar) {
            setSupportActionBar(_toolbar);
            _progress_toolbar = (ProgressBar) _toolbar.findViewById(R.id.progress_toolbar);
        }
    }

    protected void show_progress() {
        _shows++;
        _progress_toolbar.setVisibility(View.VISIBLE);
    }

    protected void hide_progress() {
        if (--_shows == 0) {
            _progress_toolbar.setVisibility(View.GONE);
        }
    }

    protected void update_title(String s) {
        _toolbar.setTitle(s);
    }
}

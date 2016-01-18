package org.lichen.garni.activities;

import android.support.v7.app.AppCompatActivity;

import java.util.Collection;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxActivity extends AppCompatActivity {
    private CompositeSubscription _subscriptions;

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = new CompositeSubscription();
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscriptions.unsubscribe();
    }

    protected void remember(Subscription s) {
        _subscriptions.add(s);
    }

    protected void remember(Collection<Subscription> subscriptions) {
        for (Subscription s : subscriptions) {
            remember(s);
        }
    }
}

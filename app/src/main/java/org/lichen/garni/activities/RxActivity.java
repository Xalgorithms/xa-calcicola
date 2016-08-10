package org.lichen.garni.activities;

import android.support.v7.app.AppCompatActivity;

import java.util.Collection;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxActivity extends AppCompatActivity {
    private LifecycleSubscriptions _subscriptions = new LifecycleSubscriptions();

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscriptions.pause();
    }

    protected void remember(Subscription s) {
        _subscriptions.remember(s);
    }

    protected void remember(Collection<Subscription> subscriptions) {
        _subscriptions.remember(subscriptions);
    }
}

package org.lichen.garni.activities;

import android.support.v4.app.Fragment;

import java.util.Collection;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxFragment extends Fragment {
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

    protected void remember(Collection<Subscription> ss) {
        _subscriptions.remember(ss);
    }
}

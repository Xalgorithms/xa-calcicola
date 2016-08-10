package org.lichen.garni.activities;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LifecycleSubscriptions {
    private CompositeSubscription _subscriptions;

    public void resume() {
        _subscriptions = new CompositeSubscription();
    }

    public void pause() {
        _subscriptions.unsubscribe();
    }

    public void remember(Subscription s) {
        _subscriptions.add(s);
    }

    public void remember(Iterable<Subscription> subscriptions) {
        for (Subscription s : subscriptions) {
            remember(s);
        }
    }
}

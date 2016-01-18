package org.lichen.garni.activities;

import android.support.v4.app.Fragment;

import java.util.Collection;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxFragment extends Fragment {
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

    protected void remember(Collection<Subscription> ss) {
        for (Subscription s : ss) {
            remember(s);
        }
    }
}

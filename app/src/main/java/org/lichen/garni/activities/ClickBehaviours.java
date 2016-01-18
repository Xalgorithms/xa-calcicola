package org.lichen.garni.activities;

import android.view.View;

import com.google.common.collect.Lists;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Collection;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ClickBehaviours {
    public Collection<Subscription> bindById(Collection<View> views, Action1<Integer> act) {
        List<Subscription> rv = Lists.newArrayList();
        for (View v : views) {
            rv.add(bindById(v, act));
        }
        return rv;
    }

    public Subscription bindById(final View v, final Action1<Integer> act) {
        return bind(v, new Action1<Void>() {
            @Override
            public void call(Void x) {
                act.call(v.getId());
            }
        });
    }

    public Subscription bind(View v, final Action1<Void> act) {
        return RxView.clicks(v)
                .observeOn(Schedulers.io())
                .subscribe(act);
    }
}


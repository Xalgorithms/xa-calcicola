package org.lichen.garni.activities;

import android.view.View;

import com.google.common.collect.Lists;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Collection;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class ClickBehaviours {
    private final PublishSubject<Integer> _subject = PublishSubject.create();

    public Collection<Subscription> bind(Collection<View> views, Action1<Integer> act) {
        List<Subscription> rv = Lists.newArrayList();
        for (View v : views) {
            final int id = v.getId();
            rv.add(RxView.clicks(v)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            _subject.onNext(id);
                        }
                    }));
            rv.add(_subject
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(act));
        }

        return rv;
    }
}


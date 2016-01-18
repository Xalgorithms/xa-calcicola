package org.lichen.garni.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lichen.garni.R;
import org.lichen.geghard.api.Transaction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class TransactionsFragment extends RxFragment implements BehaviourBinder<Context> {
    @Bind(R.id.collection_main_transactions) RecyclerView _collection;

    private ClickBehaviours _click_behaviours = new ClickBehaviours();

    private TransactionsAdapter _adapter;

    @Override
    public void bind(View v, final Action1<Context> act) {
        // TODO: these need to be restored onResume
        remember(_click_behaviours.bind(v, new Action1<Void>() {
            @Override
            public void call(Void v) {
                act.call(getContext());
            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return init(inflater.inflate(R.layout.fragment_main_transactions, container, false));
    }

    private View init(View v) {
        ButterKnife.bind(this, v);

        _adapter = new TransactionsAdapter(getContext(), this);
        _collection.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _collection.setAdapter(_adapter);

        return v;
    }

    public void update(List<Transaction> transactions) {
        _adapter.update(transactions);
    }
}

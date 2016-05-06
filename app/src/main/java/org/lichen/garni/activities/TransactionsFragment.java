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
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class TransactionsFragment extends RxFragment {
    @BindView(R.id.collection_main_transactions) RecyclerView _collection;

    private ClickBehaviours _click_behaviours = new ClickBehaviours();

    private TransactionsAdapter _adapter;

    public static TransactionsFragment make(GeghardSite site) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_SITE, site);
        TransactionsFragment rv = new TransactionsFragment();
        rv.setArguments(args);
        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return init(inflater.inflate(R.layout.fragment_main_transactions, container, false));
    }

    private View init(View v) {
        ButterKnife.bind(this, v);

        _adapter = new TransactionsAdapter(getContext(), new Receiver<Transaction>() {
            @Override
            public void receive(View v, final Transaction tr) {
                remember(_click_behaviours.bind(v, new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        GeghardSite site = getArguments().getParcelable(Constants.ARG_SITE);
                        Invocations.launchTransaction(getContext(), site, tr);
                    }
                }));
            }
        });
        _collection.setLayoutManager(new LinearLayoutManager(getContext()));
        _collection.setAdapter(_adapter);

        return v;
    }

    public void update(List<Transaction> transactions) {
        _adapter.update(transactions);
    }
}

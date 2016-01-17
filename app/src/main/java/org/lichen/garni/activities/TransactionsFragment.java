package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lichen.garni.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TransactionsFragment extends Fragment {
    @Bind(R.id.collection_main_transactions) RecyclerView _collection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return init(inflater.inflate(R.layout.fragment_main_transactions, container, false));
    }

    private View init(View v) {
        ButterKnife.bind(this, v);

        _collection.setHasFixedSize(true);
        _collection.setLayoutManager(new LinearLayoutManager(getContext()));
        _collection.setAdapter(new TransactionsAdapter());
        return v;
    }
}

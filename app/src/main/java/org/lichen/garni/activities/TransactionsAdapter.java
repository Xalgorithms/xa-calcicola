package org.lichen.garni.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lichen.garni.R;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(
            ViewGroup container, int vt) {
        return new ViewHolder(
                LayoutInflater.from(container.getContext()).inflate(
                        R.layout.recycler_item_transactions, container, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int pos) {
        // add data here
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.lichen.garni.R;
import org.lichen.geghard.api.Transaction;

import java.text.DateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private final Context _ctx;
    private List<Transaction> _transactions = Lists.newArrayList();
    private Map<String, Integer> _statuses = Maps.newHashMap();

    public TransactionsAdapter(Context ctx) {
        _ctx = ctx;
        _statuses.put("open", R.string.transaction_status_open);
        _statuses.put("closed", R.string.transaction_status_closed);
    }

    public void update(Collection<Transaction> transactions) {
        _transactions.clear();
        _transactions.addAll(transactions);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public TextView count;
        public TextView started;

        public ViewHolder(View v) {
            super(v);
            status = (TextView) v.findViewById(R.id.label_transaction_status);
            count = (TextView) v.findViewById(R.id.label_transaction_invoices);
            started = (TextView) v.findViewById(R.id.label_transaction_started);
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
        Transaction tr = _transactions.get(pos);
        int statusResId = R.string.transaction_status_unknown;

        if (_statuses.containsKey(tr.status)) {
            statusResId = _statuses.get(tr.status);
        }

        vh.status.setText(statusResId);
        vh.count.setText(_ctx.getString(R.string.fmt_transation_invoices, tr.n_invoices));
        vh.started.setText(DateFormat.getDateInstance().format(tr.created_at));
    }

    @Override
    public int getItemCount() {
        return _transactions.size();
    }
}

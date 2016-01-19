package org.lichen.garni.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Maps;

import org.lichen.garni.R;
import org.lichen.geghard.api.Transaction;

import java.text.DateFormat;
import java.util.Map;

public class TransactionsAdapter
        extends RecyclerCollectionAdapter<TransactionsAdapter.ViewHolder, Transaction> {
    private Map<String, Integer> _statuses = Maps.newHashMap();

    public TransactionsAdapter(Context ctx, Receiver receiver) {
        super(ctx, receiver);
        _statuses.put("open", R.string.transaction_status_open);
        _statuses.put("closed", R.string.transaction_status_closed);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public TextView count;
        public TextView started;
        public View target;

        public ViewHolder(View v) {
            super(v);
            status = (TextView) v.findViewById(R.id.label_transaction_status);
            count = (TextView) v.findViewById(R.id.label_transaction_invoices);
            started = (TextView) v.findViewById(R.id.label_transaction_started);
            target = v.findViewById(R.id.view_transaction_target);
        }
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_transactions;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(ViewHolder vh, Transaction tr) {
        int statusResId = R.string.transaction_status_unknown;

        if (_statuses.containsKey(tr.status)) {
            statusResId = _statuses.get(tr.status);
        }

        vh.status.setText(statusResId);
        vh.count.setText(context().getString(R.string.fmt_transation_invoices, tr.n_invoices));
        vh.started.setText(DateFormat.getDateInstance().format(tr.created_at));

        return vh.target;
    }
}

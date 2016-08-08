package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Maps;

import org.lichen.garni.R;
import org.lichen.geghard.api.Transaction;

import java.util.Map;

public class TransactionsAdapter
        extends RecyclerCollectionAdapter<TransactionsAdapter.ViewHolder, Transaction> {
    private Map<String, Integer> _statuses = Maps.newHashMap();

    public TransactionsAdapter(Context ctx, Receiver receiver) {
        super(ctx, receiver);
        _statuses.put("open", R.string.transaction_status_open);
        _statuses.put("closed", R.string.transaction_status_closed);
    }

    public static class ViewHolder extends BaseViewHolder {
        public TextView name;
        public TextView status;
        public TextView invoices;
        public TextView new_invoices;
        public TextView rules;
        public View target;

        public ViewHolder(View v) {
            super(v);
            name = textView(v, R.id.label_transaction_name);
            status = textView(v, R.id.label_transaction_status);
            invoices = textView(v, R.id.label_transaction_invoices);
            new_invoices = textView(v, R.id.label_transaction_new_invoices);
            rules = textView(v, R.id.label_transaction_rules);
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

        vh.name.setText(tr.id.substring(0, 8));
        vh.status.setText(statusResId);
        vh.invoices.setText(context().getString(R.string.fmt_transaction_invoices, tr.invoices.size()));
        vh.new_invoices.setText(context().getString(R.string.fmt_transaction_new_invoices, tr.invoices.size()));
        vh.rules.setText(context().getString(R.string.fmt_transaction_rules, tr.associations.size()));

        return vh.target;
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.Transaction;

public class TransactionInvoicesAdapter extends RecyclerCollectionAdapter<TransactionInvoicesAdapter.ViewHolder, Transaction.Invoice> {
    public static class ViewHolder extends BaseViewHolder {
        private final View target;
        TextView id;

        public ViewHolder(View v) {
            super(v);
            id = textView(v, R.id.label_affected_invoice_id);
            target = v.findViewById(R.id.container_transaction_invoices_target);
        }
    }

    public TransactionInvoicesAdapter(Context ctx, Receiver<Transaction.Invoice> receiver) {
        super(ctx, receiver);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_transaction_invoices;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(final ViewHolder vh, Transaction.Invoice i) {
        vh.id.setText(i.id);
        return vh.target;
    }
}

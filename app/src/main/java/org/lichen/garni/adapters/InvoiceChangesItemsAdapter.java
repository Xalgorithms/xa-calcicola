package org.lichen.garni.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.ChangeDocument;

public class InvoiceChangesItemsAdapter extends RecyclerCollectionAdapter<InvoiceChangesItemsAdapter.ViewHolder, ChangeDocument.CombinedChangedLine> {
    public InvoiceChangesItemsAdapter(Context ctx, Receiver<ChangeDocument.CombinedChangedLine> r) {
        super(ctx, r);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_invoice_changes_change;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(ViewHolder vh, ChangeDocument.CombinedChangedLine it) {
        vh.previous_name.setText(it.previous().name());
        vh.previous_description.setText(it.previous().description().text());
        vh.previous_price.setText(it.previous().price().format());
        vh.latest_name.setText(it.latest().name());
        vh.latest_description.setText(it.latest().description().text());
        vh.latest_price.setText(it.latest().price().format());
        return vh.itemView;
    }

    public static class ViewHolder extends BaseViewHolder {
        public final TextView previous_name;
        public final TextView previous_description;
        public final TextView previous_price;

        public final TextView latest_name;
        public final TextView latest_description;
        public final TextView latest_price;

        public ViewHolder(View v) {
            super(v);
            latest_name = textView(v, R.id.label_invoice_changes_latest_name);
            latest_description = textView(v, R.id.label_invoice_changes_latest_description);
            latest_price = textView(v, R.id.label_invoice_changes_latest_price);
            previous_name = textView(v, R.id.label_invoice_changes_previous_name);
            previous_description = textView(v, R.id.label_invoice_changes_previous_description);
            previous_price = textView(v, R.id.label_invoice_changes_previous_price);
        }
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.Line;

public class InvoiceItemsAdapter extends RecyclerCollectionAdapter<InvoiceItemsAdapter.ViewHolder, Line> {
    public InvoiceItemsAdapter(Context ctx, Receiver<Line> r) {
        super(ctx, r);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_affected_invoice_item;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(ViewHolder vh, Line it) {
        vh.name.setText(it.name());
        vh.description.setText(it.description().text());
        vh.price.setText(it.price().format());
        return vh.itemView;
    }

    public static class ViewHolder extends BaseViewHolder {
        public final TextView name;
        public final TextView description;
        public final TextView price;

        public ViewHolder(View v) {
            super(v);
            name = textView(v, R.id.label_affected_item_name);
            description = textView(v, R.id.label_affected_item_description);
            price = textView(v, R.id.label_affected_item_price);
        }
    }
}

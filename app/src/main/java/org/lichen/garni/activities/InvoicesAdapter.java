package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.Customer;
import org.lichen.geghard.api.Invoice;

import java.util.Date;

public class InvoicesAdapter extends RecyclerCollectionAdapter<InvoicesAdapter.ViewHolder, Invoice> {
    public static class ViewHolder extends BaseViewHolder {
        private final View target;
        TextView id;
        TextView customer_name;
        TextView customer_company;
        TextView issued;
        TextView total;

        public ViewHolder(View v) {
            super(v);
            id = textView(v, R.id.label_invoice_id);
            customer_name = textView(v, R.id.label_invoice_customer_name);
            customer_company = textView(v, R.id.label_invoice_customer_company);
            issued = textView(v, R.id.label_invoice_issued);
            total = textView(v, R.id.label_invoice_total);
            target = v.findViewById(R.id.view_invoice_target);
        }

        public void update_issued(Date d) {
            issued.setText(format(d));
        }
    }

    public InvoicesAdapter(Context ctx, Receiver<Invoice> receiver) {
        super(ctx, receiver);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_invoices;
    }
    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(final ViewHolder vh, Invoice i) {
        /*
        vh.id.setText(i.working_document().id());
        vh.update_issued(i.working_document().issued());
        Customer c = i.working_document().customer();
        vh.customer_company.setText(c.name());
        vh.customer_name.setText(c.contact_name());
        vh.total.setText(i.working_document().format_total());
        */
        return vh.target;
    }
}

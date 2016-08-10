package org.lichen.garni.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.InvoiceDocument;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInvoicesAdapter extends RecyclerCollectionAdapter<UserInvoicesAdapter.ViewHolder, Invoice> {
    private final java.text.DateFormat _fmt;

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.label_invoice_customer_name) TextView name;
        @BindView(R.id.label_invoice_total) TextView total;
        @BindView(R.id.label_invoice_customer_company) TextView company;
        @BindView(R.id.label_invoice_status) TextView status;
        @BindView(R.id.label_invoice_issued) TextView issued;
        @BindView(R.id.view_invoice_target) View target;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Inject Documents _documents;

    public UserInvoicesAdapter(Context ctx, Receiver<Invoice> receiver) {
        super(ctx, receiver);
        GarniApp.object_graph(ctx).inject(this);
        _fmt = DateFormat.getDateFormat(ctx);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_invoice;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(final ViewHolder vh, Invoice i) {
        InvoiceDocument doc = _documents.get(i.document.id);

        vh.name.setText(doc.customer().contact_name());
        vh.total.setText(doc.format_total());
        vh.company.setText(doc.customer().name());
        vh.status.setText("Status");
        vh.issued.setText(_fmt.format(doc.issued()));

        return vh.target;
    }
}

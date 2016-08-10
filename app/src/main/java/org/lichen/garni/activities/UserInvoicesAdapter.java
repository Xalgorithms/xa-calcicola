package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.Invoice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInvoicesAdapter extends RecyclerCollectionAdapter<UserInvoicesAdapter.ViewHolder, Invoice> {
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

    public UserInvoicesAdapter(Context ctx, Receiver<Invoice> receiver) {
        super(ctx, receiver);
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
        vh.name.setText("Foo");
        vh.total.setText("1000.00");
        vh.company.setText("Company");
        vh.status.setText("Status");
        vh.issued.setText("April 01, 2016");

        return vh.target;
    }
}

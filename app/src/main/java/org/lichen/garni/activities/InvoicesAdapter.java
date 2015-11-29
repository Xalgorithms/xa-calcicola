package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.geghard.api.Invoice;

public class InvoicesAdapter extends CollectionAdapter<Invoice> {
    public InvoicesAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected void update(View v, Invoice o) {
        ((TextView) v).setText(String.format("%s effective %s", o.id, o.effective));
    }

    @Override
    protected long id(Invoice o) {
        return o.id;
    }
}

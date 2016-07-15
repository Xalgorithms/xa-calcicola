package org.lichen.garni.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lichen.geghard.api.Invoice;

import java.util.Collections;
import java.util.List;

public class InvoicesAdapter extends BaseAdapter {
    private List<Invoice> _invoices = Collections.emptyList();
    private final LayoutInflater _inflater;

    public InvoicesAdapter(Context ctx) {
        _inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return _invoices.size();
    }

    @Override
    public Invoice getItem(int i) {
        return _invoices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).id;
    }

    @Override
    public View getView(int i, View v, ViewGroup p) {
        if (null == v) {
            v = _inflater.inflate(android.R.layout.simple_list_item_1, p, false);
        }

        Invoice inv = getItem(i);
        ((TextView) v).setText(String.format("%s effective %s", inv.id, inv.effective));

        return v;
    }

    public void update(List<Invoice> invoices) {
        _invoices = invoices;
        notifyDataSetChanged();
    }
}

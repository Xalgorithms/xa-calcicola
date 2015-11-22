package org.lichen.garni.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lichen.geghard.api.Account;

import java.util.Collections;
import java.util.List;

public class AccountsAdapter extends BaseAdapter {
    private List<Account> _accounts = Collections.emptyList();
    private final LayoutInflater _inflater;

    public AccountsAdapter(Context ctx) {
        _inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return _accounts.size();
    }

    @Override
    public Account getItem(int i) {
        return _accounts.get(i);
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

        Account acc = getItem(i);
        ((TextView) v).setText(acc.name);

        return v;
    }

    public void update(List<Account> accounts) {
        _accounts = accounts;
        notifyDataSetChanged();
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.geghard.api.Account;

public class AccountsAdapter extends CollectionAdapter<Account> {
    public AccountsAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected void update(View v, Account o) {
        ((TextView) v).setText(o.name);
    }

    @Override
    protected long id(Account o) {
        return o.id;
    }
}

package org.lichen.garni.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class Accounts {
    public static Account find(Context ctx) {
        AccountManager am = AccountManager.get(ctx);
        Account[] accounts = am.getAccountsByType(Constants.TYPE);
        return accounts.length > 0 ? accounts[0] : null;
    }

    public static Account create(Context ctx, String name) {
        AccountManager am = AccountManager.get(ctx);
        Account acc = new Account(name, Constants.TYPE);
        Account rv = null;
        if (am.addAccountExplicitly(acc, "", null)) {
            rv = acc;
        }
        return rv;
    }
}

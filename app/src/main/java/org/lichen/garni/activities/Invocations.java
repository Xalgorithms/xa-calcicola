package org.lichen.garni.activities;

import android.content.Context;
import android.content.Intent;

import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Transaction;

import rx.functions.Action1;

public class Invocations {
    public static class MainArgs {
        public GeghardSite site;
        public int user_id;

        public MainArgs(GeghardSite s, int uid) {
            this.site = s;
            this.user_id = uid;
        }
    }

    public static Action1<MainArgs> invokeMain(final Context ctx) {
        return new Action1<MainArgs>() {
            @Override
            public void call(final MainArgs args) {
                launchMain(ctx, args);
            }
        };
    }

    public static void launchMain(Context ctx, MainArgs args) {
        Intent i = new Intent(ctx, MainActivity.class);
        i.putExtra(Constants.ARG_SITE, args.site);
        i.putExtra(Constants.ARG_USER_ID, args.user_id);
        ctx.startActivity(i);
    }

    public static void launchTransaction(Context ctx, GeghardSite site, Transaction tr) {
        Intent i = new Intent(ctx, TransactionActivity.class);
        i.putExtra(Constants.ARG_SITE, site);
        i.putExtra(Constants.ARG_TRANSACTION_ID, tr.id);
        ctx.startActivity(i);
    }

    public static void launchAffectedInvoice(Context ctx, GeghardSite site, Transaction.Invoice iv) {
        Intent i = new Intent(ctx, AffectedInvoiceActivity.class);
        i.putExtra(Constants.ARG_SITE, site);
        i.putExtra(Constants.ARG_INVOICE_ID, iv.id);
        i.putExtra(Constants.ARG_DOCUMENT_ID, iv.document.id);
        ctx.startActivity(i);
    }
}

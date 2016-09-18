package org.lichen.garni.activities;

import android.content.Context;
import android.content.Intent;

import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.Transaction;

public class Invocations {
    public static void launchAffectedInvoice(Context ctx, Invoice iv) {
        Intent i = new Intent(ctx, AffectedInvoiceActivity.class);
        i.putExtra(Constants.ARG_INVOICE_ID, iv.id);
        i.putExtra(Constants.ARG_TRANSACTION_ID, iv.transaction.id);
        ctx.startActivity(i);
    }

    public static void launchUserInvoices(Context ctx) {
        Intent i = new Intent(ctx, UserInvoicesActivity.class);
        ctx.startActivity(i);
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.content.Intent;

import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.Transaction;

import rx.functions.Action1;

public class Invocations {
    public static void launchTransaction(Context ctx, Transaction tr) {
        Intent i = new Intent(ctx, UserInvoicesActivity.class);
        i.putExtra(Constants.ARG_TRANSACTION_ID, tr.id);
        ctx.startActivity(i);
    }

    public static void launchAffectedInvoice(Context ctx, Invoice iv) {
        Intent i = new Intent(ctx, AffectedInvoiceActivity.class);
        i.putExtra(Constants.ARG_INVOICE_ID, iv.id);
        i.putExtra(Constants.ARG_DOCUMENT_ID, iv.document.id);
        ctx.startActivity(i);
    }

    public static void launchUserInvoices(Context ctx) {
        Intent i = new Intent(ctx, UserInvoicesActivity.class);
        ctx.startActivity(i);
    }
}

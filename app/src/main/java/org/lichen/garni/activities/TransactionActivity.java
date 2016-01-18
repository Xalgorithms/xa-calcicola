package org.lichen.garni.activities;

import android.os.Bundle;
import android.util.Log;

import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.InvoiceSet;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TransactionActivity extends RxActivity {
    private Client _client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Log.d("DK", String.format("transaction_id: %s", getIntent().getIntExtra(Constants.ARG_TRANSACTION_ID, -1)));
    }

    @Override
    public void onResume() {
        super.onResume();
        _client = new Client(site().url());
        remember(populate_from_api(transaction_id()));
    }

    private int transaction_id() {
        return getIntent().getIntExtra(Constants.ARG_TRANSACTION_ID, -1);
    }

    private GeghardSite site() {
        return getIntent().getParcelableExtra(Constants.ARG_SITE);
    }

    private Subscription populate_from_api(int transaction_id) {
        return _client.transaction_invoices(transaction_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<InvoiceSet>() {
                    @Override
                    public void call(InvoiceSet s) {

                    }
                });
    }
}

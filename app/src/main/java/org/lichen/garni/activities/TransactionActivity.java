package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.InvoiceSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TransactionActivity extends RxActivity {
    @Bind(R.id.collection_transaction_invoices) RecyclerView _collection;

    private InvoicesAdapter _adapter;
    private Client _client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        _adapter = new InvoicesAdapter(this, new Receiver<Invoice>() {
            @Override
            public void receive(View v, Invoice it) {

            }
        });
        _collection.setAdapter(_adapter);
        _collection.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        _client = new Client(site().url());
        remember(populate_from_api(transaction_id()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(mi);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InvoiceSet>() {
                    @Override
                    public void call(InvoiceSet s) {
                        _adapter.update(s.invoices);
                    }
                });
    }
}

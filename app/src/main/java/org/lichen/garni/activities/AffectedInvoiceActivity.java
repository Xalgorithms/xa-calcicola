package org.lichen.garni.activities;

import android.os.Bundle;
import java.text.DateFormat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.lichen.garni.R;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.InvoiceDocument;
import org.lichen.geghard.api.Line;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AffectedInvoiceActivity extends RxActivity {
    private Client _client;

    @BindView(R.id.label_affected_invoice_id) TextView _id;
    @BindView(R.id.label_affected_invoice_payee) TextView _payee;
    @BindView(R.id.label_affected_invoice_total) TextView _total;
    @BindView(R.id.label_affected_invoice_issued) TextView _issued;
    @BindView(R.id.label_affected_invoice_due) TextView _due;
    @BindView(R.id.collection_affected_invoice_items) RecyclerView _items;

    private InvoiceItemsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_invoice);
        ButterKnife.bind(this);

        _adapter = new InvoiceItemsAdapter(this, new Receiver<Line>() {
            @Override
            public void receive(View v, Line it) {

            }
        });
        _items.setAdapter(_adapter);
        _items.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        //_client = new Client(site().url());
        remember(populate_from_api(document_id()));
    }

    private String document_id() {
        return getIntent().getStringExtra(Constants.ARG_DOCUMENT_ID);
    }

    private Subscription populate_from_api(String document_id) {
        return _client.document(document_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject o) {
                        populate(new InvoiceDocument(o));
                    }
                });
    }

    private void populate(InvoiceDocument doc) {
        _id.setText(doc.id());
        _payee.setText(getString(R.string.fmt_invoice_payee, doc.customer().name(), "Company"));
        _total.setText(doc.format_total());
        _issued.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(doc.issued()));
        _due.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(doc.issued()));
        _adapter.update(doc.lines());
    }
}

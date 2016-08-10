package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.lichen.garni.R;
import org.lichen.garni.adapters.InvoiceItemsAdapter;
import org.lichen.garni.adapters.Receiver;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.InvoiceDocument;
import org.lichen.geghard.api.Line;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AffectedInvoiceActivity extends CoreActivity {
    @BindView(R.id.label_invoice_id) TextView _id;
    @BindView(R.id.label_affected_invoice_payee) TextView _payee;
    @BindView(R.id.label_affected_invoice_total) TextView _total;
    @BindView(R.id.label_affected_invoice_issued) TextView _issued;
    @BindView(R.id.label_affected_invoice_due) TextView _due;
    @BindView(R.id.collection_affected_invoice_items) RecyclerView _items;

    @Inject Client _client;
    @Inject Documents _documents;

    private InvoiceItemsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_affected_invoice);

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
        if (!_documents.exists(document_id())) {
            remember(populate_from_api(document_id()));
        } else {
            populate(_documents.get(document_id()));
        }
    }

    private String document_id() {
        return getIntent().getStringExtra(Constants.ARG_DOCUMENT_ID);
    }

    private Subscription populate_from_api(final String document_id) {
        return _client.document(document_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, InvoiceDocument>() {
                    @Override
                    public InvoiceDocument call(JsonObject o) {
                        return new InvoiceDocument(document_id, o);
                    }
                })
                .doOnNext(new Action1<InvoiceDocument>() {
                    @Override
                    public void call(InvoiceDocument id) {
                        _documents.add(id);
                        populate(id);
                    }
                })
                .subscribe();
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

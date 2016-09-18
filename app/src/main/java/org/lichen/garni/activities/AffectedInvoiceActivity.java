package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.lichen.garni.R;
import org.lichen.garni.adapters.InvoiceItemsAdapter;
import org.lichen.garni.adapters.Receiver;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.EventResponse;
import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.InvoiceDocument;
import org.lichen.geghard.api.Line;

import java.text.DateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class AffectedInvoiceActivity extends CoreActivity {
    @BindView(R.id.label_invoice_id) TextView _id;
    @BindView(R.id.label_affected_invoice_payee) TextView _payee;
    @BindView(R.id.label_affected_invoice_total) TextView _total;
    @BindView(R.id.label_affected_invoice_issued) TextView _issued;
    @BindView(R.id.label_affected_invoice_due) TextView _due;
    @BindView(R.id.action_affected_invoice_lichenize) Button _lichenize;
    @BindView(R.id.collection_affected_invoice_items) RecyclerView _items;

    @Inject Client _client;
    @Inject Documents _documents;

    private final PublishSubject<InvoiceDocument> _latest_document = PublishSubject.create();

    private InvoiceItemsAdapter _adapter;

    private final ClickBehaviours _behaviours = new ClickBehaviours();
    private final Action1<Integer> _reactions = new Action1<Integer>() {
        @Override
        public void call(Integer id) {
            switch (id) {
                case R.id.action_affected_invoice_lichenize:
                    execute_transaction();
            }
        }
    };

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

        remember(_behaviours.bindById(_lichenize, _reactions));
        remember(pull_latest());
        remember(subscribe_to_latest_document());
    }

    private String invoice_id() {
        return getIntent().getStringExtra(Constants.ARG_INVOICE_ID);
    }

    private String transaction_id() { return getIntent().getStringExtra(Constants.ARG_TRANSACTION_ID); }

    private Subscription subscribe_to_latest_document() {
        return _latest_document
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InvoiceDocument>() {
                    @Override
                    public void call(InvoiceDocument invoice_doc) {
                        populate(invoice_doc);
                    }
                });
    }

    private Subscription pull_latest() {
        return _client.invoice_latest(invoice_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Invoice.Document>() {
                    @Override
                    public void call(final Invoice.Document doc) {
                        // TODO: use doc.url (need to figure out how raw URLs work in the lib)
                        remember(pull_document(doc.id, new Action1<JsonObject>() {
                            @Override
                            public void call(JsonObject o) {
                                _latest_document.onNext(new InvoiceDocument(doc.id, o));
                            }
                        }));
                    }
                });
    }

    private Subscription pull_document(String document_id, Action1<JsonObject> fn) {
        return _client.document(document_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fn);
    }

    private void execute_transaction() {
        Subscription sub = _client.execute(transaction_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<EventResponse>() {
                    @Override
                    public void call(EventResponse r) {
                        pull_latest();
                    }
                });
        remember(sub);
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

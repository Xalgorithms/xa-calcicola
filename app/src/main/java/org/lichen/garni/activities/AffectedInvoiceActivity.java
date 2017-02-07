package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.collect.Lists;
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

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class AffectedInvoiceActivity extends CoreActivity {
    @BindView(R.id.component_person_customer) View _customer;
    @BindView(R.id.component_person_supplier) View _supplier;
    @BindView(R.id.component_summary) View _summary;
    @BindView(R.id.component_changes) View _changes;
    @BindView(R.id.component_lichenize) View _lichenize_component;
    @BindView(R.id.action_lichenize) ImageButton _lichenize;
    @BindView(R.id.action_show_changes) Button _show_changes;
    @BindView(R.id.collection_affected_invoice_items) RecyclerView _items;
    @BindView(R.id.progress_lichenize) ProgressBar _progress;
    @BindView(R.id.text_lichenize) TextView _lichenize_text;

    @Inject Client _client;
    @Inject Documents _documents;

    private final PublishSubject<InvoiceDocument> _latest_document = PublishSubject.create();

    private InvoiceItemsAdapter _adapter;
    private InvoiceDocument _invoice;

    private final ClickBehaviours _behaviours = new ClickBehaviours();
    private final Action1<Integer> _reactions = new Action1<Integer>() {
        @Override
        public void call(Integer id) {
            switch (id) {
                case R.id.action_lichenize:
                    _lichenize_text.setVisibility(View.GONE);
                    _progress.setVisibility(View.VISIBLE);
                    _lichenize.setClickable(false);
                    execute_transaction();
                    break;
                case R.id.action_show_changes:
                    Invocations.launchInvoiceChanges(AffectedInvoiceActivity.this, invoice_id(), _invoice.document_id());
                    break;
            }
        }
    };
    private ViewAdapter _view_adapter;
    private boolean _executed_lichenize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_affected_invoice);

        _view_adapter = new ViewAdapter(this);
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

        init();
        remember(_behaviours.bindById(Lists.newArrayList((View) _lichenize, _show_changes), _reactions));
        remember(pull_latest());
        remember(subscribe_to_latest_document());
        show_progress();
    }

    private String invoice_id() {
        return getIntent().getStringExtra(Constants.ARG_INVOICE_ID);
    }

    private String transaction_id() { return getIntent().getStringExtra(Constants.ARG_TRANSACTION_ID); }

    private boolean lichenized() { return getIntent().getBooleanExtra(Constants.ARG_LICHENIZED, false); }

    private void init() {
        if (lichenized()) {
            _changes.setVisibility(View.VISIBLE);
            _lichenize_component.setVisibility(View.GONE);
        }
    }

    private Subscription subscribe_to_latest_document() {
        return _latest_document
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InvoiceDocument>() {
                    @Override
                    public void call(InvoiceDocument invoice_doc) {
                        _lichenize_text.setVisibility(View.VISIBLE);
                        _progress.setVisibility(View.GONE);
                        _lichenize.setClickable(true);
                        if (_executed_lichenize) {
                            _changes.setVisibility(View.VISIBLE);
                            _lichenize_component.setVisibility(View.GONE);
                        }
                        update_title(invoice_doc.id());
                        hide_progress();
                        populate(invoice_doc);
                    }
                });
    }

    private Subscription pull_latest() {
        return _client.invoice_latest(invoice_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Invoice.Document>() {
                    @Override
                    public void call(final Invoice.Document doc) {
                        if (_documents.exists(doc.id)) {
                            _latest_document.onNext(_documents.get(doc.id));
                        } else {
                            // TODO: use doc.url (need to figure out how raw URLs work in the lib)
                            remember(pull_document(doc.id, new Action1<JsonObject>() {
                                @Override
                                public void call(JsonObject o) {
                                    InvoiceDocument id = new InvoiceDocument(doc.id, o);
                                    _documents.add(id);
                                    _latest_document.onNext(id);
                                }
                            }));
                        }
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
                        _executed_lichenize = true;
                        pull_latest();
                    }
                });
        remember(sub);
    }

    private void populate(InvoiceDocument doc) {
        _invoice = doc;
        _view_adapter.person(doc.customer(), _customer);
        _view_adapter.person(doc.supplier(), _supplier);
        _view_adapter.summary(doc, _summary);
        _view_adapter.changes(doc, _changes);
        _adapter.update(doc.lines());
    }
}

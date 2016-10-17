package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.lichen.garni.R;
import org.lichen.garni.adapters.InvoiceChangesItemsAdapter;
import org.lichen.garni.adapters.Receiver;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Change;
import org.lichen.geghard.api.ChangeDocument;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.InvoiceDocument;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class InvoiceChangesActivity extends CoreActivity {
    @BindView(R.id.collection_invoice_changes_changes) RecyclerView _items;

    @Inject Client _client;
    @Inject Documents _documents;

    private final PublishSubject<ChangeDocument> _latest_change = PublishSubject.create();

    private InvoiceDocument _invoice;
    private InvoiceChangesItemsAdapter _adapter;

    public void onCreate(Bundle sis) {
        super.onCreate(sis);
        init(R.layout.activity_invoice_changes);
        _adapter = new InvoiceChangesItemsAdapter(this, new Receiver<ChangeDocument.CombinedChangedLine>() {
            @Override
            public void receive(View v, ChangeDocument.CombinedChangedLine it) {

            }
        });
        _items.setAdapter(_adapter);
        _items.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onResume() {
        super.onResume();
        update_invoice();
        remember(pull_changes());
        remember(subscribe_to_latest_change());
    }

    private String invoice_id() {
        return getIntent().getStringExtra(Constants.ARG_INVOICE_ID);
    }

    private String invoice_document_id() {
        return getIntent().getStringExtra(Constants.ARG_DOCUMENT_ID);
    }

    private void update_invoice() {
        _invoice = _documents.get(invoice_document_id());
    }

    private void populate(ChangeDocument cd) {
        _adapter.update(cd.lines());
    }

    private Subscription subscribe_to_latest_change() {
        show_progress();
        return _latest_change
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChangeDocument>() {
                    @Override
                    public void call(ChangeDocument cd) {
                        hide_progress();
                        populate(cd);
                    }
                });
    }

    private Subscription pull_changes() {
        show_progress();
        return _client.invoice_change(invoice_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Change>() {
                    @Override
                    public void call(Change change) {
                        hide_progress();
                        ChangeDocument cd = new ChangeDocument(_invoice, change.previous, change.latest);
                        _latest_change.onNext(cd);
                    }
                });
    }
}

package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.adapters.Receiver;
import org.lichen.garni.adapters.UserInvoicesAdapter;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.InvoiceDocument;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class UserInvoicesActivity extends RxActivity {
    @BindView(R.id.collection_user_invoices_invoices) RecyclerView _collection;
    @Inject Client _client;
    @Inject Documents _documents;

    private UserInvoicesAdapter _adapter;
    private ClickBehaviours _click_behaviours = new ClickBehaviours();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invoices);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _adapter = new UserInvoicesAdapter(this, new Receiver<Invoice>() {
            @Override
            public void receive(View v, final Invoice it) {
                remember(_click_behaviours.bind(v, new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Invocations.launchAffectedInvoice(UserInvoicesActivity.this, it);
                    }
                }));
            }
        });
        _collection.setAdapter(_adapter);
        _collection.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        remember(populate_from_api());
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

    private Subscription populate_from_api() {
        final List<Invoice> cached = Lists.newArrayList();

        // TODO_bug: Seems to run twice after initial create
        Observable<String> document_ids = _client.user_invoices(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<List<Invoice>>() {
                    @Override
                    public void call(List<Invoice> invoices) {
                        cached.addAll(invoices);
                    }
                })
                .flatMap(new Func1<List<Invoice>, Observable<String>>() {
                             @Override
                             public Observable<String> call(List<Invoice> invoices) {
                                 return Observable.from(Iterables.transform(invoices, new Function<Invoice, String>() {
                                     @Override
                                     public String apply(Invoice i) {
                                         return i.document.id;
                                     }
                                 }));
                             }
                         }
                )
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String document_id) {
                        return !_documents.exists(document_id);
                    }
                });
        return document_ids.flatMap(new Func1<String, Observable<JsonObject>>() {
                    @Override
                    public Observable<JsonObject> call(String document_id) {
                        return _client.document(document_id);
                    }
                })
                .zipWith(document_ids, new Func2<JsonObject, String, InvoiceDocument>() {
                    @Override
                    public InvoiceDocument call(JsonObject o, String id) {
                        return new InvoiceDocument(id, o);
                    }
                })
                .doOnNext(new Action1<InvoiceDocument>() {
                    @Override
                    public void call(InvoiceDocument doc) {
                        _documents.add(doc);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        _adapter.update(cached);
                    }
                }).subscribe();
    }
}

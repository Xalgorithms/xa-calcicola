package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.lichen.garni.R;
import org.lichen.garni.adapters.Receiver;
import org.lichen.garni.adapters.UserInvoicesAdapter;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class UserInvoicesActivity extends CoreActivity {
    @BindView(R.id.collection_user_invoices_invoices) RecyclerView _collection;
    @Inject Client _client;

    private UserInvoicesAdapter _adapter;
    private ClickBehaviours _click_behaviours = new ClickBehaviours();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d("> creating");
        init(R.layout.activity_user_invoices);

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

        Timber.d("> resuming");
        update_title(user_email());
        remember(populate_invoices());
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

    private String user_id() {
        return getIntent().getStringExtra(Constants.ARG_USER_ID);
    }

    private String user_email() {
        return getIntent().getStringExtra(Constants.ARG_USER_EMAIL);
    }

    private Subscription populate_invoices() {
        show_progress();
        Timber.d("> populating invoices");
        return _client.user_invoices(user_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Invoice>>() {
                    @Override
                    public void call(List<Invoice> invoices) {
                        hide_progress();
                        Timber.d("> received invoices (size=%s)", invoices.size());
                        _adapter.update(invoices);
                    }
                });
    }
}

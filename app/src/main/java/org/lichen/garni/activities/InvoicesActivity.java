package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Account;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class InvoicesActivity extends AppCompatActivity {
    public static final String ARG_SITE = "org.lichen.garni.InvoicesActivity.ARGS.SITE";

    private final PublishSubject<String> _account_title = PublishSubject.create();
    private final PublishSubject<List<Invoice>> _current_invoices = PublishSubject.create();

    @Bind(R.id.list_invoices) ListView _invoices;

    private Client _client;
    private CompositeSubscription _subscriptions;
    private InvoicesAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _adapter = new InvoicesAdapter(this);
        _invoices.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _subscriptions = new CompositeSubscription();

        GeghardSite s = getIntent().getParcelableExtra(ARG_SITE);
        _client = new Client(s.url());
        _subscriptions.add(populateFromApi(1));
        _subscriptions.add(updateTitle());
        _subscriptions.add(updateAdapter());
    }

    private Subscription updateTitle() {
        return _account_title.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        InvoicesActivity.this.setTitle(s);
                    }
                });
    }

    private Subscription updateAdapter() {
        return _current_invoices.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Invoice>>() {
                    @Override
                    public void call(List<Invoice> invoices) {
                        _adapter.update(invoices);
                    }
                });
    }

    private Subscription populateFromApi(int id) {
        return _client.account(id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Account, Observable<List<Invoice>>>() {
                    @Override
                    public Observable<List<Invoice>> call(Account account) {
                        _account_title.onNext(account.name);
                        return _client.account_invoices(account.id);
                    }
                })
                .subscribe(new Action1<List<Invoice>>() {
                    @Override
                    public void call(List<Invoice> invoices) {
                        _current_invoices.onNext(invoices);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscriptions.unsubscribe();
    }
}

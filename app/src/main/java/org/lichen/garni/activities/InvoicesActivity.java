package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class InvoicesActivity extends AppCompatActivity {
    private final PublishSubject<String> _account_title = PublishSubject.create();
    private final PublishSubject<List<Invoice>> _current_invoices = PublishSubject.create();

    private Client _client;
    private CompositeSubscription _subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        _subscriptions = new CompositeSubscription();

        GeghardSite s = getIntent().getParcelableExtra(Constants.ARG_SITE);
        int uid = getIntent().getIntExtra(Constants.ARG_USER_ID, -1);

        _client = new Client(s.url());
        _subscriptions.add(populateFromApi(uid));
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
                        //_adapter.update(invoices);
                    }
                });
    }

    private Subscription populateFromApi(int user_id) {
        return _client.user_invoices(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<Invoice>>() {
                    @Override
                    public void call(List<Invoice> invoices) {
                        _current_invoices.onNext(invoices);
                    }
                });
/*
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
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscriptions.unsubscribe();
    }
}

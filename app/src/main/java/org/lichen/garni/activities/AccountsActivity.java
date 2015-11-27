package org.lichen.garni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.squareup.sqlbrite.BriteDatabase;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Account;
import org.lichen.geghard.api.Client;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class AccountsActivity extends RxActivity {
    @Inject BriteDatabase _db;

    @Bind(R.id.list_accounts_accounts) ListView _accounts;

    private final PublishSubject<List<Account>> _update_accounts = PublishSubject.create();

    private Client _client;
    private AccountsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _adapter = new AccountsAdapter(this);
        _accounts.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        GeghardSite s = getIntent().getParcelableExtra(Constants.ARG_SITE);
        int user_id = getIntent().getIntExtra(Constants.ARG_USER_ID, -1);

        _client = new Client(s.url());

        remember(populate_from_api(user_id));
        remember(update_adapter());
        remember(activate_account());
    }

    private Subscription update_adapter() {
        return _update_accounts.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Account>>() {
                    @Override
                    public void call(List<Account> accounts) {
                        _adapter.update(accounts);
                    }
                });
    }

    private Subscription populate_from_api(int user_id) {
        return _client.user_accounts(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<Account>>() {
                    @Override
                    public void call(List<Account> accounts) {
                        _update_accounts.onNext(accounts);
                    }
                });
    }

    private Subscription activate_account() {
        return RxAdapterView.itemClicks(_accounts).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer i) {
                        Account acc = _adapter.getItem(i);
                        Intent in = new Intent(AccountsActivity.this, InvoicesActivity.class);
                        in.putExtra(Constants.ARG_SITE, getIntent().getParcelableExtra(Constants.ARG_SITE));
                        in.putExtra(Constants.ARG_ACCOUNT_ID, acc.id);
                        startActivity(in);
                    }
                });
    }
}

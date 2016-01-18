package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Transaction;
import org.lichen.geghard.api.TransactionSet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity
        extends RxActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar_main) Toolbar _toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;
    @Bind(R.id.view_main_nav) NavigationView _nav;

    private final PublishSubject<List<Transaction>> _update_transactions = PublishSubject.create();

    private Client _client;
    private TransactionsFragment _transactions_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.setDrawerListener(toggle);
        toggle.syncState();

        _nav.setNavigationItemSelectedListener(this);
    }

    public void onResume() {
        super.onResume();
        GeghardSite s = getIntent().getParcelableExtra(Constants.ARG_SITE);

        _client = new Client(s.url());
        remember(update_transactions());
    }

    @Override
    public void onBackPressed() {
        if (!maybeCloseDrawer()) {
            super.onBackPressed();
        }
    }

    private boolean maybeCloseDrawer() {
        boolean rv = _drawer.isDrawerOpen(GravityCompat.START);
        if (rv) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        return rv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navitem_main_transactions:
                int uid = getIntent().getIntExtra(Constants.ARG_USER_ID, -1);
                _transactions_frag = TransactionsFragment.make(
                        (GeghardSite) getIntent().getParcelableExtra(Constants.ARG_SITE));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_main_frame, _transactions_frag);
                ft.commit();
                remember(populate_from_api(uid));
                break;
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Subscription populate_from_api(int uid) {
        return _client.user_transactions(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<TransactionSet>() {
                    @Override
                    public void call(TransactionSet ts) {
                        _update_transactions.onNext(ts.transactions);
                    }
                });
    }

    private Subscription update_transactions() {
        return _update_transactions
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Transaction>>() {
                    @Override
                    public void call(List<Transaction> transactions) {
                        _transactions_frag.update(transactions);
                    }
                });
    }
}

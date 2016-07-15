package org.lichen.garni.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewClickEvent;
import com.jakewharton.rxbinding.widget.AdapterViewItemClickEvent;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.sqlbrite.BriteDatabase;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private final PublishSubject<Void> _connectClick = PublishSubject.create();
    private final PublishSubject<GeghardSite> _activate = PublishSubject.create();

    @Inject BriteDatabase _db;
    @Bind(R.id.list_previous) ListView _previous_sites;
    @Bind(R.id.text_server_address) EditText _server_address;
    @Bind(R.id.button_connect) Button _connect;

    private CompositeSubscription _subscriptions;
    private SitesAdapter _sites_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
        _sites_adapter = new SitesAdapter(this);
        _previous_sites.setAdapter(_sites_adapter);

        RxView.clickEvents(_connect)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ViewClickEvent>() {
                    @Override
                    public void call(ViewClickEvent e) {
                        _connectClick.onNext(null);
                    }
                });
        RxAdapterView.itemClickEvents(_previous_sites)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<AdapterViewItemClickEvent>() {
                    @Override
                    public void call(AdapterViewItemClickEvent e) {
                        GeghardSite s = _sites_adapter.getItem(e.position());
                        _activate.onNext(s);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        _subscriptions = new CompositeSubscription();

        _subscriptions.add(subscribeToUrl());
        _subscriptions.add(subscribeToActivate());
        _subscriptions.add(subscribeToSites());
    }

    private Subscription subscribeToActivate() {
        return _activate.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GeghardSite>() {
                    @Override
                    public void call(GeghardSite o) {
                        Intent i = new Intent(MainActivity.this, InvoicesActivity.class);
                        i.putExtra(InvoicesActivity.ARG_SITE, o);
                        startActivity(i);
                    }
                });
    }

    private Subscription subscribeToUrl() {
        return Observable.combineLatest(
                _connectClick, RxTextView.textChanges(_server_address),
                new Func2<Void, CharSequence, String>() {
                    @Override
                    public String call(Void ignored, CharSequence text) {
                        return text.toString();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        long id = _db.insert(GeghardSite.TABLE, new GeghardSite.Maker().url(url).make(), SQLiteDatabase.CONFLICT_REPLACE);
                        _activate.onNext(GeghardSite.make(id, url));
                    }
                });
    }

    private Subscription subscribeToSites() {
        return _db.createQuery(GeghardSite.TABLE, GeghardSite.Q_ALL)
                .mapToList(GeghardSite.FROM_CURSOR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_sites_adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        _subscriptions.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

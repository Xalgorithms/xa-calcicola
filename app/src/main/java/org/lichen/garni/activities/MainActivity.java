package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewClickEvent;
import com.squareup.sqlbrite.BriteDatabase;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private final PublishSubject<Void> _performLogin = PublishSubject.create();
    private final PublishSubject<Void> _performChange = PublishSubject.create();

    @Inject BriteDatabase _db;

    @Bind(R.id.input_main_email) EditText _email;
    @Bind(R.id.input_main_password) EditText _password;
    @Bind(R.id.input_previous_sites) AutoCompleteTextView _previous_sites;
    @Bind(R.id.button_main_change) Button _change;
    @Bind(R.id.button_main_login) Button _login;

    private CompositeSubscription _subscriptions;
    private SitesAdapter _sites_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        bind(_login, _performLogin);
        bind(_change, _performChange);

        _sites_adapter = new SitesAdapter(this);
        _previous_sites.setAdapter(_sites_adapter);
    }

    private void bind(Button b, final PublishSubject<Void> subject) {
        RxView.clickEvents(b)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ViewClickEvent>() {
                    @Override
                    public void call(ViewClickEvent e) {
                        subject.onNext(null);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        _subscriptions = new CompositeSubscription();

        _subscriptions.add(build_change_ui_reaction());
        _subscriptions.add(subscribe_to_sites());
//        _subscriptions.add(subscribeToUrl());
//        _subscriptions.add(subscribeToActivate());
//        _subscriptions.add(subscribe_to_sites());
    }

    private Subscription build_change_ui_reaction() {
        return _performChange.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        _change.setVisibility(View.GONE);
                        _previous_sites.setVisibility(View.VISIBLE);
                    }
                });
    }

/*
    private Subscription subscribeToActivate() {
        return _activate
                .subscribeOn(Schedulers.io())
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
*/
    private Subscription subscribe_to_sites() {
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

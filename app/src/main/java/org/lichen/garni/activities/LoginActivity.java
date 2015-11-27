package org.lichen.garni.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
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

public class LoginActivity extends RxActivity {
    @Inject BriteDatabase _db;

    @Bind(R.id.input_login_email) EditText _email;
    @Bind(R.id.input_login_password) EditText _password;
    @Bind(R.id.input_login_previous_sites) AutoCompleteTextView _previous_sites;
    @Bind(R.id.action_login_change) Button _change;
    @Bind(R.id.action_login_login) Button _login;

    private SitesAdapter _sites_adapter;

    private GeghardSite _latest_site;

    private static class ClickBehaviour implements Action1<Void> {
        public final PublishSubject<Void> subject = PublishSubject.create();

        private int _id;

        @Override
        public void call(Void v) {
            subject.onNext(null);
        }

        public Subscription bind(Button b) {
            _id = b.getId();
            return RxView.clicks(b)
                    .observeOn(Schedulers.io())
                    .subscribe(this);
        }

        public Subscription subscribe(final Action1<Integer> act) {
            return subject.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            act.call(_id);
                        }
                    });
        }
    }

    private final ClickBehaviour _behaviour_change = new ClickBehaviour();
    private final ClickBehaviour _behaviour_login = new ClickBehaviour();

    private final PublishSubject<GeghardSite> _connect = PublishSubject.create();

    private final Action1<Integer> _click_reactions = new Action1<Integer>() {
        @Override
        public void call(Integer id) {
            switch (id) {
                case R.id.action_login_change:
                    show_previous_sites();
                    break;
                case R.id.action_login_login:
                    connect();
            }
        }
    };

    private void show_previous_sites() {
        _change.setVisibility(View.GONE);
        _previous_sites.setVisibility(View.VISIBLE);
    }

    private void hide_previous_sites() {
        _change.setVisibility(View.VISIBLE);
        _previous_sites.setVisibility(View.GONE);
    }

    private void connect() {
        String url = _previous_sites.getText().toString();
        if (url.isEmpty()) {
            _connect.onNext(_latest_site);
        } else {
            _connect.onNext(maybe_make_latest(url));
        }
    }

    private void update_latest_site(GeghardSite s) {
        _latest_site = s;
        hide_previous_sites();
    }

    private GeghardSite maybe_make_latest(String url) {
        GeghardSite rv = null;

        if (null != url && !url.isEmpty()) {
            ContentValues vals = new GeghardSite.Maker().url(url).latest(1).make();
            ContentValues updates = new ContentValues();

            updates.put(GeghardSite.COL_LATEST, 0);
            _db.update(GeghardSite.TABLE, updates, GeghardSite.COL_LATEST + "=1");
            long id = _db.insert(GeghardSite.TABLE, vals, SQLiteDatabase.CONFLICT_REPLACE);
            rv = GeghardSite.make(id, url, 1);
        }

        return rv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _sites_adapter = new SitesAdapter(this);
        _previous_sites.setAdapter(_sites_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        remember(subscribe_to_sites());
        remember(subscribe_to_latest_site());
        remember(subscribe_to_connect());

        remember(_behaviour_change.bind(_change));
        remember(_behaviour_change.subscribe(_click_reactions));
        remember(_behaviour_login.bind(_login));
        remember(_behaviour_login.subscribe(_click_reactions));
    }

    private Subscription subscribe_to_connect() {
        return _connect.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GeghardSite>() {
                    @Override
                    public void call(GeghardSite s) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(Constants.ARG_SITE, s);
                        startActivity(i);
                    }
                });
    }

    private Subscription subscribe_to_sites() {
        return _db.createQuery(GeghardSite.TABLE, GeghardSite.Q_ALL)
                .mapToList(GeghardSite.FROM_CURSOR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_sites_adapter);
    }

    private Subscription subscribe_to_latest_site() {
        return _db.createQuery(GeghardSite.TABLE, GeghardSite.Q_LATEST)
                .mapToOne(GeghardSite.FROM_CURSOR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GeghardSite>() {
                    @Override
                    public void call(GeghardSite s) {
                        update_latest_site(s);
                    }
                });
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

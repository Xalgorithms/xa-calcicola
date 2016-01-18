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

import com.google.common.collect.Lists;
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

    private final ClickBehaviours _click_behaviours = new ClickBehaviours();
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

    private final PublishSubject<Invocations.MainArgs> _connect = PublishSubject.create();

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
        Invocations.MainArgs args = new Invocations.MainArgs(_latest_site, 1);
        if (!url.isEmpty()) {
            args.site = maybe_make_latest(url);
        }
        _connect.onNext(args);
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

        remember(_click_behaviours.bind(Lists.newArrayList((View) _change, _login), _click_reactions));
    }

    private Subscription subscribe_to_connect() {
        return _connect.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Invocations.invokeMain(LoginActivity.this));
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

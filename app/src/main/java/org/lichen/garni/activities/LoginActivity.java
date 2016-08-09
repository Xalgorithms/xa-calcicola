package org.lichen.garni.activities;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class LoginActivity extends RxActivity {
    @Inject BriteDatabase _db;

    @BindView(R.id.input_login_email) EditText _email;
    @BindView(R.id.input_login_password) EditText _password;
    @BindView(R.id.action_login_login) Button _login;

    private final ClickBehaviours _click_behaviours = new ClickBehaviours();
    private final Action1<Integer> _click_reactions = new Action1<Integer>() {
        @Override
        public void call(Integer id) {
            switch (id) {
                case R.id.action_login_login:
                    connect();
            }
        }
    };

    private final PublishSubject<Invocations.MainArgs> _connect = PublishSubject.create();

    private void connect() {
        /*
        String url = _previous_sites.getText().toString();
        Invocations.MainArgs args = new Invocations.MainArgs(_latest_site, 1);
        if (!url.isEmpty()) {
            args.site = maybe_make_latest(url);
        }
        _connect.onNext(args);
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        remember(subscribe_to_connect());

        remember(_click_behaviours.bindById(Lists.newArrayList((View) _login), _click_reactions));
    }

    private Subscription subscribe_to_connect() {
        return _connect.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Invocations.invokeMain(LoginActivity.this));
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

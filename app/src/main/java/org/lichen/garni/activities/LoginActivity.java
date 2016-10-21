package org.lichen.garni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.common.collect.Lists;
import com.squareup.sqlbrite.BriteDatabase;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.metrics.MetricsManager;

import org.lichen.garni.BuildConfig;
import org.lichen.garni.R;
import org.lichen.garni.services.RegistrationIntentService;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class LoginActivity extends CoreActivity {
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

    private final PublishSubject<Void> _connect = PublishSubject.create();

    private void connect() {
        _connect.onNext(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_login);
        registerWithHockey();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    @Override
    public void onResume() {
        super.onResume();

        remember(subscribe_to_connect());
        remember(_click_behaviours.bindById(Lists.newArrayList((View) _login), _click_reactions));
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    private Subscription subscribe_to_connect() {
        return _connect.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (gcmUp()) {
                            Invocations.launchUserInvoices(LoginActivity.this);
                        }
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

    private boolean gcmUp() {
        if (checkPlayServices()) {
            Intent i = new Intent(this, RegistrationIntentService.class);
            startService(i);

            return true;
        }

        return false;
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Timber.d("Failed to resolved Play error");
                finish();
            }

            return false;
        }

        return true;
    }

    // Hockey stuff
    private void checkForCrashes() {
        if (!BuildConfig.DEBUG) {
            CrashManager.register(this);
        }
    }

    private void registerWithHockey() {
        if (!BuildConfig.DEBUG) {
            // TODO: Remove when we're in the store (probably with prod/dev builds)
            UpdateManager.register(this);
            MetricsManager.register(getApplication());
        }
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}

package org.lichen.garni.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.common.collect.Lists;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.squareup.sqlbrite.BriteDatabase;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.metrics.MetricsManager;

import org.lichen.garni.BuildConfig;
import org.lichen.garni.R;
import org.lichen.garni.services.RegistrationIntentService;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Completion;
import org.lichen.geghard.api.User;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoginActivity extends CoreActivity {
    @Inject BriteDatabase _db;
    @Inject Client _client;

    @BindView(R.id.input_login_email) EditText _email;
    @BindView(R.id.input_login_password) EditText _password;
    @BindView(R.id.action_login_login) Button _login;
    @BindView(R.id.progress_login_login) ProgressBar _progress;
    @BindView(R.id.label_login_failed) TextView _label_login_failed;

    private final ClickBehaviours _click_behaviours = new ClickBehaviours();
    private final Action1<Integer> _click_reactions = new Action1<Integer>() {
        @Override
        public void call(Integer id) {
            switch (id) {
                case R.id.action_login_login:
                    login(_email.getText().toString());
                    break;
            }
        }
    };

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

        reset_login_widgets();

        // FIXME: improve
        _email.setText(recall());

        remember(_click_behaviours.bindById(Lists.newArrayList((View) _login), _click_reactions));
        remember(login_text_change());
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    private void show_login_progress() {
        _progress.setVisibility(View.VISIBLE);
        _login.setVisibility(View.GONE);
        _label_login_failed.setVisibility(View.GONE);
    }

    private void hide_login_progress() {
        _progress.setVisibility(View.GONE);
        _login.setVisibility(View.VISIBLE);
    }

    private void reset_login_widgets() {
        hide_login_progress();
        _label_login_failed.setVisibility(View.GONE);
    }

    private void show_login_failure() {
        hide_login_progress();
        _label_login_failed.setVisibility(View.VISIBLE);
    }

    private void login(String email) {
        show_login_progress();
        remember(_client.user(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Completion<User>() {
                    @Override
                    protected void response(User u) {
                        remember(u);
                        if (gcmUp(u.id)) {
                            Invocations.launchUserInvoices(LoginActivity.this, u.id, u.email);
                        }
                    }

                    @Override
                    protected void handle_404() {
                        show_login_failure();
                    }
                }));
    }

    private Subscription login_text_change() {
        return RxTextView.textChangeEvents(_email)
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent e) {
                        boolean enabled = e.text().length() > 0;
                        _login.setClickable(enabled);
                        _login.setEnabled(enabled);
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

    private boolean gcmUp(String user_id) {
        if (checkPlayServices()) {
            Intent i = new Intent(this, RegistrationIntentService.class);
            i.putExtra(Constants.ARG_USER_ID, user_id);
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

    private void remember(User u) {
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(Constants.KEY_USER_EMAIL, u.email);
        e.putString(Constants.KEY_USER_ID, u.id);
        e.commit();
    }

    private String recall() {
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        return prefs.getString(Constants.KEY_USER_EMAIL, "bob@nowhere.com");
    }
}

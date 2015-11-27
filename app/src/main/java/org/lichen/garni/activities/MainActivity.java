package org.lichen.garni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.action_main_accounts) Button _accounts;
    @Bind(R.id.action_main_invoices) Button _invoices;
    @Bind(R.id.action_main_new_account) Button _new_account;
    @Bind(R.id.action_main_new_invoice) Button _new_invoice;

    private GeghardSite _site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        _site = getIntent().getParcelableExtra(Constants.ARG_SITE);
        bind(_accounts, AccountsActivity.class);
        bind(_invoices, InvoicesActivity.class);
        bind(_new_account, NewAccountActivity.class);
        bind(_new_invoice, NewInvoiceActivity.class);
    }

    private void bind(View v, final Class<?> klass) {
        RxView.clicks(v).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent i = new Intent(MainActivity.this, klass);
                        i.putExtra(Constants.ARG_SITE, _site);
                        i.putExtra(Constants.ARG_USER_ID, 1);
                        startActivity(i);
                    }
                });
    }
}
package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.action_main_accounts) Button _accounts;
    @Bind(R.id.action_main_invoices) Button _invoices;
    @Bind(R.id.action_main_new_account) Button _new_account;
    @Bind(R.id.action_main_new_invoice) Button _new_invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }
}
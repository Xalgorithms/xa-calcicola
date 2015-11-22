package org.lichen.garni.activities;

import android.os.Bundle;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.GeghardSite;

import butterknife.ButterKnife;

public class AccountsActivity extends RxActivity {
    public static final String ARG_SITE = "org.lichen.garni.AccountsActivity.ARGS.SITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        GeghardSite s = getIntent().getParcelableExtra(ARG_SITE);
    }
}

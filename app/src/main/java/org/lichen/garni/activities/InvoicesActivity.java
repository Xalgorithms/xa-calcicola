package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.lichen.garni.R;

public class InvoicesActivity extends AppCompatActivity {
    public static final String ARG_SITE = "org.lichen.garni.InvoicesActivity.ARGS.SITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);
    }
}

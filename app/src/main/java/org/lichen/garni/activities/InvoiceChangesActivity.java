package org.lichen.garni.activities;

import android.os.Bundle;

import org.lichen.garni.R;

public class InvoiceChangesActivity extends CoreActivity {
    public void onCreate(Bundle sis) {
        super.onCreate(sis);
        init(R.layout.activity_invoice_changes);
    }

    public void onResume() {
        super.onResume();
    }

    private String invoice_id() {
        return getIntent().getStringExtra(Constants.ARG_INVOICE_ID);
    }
}

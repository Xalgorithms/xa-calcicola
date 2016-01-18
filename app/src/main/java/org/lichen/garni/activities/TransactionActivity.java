package org.lichen.garni.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.lichen.garni.R;

public class TransactionActivity extends RxActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Log.d("DK", String.format("transaction_id: %s", getIntent().getIntExtra(Constants.ARG_TRANSACTION_ID, -1)));
    }
}

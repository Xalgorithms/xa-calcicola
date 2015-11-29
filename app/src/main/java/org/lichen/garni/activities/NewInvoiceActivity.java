package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.lichen.garni.R;

public class NewInvoiceActivity extends RxActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.actions_new_invoice, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem m) {
        switch (m.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_new_invoice_save:
                save();
                return true;
        }
        return super.onOptionsItemSelected(m);
    }

    private void save() {
        finish();
    }
}

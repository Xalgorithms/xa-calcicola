package org.lichen.garni.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class NewInvoiceActivity extends RxActivity {
    private PublishSubject<Date> _update_effective = PublishSubject.create();
    private PublishSubject<Date> _update_due = PublishSubject.create();

    @Bind(R.id.input_new_invoice_effective) TextView _effective;
    @Bind(R.id.input_new_invoice_due) TextView _due;

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private PublishSubject<Date> _subject;

        @NonNull @Override
        public Dialog onCreateDialog(Bundle b) {
            final Calendar c = Calendar.getInstance();

            return new DatePickerDialog(
                    getActivity(),
                    this,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            _subject.onNext(new GregorianCalendar(y, m, d).getTime());
        }

        public void show(PublishSubject<Date> subject, FragmentManager fm) {
            _subject = subject;
            show(fm, "date_picker");
        }
    }

    private DatePickerFragment _date_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        _date_picker = new DatePickerFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        remember(bind_calendar(_effective, _update_effective));
        remember(update_date(_effective, _update_effective));
        remember(bind_calendar(_due, _update_due));
        remember(update_date(_due, _update_due));
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

    private Subscription bind_calendar(TextView v, final PublishSubject<Date> subject) {
        return RxView.clicks(v).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                _date_picker.show(subject, getSupportFragmentManager());
            }
        });
    }

    private Subscription update_date(final TextView v, PublishSubject<Date> subject) {
        return subject.subscribe(new Action1<Date>() {
            @Override
            public void call(Date date) {
                v.setText(DateFormat.getDateInstance().format(date));
            }
        });
    }
}

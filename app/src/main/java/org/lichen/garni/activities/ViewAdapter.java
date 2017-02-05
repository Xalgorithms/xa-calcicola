package org.lichen.garni.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.lichen.garni.R;
import org.lichen.geghard.api.InvoiceDocument;
import org.lichen.geghard.api.Person;
import org.lichen.geghard.api.Totals;

import java.text.DateFormat;

public class ViewAdapter {
    private final Context _ctx;

    public ViewAdapter(Context ctx) {
        _ctx = ctx;
    }

    public void person(Person person, View view) {
        text(view, R.id.label_person_name, _ctx.getString(R.string.fmt_invoice_payee, person.name(), "Company"));
        text(view, R.id.label_person_location, person.city());
        text(view, R.id.label_person_country, person.country());
    }

    private void text(View v, int resId, String s) {
        TextView tv = (TextView) v.findViewById(resId);
        if (null != tv) {
            tv.setText(s);
        }
    }

    public void summary(InvoiceDocument doc, View view) {
        Totals tots = doc.totals();
        if (tots != null) {
            if (tots.payable() != null) {
                text(view, R.id.label_summary_total, tots.payable().format());
            }
            if (tots.tax_exclusive() != null) {
                text(view, R.id.label_summary_tax, tots.tax_exclusive().format());
            }
            if (tots.total() != null) {
                text(view, R.id.label_summary_subtotal, tots.total().format());
            }
        }
        text(view, R.id.label_summary_issued, DateFormat.getDateInstance(DateFormat.SHORT).format(doc.issued()));
        text(view, R.id.label_summary_due, DateFormat.getDateInstance(DateFormat.SHORT).format(doc.issued()));
    }

    public void changes(InvoiceDocument doc, View view) {
        text(view, R.id.text_changes_rules, _ctx.getString(R.string.fmt_applied_rules, 3));
    }
}

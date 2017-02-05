package org.lichen.garni.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.Invoice;
import org.lichen.geghard.api.InvoiceDocument;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class UserInvoicesAdapter extends RecyclerCollectionAdapter<UserInvoicesAdapter.ViewHolder, Invoice> {
    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.label_invoice_customer_name) TextView name;
        @BindView(R.id.label_invoice_total) TextView total;
        @BindView(R.id.label_invoice_customer_company) TextView company;
        @BindView(R.id.label_invoice_status) TextView status;
        @BindView(R.id.label_invoice_issued) TextView issued;
        @BindView(R.id.view_invoice_target) View target;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Inject Documents _documents;
    @Inject Client _client;

    private final java.text.DateFormat _fmt;
    private final Map<String, PublishSubject<InvoiceDocument>> _subjects = Maps.newConcurrentMap();

    public UserInvoicesAdapter(Context ctx, Receiver<Invoice> receiver) {
        super(ctx, receiver);
        GarniApp.object_graph(ctx).inject(this);
        _fmt = DateFormat.getDateFormat(ctx);
    }

    @Override
    protected int layout_res_id() {
        return R.layout.recycler_item_invoice;
    }

    @Override
    protected ViewHolder make_view_holder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected View init_view_holder(final ViewHolder vh, Invoice i) {
        if (i.revisions.size() > 0) {
            final String id = i.revisions.get(i.revisions.size() - 1).document.id;
            Action1<InvoiceDocument> receive_document = new Action1<InvoiceDocument>() {
                @Override
                public void call(InvoiceDocument doc) {
                    Timber.d(">> populating (document_id=%s)", doc.document_id());
                    populate(vh, doc);
                    _subjects.remove(id);
                }
            };

            if (_documents.exists(id)) {
                Timber.d("> populating from existing document (id=%s)", id);
                populate(vh, _documents.get(id));
            } else {
                if (!_subjects.containsKey(id)) {
                    Timber.d("> establishing subject (id=%s)", id);
                    _subjects.put(id, PublishSubject.<InvoiceDocument>create());
                    _client.document(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Action1<JsonObject>() {
                                @Override
                                public void call(JsonObject o) {
                                    Timber.d(">> received document (id=%s)", id);
                                    InvoiceDocument doc = new InvoiceDocument(id, o);
                                    _documents.add(doc);
                                    _subjects.get(id).onNext(doc);
                                }
                            });
                }

                Timber.d("> subscribing (id=%s)", id);
                _subjects.get(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(receive_document);
            }
        }
        return vh.target;
    }

    private void populate(ViewHolder vh, InvoiceDocument doc) {
        vh.name.setText(doc.customer().name());
        if (doc.totals() != null && doc.totals().payable() != null) {
            vh.total.setText(doc.totals().payable().format());
        }
        vh.company.setText(doc.customer().name());
        vh.status.setText("Status");
        vh.issued.setText(_fmt.format(doc.issued()));
    }
}

package org.lichen.garni.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.common.base.Function;
import com.google.gson.JsonObject;

import org.lichen.garni.GarniApp;
import org.lichen.garni.activities.AffectedInvoiceActivity;
import org.lichen.garni.activities.Constants;
import org.lichen.garni.data.Documents;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.InvoiceDocument;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    @Inject Client _client;
    @Inject Documents _documents;

    @Override
    public void onCreate() {
        super.onCreate();
        GarniApp.object_graph(this).inject(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String document_id = data.getString("document_id", null);
        final String invoice_id = data.getString("invoice_id", null);
        final String transaction_id = data.getString("transaction_id", null);

        find_document(document_id, new Function<InvoiceDocument, Void>() {
            @Override
            public Void apply(InvoiceDocument doc) {
                send_notification(doc, invoice_id, transaction_id);
                return null;
            }
        });
    }

    private void send_notification(InvoiceDocument doc, String invoice_id, String transaction_id) {
        Intent i = new Intent(this, AffectedInvoiceActivity.class);
        i.putExtra(Constants.ARG_DOCUMENT_ID, doc.document_id());
        i.putExtra(Constants.ARG_INVOICE_ID, invoice_id);
        i.putExtra(Constants.ARG_TRANSACTION_ID, transaction_id);

        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder bldr = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle("Invoice from " + doc.customer().contact_name())
                .setContentText("Touch to view details")
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pi);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, bldr.build());
    }

    private void find_document(final String document_id, final Function<InvoiceDocument, Void> fn) {
        if (_documents.exists(document_id)) {
            fn.apply(_documents.get(document_id));
        } else {
            _client.document(document_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Func1<JsonObject, InvoiceDocument>() {
                        @Override
                        public InvoiceDocument call(JsonObject o) {
                            return new InvoiceDocument(document_id, o);
                        }
                    })
                    .doOnNext(new Action1<InvoiceDocument>() {
                        @Override
                        public void call(InvoiceDocument doc) {
                            _documents.add(doc);
                            fn.apply(doc);
                        }
                    }).subscribe();
        }
    }
}

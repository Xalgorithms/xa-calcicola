package org.lichen.garni.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import org.lichen.garni.activities.AffectedInvoiceActivity;

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String document_id = data.getString("document_id", null);
        String invoice_id = data.getString("invoice_id", null);

        Intent i = new Intent(this, AffectedInvoiceActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder bldr = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle("New invoice")
                .setContentText("This is a new invoice")
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pi);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, bldr.build());
    }
}

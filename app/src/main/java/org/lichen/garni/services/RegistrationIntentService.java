package org.lichen.garni.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.lichen.garni.R;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.EventResponse;

import java.io.IOException;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService() {
        super("RIS");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID id = InstanceID.getInstance(this);
        String token = null;
        try {
            token = id.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            // oops
        }

        sendRegistrationToServer(token);
        subscribe(token);

        // LBM notify complete
    }

    private void subscribe(String token) {
        GcmPubSub ps = GcmPubSub.getInstance(this);
        try {
            ps.subscribe(token, "/topics/global", null);
        } catch (IOException e) {
            // oops
        }
    }

    private void sendRegistrationToServer(String token) {
        Client cl = new Client("https://xa-lichen.herokuapp.com");
        cl.register(1, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<EventResponse>() {
                    @Override
                    public void call(EventResponse eventResponse) {

                    }
                });
    }
}

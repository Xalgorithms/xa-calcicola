package org.lichen.garni.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;
import org.lichen.garni.activities.Constants;
import org.lichen.geghard.api.Client;
import org.lichen.geghard.api.EventResponse;

import java.io.IOException;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegistrationIntentService extends IntentService {
    @Inject Client _client;

    public RegistrationIntentService() {
        super("RIS");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GarniApp.object_graph(this).inject(this);
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

        sendRegistrationToServer(token, intent.getStringExtra(Constants.ARG_USER_ID));
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

    private void sendRegistrationToServer(String token, String user_id) {
        _client.register(user_id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<EventResponse>() {
                    @Override
                    public void call(EventResponse eventResponse) {

                    }
                });
    }
}

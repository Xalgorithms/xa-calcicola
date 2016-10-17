package org.lichen.geghard.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Client {
    private final String _url;

    public Client(String url) {
        _url = url;
    }

    public Observable<Invoice> invoice(int id) {
        return endpoint().invoice(id);
    }

    public Observable<List<Invoice>> user_invoices(int user_id) {
        return endpoint().user_invoices(user_id);
    }

    public Observable<List<Transaction>> user_transactions(int user_id) {
        return endpoint().user_transactions(user_id);
    }

    public Observable<JsonObject> document(String id) {
        return endpoint().document(id);
    }

    public Observable<Change> invoice_change(String invoice_id) {
        return endpoint().invoice_change(invoice_id);
    }

    public Observable<EventResponse> register(int user_id, String token) {
        return endpoint().post_event(Event.make_register(user_id, token));
    }

    public Observable<EventResponse> execute(String transaction_id) {
        return endpoint().post_event(Event.make_execute(transaction_id));
    }

    public Observable<Invoice.Document> invoice_latest(String invoice_id) {
        return endpoint().invoice_latest(invoice_id);
    }

    private Endpoint endpoint() {
        return retrofit().create(Endpoint.class);
    }


    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(_url)
                .addConverterFactory(gson())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static GsonConverterFactory gson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        return GsonConverterFactory.create(gson);
    }
}

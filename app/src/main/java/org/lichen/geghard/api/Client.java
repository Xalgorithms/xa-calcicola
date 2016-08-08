package org.lichen.geghard.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public Observable<List<Account>> accounts() {
        return endpoint().accounts();
    }

    public Observable<List<Account>> user_accounts(int user_id) {
        return endpoint().user_accounts(user_id);
    }

    public Observable<Account> account(int id) {
        return endpoint().account(id);
    }

    public Observable<List<Invoice>> account_invoices(int id) {
        return endpoint().account_invoices(id);
    }

    public Observable<List<Invoice>> user_invoices(int user_id) {
        return endpoint().user_invoices(user_id);
    }

    public Observable<List<Transaction>> user_transactions(int user_id) {
        return endpoint().user_transactions(user_id);
    }

    public Observable<InvoiceSet> transaction_invoices(String transaction_id) {
        return endpoint().transaction_invoices(transaction_id);
    }

    public Observable<Transaction> transaction(String id) {
        return endpoint().transaction(id);
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

package org.lichen.geghard.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;

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

    public Observable<Account> account(int id) {
        return endpoint().account(id);
    }

    public Observable<List<Invoice>> account_invoices(int id) {
        return endpoint().account_invoices(id);
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

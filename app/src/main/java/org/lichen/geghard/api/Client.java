package org.lichen.geghard.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import rx.functions.Action1;

public class Client {
    private final String _url;

    public Client(String url) {
        _url = url;
    }

    public void invoice(int id, final Action1<Invoice> fn) {
        endpoint().invoice(id).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Response<Invoice> response, Retrofit retrofit) {
                fn.call(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private Endpoint endpoint() {
        return retrofit().create(Endpoint.class);
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(_url)
                .addConverterFactory(gson())
                .build();
    }

    private static GsonConverterFactory gson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        return GsonConverterFactory.create(gson);
    }
}

package org.lichen.geghard.api;

import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Geghard {
    public static void invoice(int id, final Function<Invoice, Void> fn) {
        endpoint().invoice(id).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Response<Invoice> response, Retrofit retrofit) {
                fn.apply(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private static GeghardEndpoint endpoint() {
        return retrofit().create(GeghardEndpoint.class);
    }

    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:3000")
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

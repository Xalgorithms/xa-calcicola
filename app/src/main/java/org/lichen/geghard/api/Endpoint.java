package org.lichen.geghard.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface Endpoint {
    @GET("/api/v1/invoices/{id}")
    Observable<Invoice> invoice(@Path("id") int id);

    @GET("/api/v1/accounts/{id}")
    Observable<Account> account(@Path("id") int id);

    @GET("/api/v1/accounts/{id}/invoices")
    Observable<List<Invoice>> account_invoices(@Path("id") int id);
}

package org.lichen.geghard.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public interface Endpoint {
    @GET("/api/v1/invoices/{id}")
    Observable<Invoice> invoice(@Path("id") int id);

    @POST("/api/v1/invoices")
    Observable<Invoice> create_invoice(@Body Invoice invoice);

    @GET("/api/v1/accounts")
    Observable<List<Account>> accounts();

    @GET("/api/v1/accounts/{id}")
    Observable<Account> account(@Path("id") int id);

    @GET("/api/v1/accounts/{id}/invoices")
    Observable<List<Invoice>> account_invoices(@Path("id") int id);
}

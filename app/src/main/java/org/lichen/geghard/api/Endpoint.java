package org.lichen.geghard.api;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface Endpoint {
    @GET("/api/v1/invoices/{id}")
    Observable<Invoice> invoice(@Path("id") int id);

    @POST("/api/v1/invoices")
    Observable<Invoice> create_invoice(@Body Invoice invoice);

    @GET("/api/v1/accounts")
    Observable<List<Account>> accounts();

    @GET("/api/v1/users/{id}/accounts")
    Observable<List<Account>> user_accounts(@Path("id") int id);

    @GET("/api/v1/accounts/{id}")
    Observable<Account> account(@Path("id") int id);

    @GET("/api/v1/accounts/{id}/invoices")
    Observable<List<Invoice>> account_invoices(@Path("id") int id);

    @GET("/api/v1/users/{id}/invoices")
    Observable<List<Invoice>> user_invoices(@Path("id") int id);

    @GET("/api/v1/users/{id}/transactions")
    Observable<List<Transaction>> user_transactions(@Path("id") int id);

    @GET("/api/v1/transactions/{id}/invoices")
    Observable<InvoiceSet> transaction_invoices(@Path("id") String id);

    @GET("/api/v1/transactions/{id}")
    Observable<Transaction> transaction(@Path("id") String id);
}

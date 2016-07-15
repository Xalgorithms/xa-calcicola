package org.lichen.geghard.api;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface Endpoint {
    @GET("/api/v1/invoices/{id}")
    Call<Invoice> invoice(@Path("id") int id);
}

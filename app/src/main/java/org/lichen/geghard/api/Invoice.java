package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Invoice {
    public int id;
    public JsonObject document;

    public InvoiceDocument working_document() {
        return new InvoiceDocument(document);
    }
}

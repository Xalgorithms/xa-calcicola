package org.lichen.garni.data;

import com.google.common.collect.Maps;

import org.lichen.geghard.api.InvoiceDocument;

import java.util.Map;

public class Documents {
    private Map<String, InvoiceDocument> _documents = Maps.newConcurrentMap();

    public void add(InvoiceDocument doc) {
        _documents.put(doc.document_id(), doc);
    }

    public InvoiceDocument get(String document_id) {
        return _documents.get(document_id);
    }

    public boolean exists(String document_id) {
        return _documents.containsKey(document_id);
    }
}

package org.lichen.geghard.api;

import com.google.common.base.Function;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

public class InvoiceDocument extends Document {
    public InvoiceDocument(JsonObject o) {
        super(o);
    }

    public String id() {
        return string("id");
    }

    public Date issued() {
        return date("issued");
    }

    public Customer customer() {
        return new Customer(locate("parties.customer"));
    }

    public List<Line> lines() {
        return collection("lines", new Function<JsonElement, Line>() {
            @Override
            public Line apply(JsonElement o) {
                return new Line(o.getAsJsonObject());
            }
        });
    }

    public String format_total() {
        double total = 0.0;
        for (Line ln : lines()) {
            total += ln.price().amount();
        }
        return String.format("%f", total);
    }
}

package org.lichen.geghard.api;


import com.google.gson.JsonObject;

public class Price extends Document {
    public Price(JsonObject o) {
        super(o);
    }

    public double amount() {
        return double_value("amount");
    }

    public String format() {
        return format_currency(string("currency"), amount());
    }
}

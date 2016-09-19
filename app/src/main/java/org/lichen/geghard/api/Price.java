package org.lichen.geghard.api;


import com.google.gson.JsonObject;

public class Price extends Document {
    String _currency;

    public Price(JsonObject o, String currency) {
        super(o);
        _currency = currency;
    }

    public Price(JsonObject o) {
        super(o);
    }

    public String currency() {
        return string("currency");
    }

    public double amount() {
        return double_value("amount");
    }

    public String format() {
        return format_currency(null != _currency ? _currency : string("currency"), amount());
    }
}

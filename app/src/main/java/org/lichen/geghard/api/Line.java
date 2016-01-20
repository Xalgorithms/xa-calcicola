package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Line extends Document {
    public Line(JsonObject o) {
        super(o);
    }

    public Price price() {
        return new Price(locate("price"));
    }
}

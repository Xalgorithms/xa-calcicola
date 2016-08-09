package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Line extends Document {
    public Line(JsonObject o) {
        super(o);
    }

    public String name() {
        return Document.string(locate("item"), "name");
    }
    public Price price() {
        return new Price(locate("price"));
    }
    public TranslatedText description() { return new TranslatedText(locate("item.description")); }
}

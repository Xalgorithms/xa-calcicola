package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class ChangedLine extends Document {
    Line _line;

    public ChangedLine(JsonObject o, Line line) {
        super(o);
        _line = line;
    }

    public String name() {
        String over = null;
        JsonObject o = locate("item");
        if (null != o) {
            over = string(o, "name");
        }
        return null == over ? _line.name() : over;
    }

    public Price price() {
        Price over = null;
        Price under = _line.price();
        JsonObject o = locate("price");
        if (null != o) {
            over = new Price(o, under.currency());
        }

        return null == over ? under : over;
    }

    public TranslatedText description() {
        TranslatedText over = null;
        JsonObject o = locate("item.description");
        if (null != o) {
            over = new TranslatedText(o);
        }

        return null == over ? _line.description() : over;
    }
}

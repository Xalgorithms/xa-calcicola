package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class TranslatedText extends Document {
    public TranslatedText(JsonObject o) {
        super(o);
    }

    public String code() { return string("language"); }
    public String text() { return string("text"); }
}

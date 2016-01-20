package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Customer extends Document {
    public Customer(JsonObject o) {
        super(o);
    }

    public String name() {
        return string("name");
    }

    public String contact_name() {
        JsonObject o = locate("person.name");
        return String.format("%s %s", string(o, "first"), string(o, "family"));
    }
}

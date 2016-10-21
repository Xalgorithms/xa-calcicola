package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Person extends Document {
    public Person(JsonObject o) {
        super(o);
    }

    public String name() {
        String rv = contact_name();
        return null != rv ? rv : string("name");
    }

    public String city() { return string(locate("address"), "city"); }

    public String country() { return string(locate("address"), "country_code"); }

    public String contact_name() {
        JsonObject o = locate("person.name");
        return null != o ? String.format("%s %s", string(o, "first"), string(o, "family")) : null;
    }
}

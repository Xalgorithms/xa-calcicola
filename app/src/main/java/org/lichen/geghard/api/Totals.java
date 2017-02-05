package org.lichen.geghard.api;

import com.google.gson.JsonObject;

public class Totals extends Document {
    public Totals(JsonObject o) {
        super(o);
    }

    public Price payable() {
        return price("payable");
    }

    public Price total() {
        return price("total");
    }

    public Price tax_inclusive() {
        return price("tax_inclusive");
    }

    public Price tax_exclusive() {
        return price("tax_exclusive");
    }
}

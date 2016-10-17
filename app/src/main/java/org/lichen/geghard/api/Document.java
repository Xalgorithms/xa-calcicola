package org.lichen.geghard.api;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class Document {
    private final JsonObject _object;

    public Document(JsonObject o) {
        _object = o;
    }

    public static String string(JsonObject o, String k) {
        return o.get(k).getAsString();
    }

    public static String format_currency(String code, double amount) {
        // API24: would prefer CurrencyAmount but that's API24
        Currency c = Currency.getInstance(code);
        // API24: nf.setCurrency(c);

        String fmt = "0";
        String fracs = Strings.repeat("0", c.getDefaultFractionDigits());
        if (fracs.length() > 0) {
            fmt += "." + fracs;
        }
        DecimalFormat df = new DecimalFormat(fmt);
        return c.getSymbol() + df.format(amount);
    }

    public String string(String k) {
        return string(_object, k);
    }

    public Date date(String k) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date rv = null;
        try {
            rv = f.parse(string(k));
        } catch (ParseException e) {
        }
        return rv;
    }

    public Double double_value(String k) {
        return _object.get(k).getAsDouble();
    }

    public Person person(String k) {
        return new Person(locate(k));
    }

    public <T extends Document> List<T> collection(String k, Function<JsonElement, T> fn) {
        return Lists.transform(Lists.newArrayList(_object.getAsJsonArray(k)), fn);
    }

    public List<JsonObject> collection(String k) {
        return Lists.transform(
                Lists.newArrayList(_object.getAsJsonArray(k)),
                new Function<JsonElement, JsonObject>() {
                    @Override
                    public JsonObject apply(JsonElement el) {
                        return el.getAsJsonObject();
                    }
                });
    }

    protected JsonObject locate(String k) {
        return locate(_object, Lists.newArrayList(Splitter.on('.').split(k)));
    }

    private JsonObject locate(JsonObject o, List<String> path) {
        JsonObject rv = null;
        if (null != o && !path.isEmpty()) {
            JsonObject co = o.getAsJsonObject(path.get(0));
            if (1 == path.size()) {
                rv = co;
            } else {
                rv = locate(co, path.subList(1, path.size()));
            }
        }
        return rv;
    }
}

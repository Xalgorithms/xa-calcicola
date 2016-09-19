package org.lichen.geghard.api;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ChangeDocument {
    private static class InvoiceFilterDocument {
        private final Document _filter;
        private final InvoiceDocument _base;

        public InvoiceFilterDocument(JsonObject filter, InvoiceDocument base) {
            _filter = new Document(filter);
            _base = base;
        }

        public List<ChangedLine> lines() {
            List<JsonObject> filter_lines = _filter.collection("lines");
            List<Line> base_lines = _base.lines();
            List<ChangedLine> lines = Lists.newArrayList();

            for (int i = 0; i < base_lines.size(); i++) {
                JsonObject filter_line = null;
                if (i < filter_lines.size()) {
                    filter_line = filter_lines.get(i);
                }

                lines.add(new ChangedLine(filter_line, base_lines.get(i)));
            }

            return lines;
        }
    }

    public static class CombinedChangedLine {
        private final ChangedLine _previous;
        private final ChangedLine _latest;

        public CombinedChangedLine(ChangedLine previous, ChangedLine latest) {
            _previous = previous;
            _latest = latest;
        }

        public ChangedLine previous() {
            return _previous;
        }

        public ChangedLine latest() {
            return _latest;
        }
    }

    private InvoiceFilterDocument _previous;
    private InvoiceFilterDocument _latest;

    public ChangeDocument(InvoiceDocument invoice, JsonObject previous, JsonObject latest) {
        _previous = new InvoiceFilterDocument(previous, invoice);
        _latest = new InvoiceFilterDocument(latest, invoice);
    }

    public InvoiceFilterDocument previous() {
        return _previous;
    }

    public InvoiceFilterDocument latest() {
        return _latest;
    }

    public List<CombinedChangedLine> lines () {
        List<ChangedLine> previous = _previous.lines();
        List<ChangedLine> latest = _latest.lines();
        List<CombinedChangedLine> rv = Lists.newArrayList();

        for (int i = 0; i < previous.size(); i++) {
            rv.add(new CombinedChangedLine(previous.get(i), latest.get(i)));
        }

        return rv;
    }
}

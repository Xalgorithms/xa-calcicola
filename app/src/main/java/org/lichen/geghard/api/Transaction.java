package org.lichen.geghard.api;

import java.util.List;

public class Transaction {
    public class Association {
        class Rule {
            String id;
            String reference;
        }

        class Transformation {
            String id;
            String name;
        }

        Rule rule;
        Transformation transformation;
    }

    public String id;
    public String status;
    public List<Invoice> invoices;
    public List<Association> associations;
}

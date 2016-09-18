package org.lichen.geghard.api;

import java.util.List;

public class Invoice {
    public class Document {
        public String id;
        public String url;
    }

    public class Revision {
        public Document document;
    }

    public class Transaction {
        public String id;
    }

    public String id;
    public Transaction transaction;
    public List<Revision> revisions;
}


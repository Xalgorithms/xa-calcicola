package org.lichen.geghard.api;

import java.util.Date;
import java.util.List;

public class Invoice {
    int id;
    Date effective;
    Account account;
    List<Invocation> invocations;
}

package org.lichen.geghard.api;

import java.util.Date;
import java.util.List;

public class Invoice {
    public int id;
    public Date effective;
    public Account account;
    public List<Invocation> invocations;
}

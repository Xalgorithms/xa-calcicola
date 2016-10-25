package org.lichen.geghard.api;

public class Event {
    static class RegisterEvent {
        String token;
        String user_public_id;
    }

    static class TransactionExecuteEvent {
        String transaction_public_id;
    }

    String event_type;
    RegisterEvent register_event;
    TransactionExecuteEvent transaction_execute_event;

    public static Event make_register(String user_id, String token) {
        Event rv = make_basic("register");

        rv.register_event = new RegisterEvent();
        rv.register_event.user_public_id = user_id;
        rv.register_event.token = token;

        return rv;
    }

    public static Event make_execute(String transaction_id) {
        Event rv = make_basic("transaction_execute");

        rv.transaction_execute_event = new TransactionExecuteEvent();
        rv.transaction_execute_event.transaction_public_id = transaction_id;

        return rv;
    }

    private static Event make_basic(String t) {
        Event rv = new Event();
        rv.event_type = t;
        return rv;
    }
}

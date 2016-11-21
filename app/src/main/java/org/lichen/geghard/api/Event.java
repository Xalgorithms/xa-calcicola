package org.lichen.geghard.api;

public class Event<T> {
    String event_type;
    T payload;

    static class RegisterPayload {
        String token;
        String user_id;
    }

    static class TransactionExecutePayload {
        String transaction_id;
    }

    public static Event make_register(String user_id, String token) {
        Event<RegisterPayload> rv = new Event<>();
        rv.event_type = "register";
        rv.payload = new RegisterPayload();
        rv.payload.user_id = user_id;
        rv.payload.token = token;

        return rv;
    }

    public static Event make_execute(String transaction_id) {
        Event<TransactionExecutePayload> rv = new Event<>();
        rv.event_type = "transaction_execute";
        rv.payload = new TransactionExecutePayload();
        rv.payload.transaction_id = transaction_id;
        return rv;
    }
}

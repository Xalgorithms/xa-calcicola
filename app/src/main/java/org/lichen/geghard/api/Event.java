package org.lichen.geghard.api;

public class Event {
    static class RegisterEvent {
        String token;
        int user_id;
    }

    String event_type;
    RegisterEvent register_event;

    public static Event make_register(int user_id, String token) {
        Event rv = make_basic("register");

        rv.register_event = new RegisterEvent();
        rv.register_event.user_id = user_id;
        rv.register_event.token = token;

        return rv;
    }

    private static Event make_basic(String t) {
        Event rv = new Event();
        rv.event_type = t;
        return rv;
    }
}

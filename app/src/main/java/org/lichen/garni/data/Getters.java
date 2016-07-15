package org.lichen.garni.data;

import android.database.Cursor;

public class Getters {
    public static long get_long(Cursor c, String col) {
        return c.getLong(c.getColumnIndexOrThrow(col));
    }

    public static String get_string(Cursor c, String col) {
        return c.getString(c.getColumnIndexOrThrow(col));
    }
}

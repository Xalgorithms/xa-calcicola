package org.lichen.garni.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public OpenHelper(Context ctx) {
        super(ctx, "garni.db", null, VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
    }

    @Override public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {

    }
}

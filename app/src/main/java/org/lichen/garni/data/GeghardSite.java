package org.lichen.garni.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import auto.parcel.AutoParcel;
import rx.functions.Func1;

@AutoParcel
public abstract class GeghardSite implements Parcelable {
    public static final String TABLE = "garni_gehard_sites";
    public static final String COL_ID = "_id";
    public static final String COL_URL = "url";
    public static final String COL_LATEST = "latest";

    public static final String CREATE = ""
            + "CREATE TABLE " + TABLE + "("
            + COL_ID  + " INTEGER NOT NULL PRIMARY KEY, "
            + COL_URL + " TEXT NOT NULL UNIQUE, "
            + COL_LATEST + " INTEGER"
            + ")";
    public static final String Q_ALL = ""
            + "SELECT * FROM " + TABLE;
    public static final String Q_LATEST = ""
            + "SELECT * FROM " + TABLE
            + " WHERE latest=1";

    public abstract long id();
    public abstract String url();
    public abstract int latest();

    public static final Func1<Cursor, GeghardSite> FROM_CURSOR = new Func1<Cursor, GeghardSite>() {
        @Override
        public GeghardSite call(Cursor c) {
            return make(Getters.get_long(c, COL_ID), Getters.get_string(c, COL_URL), Getters.get_int(c, COL_LATEST));
        }
    };

    public static final GeghardSite make(long id, String url, int latest) {
        return new AutoParcel_GeghardSite(id, url, latest);
    }

    public static final class Maker {
        private final ContentValues _values = new ContentValues();

        public Maker id(long id) {
            _values.put(COL_ID, id);
            return this;
        }

        public Maker url(String url) {
            _values.put(COL_URL, url);
            return this;
        }

        public Maker latest(int latest) {
            _values.put(COL_LATEST, latest);
            return this;
        }

        public ContentValues make() {
            return _values;
        }
    }

    @Override
    public String toString() {
        return url();
    }
}

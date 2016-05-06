package org.lichen.garni.data;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module(complete = false, library = true)
public final class DataModule {
    @Provides @Singleton
    SQLiteOpenHelper provideOpenHelper(Application app) {
        return new OpenHelper(app);
    }

    @Provides @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String s) {
                Timber.tag("DB").v(s);
            }
        });
    }

    @Provides @Singleton
    BriteDatabase provideBriteDatabase(SqlBrite b, SQLiteOpenHelper h) {
        return b.wrapDatabaseHelper(h, Schedulers.io());
    }
}

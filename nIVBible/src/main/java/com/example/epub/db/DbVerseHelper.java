package com.example.epub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.epub.db.html.Verse;
import com.example.epub.selection.Selection;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class DbVerseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something
    // appropriate for your app
    public static final String DATABASE_NAME = "Verse.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    public static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private RuntimeExceptionDao<Verse, Integer> verseRuntimeDao = null;

    public DbVerseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.d(DbVerseHelper.class.getSimpleName(), "Create DB");
        try {
            TableUtils.createTable(connectionSource, Selection.class);
            TableUtils.createTable(connectionSource, Verse.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d(DbVerseHelper.class.getSimpleName(), "Upgrade DB");
        try {
            TableUtils.dropTable(connectionSource, Selection.class, true);
            TableUtils.dropTable(connectionSource, Verse.class, true);
//            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<Verse, Integer> getVerseRuntimeDao() {
        if (verseRuntimeDao == null) {
            verseRuntimeDao = getRuntimeExceptionDao(Verse.class);
        }
        return verseRuntimeDao;
    }


    public void addVerse(Verse verse) throws SQLException {
        getVerseRuntimeDao().createOrUpdate(verse);
    }

    public List<Verse> getAllVerses() throws SQLException {
        return getVerseRuntimeDao().queryForAll();
    }

    public List<Verse> searchVerses(String bookName, String keySearch) throws SQLException {
        QueryBuilder<Verse, Integer> queryBuilder = getVerseRuntimeDao().queryBuilder();
        Where<Verse, Integer> where = queryBuilder.where();

        where.eq(Verse.BOOK_NAME, bookName)
                .and().like(Verse.TEXT, "%" + keySearch + "%");

        PreparedQuery<Verse> preparedQuery = queryBuilder.prepare();
        return getVerseRuntimeDao().query(preparedQuery);
    }

    /**
     * Get all verses by book name
     */
    public List<Verse> getAllVerses(String bookName) throws SQLException {
        return getVerseRuntimeDao().queryForEq(Verse.BOOK_NAME, bookName);
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        verseRuntimeDao = null;
    }

}

package com.example.epub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.epub.db.html.Verse;
import com.example.epub.selection.Selection;
import com.example.epub.selection.SelectionColor;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.nivbible.R;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something
    // appropriate for your app
    public static final String DATABASE_NAME = "Book.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    public static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private RuntimeExceptionDao<Selection, Integer> selectionRuntimeDao = null;
    private RuntimeExceptionDao<Verse, Integer> verseRuntimeDao = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);

        DatabaseInitializer initializer = new DatabaseInitializer(context);
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.d(DbHelper.class.getSimpleName(), "Create DB");
//        try {
//            TableUtils.createTable(connectionSource, Selection.class);
//            TableUtils.createTable(connectionSource, Verse.class);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d(DbHelper.class.getSimpleName(), "Upgrade DB");
//        try {
//            TableUtils.dropTable(connectionSource, Selection.class, true);
//            TableUtils.dropTable(connectionSource, Verse.class, true);
//            // after we drop the old databases, we create the new ones
        onCreate(db, connectionSource);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao
     * for our SimpleData class. It will create it or just give the cached
     * value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Selection, Integer> getSelectionDataDao() {
        if (selectionRuntimeDao == null) {
            selectionRuntimeDao = getRuntimeExceptionDao(Selection.class);
        }
        return selectionRuntimeDao;
    }

    public RuntimeExceptionDao<Verse, Integer> getVerseRuntimeDao() {
        if (verseRuntimeDao == null) {
            verseRuntimeDao = getRuntimeExceptionDao(Verse.class);
        }
        return verseRuntimeDao;
    }

    public Selection getSelectionById(int id) throws SQLException {
        return getSelectionDataDao().queryForId(id);
    }

    public List<Selection> getAllNotes(String fileName) throws SQLException {
        QueryBuilder<Selection, Integer> queryBuilder = getSelectionDataDao().queryBuilder();
        Where<Selection, Integer> where = queryBuilder.where();

        where.isNotNull(Selection.COL_NOTE)
        .and().eq(Selection.COL_FILE_NAME, fileName);

        PreparedQuery<Selection> preparedQuery = queryBuilder.prepare();

        List<Selection> notes = getSelectionDataDao().query(preparedQuery);
        if (notes == null) {
            notes = new ArrayList<>();
        }
        return notes;
    }

    /**
     * Find selection bound the word in start/end position
     */
    public Selection findSelectionFromPosition(String fileName, int pageNumber, int start, int end) throws SQLException {
        QueryBuilder<Selection, Integer> queryBuilder = getSelectionDataDao().queryBuilder();
        Where<Selection, Integer> where = queryBuilder.where();
//        queryWhere.put(Selection.COL_PAGE, pageNumber);
//        queryWhere.put(Selection.COL_FILE_NAME, fileName);
        where.le(Selection.COL_START, start)
                .and().ge(Selection.COL_END, end)
                .and().eq(Selection.COL_PAGE, pageNumber)
                .and().eq(Selection.COL_FILE_NAME, fileName)
                .and().ne(Selection.COL_COLOR, SelectionColor.INITIAL);

        PreparedQuery<Selection> preparedQuery = queryBuilder.prepare();

        return getSelectionDataDao().queryForFirst(preparedQuery);
    }

    /**
     * Create or update existing selection
     *
     * @param selection Selection to be updated
     * @return id of selection is updated or created
     * @throws SQLException
     */
    public int createOrUpdateSelection(Selection selection) throws SQLException {
        QueryBuilder<Selection, Integer> queryBuilder = getSelectionDataDao().queryBuilder();
        Where<Selection, Integer> where = queryBuilder.where();

        // Check if already has a selection in range
        // Update color, text,note to db
        // Otherwise, create new selection
        where.eq(Selection.COL_START, selection.getStart())
                .and().eq(Selection.COL_END, selection.getEnd())
                .and().eq(Selection.COL_FILE_NAME, selection.getFileName())
                .and().eq(Selection.COL_PAGE, selection.getPageNumber());

        PreparedQuery<Selection> preparedQuery = queryBuilder.prepare();
        Selection foundSelection = getSelectionDataDao().queryForFirst(preparedQuery);

        if (foundSelection != null) {
            // If color of updating selection is initial, mean delete this selection
            if (selection.getColor().equals(SelectionColor.INITIAL)) {
                getSelectionDataDao().delete(foundSelection);
            } else {
                foundSelection.setColor(selection.getColor());
                foundSelection.setText(selection.getText());
                foundSelection.setNote(selection.getNote());
                getSelectionDataDao().update(foundSelection);
            }

            return foundSelection.getId();
        } else if (!selection.getColor().equals(SelectionColor.INITIAL)) {
            getSelectionDataDao().create(selection);
            return selection.getId();
        } else {
            return 0;
        }
    }

    public List<Selection> getByPage(String fileName, int pageNumber) throws SQLException {
        Map<String, Object> queryWhere = new HashMap<>();
        queryWhere.put(Selection.COL_PAGE, pageNumber);
        queryWhere.put(Selection.COL_FILE_NAME, fileName);
        return getSelectionDataDao().queryForFieldValues(queryWhere);

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
        selectionRuntimeDao = null;
        verseRuntimeDao = null;
    }

}

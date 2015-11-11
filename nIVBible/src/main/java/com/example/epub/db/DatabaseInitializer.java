package com.example.epub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Initialize the database by installing the application. It copies the existing
 * database from /assets folder to the device.
 *
 * @author neo.
 * @version 1
 */
public class DatabaseInitializer extends SQLiteOpenHelper {

    /**
     * See {@link SQLiteDatabase}
     */
    private SQLiteDatabase database;
    /**
     * See {@link Context}
     */
    private final Context context;

    /**
     * The Constructor
     *
     * @param context the {@link Context}
     */
    public DatabaseInitializer(Context context) {
        super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Coping the existing database to the device.
     *
     * @throws IOException
     */
    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            database = this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    /**
     * Checking the existing of the application database on the device
     *
     * @return true if the database is existed, else false
     */
    private boolean checkDatabase() {
//        SQLiteDatabase checkDB = null;
//        try {
//            String myPath = getDbPath() + DbHelper.DATABASE_NAME;
//            Log.e(DatabaseInitializer.class.getSimpleName(), "myPath" + myPath);
//            checkDB = SQLiteDatabase.openDatabase(myPath, null,
//                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//        } catch (Exception e) {
//            Log.e(DatabaseInitializer.class.getSimpleName(),
//                    "Could not open the database.", e);
//        }
//        if (checkDB != null) {
//            checkDB.close();
//        }
//
//        return checkDB != null;
        File dbFile = context.getDatabasePath(DbHelper.DATABASE_NAME);
        return dbFile != null && dbFile.exists();
    }

    /**
     * Copy the database from assets folder to the device
     *
     * @throws IOException
     */
    private void copyDatabase() throws IOException {
        InputStream myInput = context.getAssets().open(DbHelper.DATABASE_NAME);
        String outFileName = getDbPath() + DbHelper.DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private String getDbPath() {
        /*
      the locate of database on the device
     */
        String DB_PATH = "/data/data/%s/databases/";
        return String.format(DB_PATH, context.getPackageName());
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

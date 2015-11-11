package com.example.epub.db;

import com.example.epub.db.html.Verse;
import com.example.epub.selection.Selection;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * DatabaseConfigUtil
 * Created by neo on 10/28/2015.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final String CONFIG_FILE = "D:/Project/Amplified/AndroidStudioProject/nIVBible/src/main/res/raw/ormlite_config.txt";
    private static final Class<?>[] classes = new Class[]{
            Selection.class, Verse.class
    };

    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile(new File(CONFIG_FILE), classes);
    }
}

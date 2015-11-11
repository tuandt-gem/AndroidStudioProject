package com.example.EpubProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.EpubProject.utils.PageUTils;
import com.example.EpubProject.utils.PageUTilsBookName;
import com.example.EpubProject.utils.PageUTilsBookNumber;
import com.example.epub.db.html.Verse;
import com.example.epub.selection.Selection;

import nl.siegmann.epublib.domain.Resource;

/*
 * Assorted utility functions
 */
public class Utility {
    public static final String ERROR_STRING_ID_EXTRA = "ERROR_STRING_ID_EXTRA";

    public static void showToast(Context context, int stringId) {
        Toast msg = Toast.makeText(context, stringId, Toast.LENGTH_SHORT);
        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
                msg.getXOffset() / 2);
        msg.show();
    }

    public static void finishWithError(Activity activity, int stringId) {
        Intent intent = new Intent();
        intent.putExtra(ERROR_STRING_ID_EXTRA, stringId);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        activity.finish();
    }

    public static void showErrorToast(Context context, Intent intent) {
        if (intent != null) {
            int stringId = intent.getIntExtra(ERROR_STRING_ID_EXTRA, 0);
            if (stringId != 0) {
                showToast(context, stringId);
            }
        }
    }

    /*
     * Return path part of a filename
     */
    public static String extractPath(String fileName) {
        try {
            String path = new File(fileName).getCanonicalFile().getParent();
            // remove leading '/'
            return path == null ? "" : path.substring(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String concatPath(String basePath, String pathToAdd) {
        String rawPath = basePath + '/' + pathToAdd;
        if ((basePath == null) || basePath.isEmpty()
                || pathToAdd.startsWith("/")) {
            rawPath = pathToAdd;
        }
        try {
            return new File(rawPath).getCanonicalPath().substring(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Copy text to clipboard
     */
    public static void copyToClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
    }

    public static void writeToFile(String data, String filename) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
            if (file.exists())
                file.delete();

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.append(data);
            osw.close();
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static boolean isStringEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String formatNoteVerses(Selection selection) {
        return PageUTilsBookName.getEng(selection.getPageNumber()) + " " +
                PageUTilsBookNumber.getEng(selection.getPageNumber()) + ": "
                + selection.getStartVerseNumber() + "-" + selection.getEndVerseNumber();
    }

    public static String formatNoteChapter(Selection selection) {
        return PageUTilsBookName.getEng(selection.getPageNumber()) + " " +
                PageUTilsBookNumber.getEng(selection.getPageNumber());
    }

    public static String formatChapterVerses(Verse verse) {
//        return verse.getChapterTitle() + " " +
//                verse.getChapterNumber() + ": "
//                + verse.getVerseNumber();
        return verse.getChapterTitle() + " " +
                verse.getChapterNumber();
    }
}

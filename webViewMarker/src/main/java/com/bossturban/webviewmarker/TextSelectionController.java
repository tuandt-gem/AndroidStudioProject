package com.bossturban.webviewmarker;

import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextSelectionController {
    public static final String TAG = "TextSelectionController";
    public static final String INTERFACE_NAME = "TextSelection";

    private TextSelectionControlListener mListener;

    public TextSelectionController(TextSelectionControlListener listener) {
        mListener = listener;
    }

    @JavascriptInterface
    public void jsError(String error) {
        if (mListener != null) {
            mListener.jsError(error);
        }
    }

    @JavascriptInterface
    public void noteClicked(int id) {
        if (mListener != null) {
            mListener.noteClicked(id);
        }
    }

    @JavascriptInterface
    public void onSingleTouchWord(int start, int end) {
        if (mListener != null) {
            mListener.onSingleTouch(start, end);
        }
    }

    @JavascriptInterface
    public void notifyRangeCoords(int left, int top, int bottom, int right, int fontSize) {
        if (mListener != null) {
            mListener.notifyRangeCoords(left, top, bottom, right, fontSize);
        }
    }

    @JavascriptInterface
    public void jsLog(String message) {
        if (mListener != null) {
            mListener.jsLog(message);
        }
    }

    @JavascriptInterface
    public void startSelectionMode() {
        if (mListener != null) {
            mListener.startSelectionMode();
        }
    }

    @JavascriptInterface
    public void endSelectionMode() {
        if (mListener != null) {
            mListener.endSelectionMode();
        }
    }

    @JavascriptInterface
    public void saveSelection(int start, int end, int startVerseNumber, int endVerseNumber, String text, int fontSize) {
        if (mListener != null) {
            mListener.saveSelection(start, end, startVerseNumber, endVerseNumber, text, fontSize);
        }
    }

    @JavascriptInterface
    public void selectionChanged(String range, String text, int fontSize, String handleBounds, boolean isReallyChanged) {
        if (mListener != null) {
            mListener.selectionChanged(range, text, fontSize, handleBounds, isReallyChanged);
        }
    }

    @JavascriptInterface
    public void setContentWidth(float contentWidth) {
        if (mListener != null) {
            mListener.setContentWidth(contentWidth);
        }
    }

    @JavascriptInterface
    public void saveHtml(String html) {
        writeToFile(html, "a.html");
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
}

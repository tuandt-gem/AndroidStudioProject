package com.example.EpubProject.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class MakeExternalDirectoryFromAssets extends AsyncTask<String, Void, File> {

    private IOnCopyCompleteListener onCopyCompleteListener;
    private Context context;

    public MakeExternalDirectoryFromAssets(Context context, IOnCopyCompleteListener onCopyCompleteListener) {
        this.context = context;
        this.onCopyCompleteListener = onCopyCompleteListener;
    }

    public interface IOnCopyCompleteListener {
        void onCopyCompleted(File file, Uri uri);
    }

    @Override
    protected File doInBackground(String... params) {
        return copyAssets(context, params[0]);
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (onCopyCompleteListener != null)
            onCopyCompleteListener.onCopyCompleted(result, null);
    }

    public static File getDirectory() {
        return new File(Environment.getExternalStorageDirectory()
                + File.separator + "./data");
    }

    public File copyAssets(Context context, String filename) {
        File mydir = getDirectory();
        if (!mydir.exists())
            mydir.mkdirs();

        File outFile = new File(mydir, filename);
        if (outFile.exists())
            return outFile;

        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}

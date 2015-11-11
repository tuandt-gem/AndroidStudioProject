package com.example.EpubProject.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class FileMover {

	public static File copyAssets(Context context, String filename) {
		File outFile = null;
		AssetManager assetManager = context.getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			File mydir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "MyPdfTesting/");
			if (!mydir.exists())
				mydir.mkdirs();
			else
				Log.d("error", "dir. already exists");
			outFile = new File(mydir, filename);
			out = new FileOutputStream(outFile);
			copyFile(in, out);
		} catch (IOException e) {
			Log.e("tag", "Failed to copy asset file: " + filename, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// NOOP
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// NOOP
				}
			}
		}
		return outFile;
	}

	private static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}

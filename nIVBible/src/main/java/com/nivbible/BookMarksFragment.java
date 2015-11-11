package com.nivbible;

import java.io.File;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.adapter.ImageAdapter;
import com.example.EpubProject.MainActivity;
import com.example.EpubProject.utils.Constants;
import com.nivbible.R;

public class BookMarksFragment extends android.support.v4.app.Fragment
		implements OnClickListener, OnItemClickListener {

	static GridView gridView;
	Cursor imageCursor;
	ImageAdapter imgAdapter;
	private SharedPreferences preferencesBook;
	String name = null;
	ArrayList<String> fileName = new ArrayList<String>();

	public void moveWithBundle(String fileName) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.SELECTED_FILE, name);

		if (fileName.length() > 4) {
			try {
				File file = new File(fileName);
				String url = file.getName();
				bundle.putString(Constants.SELECTED_FILE, fileName);
				bundle.putString(Constants.FILE_URI, url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		((MainActivity) EpubReader.getActivityIsntanse())
				.moveToReaderFrament(bundle);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bookmarks_grid, null);
		fileName = new ArrayList<String>();
		imgAdapter = new ImageAdapter(this, fileName);
		preferencesBook = getActivity().getSharedPreferences("book", 0);
		name = PreferenceManager.getDefaultSharedPreferences(getActivity())
				.getString(Constants.SELECTED_FILE, Constants.AKUAPEM);

		Log.e("hello bookmark fg", "" + name);

		updateAdapters();
		GridView sdcardImages = (GridView) rootView
				.findViewById(R.id.bookmarks_grid);
		sdcardImages.setAdapter(imgAdapter);

		((MainActivity) EpubReader.getActivityIsntanse())
				.visibleBookMark(false);
		((MainActivity) EpubReader.getActivityIsntanse())
				.changeBookName("Bookmarks");
		/*
		 * ((MainActivity) EpubReader.getActivityIsntanse())
		 * .visiblechangeBookNumber(false);
		 */
		((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(true);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
		((MainActivity) EpubReader.getActivityIsntanse()).getActionBarView()
				.setOnItemClickListner(this);
		((MainActivity) EpubReader.getActivityIsntanse()).getActionBarView()
				.setOnItemClickListner(this);
		return rootView;
	}

	private void updateAdapters() {
		File imageDir = new File(Environment.getExternalStorageDirectory(),
				name);
		Log.e("", "media at this folder is" + imageDir);

		if (imageDir.isDirectory()) {
			fileName.clear();
			File[] mediaFiles = imageDir.listFiles();
			Log.e("", "List of files " + imageDir.length());
			for (int i = 0; i < mediaFiles.length; i++) {
				fileName.add(mediaFiles[i].getAbsolutePath());
			}

		}
		imgAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == 0) {
			preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
		} else {
			preferencesBook.edit().putInt("position", Constants.POS1_START).commit();
		}
		name = Constants.FILE_NAME[position];
		PreferenceManager
				.getDefaultSharedPreferences(getActivity())
				.edit()
				.putString(Constants.SELECTED_FILE,
						Constants.FILE_NAME[position]).commit();
		Log.e("Page number is", "Page number shown" + name);
		updateAdapters();

	}

	@Override
	public void onClick(View v) {

	}

//	private URL ConvertToUrl(String urlStr) {
//		try {
//			URL url = new URL(urlStr);
//			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
//					url.getHost(), url.getPort(), url.getPath(),
//					url.getQuery(), url.getRef());
//			url = uri.toURL();
//			return url;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

}

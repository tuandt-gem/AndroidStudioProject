package com.nivbible;

import com.example.EpubProject.MainActivity;
import com.nivbible.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpFragment extends android.support.v4.app.Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.help_fragment, null);

		EpubReader.getDrawerInstanse(EpubReader.getActivityIsntanse())
				.setSlidingEnabled(true);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(false);
		((MainActivity) EpubReader.getActivityIsntanse())
				.visibleBookMark(false);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
		((MainActivity) EpubReader.getActivityIsntanse())
				.changeBookName("Help");

		return rootView;
	}

}

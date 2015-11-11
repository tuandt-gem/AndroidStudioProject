package com.nivbible;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.EpubProject.MainActivity;
import com.nivbible.R;

public class ShareFragment extends android.support.v4.app.Fragment implements
		OnClickListener, OnItemClickListener {
	private EpubReader _eEpubReader;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.share_fragment, null);

		// _pdf_reader = (PdfViewer) parent
		// .findViewById(R.id.pdf_viewer_fragment_reader);
		EpubReader.getDrawerInstanse(EpubReader.getActivityIsntanse())
				.setSlidingEnabled(true);
		((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}

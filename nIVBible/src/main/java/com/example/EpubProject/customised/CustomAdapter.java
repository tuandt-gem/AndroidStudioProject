package com.example.EpubProject.customised;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.EpubProject.utils.Constants;
import com.nivbible.R;

public class CustomAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflator;
//	final String[] languages = { "NIV", "Amplified" };
	final String[] languages = Constants.FILE_NAME;

	public CustomAdapter(Context context) {
		this.mContext = context;
		this.inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return languages.length;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MainListHolder mHolder;

		View v = convertView;
		if (convertView == null) {
			mHolder = new MainListHolder();

			v = inflator.inflate(R.layout.screen_popup, null);

			mHolder.language = (TextView) v.findViewById(R.id.txtvw1);

			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();
		}
		mHolder.language.setText(languages[position]);

//		mHolder.language.setPadding(50, 30, 20, 20);
		return v;
	}
 
	class MainListHolder {
		private TextView language;
	}

}

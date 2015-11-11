package com.adapter;

import java.util.ArrayList;

import com.example.EpubProject.customised.SquareImageView;
import com.nivbible.BookMarksFragment;
import com.nivbible.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<String> fileName;
	private BookMarksFragment bookmarFragemnt;

	public ImageAdapter(BookMarksFragment context, ArrayList<String> fileName) {
		this.fileName = fileName;
		this.mInflater = LayoutInflater.from(context.getActivity());
		this.bookmarFragemnt = context;
	}

	public int getCount() {
		if (fileName == null)
			return 0;
		return fileName.size();
	}

	public String getItem(int position) {
		if (fileName == null)
			return "";
		return fileName.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bookmarks_screen, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap myBitmap = BitmapFactory.decodeFile(fileName.get(position));
		
		holder.imageview.getImageView().setImageBitmap(myBitmap);

		holder.imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				bookmarFragemnt.moveWithBundle(fileName.get(position));
				
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public final SquareImageView imageview;

		public ViewHolder(View convertView) {
			imageview = (SquareImageView) convertView
					.findViewById(R.id.thumbImage);

		}

	}
}

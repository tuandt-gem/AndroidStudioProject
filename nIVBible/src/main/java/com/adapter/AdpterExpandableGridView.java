package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nivbible.R;

public class AdpterExpandableGridView extends BaseAdapter implements IConstant {
	private LayoutInflater infalter;
	private int size;

	public AdpterExpandableGridView(Context applicationContext, int size) {
		this.infalter = LayoutInflater.from(applicationContext);
		this.size = size;

	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Integer getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View row, ViewGroup parent) {
		View convertView = row;
		final ViewHolder holder;
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.expandable_gridview,
					parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtvw_fontname.setText(Integer.toString(position + 1));
		return convertView;
	}

	private static class ViewHolder {
		final TextView txtvw_fontname;

		ViewHolder(View convertView) {
			txtvw_fontname = (TextView) convertView
					.findViewById(R.id.txtvw_fontname);
		}

	}

}

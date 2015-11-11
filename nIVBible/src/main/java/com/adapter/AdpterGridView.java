package com.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nivbible.R;

public class AdpterGridView extends BaseAdapter implements IConstant {
	private List<FontSizes> list;
	private LayoutInflater infalter;
	private SharedPreferences sharedPreferences;

	public AdpterGridView(Context applicationContext, List<FontSizes> list2) {

		sharedPreferences = applicationContext
				.getSharedPreferences("gameid", 0);
		Context applicationContext1 = applicationContext;
		this.infalter = LayoutInflater.from(applicationContext);
		this.list = list2;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public FontSizes getItem(int position) {
		return list.get(position);
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
			convertView = infalter.inflate(R.layout.gridview_menu, parent,
					false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.lnr_font_size.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!list.get(position).isFirstClick()) {
					holder.checkBox.setChecked(true);
					list.get(position).setFirstClick(true);
					sharedPreferences.edit()
							.putString("position", "" + position).commit();
				} else {
					holder.checkBox.setChecked(false);
					list.get(position).setFirstClick(false);
				}
			}
		});

		list.get(position)
				.setFirstClick(
						list.get(position)
								.getFontId()
								.contains(
										sharedPreferences.getString("position",
												"dfsf")));

		holder.checkBox.setChecked(list.get(position).isFirstClick());
		holder.txtvw_fontname.setText(list.get(position).getFontName());

		return convertView;
	}

	private static class ViewHolder {
		final CheckBox checkBox;
		final LinearLayout lnr_font_size;
		final TextView txtvw_fontname;

		ViewHolder(View convertView) {
			checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			lnr_font_size = (LinearLayout) convertView
					.findViewById(R.id.lnr_font_size);
			txtvw_fontname = (TextView) convertView
					.findViewById(R.id.txtvw_fontname);
		}

	}

}

package com.grid.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nivbible.R;

public class AdapterItems extends BaseAdapter {

	private LayoutInflater _infaltor;

	private List<PageList> list;

	public AdapterItems(Context context) {
		_infaltor = LayoutInflater.from(context);

		// init list here
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = _infaltor.inflate(R.layout.row_item, null);
		TextView _txt = (TextView) view.findViewById(R.id.txt_title);
		String title = list.get(position).title;

		LinearLayout _linear = (LinearLayout) view.findViewById(R.id.parent);
		_txt.setText(title);

		_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				list.get(position).isChecked = !list.get(position).isChecked;
				notifyDataSetChanged();
			}
		});

		if (list.get(position).isChecked) {
			if (_linear.getChildCount() > 0) {
				_linear.removeAllViews();
				notifyDataSetChanged();
			}
			int numberOfCount = list.get(position).count;
			if (numberOfCount % 5 == 0) {
				numberOfCount = numberOfCount / 5;
			} else {
				numberOfCount = (numberOfCount / 5) + 1;
			}
			for (int i = 0; i < numberOfCount; i++) {
				View grdItem = _infaltor.inflate(R.layout.child_row, null);
				GridItem item = (GridItem) grdItem.findViewById(R.id.grid_item);
				for (int j = 0; j < 5; j++) {
					int text = i * 5 + 1 + j;
					item.getTextViewAt(j).setText("" + text);
					if (text > list.get(position).count) {
						item.getTextViewAt(j).setVisibility(View.INVISIBLE);
					}
				}
			}
		} else {
			if (_linear.getChildCount() > 0) {
				_linear.removeAllViews();
				notifyDataSetChanged();
			}
		}
		return view;
	}
}

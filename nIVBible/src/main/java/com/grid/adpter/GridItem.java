package com.grid.adpter;

import com.nivbible.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridItem extends LinearLayout {

	public GridItem(Context context) {
		super(context);
		initViews();
	}

	public GridItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public GridItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {
		setOrientation(HORIZONTAL);
		for (int i = 0; i < 5; i++) {
			TextView _txt = new TextView(getContext());
			_txt.setTextSize(20);
			_txt.setGravity(Gravity.CENTER);
			_txt.setTextColor(Color.parseColor("#ffffff"));
			_txt.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_boader));
			LayoutParams params = new LayoutParams(0, (int) getMessuredHeight());
			params.weight = 1;
			_txt.setLayoutParams(params);
			addView(_txt);
		}
	}

	private float getMessuredHeight() {
		int width = getResources().getDisplayMetrics().widthPixels;
		return width / 5;
	}

	public TextView getTextViewAt(int index) {
		if (getChildCount() > index) {
			TextView txt = (TextView) getChildAt(index);
			return txt;
		}
		return null;
	}

}

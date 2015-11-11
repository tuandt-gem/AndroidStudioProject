package com.example.EpubProject.customised;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nivbible.R;


public class SquareImageView extends LinearLayout {
	private ImageView imgveiw;

	public SquareImageView(Context context) {
		super(context);
		initView();
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {

		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int final_width = screenWidth / 2;
		LayoutParams params = new LayoutParams(final_width, final_width + 20);
		params.gravity = Gravity.CENTER;
		RelativeLayout relative = new RelativeLayout(getContext());
		relative.setLayoutParams(params);
		initImageView(relative);
		initMarkView(relative, final_width);
		addView(relative);
	}

	private void initImageView(RelativeLayout relative) {
		android.widget.RelativeLayout.LayoutParams imgParam = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		imgveiw = new ImageView(getContext());
		imgveiw.setLayoutParams(imgParam);
		imgveiw.setScaleType(ScaleType.CENTER_INSIDE);
		relative.addView(imgveiw);
	}

	private void initMarkView(RelativeLayout relative, int totalWidh) {
		android.widget.RelativeLayout.LayoutParams imgParam = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.bookmark_nav_icon_selected);

		imgveiw.setPadding(0, bm.getHeight() / 5, 0, 0);

		imgParam.setMargins(totalWidh - 3 * bm.getWidth(), 0, 0, 0);
		imgParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ImageView imgveiw = new ImageView(getContext());
		imgveiw.setLayoutParams(imgParam);
		imgveiw.setImageResource(R.drawable.bookmark_nav_icon_selected);
		relative.addView(imgveiw);
	}

	public ImageView getImageView() {
		return imgveiw;
	}
}

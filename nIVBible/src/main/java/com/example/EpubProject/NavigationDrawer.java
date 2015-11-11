package com.example.EpubProject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.nivbible.R;

public class NavigationDrawer extends RelativeLayout implements
		View.OnClickListener {

	private IOnDrawerListner _on_drawer_listner;
	private ArrayList<View> _drawer_items;
	private int _current_option_index = 0;

	public NavigationDrawer(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.drawer_layout, this, true);
		initControls();
	}

	private void initControls() {
		_drawer_items = new ArrayList<View>();
		_drawer_items.add(findViewById(R.id.lnr_bible_reader));
		_drawer_items.add(findViewById(R.id.lnr_bible_bookmark));
		_drawer_items.add(findViewById(R.id.lnr_bible_share));
		_drawer_items.add(findViewById(R.id.lnr_bible_notes));
		_drawer_items.add(findViewById(R.id.lnr_bible_font_size));
		_drawer_items.add(findViewById(R.id.lnr_bible_help));

		for (int i = 0; i < _drawer_items.size(); i++)
			_drawer_items.get(i).setOnClickListener(this);
		_drawer_items.get(_current_option_index).setBackgroundResource(
				R.drawable.com_bible_drawable_drawer_item_selected);
	}

	public void select(int index) {
		for (int i = 0; i < _drawer_items.size(); i++)
			_drawer_items.get(i).setBackgroundResource(
					R.drawable.com_bible_drawable_drawer_item_selected);
		_current_option_index = index;
		_drawer_items.get(_current_option_index).setBackgroundResource(
				R.drawable.com_bible_drawable_drawer_item_selected);
	}

	public void setOnDrawerItemClickListner(IOnDrawerListner aMgr) {
		_on_drawer_listner = aMgr;
	}

	@Override
	public void onClick(View view) {

		setDrawerView(view);
	}

	public void setDrawerView(View view) {
		if (onSliderListner != null)
			onSliderListner.onSlide();
		for (int i = 0; i < _drawer_items.size(); i++) {
			if (view.getId() == _drawer_items.get(i).getId()) {
				// if (i != _current_option_index) {
				_on_drawer_listner.onDrawerItemClick(view);
				_current_option_index = i;
				// }
				view.setBackgroundResource(R.drawable.com_bible_drawable_drawer_item_selected);
			} else {
				_drawer_items.get(i).setBackgroundResource(
						R.drawable.com_bible_drawable_drawer_item_selected);
			}
		}
	}

	public void setSelectionByResourceId(int resourceId) {
		if (onSliderListner != null)
			onSliderListner.onSlide();
		for (int i = 0; i < _drawer_items.size(); i++) {
			if (resourceId == _drawer_items.get(i).getId()) {
				_on_drawer_listner.onDrawerItemClick(_drawer_items.get(i));
				_drawer_items.get(i).setBackgroundResource(
						R.drawable.com_bible_drawable_drawer_item_selected);
				_current_option_index = i;
			} else {
				_drawer_items.get(i).setBackgroundResource(
						R.drawable.com_bible_drawable_drawer_item_selected);
			}
		}
	}

	/**
	 * @author Wild Coder
	 * @param currentScreenTag
	 *            the currentScreenTag to set
	 */
	public void setCurrentScreenTag(int currentScreenTag) {
		this._current_option_index = currentScreenTag;
	}

	public interface IOnDrawerListner {
		public void onDrawerItemClick(View view);
	}

	private IOnSliderListner onSliderListner;

	public void setOnSlideListner(IOnSliderListner onSliderListner) {
		this.onSliderListner = onSliderListner;
	}

	public interface IOnSliderListner {
		public void onSlide();
	}
}

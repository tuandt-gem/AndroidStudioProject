package com.grid.adpter;

public class PageList {

	String title;
	int count;
	boolean isChecked = false;

	public PageList(String title, int count, boolean isChecked) {
		this.title = title;
		this.count = count;
		this.isChecked = isChecked;
	}
}

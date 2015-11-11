package com.example.EpubProject.utils;

public class ListPage {
	public String name;
	public int pageCount;
	private boolean isChecked;

	public ListPage(String name, int pageCount, boolean isChecked) {
		this.name = name;
		this.pageCount = pageCount;
		this.setChecked(isChecked);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}

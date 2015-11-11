package com.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class FontSizes implements Parcelable {

	private String fontSize;
	private String fontId;
	private String fontName;
	private boolean isFirstClick;

	public FontSizes() {

	}

	public FontSizes(Parcel in) {

		this.setFontId(in.readString());
		this.setFontSize(in.readString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getFontId());
		dest.writeString(this.getFontSize());
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontId() {
		return fontId;
	}

	public void setFontId(String fontId) {
		this.fontId = fontId;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public boolean isFirstClick() {
		return isFirstClick;
	}

	public void setFirstClick(boolean isFirstClick) {
		this.isFirstClick = isFirstClick;
	}

	public static final Parcelable.Creator<FontSizes> CREATOR = new Parcelable.Creator<FontSizes>() {
		public FontSizes createFromParcel(Parcel in) {
			return new FontSizes(in);
		}

		public FontSizes[] newArray(int size) {
			return new FontSizes[size];
		}
	};

}

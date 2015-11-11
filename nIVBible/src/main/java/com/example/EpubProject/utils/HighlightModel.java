package com.example.EpubProject.utils;

import com.google.gson.Gson;

public class HighlightModel {
	public String language;
	public int page;
	public String phHighlightClassId;
	public String html;
	public String color;
	
	public HighlightModel(String hLanguage, int hPage, String phHighlightId, String htmlSentence, String hColor) {
		this.language = hLanguage;
		this.page = hPage;
		this.phHighlightClassId = phHighlightId;
		this.html = htmlSentence;
		this.color = hColor;
	}
	
	public static HighlightModel fromString(String json) {
		return new Gson().fromJson(json, HighlightModel.class);
	}
	public String toString () {
		return new Gson().toJson(this);
	}
}

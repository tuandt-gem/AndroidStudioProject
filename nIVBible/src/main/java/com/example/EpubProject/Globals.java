package com.example.EpubProject;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.EpubProject.utils.Constants;
import com.example.EpubProject.utils.HighlightModel;

public class Globals {
    public static final String TAG = "SimpleEpubViewer";
    public static final String UTF8 = "UTF-8";
    public static final int WEB_SERVER_PORT = 1025;
    
	public Context	context;
	public ArrayList<HighlightModel> highlightArray;
	String key = "highlightArray";

	public static final int PREF_MODE 				= Context.MODE_PRIVATE;
	public static final String PREF_NAME 			= "TwiHighlgiht";

    public Globals(Context context) {
    	this.context = context;
    	highlightArray = new ArrayList<HighlightModel>();
    	
    	load();
    }
    public void load() {
		highlightArray.clear();
		
		String strHighlights = readPrefString(context, key, "");
		if ( TextUtils.isEmpty(strHighlights) )
			return;
		
		try {
			JSONArray jsonArray = new JSONArray(strHighlights);
			for ( int i = 0 ; i < jsonArray.length() ; i++ ) {
				String strHighlight = jsonArray.getString(i);
				highlightArray.add(HighlightModel.fromString(strHighlight));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		JSONArray jsonArray = new JSONArray();
		for ( HighlightModel aHighlight : highlightArray) {
			jsonArray.put(aHighlight.toString());
		}
		savePrefString(context, key, jsonArray.toString());
	}
	
	public void saveHighlight(int page, String hightlightId, String html, String color) {
		HighlightModel existHighlight = null;
		String currentLanguage = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(Constants.SELECTED_FILE, Constants.AKUAPEM);
 		for ( HighlightModel aHighlight : highlightArray) {
			if (aHighlight.language.equals(currentLanguage) && aHighlight.page == page && aHighlight.phHighlightClassId.equals(hightlightId)) {
				existHighlight = aHighlight;
				break;
			}
		}
 		
		if (existHighlight != null) {
			existHighlight.html = html;
			existHighlight.color = color;
		} else {
			highlightArray.add(new HighlightModel(currentLanguage, page, hightlightId, html, color));
		}
 		save();
	}
	
	public void removeHighlight(int page, String hightlightId) {
		HighlightModel existHighlight = null;
		String currentLanguage = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(Constants.SELECTED_FILE, Constants.AKUAPEM);
 		for ( HighlightModel aHighlight : highlightArray) {
			if (aHighlight.language.equals(currentLanguage) && aHighlight.page == page && aHighlight.phHighlightClassId.equals(hightlightId)) {
				existHighlight = aHighlight;
				break;
			}
		}
 		
 		if (existHighlight != null) {
 			highlightArray.remove(existHighlight);
 	 		save();
 		} else {
 			Log.e("TWI", "Cann't find the highlight");
 		}
	}
	
	public static String readPrefString(Context context,String key,String defValue) {
		int mode = PREF_MODE;
		String name = PREF_NAME;
		String result = "";
		SharedPreferences mySharedPreferences =context.getSharedPreferences(name, mode);
		if(mySharedPreferences != null){
			result = mySharedPreferences.getString(key, defValue);
		}
		return result;
	}
    
    public static void savePrefString(Context context, String key, String val)
	{
		int mode = PREF_MODE;
		String name = PREF_NAME;
		SharedPreferences mySharedPreferences = context.getSharedPreferences(name, mode);
		if(mySharedPreferences != null){	
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString(key, val);
			editor.commit();  
		}
	}
}

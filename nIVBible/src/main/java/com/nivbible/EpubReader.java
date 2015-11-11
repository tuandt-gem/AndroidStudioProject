package com.nivbible;

import com.crashlytics.android.Crashlytics;
import com.example.EpubProject.DrawerIntializer;
import com.example.EpubProject.Globals;
import com.example.EpubProject.utils.Constants;
import com.facebook.FacebookSdk;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import io.fabric.sdk.android.Fabric;

public class EpubReader extends Application {

	private static FlowOrganizer _flow;
	private static DrawerIntializer _drawer;
	private static FragmentActivity _activity;
	private static SharedPreferences preferencesBook;
	private static RelativeLayout layout;

	Globals mGlobals;

	public static void initActivity(FragmentActivity _activity) {
		EpubReader._activity = _activity;
		preferencesBook = _activity.getSharedPreferences("book", 0);
	}

	public static FragmentActivity getActivityIsntanse() {
		return _activity;
	}

	public static FlowOrganizer getFlowInstanse() {
		if (_flow == null) {
			_flow = new FlowOrganizer(_activity, R.id.framge_parent);
		}
		return _flow;
	}

	public static DrawerIntializer getDrawerInstanse(Activity activty) {
		if (_drawer == null) {
			_drawer = new DrawerIntializer(activty);
		}
		return _drawer;
	}

	public static void onDestroy() {
		_activity = null;
		_flow = null;
		preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
		Log.e("ddestro", "Application");

	}

	public static RelativeLayout getLayout() {
		return layout;
	}

	public static void setLayout(RelativeLayout layout) {
		EpubReader.layout = layout;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		// Facebook SDK
		FacebookSdk.sdkInitialize(getApplicationContext());

		mGlobals = new Globals(this);
	}

	public Globals getGlobals() {
		return mGlobals;
	}

}

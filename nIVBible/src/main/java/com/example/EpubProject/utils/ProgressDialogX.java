package com.example.EpubProject.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogX extends ProgressDialog {
	public ProgressDialogX(Context context) {
		super(context);
	}

	private boolean mCancelled = false;

	public boolean isCancelled() {
		return mCancelled;
	}

	@Override
	public void cancel() {
		mCancelled = true;
		super.cancel();
	}
}

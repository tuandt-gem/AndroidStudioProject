package com.example.EpubProject;

import android.net.Uri;

public interface IResourceSource {
	/*
	 * Fetch the requested resource
	 */
	public ResourceResponse fetch(Uri resourceUri);
}

package com.adapter;

import java.nio.charset.Charset;

import android.util.Base64;

public class Base64EEncoder {
	public static String base64Encode(String token) {
		byte[] encodedBytes = Base64.encode(token.getBytes(), Base64.DEFAULT);
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

	public static String base64Decode(String token) {
		byte[] decodedBytes = Base64.decode(token.getBytes(), Base64.DEFAULT);
		return new String(decodedBytes, Charset.forName("UTF-8"));
	}

}

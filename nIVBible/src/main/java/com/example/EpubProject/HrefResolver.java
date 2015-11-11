package com.example.EpubProject;

/*
 * Converts relative href to absolute
 */
public class HrefResolver {
	/*
	 * path to file holding the href
	 */
	private String mParentPath;

	public HrefResolver(String parentFileName) {
		mParentPath = Utility.extractPath(parentFileName);
	}

	public String ToAbsolute(String relativeHref) {
		return Utility.concatPath(mParentPath, relativeHref);
	}

	public String getParentPath() {

		return mParentPath;
	}

}

package com.example.EpubProject.utils;

import android.util.Log;

public class PageUTilsBookName {

	public enum LanguageTypeBookName {
		ENGLISHBOOK, EQBOOK;
	}

	public static String getPageCountBook(LanguageTypeBookName languageType,
			int pageNumer) {

		switch (languageType) {
		case ENGLISHBOOK:
			return getEng(pageNumer);
		case EQBOOK:
			return getEq(pageNumer);
		default:
			break;
		}
		return "";
	}

	public static String getEng(int pageNumber) {
		Log.e(PageUTilsBookName.class.getSimpleName(), "pageNumber=" + pageNumber);
		pageNumber -= Constants.POS1_START;
		Log.e("after book", "" + pageNumber);
		if (0 <= pageNumber && pageNumber < 50) {
			return "Genesis";
		}
		if (50 <= pageNumber && pageNumber < 90) {
			return "Exodus";
		}
		if (90 <= pageNumber && pageNumber < 117) {
			return "Leviticus";
		}
		if (117 <= pageNumber && pageNumber < 153) {
			return "Numbers";
		}
		if (153 <= pageNumber && pageNumber < 187) {
			return "Deuteronomy";
		}
		if (187 <= pageNumber && pageNumber < 211) {
			return "Joshua";
		}
		if (211 <= pageNumber && pageNumber < 232) {
			return "Judges";
		}
		if (232 <= pageNumber && pageNumber < 236) {
			return "Ruth";
		}
		if (236 <= pageNumber && pageNumber < 267) {
			return "1 Samuel";
		}
		if (267 <= pageNumber && pageNumber < 291) {
			return "2 Samuel";
		}
		if (291 <= pageNumber && pageNumber < 313) {
			return "1 Kings";
		}
		if (313 <= pageNumber && pageNumber < 338) {
			return "2 Kings";
		}
		if (338 <= pageNumber && pageNumber < 367) {
			return "1 Chronicles";
		}
		if (367 <= pageNumber && pageNumber < 403) {
			return "2 Chronicles";
		}
		if (403 <= pageNumber && pageNumber < 413) {
			return "Ezra";
		}
		if (413 <= pageNumber && pageNumber < 426) {
			return "Nehemiah";
		}

		if (426 <= pageNumber && pageNumber < 436) {

			return "Esther";

		}
		if (436 <= pageNumber && pageNumber < 478) {
			return "Job";
		}
		if (478 <= pageNumber && pageNumber < 628) {
			return "Psalms";
		}
		if (628 <= pageNumber && pageNumber < 659) {
			return "Proverbs";
		}
		if (659 <= pageNumber && pageNumber < 671) {
			return "Ecclesiastes";
		}

		if (671 <= pageNumber && pageNumber < 679) {
			return "Song Of Solomon";
		}

		if (679 <= pageNumber && pageNumber < 745) {
			return "Isaiah";
		}
		if (745 <= pageNumber && pageNumber < 797) {
			return "Jeremiah";
		}
		if (797 <= pageNumber && pageNumber < 802) {
			return "Lamentations";
		}
		if (802 <= pageNumber && pageNumber < 850) {
			return "Ezekiel";
		}
		if (850 <= pageNumber && pageNumber < 862) {
			return "Daniel";
		}
		if (862 <= pageNumber && pageNumber < 876) {
			return "Hosea";
		}
		if (876 <= pageNumber && pageNumber < 879) {
			return "Joel";
		}

		if (879 <= pageNumber && pageNumber < 888) {
			return "Amos";
		}
		if (888 <= pageNumber && pageNumber < 889) {
			return "Obadiah";

		}
		if (889 <= pageNumber && pageNumber < 893) {
			return "Jonah";
		}
		if (893 <= pageNumber && pageNumber < 900) {
			return "Micah";
		}
		if (900 <= pageNumber && pageNumber < 903) {
			return "Nahum";
		}
		if (903 <= pageNumber && pageNumber < 906) {
			return "Habakkuk";
		}
		if (906 <= pageNumber && pageNumber < 909) {
			return "Zephaniah";
		}
		if (909 <= pageNumber && pageNumber < 911) {
			return "Haggai";
		}
		if (911 <= pageNumber && pageNumber < 925) {
			return "Zechariah";
		}
		if (925 <= pageNumber && pageNumber < 929) {
			return "Malachi";
		}

		if (929 <= pageNumber && pageNumber < 957) {
			return "Matthew";
		}
		if (957 <= pageNumber && pageNumber < 973) {
			return "Mark";
		}
		if (973 <= pageNumber && pageNumber < 997) {
			return "Luke";
		}
		if (997 <= pageNumber && pageNumber < 1018) {
			return "John";
		}
		if (1018 <= pageNumber && pageNumber < 1046) {
			return "Acts";
		}
		if (1046 <= pageNumber && pageNumber < 1062) {
			return "Romans";
		}
		if (1062 <= pageNumber && pageNumber < 1078) {
			return "1 Corinthians";
		}
		if (1078 <= pageNumber && pageNumber < 1091) {
			return "2 Corinthians";
		}
		if (1091 <= pageNumber && pageNumber < 1097) {
			return "Galatians";

		}
		if (1097 <= pageNumber && pageNumber < 1103) {
			return "Ephesians";
		}
		if (1103 <= pageNumber && pageNumber < 1107) {
			return "Philippians";
		}
		if (1107 <= pageNumber && pageNumber < 1111) {
			return "Colossians";
		}
		if (1111 <= pageNumber && pageNumber < 1116) {
			return "1 Thessalonians";
		}
		if (1116 <= pageNumber && pageNumber < 1119) {
			return "2 Thessalonians";
		}
		if (1119 <= pageNumber && pageNumber < 1125) {
			return "1 Timothy";
		}
		if (1125 <= pageNumber && pageNumber < 1129) {
			return "2 Timothy";
		}
		if (1129 <= pageNumber && pageNumber < 1132) {
			return "Titus";
		}
		if (1132 <= pageNumber && pageNumber < 1133) {
			return "Philemon";
		}
		if (1133 <= pageNumber && pageNumber < 1146) {
			return "Hebrews";
		}
		if (1146 <= pageNumber && pageNumber < 1151) {
			return "James";
		}
		if (1151 <= pageNumber && pageNumber < 1156) {
			return "1 Peter";
		}
		if (1156 <= pageNumber && pageNumber < 1159) {
			return "2 Peter";
		}
		if (1159 <= pageNumber && pageNumber < 1164) {
			return "1 John";
		}
		if (1164 <= pageNumber && pageNumber < 1165) {
			return "2 John";
		}
		if (1165 <= pageNumber && pageNumber < 1166) {
			return "3 John";
		}
		if (1166 <= pageNumber && pageNumber < 1167) {
			return "Jude";
		}
		if (1167 <= pageNumber) {
			return "Revelation";
		}
		return "";
	}

	public static String getEq(int pageNumber) {
		Log.e("book", "" + pageNumber);
		pageNumber -= Constants.POS0_START;
		Log.e("after book", "" + pageNumber);
		if (0 <= pageNumber && pageNumber < 50) {
			return "Genesis";

		}
		if (50 <= pageNumber && pageNumber < 90) {
			return "Exodus";
		}
		if (90 <= pageNumber && pageNumber < 117) {
			return "Leviticus";
		}
		if (117 <= pageNumber && pageNumber < 153) {
			return "Numbers";
		}
		if (153 <= pageNumber && pageNumber < 187) {
			return "Deuteronomy";
		}
		if (187 <= pageNumber && pageNumber < 211) {
			return "Joshua";
		}
		if (211 <= pageNumber && pageNumber < 232) {
			return "Judges";
		}
		if (232 <= pageNumber && pageNumber < 236) {
			return "Ruth";
		}
		if (236 <= pageNumber && pageNumber < 267) {
			return "1 Samuel";
		}
		if (267 <= pageNumber && pageNumber < 291) {
			return "2 Samuel";
		}
		if (291 <= pageNumber && pageNumber < 313) {
			return "1 Kings";
		}
		if (313 <= pageNumber && pageNumber < 338) {
			return "2 Kings";
		}
		if (338 <= pageNumber && pageNumber < 367) {
            return "1 Chronicles";
		}
		if (367 <= pageNumber && pageNumber < 403) {
            return "2 Chronicles";
		}
		if (403 <= pageNumber && pageNumber < 413) {
			return "Ezra";
		}
		if (413 <= pageNumber && pageNumber < 426) {
			return "Nehemiah";
		}

		if (426 <= pageNumber && pageNumber < 436) {
			return "Esther";
		}
		if (436 <= pageNumber && pageNumber < 478) {
			return "Job";
		}
		if (478 <= pageNumber && pageNumber < 628) {
			return "Psalms";
		}
		if (628 <= pageNumber && pageNumber < 659) {
            return "Proverbs";
		}
		if (659 <= pageNumber && pageNumber < 671) {
            return "Ecclesiastes";
		}

		if (671 <= pageNumber && pageNumber < 679) {
			return "Song of Solomon";
		}

		if (679 <= pageNumber && pageNumber < 745) {

			return "Isaiah";

		}
		if (745 <= pageNumber && pageNumber < 797) {
			return "Jeremiah";
		}
		if (797 <= pageNumber && pageNumber < 802) {
			return "Lamentations";
		}
		if (802 <= pageNumber && pageNumber < 850) {
			return "Ezekiel";
		}
		if (850 <= pageNumber && pageNumber < 862) {
			return "Daniel";
		}
		if (862 <= pageNumber && pageNumber < 876) {
			return "Hosea";
		}
		if (876 <= pageNumber && pageNumber < 879) {
			return "Joel";
		}

		if (879 <= pageNumber && pageNumber < 888) {
			return "Amos";
		}
		if (888 <= pageNumber && pageNumber < 889) {
			return "Obadiah";
		}
		if (889 <= pageNumber && pageNumber < 893) {
			return "Jona";
		}
		if (893 <= pageNumber && pageNumber < 900) {
			return "Micah";
		}
		if (900 <= pageNumber && pageNumber < 903) {
			return "Nahum";
		}
		if (903 <= pageNumber && pageNumber < 906) {
			return "Habakkuk";
		}
		if (906 <= pageNumber && pageNumber < 909) {
			return "Zephaniah";
		}
		if (909 <= pageNumber && pageNumber < 911) {
			return "Haggai";
		}
		if (911 <= pageNumber && pageNumber < 925) {
			return "Zechariah";
		}
		if (925 <= pageNumber && pageNumber < 929) {
			return "Malachi";
		}

		if (929 <= pageNumber && pageNumber < 957) {
			return "Matthew";
		}
		if (957 <= pageNumber && pageNumber < 973) {
			return "Mark";
		}
		if (973 <= pageNumber && pageNumber < 997) {
			return "Luke";

		}
		if (997 <= pageNumber && pageNumber < 1018) {
			return "John";
		}
		if (1018 <= pageNumber && pageNumber < 1046) {
			return "Acts";
		}
		if (1046 <= pageNumber && pageNumber < 1062) {
			return "Romans";
		}
		if (1062 <= pageNumber && pageNumber < 1078) {
			return "1 Corinthians";
		}
		if (1078 <= pageNumber && pageNumber < 1091) {
			return "2 Corinthians";
		}
		if (1091 <= pageNumber && pageNumber < 1097) {
			return "Galatians";
		}
		if (1097 <= pageNumber && pageNumber < 1103) {
			return "Ephesians";
		}
		if (1103 <= pageNumber && pageNumber < 1107) {
			return "Philippians";
		}
		if (1107 <= pageNumber && pageNumber < 1111) {
			return "Colossians";
		}
		if (1111 <= pageNumber && pageNumber < 1116) {
			return "1 Thessalonians";
		}
		if (1116 <= pageNumber && pageNumber < 1119) {
			return "2 Thessalonians";
		}
		if (1119 <= pageNumber && pageNumber < 1125) {
			return "1 Timothy";
		}
		if (1125 <= pageNumber && pageNumber < 1129) {
			return "2 Timothy";
		}
		if (1129 <= pageNumber && pageNumber < 1132) {
			return "Titus";

		}
		if (1132 <= pageNumber && pageNumber < 1133) {
			return "Philemon";
		}
		if (1133 <= pageNumber && pageNumber < 1146) {
			return "Hebrews";
		}
		if (1146 <= pageNumber && pageNumber < 1151) {
			return "James";

		}
		if (1151 <= pageNumber && pageNumber < 1156) {
			return "1 Peter";

		}
		if (1156 <= pageNumber && pageNumber < 1159) {
			return "2 Peter";

		}
		if (1159 <= pageNumber && pageNumber < 1164) {
			return "1 John";

		}
		if (1164 <= pageNumber && pageNumber < 1165) {
			return "2 John";

		}
		if (1165 <= pageNumber && pageNumber < 1166) {
			return "3 John";

		}
		if (1166 <= pageNumber && pageNumber < 1167) {
			return "Jude";

		}
		if (1167 <= pageNumber) {
            return "Revelation";
		}
		return "";

	}

}

package com.example.EpubProject.utils;

import android.util.Log;

import com.example.epub.db.html.Verse;

import java.util.ArrayList;
import java.util.List;

public class PageUTilsBook {

    public enum LanguageTypeBook {
        ENGLISHBOOK, EQBOOK;
    }

    public static int getPageCount(LanguageTypeBook languageType) {

        switch (languageType) {
            case ENGLISHBOOK:

                getEng();

                break;
            case EQBOOK:

                getEq();
                break;
            default:
                break;
        }
        return 0;
    }

    private static void getEng() {
        ListPageBookEnglish.add(new ListPageBook("Genesis", 0));
        ListPageBookEnglish.add(new ListPageBook("Exodus", 50));
        ListPageBookEnglish.add(new ListPageBook("Leviticus", 90));
        ListPageBookEnglish.add(new ListPageBook("Numbers", 117));
        ListPageBookEnglish.add(new ListPageBook("Deuteronomy", 153));
        ListPageBookEnglish.add(new ListPageBook("Joshua", 187));
        ListPageBookEnglish.add(new ListPageBook("Judges", 211));
        ListPageBookEnglish.add(new ListPageBook("Ruth", 232));
        ListPageBookEnglish.add(new ListPageBook("1 Samuel", 236));
        ListPageBookEnglish.add(new ListPageBook("2 Samuel", 267));
        ListPageBookEnglish.add(new ListPageBook("1 Kings", 297));
        ListPageBookEnglish.add(new ListPageBook("2 Kings", 313));
        ListPageBookEnglish.add(new ListPageBook("1 Chronicles", 338));
        ListPageBookEnglish.add(new ListPageBook("2 Chronicles", 367));
        ListPageBookEnglish.add(new ListPageBook("Ezra", 403));
        ListPageBookEnglish.add(new ListPageBook("Nehemiah", 413));
        ListPageBookEnglish.add(new ListPageBook("Esther", 426));
        ListPageBookEnglish.add(new ListPageBook("Job", 436));
        ListPageBookEnglish.add(new ListPageBook("Psalms", 478));
        ListPageBookEnglish.add(new ListPageBook("Proverbs", 628));
        ListPageBookEnglish.add(new ListPageBook("Ecclesiastes", 659));
        ListPageBookEnglish.add(new ListPageBook("Song Of Solomon", 671));
        ListPageBookEnglish.add(new ListPageBook("Isaiah", 679));
        ListPageBookEnglish.add(new ListPageBook("Jeremiah", 745));
        ListPageBookEnglish.add(new ListPageBook("Lamentations", 797));
        ListPageBookEnglish.add(new ListPageBook("Ezekiel", 802));
        ListPageBookEnglish.add(new ListPageBook("Daniel", 850));
        ListPageBookEnglish.add(new ListPageBook("Hosea", 862));
        ListPageBookEnglish.add(new ListPageBook("Joel", 876));
        ListPageBookEnglish.add(new ListPageBook("Amos", 879));
        ListPageBookEnglish.add(new ListPageBook("Obadiah", 888));
        ListPageBookEnglish.add(new ListPageBook("Jonah", 889));
        ListPageBookEnglish.add(new ListPageBook("Micah", 893));
        ListPageBookEnglish.add(new ListPageBook("Nahum", 900));
        ListPageBookEnglish.add(new ListPageBook("Habakkuk", 903));
        ListPageBookEnglish.add(new ListPageBook("Zephaniah", 906));
        ListPageBookEnglish.add(new ListPageBook("Haggai", 909));
        ListPageBookEnglish.add(new ListPageBook("Zechariah", 911));
        ListPageBookEnglish.add(new ListPageBook("Malachi", 925));
        ListPageBookEnglish.add(new ListPageBook("Matthew", 929));
        ListPageBookEnglish.add(new ListPageBook("Mark", 957));
        ListPageBookEnglish.add(new ListPageBook("Luke", 973));
        ListPageBookEnglish.add(new ListPageBook("John", 997));
        ListPageBookEnglish.add(new ListPageBook("Acts", 1018));
        ListPageBookEnglish.add(new ListPageBook("Romans", 1046));
        ListPageBookEnglish.add(new ListPageBook("1 Corinthians", 1062));
        ListPageBookEnglish.add(new ListPageBook("2 Corinthians", 1078));
        ListPageBookEnglish.add(new ListPageBook("Galatians", 1091));
        ListPageBookEnglish.add(new ListPageBook("Ephesians", 1097));
        ListPageBookEnglish.add(new ListPageBook("Philippians", 1103));
        ListPageBookEnglish.add(new ListPageBook("Colossians", 1107));
        ListPageBookEnglish.add(new ListPageBook("1 Thessalonians", 1111));
        ListPageBookEnglish.add(new ListPageBook("2 Thessalonians", 1116));
        ListPageBookEnglish.add(new ListPageBook("1 Timothy", 1119));
        ListPageBookEnglish.add(new ListPageBook("2 Timothy", 1125));
        ListPageBookEnglish.add(new ListPageBook("Titus", 1129));
        ListPageBookEnglish.add(new ListPageBook("Philemon", 1132));
        ListPageBookEnglish.add(new ListPageBook("Hebrews", 1133));
        ListPageBookEnglish.add(new ListPageBook("James", 1146));
        ListPageBookEnglish.add(new ListPageBook("1 Peter", 1151));
        ListPageBookEnglish.add(new ListPageBook("2 Peter", 1156));
        ListPageBookEnglish.add(new ListPageBook("1 John", 1159));
        ListPageBookEnglish.add(new ListPageBook("2 John", 1164));
        ListPageBookEnglish.add(new ListPageBook("3 John", 1165));
        ListPageBookEnglish.add(new ListPageBook("Jude", 1166));
        ListPageBookEnglish.add(new ListPageBook("Revelation", 1167));
    }

    private static void getSwahili() {
        ListPageBookSwahili.add(new ListPageBook("Mwanzo", 0));
        ListPageBookSwahili.add(new ListPageBook("Kutoka", 50));
        ListPageBookSwahili.add(new ListPageBook("Mambo ya Walawi", 90));
        ListPageBookSwahili.add(new ListPageBook("Hesabu", 117));
        ListPageBookSwahili.add(new ListPageBook("Kumbukumbu la Torati", 153));
        ListPageBookSwahili.add(new ListPageBook("Yoshua", 187));
        ListPageBookSwahili.add(new ListPageBook("Waamuzi", 211));
        ListPageBookSwahili.add(new ListPageBook("Ruthu", 232));
        ListPageBookSwahili.add(new ListPageBook("1 Samweli", 236));
        ListPageBookSwahili.add(new ListPageBook("2 Samweli", 267));
        ListPageBookSwahili.add(new ListPageBook("1 Wafalme", 291));
        ListPageBookSwahili.add(new ListPageBook("2 Wafalme", 313));
        ListPageBookSwahili.add(new ListPageBook("1 Nyakati", 338));
        ListPageBookSwahili.add(new ListPageBook("2 Nyakati", 367));
        ListPageBookSwahili.add(new ListPageBook("Ezra", 403));
        ListPageBookSwahili.add(new ListPageBook("Nehemia", 413));
        ListPageBookSwahili.add(new ListPageBook("Esta", 426));
        ListPageBookSwahili.add(new ListPageBook("Ayubu", 436));
        ListPageBookSwahili.add(new ListPageBook("Zaburi", 478));
        ListPageBookSwahili.add(new ListPageBook("Methali", 628));
        ListPageBookSwahili.add(new ListPageBook("Mhubiri", 659));
        ListPageBookSwahili.add(new ListPageBook("Wimbo wa Sulemani", 671));
        ListPageBookSwahili.add(new ListPageBook("Isaya", 679));
        ListPageBookSwahili.add(new ListPageBook("Yeremia", 745));
        ListPageBookSwahili.add(new ListPageBook("Maombolezo", 797));
        ListPageBookSwahili.add(new ListPageBook("Ezekieli", 802));
        ListPageBookSwahili.add(new ListPageBook("Danieli", 850));
        ListPageBookSwahili.add(new ListPageBook("Hosea", 862));
        ListPageBookSwahili.add(new ListPageBook("Yoeli", 876));
        ListPageBookSwahili.add(new ListPageBook("Amosi", 879));
        ListPageBookSwahili.add(new ListPageBook("Obadia", 888));
        ListPageBookSwahili.add(new ListPageBook("Yona", 889));
        ListPageBookSwahili.add(new ListPageBook("Mika", 893));
        ListPageBookSwahili.add(new ListPageBook("Nahumu", 900));
        ListPageBookSwahili.add(new ListPageBook("Habakuki", 903));
        ListPageBookSwahili.add(new ListPageBook("Sefania", 906));
        ListPageBookSwahili.add(new ListPageBook("Hagai", 909));
        ListPageBookSwahili.add(new ListPageBook("Zekaria", 911));
        ListPageBookSwahili.add(new ListPageBook("Malaki", 925));
        ListPageBookSwahili.add(new ListPageBook("Mathayo", 929));
        ListPageBookSwahili.add(new ListPageBook("Marko", 957));
        ListPageBookSwahili.add(new ListPageBook("Luka", 973));
        ListPageBookSwahili.add(new ListPageBook("Yohana", 997));
        ListPageBookSwahili.add(new ListPageBook("Matendo ya Mitume", 1018));
        ListPageBookSwahili.add(new ListPageBook("Waroma", 1046));
        ListPageBookSwahili.add(new ListPageBook("1 Wakorintho", 1062));
        ListPageBookSwahili.add(new ListPageBook("2 Wakorintho", 1078));
        ListPageBookSwahili.add(new ListPageBook("Wagalatia", 1091));
        ListPageBookSwahili.add(new ListPageBook("Waefeso", 1097));
        ListPageBookSwahili.add(new ListPageBook("Wafilipi", 1103));
        ListPageBookSwahili.add(new ListPageBook("Wakolosai", 1107));
        ListPageBookSwahili.add(new ListPageBook("1 Wathesalonike", 1111));
        ListPageBookSwahili.add(new ListPageBook("2 Wathesalonike", 1116));
        ListPageBookSwahili.add(new ListPageBook("1 Timotheo", 1119));
        ListPageBookSwahili.add(new ListPageBook("2 Timotheo", 1125));
        ListPageBookSwahili.add(new ListPageBook("Tito", 1129));
        ListPageBookSwahili.add(new ListPageBook("Filemoni", 1132));
        ListPageBookSwahili.add(new ListPageBook("Waebrania", 1133));
        ListPageBookSwahili.add(new ListPageBook("Yakobo", 1146));
        ListPageBookSwahili.add(new ListPageBook("1 Petro", 1151));
        ListPageBookSwahili.add(new ListPageBook("2 Petro", 1156));
        ListPageBookSwahili.add(new ListPageBook("1 Yohana", 1159));
        ListPageBookSwahili.add(new ListPageBook("2 Yohana", 1164));
        ListPageBookSwahili.add(new ListPageBook("3 Yohana", 1165));
        ListPageBookSwahili.add(new ListPageBook("Yuda", 1166));
        ListPageBookSwahili.add(new ListPageBook("Ufunuo", 1167));
    }

    private static void getEq() {
        ListPageBookAkuapem.add(new ListPageBook("Genesis", 0));
        ListPageBookAkuapem.add(new ListPageBook("Exodus", 50));
        ListPageBookAkuapem.add(new ListPageBook("Leviticus", 90));
        ListPageBookAkuapem.add(new ListPageBook("Numbers", 117));
        ListPageBookAkuapem.add(new ListPageBook("Deuteronomy", 153));
        ListPageBookAkuapem.add(new ListPageBook("Joshua", 187));
        ListPageBookAkuapem.add(new ListPageBook("Judges", 211));
        ListPageBookAkuapem.add(new ListPageBook("Ruth", 232));
        ListPageBookAkuapem.add(new ListPageBook("1 Samuel", 236));
        ListPageBookAkuapem.add(new ListPageBook("2 Samuel", 267));
        ListPageBookAkuapem.add(new ListPageBook("1 Kings", 291));
        ListPageBookAkuapem.add(new ListPageBook("2 Kings", 313));
        ListPageBookAkuapem.add(new ListPageBook("1 Chronicles", 338));
        ListPageBookAkuapem.add(new ListPageBook("2 Chronicles", 367));
        ListPageBookAkuapem.add(new ListPageBook("Ezra", 403));
        ListPageBookAkuapem.add(new ListPageBook("Nehemiah", 413));
        ListPageBookAkuapem.add(new ListPageBook("Esther", 426));
        ListPageBookAkuapem.add(new ListPageBook("Job", 436));
        ListPageBookAkuapem.add(new ListPageBook("Psalms", 478));
        ListPageBookAkuapem.add(new ListPageBook("Proverbs", 628));
        ListPageBookAkuapem.add(new ListPageBook("Ecclesiastes", 659));
        ListPageBookAkuapem.add(new ListPageBook("Song of Solomon", 671));
        ListPageBookAkuapem.add(new ListPageBook("Isaiah", 679));
        ListPageBookAkuapem.add(new ListPageBook("Jeremiah", 745));
        ListPageBookAkuapem.add(new ListPageBook("Lamentations", 797));
        ListPageBookAkuapem.add(new ListPageBook("Ezekiel", 802));
        ListPageBookAkuapem.add(new ListPageBook("Daniel", 850));
        ListPageBookAkuapem.add(new ListPageBook("Hosea", 862));
        ListPageBookAkuapem.add(new ListPageBook("Joel", 876));
        ListPageBookAkuapem.add(new ListPageBook("Amos", 879));
        ListPageBookAkuapem.add(new ListPageBook("Obadiah", 888));
        ListPageBookAkuapem.add(new ListPageBook("Jona", 889));
        ListPageBookAkuapem.add(new ListPageBook("Micah", 893));
        ListPageBookAkuapem.add(new ListPageBook("Nahum", 900));
        ListPageBookAkuapem.add(new ListPageBook("Habakkuk", 903));
        ListPageBookAkuapem.add(new ListPageBook("Zephaniah", 906));
        ListPageBookAkuapem.add(new ListPageBook("Haggai", 909));
        ListPageBookAkuapem.add(new ListPageBook("Zechariah", 911));
        ListPageBookAkuapem.add(new ListPageBook("Malachi", 925));
        ListPageBookAkuapem.add(new ListPageBook("Matthew", 929));
        ListPageBookAkuapem.add(new ListPageBook("Mark", 957));
        ListPageBookAkuapem.add(new ListPageBook("Luke", 973));
        ListPageBookAkuapem.add(new ListPageBook("John", 997));
        ListPageBookAkuapem.add(new ListPageBook("Acts", 1018));
        ListPageBookAkuapem.add(new ListPageBook("Romans", 1046));
        ListPageBookAkuapem.add(new ListPageBook("1 Corinthians", 1062));
        ListPageBookAkuapem.add(new ListPageBook("2 Corinthians", 1078));
        ListPageBookAkuapem.add(new ListPageBook("Galatians", 1091));
        ListPageBookAkuapem.add(new ListPageBook("Ephesians", 1097));
        ListPageBookAkuapem.add(new ListPageBook("Philippians", 1103));
        ListPageBookAkuapem.add(new ListPageBook("Colossians", 1107));
        ListPageBookAkuapem.add(new ListPageBook("1 Thessalonians", 1111));
        ListPageBookAkuapem.add(new ListPageBook("2 Thessalonians", 1116));
        ListPageBookAkuapem.add(new ListPageBook("1 Timothy", 1119));
        ListPageBookAkuapem.add(new ListPageBook("2 Timothy", 1125));
        ListPageBookAkuapem.add(new ListPageBook("Titus", 1129));
        ListPageBookAkuapem.add(new ListPageBook("Philemon", 1132));
        ListPageBookAkuapem.add(new ListPageBook("Hebrews", 1133));
        ListPageBookAkuapem.add(new ListPageBook("James", 1146));
        ListPageBookAkuapem.add(new ListPageBook("1 Peter", 1151));
        ListPageBookAkuapem.add(new ListPageBook("2 Peter", 1156));
        ListPageBookAkuapem.add(new ListPageBook("1 John", 1159));
        ListPageBookAkuapem.add(new ListPageBook("2 John", 1164));
        ListPageBookAkuapem.add(new ListPageBook("3 John", 1165));
        ListPageBookAkuapem.add(new ListPageBook("Jude", 1166));
        ListPageBookAkuapem.add(new ListPageBook("Revelation", 1167));
    }

    public static List<ListPageBook> getListPageBookEnglis() {
        if (ListPageBookEnglish.size() == 0) {
            getEng();
        }
        return ListPageBookEnglish;
    }

    public static List<ListPageBook> getListPageBookSwahili() {
        if (ListPageBookSwahili.size() == 0) {
            getSwahili();
        }
        return ListPageBookSwahili;
    }

    public static void setListPageBookEnglis(
            List<ListPageBook> ListPageBookEnglis) {
        PageUTilsBook.ListPageBookEnglish = ListPageBookEnglis;
    }

    public static List<ListPageBook> getListPageBookEq() {
        if (ListPageBookAkuapem.size() == 0) {
            getEq();
        }
        return ListPageBookAkuapem;
    }

    public static void setListPageBookEq(List<ListPageBook> ListPageBookEq) {
        PageUTilsBook.ListPageBookAkuapem = ListPageBookEq;
    }

    public static int getPagePosition(Verse verse) {
        int index = Constants.POS0_START;
        int len = getListPageBookEnglis().size();
        for (int i = 0; i < len; i++) {

            if (verse.getChapterTitle().toLowerCase().contains(getListPageBookEnglis().get(i).nameBook.toLowerCase())
                    || verse.getChapterTitle().toLowerCase().contains(getListPageBookSwahili().get(i).nameBook.toLowerCase())) {
                index += getListPageBookEnglis().get(i).pageCountBook;
                break;
            }
        }

        Log.e("@@@@@", "index=" + index);
//        int position = 0;
//        for (; index >= 1; index--) {
//            // Page number start with 1
//            position += getListPageBookEnglis().get(index - 1).pageCountBook;
//        }
//
//        Log.e("@@@@@", "position=" + position);

//        return position + Constants.POS0_START + verse.getChapterNumber() -1;
        return index +  verse.getChapterNumber() - 1;
    }

    private static List<ListPageBook> ListPageBookEnglish = new ArrayList<>();
    private static List<ListPageBook> ListPageBookAkuapem = new ArrayList<>();
    private static List<ListPageBook> ListPageBookSwahili= new ArrayList<>();
}

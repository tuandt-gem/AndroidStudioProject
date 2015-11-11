package com.example.EpubProject.utils;

import java.util.ArrayList;
import java.util.List;

public class PageUTils {

    public enum LanguageType {
        ENGLISH, EQ;
    }

    public static int getPageCount(LanguageType languageType) {

        switch (languageType) {
            case ENGLISH:
                getEng();
                break;
            case EQ:
                getEq();
                break;
            default:
                break;
        }
        return 0;
    }

    private static void getEng() {
        listPageEnglish.add(new ListPage("Genesis", 50, false));
        listPageEnglish.add(new ListPage("Exodus", 40, false));
        listPageEnglish.add(new ListPage("Leviticus", 27, false));
        listPageEnglish.add(new ListPage("Numbers", 36, false));
        listPageEnglish.add(new ListPage("Deuteronomy", 34, false));
        listPageEnglish.add(new ListPage("Joshua", 24, false));
        listPageEnglish.add(new ListPage("Judges", 21, false));
        listPageEnglish.add(new ListPage("Ruth", 4, false));
        listPageEnglish.add(new ListPage("1 Samuel", 31, false));
        listPageEnglish.add(new ListPage("2 Samuel", 24, false));
        listPageEnglish.add(new ListPage("1 Kings", 22, false));
        listPageEnglish.add(new ListPage("2 Kings", 25, false));
        listPageEnglish.add(new ListPage("1 Chronicles", 29, false));
        listPageEnglish.add(new ListPage("2 Chronicles", 36, false));
        listPageEnglish.add(new ListPage("Ezra", 10, false));
        listPageEnglish.add(new ListPage("Nehemiah", 13, false));
        listPageEnglish.add(new ListPage("Esther", 10, false));
        listPageEnglish.add(new ListPage("Job", 42, false));
        listPageEnglish.add(new ListPage("Psalms", 150, false));
        listPageEnglish.add(new ListPage("Proverbs", 31, false));
        listPageEnglish.add(new ListPage("Ecclesiastes", 12, false));
        listPageEnglish.add(new ListPage("Song Of Solomon", 8, false));
        listPageEnglish.add(new ListPage("Isaiah", 66, false));
        listPageEnglish.add(new ListPage("Jeremiah", 52, false));
        listPageEnglish.add(new ListPage("Lamentations", 5, false));
        listPageEnglish.add(new ListPage("Ezekiel", 48, false));
        listPageEnglish.add(new ListPage("Daniel", 12, false));
        listPageEnglish.add(new ListPage("Hosea", 14, false));
        listPageEnglish.add(new ListPage("Joel", 3, false));
        listPageEnglish.add(new ListPage("Amos", 9, false));
        listPageEnglish.add(new ListPage("Obadiah", 1, false));
        listPageEnglish.add(new ListPage("Jonah", 4, false));
        listPageEnglish.add(new ListPage("Micah", 7, false));
        listPageEnglish.add(new ListPage("Nahum", 3, false));
        listPageEnglish.add(new ListPage("Habakkuk", 3, false));
        listPageEnglish.add(new ListPage("Zephaniah", 3, false));
        listPageEnglish.add(new ListPage("Haggai", 2, false));
        listPageEnglish.add(new ListPage("Zechariah", 14, false));
        listPageEnglish.add(new ListPage("Malachi", 4, false));
        listPageEnglish.add(new ListPage("Matthew", 28, false));
        listPageEnglish.add(new ListPage("Mark", 16, false));
        listPageEnglish.add(new ListPage("Luke", 24, false));
        listPageEnglish.add(new ListPage("John", 21, false));
        listPageEnglish.add(new ListPage("Acts", 28, false));
        listPageEnglish.add(new ListPage("Romans", 16, false));
        listPageEnglish.add(new ListPage("1 Corinthians", 16, false));
        listPageEnglish.add(new ListPage("2 Corinthians", 13, false));
        listPageEnglish.add(new ListPage("Galatians", 6, false));
        listPageEnglish.add(new ListPage("Ephesians", 6, false));
        listPageEnglish.add(new ListPage("Philippians", 4, false));
        listPageEnglish.add(new ListPage("Colossians", 4, false));
        listPageEnglish.add(new ListPage("1 Thessalonians", 5, false));
        listPageEnglish.add(new ListPage("2 Thessalonians", 3, false));
        listPageEnglish.add(new ListPage("1 Timothy", 6, false));
        listPageEnglish.add(new ListPage("2 Timothy", 4, false));
        listPageEnglish.add(new ListPage("Titus", 3, false));
        listPageEnglish.add(new ListPage("Philemon", 1, false));
        listPageEnglish.add(new ListPage("Hebrews", 13, false));
        listPageEnglish.add(new ListPage("James", 5, false));
        listPageEnglish.add(new ListPage("1 Peter", 5, false));
        listPageEnglish.add(new ListPage("2 Peter", 3, false));
        listPageEnglish.add(new ListPage("1 John", 5, false));
        listPageEnglish.add(new ListPage("2 John", 1, false));
        listPageEnglish.add(new ListPage("3 John", 1, false));
        listPageEnglish.add(new ListPage("Jude", 1, false));
        listPageEnglish.add(new ListPage("Revelation", 22, false));
    }

    private static void getEq() {
        listPageAkuapem.add(new ListPage("Genesis", 50, false));
        listPageAkuapem.add(new ListPage("Exodus", 40, false));
        listPageAkuapem.add(new ListPage("Leviticus", 27, false));
        listPageAkuapem.add(new ListPage("Numbers", 36, false));
        listPageAkuapem.add(new ListPage("Deuteronomy", 34, false));
        listPageAkuapem.add(new ListPage("Joshua", 24, false));
        listPageAkuapem.add(new ListPage("Judges", 21, false));
        listPageAkuapem.add(new ListPage("Ruth", 4, false));
        listPageAkuapem.add(new ListPage("1 Samuel", 31, false));
        listPageAkuapem.add(new ListPage("2 Samuel", 24, false));
        listPageAkuapem.add(new ListPage("1 Kings", 22, false));
        listPageAkuapem.add(new ListPage("2 Kings", 25, false));
        listPageAkuapem.add(new ListPage("1 Chronicles", 29, false));
        listPageAkuapem.add(new ListPage("2 Chronicles", 36, false));
        listPageAkuapem.add(new ListPage("Ezra", 10, false));
        listPageAkuapem.add(new ListPage("Nehemiah", 13, false));
        listPageAkuapem.add(new ListPage("Esther", 10, false));
        listPageAkuapem.add(new ListPage("Job", 42, false));
        listPageAkuapem.add(new ListPage("Psalms", 150, false));
        listPageAkuapem.add(new ListPage("Proverbs", 31, false));
        listPageAkuapem.add(new ListPage("Ecclesiastes", 12, false));
        listPageAkuapem.add(new ListPage("Song of Solomon", 8, false));
        listPageAkuapem.add(new ListPage("Isaiah", 66, false));
        listPageAkuapem.add(new ListPage("Jeremiah", 52, false));
        listPageAkuapem.add(new ListPage("Lamentations", 5, false));
        listPageAkuapem.add(new ListPage("Ezekiel", 48, false));
        listPageAkuapem.add(new ListPage("Daniel", 12, false));
        listPageAkuapem.add(new ListPage("Hosea", 14, false));
        listPageAkuapem.add(new ListPage("Joel", 3, false));
        listPageAkuapem.add(new ListPage("Amos", 9, false));
        listPageAkuapem.add(new ListPage("Obadiah", 1, false));
        listPageAkuapem.add(new ListPage("Jona", 4, false));
        listPageAkuapem.add(new ListPage("Micah", 7, false));
        listPageAkuapem.add(new ListPage("Nahum", 3, false));
        listPageAkuapem.add(new ListPage("Habakkuk", 3, false));
        listPageAkuapem.add(new ListPage("Zephaniah", 3, false));
        listPageAkuapem.add(new ListPage("Haggai", 2, false));
        listPageAkuapem.add(new ListPage("Zechariah", 14, false));
        listPageAkuapem.add(new ListPage("Malachi", 4, false));
        listPageAkuapem.add(new ListPage("Matthew", 28, false));
        listPageAkuapem.add(new ListPage("Mark", 16, false));
        listPageAkuapem.add(new ListPage("Luke", 24, false));
        listPageAkuapem.add(new ListPage("John", 21, false));
        listPageAkuapem.add(new ListPage("Acts", 28, false));
        listPageAkuapem.add(new ListPage("Romans", 16, false));
        listPageAkuapem.add(new ListPage("1 Corinthians", 16, false));
        listPageAkuapem.add(new ListPage("2 Corinthians", 13, false));
        listPageAkuapem.add(new ListPage("Galatians", 6, false));
        listPageAkuapem.add(new ListPage("Ephesians", 6, false));
        listPageAkuapem.add(new ListPage("Philippians", 4, false));
        listPageAkuapem.add(new ListPage("Colossians", 5, false));
        listPageAkuapem.add(new ListPage("1 Thessalonians", 5, false));
        listPageAkuapem.add(new ListPage("2 Thessalonians", 3, false));
        listPageAkuapem.add(new ListPage("1 Timothy", 6, false));
        listPageAkuapem.add(new ListPage("2 Timothy", 4, false));
        listPageAkuapem.add(new ListPage("Titus", 3, false));
        listPageAkuapem.add(new ListPage("Philemon", 1, false));
        listPageAkuapem.add(new ListPage("Hebrews", 13, false));
        listPageAkuapem.add(new ListPage("James", 5, false));
        listPageAkuapem.add(new ListPage("1 Peter", 5, false));
        listPageAkuapem.add(new ListPage("2 Peter", 3, false));
        listPageAkuapem.add(new ListPage("1 John", 5, false));
        listPageAkuapem.add(new ListPage("2 John", 1, false));
        listPageAkuapem.add(new ListPage("3 John", 1, false));
        listPageAkuapem.add(new ListPage("Jude", 1, false));
        listPageAkuapem.add(new ListPage("Revelation", 22, false));
    }

    public static List<ListPage> getListPageEnglis() {
        return listPageEnglish;
    }

    public static List<ListPage> getListPageEq() {
        return listPageAkuapem;
    }

    public static List<ListPage> listPageEnglish = new ArrayList<>();
    public static List<ListPage> listPageAkuapem = new ArrayList<>();
}

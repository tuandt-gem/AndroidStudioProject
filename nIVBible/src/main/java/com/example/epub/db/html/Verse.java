package com.example.epub.db.html;

import com.example.EpubProject.utils.PageUTilsBook;
import com.example.EpubProject.utils.PageUTilsBookNumber;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Verse in a page
 * Created by neo on 10/28/2015.
 */
@DatabaseTable(tableName = "verse")
public class Verse {
    public static final String BOOK_NAME = "book_name";
    public static final String TEXT = "text";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = BOOK_NAME)
    private String mBookName;
    @DatabaseField(columnName = "chapter_title")
    private String mChapterTitle;
    @DatabaseField(columnName = "chapter_number")
    private int mChapterNumber;
    @DatabaseField(columnName = "verse_number")
    private int mVerseNumber;
    @DatabaseField(columnName = TEXT)
    private String mText;

    public Verse() {
        // For Ormlite
    }

    public Verse(String bookname, String chapterTitle, int chapterNumber, int verseNumber, String text) {
        this.mBookName = bookname;
        this.mChapterTitle = chapterTitle;
        this.mChapterNumber = chapterNumber;
        this.mVerseNumber = verseNumber;
        this.mText = text;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getChapterTitle() {
        return mChapterTitle;
    }

    public int getChapterNumber() {
        return mChapterNumber;
    }

    public int getVerseNumber() {
        return mVerseNumber;
    }

    public String getText() {
        return mText;
    }

    public int getPageNumber() {
//        return PageUTilsBookNumber.getPagePosition(mChapterNumber);
        return PageUTilsBook.getPagePosition(this);
    }
}

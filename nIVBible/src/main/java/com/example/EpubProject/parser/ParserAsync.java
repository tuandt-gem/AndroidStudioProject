package com.example.EpubProject.parser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.EpubProject.utils.Constants;
import com.example.epub.db.DbVerseHelper;
import com.example.epub.db.html.Verse;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Parse book AsyncTask
 * Created by neo on 10/28/2015.
 */
public class ParserAsync extends AsyncTask<String, Void, Void> {
    private static final String TAG = "==epublib==";
    private static final String PREF_PARSE = "parse";
    private final DbVerseHelper db;
    private Context mContext;
    private IOnComplete mIOnComplete;
    private SharedPreferences preferencesBook;

    public ParserAsync(Context context, IOnComplete onComplete) {
        this.mContext = context;
        mIOnComplete = onComplete;
        db = new DbVerseHelper(context);
    }

    @Override
    protected Void doInBackground(String[] params) {
//        preferencesBook = mContext.getSharedPreferences("book", 0);
//        if (!preferencesBook.getBoolean(PREF_PARSE, false)) {
        for (int i = 0; i < Constants.FILE_NAME.length; i++) {
            getBook(Constants.FILE_NAME[i]);
        }
//        }

        return null;
    }


    private void getBook(String fileName) {
        AssetManager assetManager = mContext.getAssets();
        Book book = null;
        try {
            // find InputStream for book
            InputStream epubInputStream = assetManager.open(fileName);


            // Load Book from inputStream
            book = (new EpubReader()).readEpub(epubInputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Log the book's authors
//            if (book.getMetadata() != null) {
//                Log.i(TAG, "author(s): " + book.getMetadata().getAuthors());
//            }

        if (book == null) {
            Toast.makeText(mContext, "Book null", Toast.LENGTH_LONG).show();
            return;
        }
        // Log the book's title
        Log.i(TAG, "START BOOK: ==============");
        Log.i(TAG, "title: " + book.getTitle());


        // Log the book's coverimage property
//            if (book.getCoverImage() != null) {
//                Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
//                Log.i(TAG, "Coverimage is " + coverImage.getWidth() + " by " + coverImage.getHeight() + " pixels");
//            }

//            Log.d(TAG, "Book contents " + book.getContents().get(0).getHref());

        // Read navigation
//        for (String href : book.getResources().getAllHrefs()) {
        for (Resource res : book.getContents()) {
            String href = res.getHref();
            // Get all content without index or navigator
            String[] split = href.split("/");
            String hrefName = split[split.length - 1].replaceAll(".xhtml", "");

            // Each chapter name start with number (greater or equal 05) at 2 letters
            // Use it to add only chapter without add TOC files
            String twoFirstLetter = hrefName.substring(0, 2);
            boolean isNumber = StringUtil.isNumeric(twoFirstLetter);

//                if (href.startsWith("Text") && !href.startsWith("Text/BIBLE")) {
            if (isNumber && Integer.parseInt(twoFirstLetter) >= 5) {
                Log.d(TAG, "book href " + hrefName);

                Resource chapterResource = book.getResources().getByHref(href);

                int len = hrefName.length();
                int num = 1;
                if (Character.isDigit(hrefName.charAt(len - 1))) {
                    String numText;
                    if (Character.isDigit(hrefName.charAt(len - 2))) {
                        numText = hrefName.substring(len - 2, len);
                    } else {
                        numText = hrefName.substring(len - 1, len);
                    }

                    num = Integer.parseInt(numText);
                }

//                chapterNumber = chapterNumber.substring(chapterNumber.length() - 2).replaceAll("[^0-9]", "");
//
//                if (Utility.isStringEmpty(chapterNumber)) {
//                    num = 1;
//                } else {
//                    num = Integer.parseInt(chapterNumber);
//                }
                Log.i(TAG, "Num=" + num);

                hrefName = hrefName.split("-")[0];
                String chapterTitle = getChapterTitle(book, hrefName);
                Log.i(TAG, "Title=" + chapterTitle);
                try {
                    String chapterHtml = new String(chapterResource.getData());
                    parseHtml(chapterHtml, fileName, chapterTitle, num);
                } catch (IOException e) {
                    Log.e(TAG, "Read book error " + e.getMessage());
                }
            }
        }

    }

    private String getChapterTitle(Book book, String href) {
        for (TOCReference tocReference : book.getTableOfContents().getTocReferences()) {
            try {
                String completeHref = tocReference.getCompleteHref();
                if (completeHref != null && completeHref.contains(href)) {
                    return tocReference.getTitle();
                }
            } catch (NullPointerException ex) {
//                Log.e("getChapterTitle", "Why null? " + ex.getMessage());
            }
        }
        return null;
    }

    /**
     * Check a <p> is a verse or not and return verse number
     * Return 0 if this tag isn't a verse
     */
    private int getVerseNumber(String bookName, Element pTag) {
        String html = pTag.text();
        if (bookName.equals(Constants.NIV_1984)
                || bookName.equals(Constants.AKUAPEM)) {
            // Verse format example "Gen 1:1"
            if (html.length() <= 6) {
                return 0;
            }

            String number = html.split(":")[0];
            number = number.substring(4, number.length());

            if (!Character.isDigit(number.charAt(0))) {
                return 0;
            }

            return Integer.parseInt(number);
        } else {
            // Common format: every verse start with a number
            if (html.length() < 3) {
                return 0;
            }

            if (!Character.isDigit(html.charAt(0))) {
                return 0;
            }

            String verseNum;
            if (Character.isDigit(html.charAt(1))) {
                verseNum = html.substring(0, 2);

            } else {
                verseNum = html.substring(0, 1);
            }

            Log.e(TAG, "GET VERSE NUMBER " + verseNum);
            return Integer.parseInt(verseNum);
        }
    }

    /**
     * Parse html from chapter
     *
     * @param html String html
     */
    private boolean parseHtml(String html, String bookName, String chapterTitle, int chapterNumber) throws IOException {
        boolean hasContent = false;
        Document document = Jsoup.parse(html);
        Elements pTags = document.getElementsByTag("p");

        int len = pTags.size();

        int nonVerseCount = 0;
//        for (int i = 0; i < len; i++) {
//            Element pTag = pTags.get(i);
//            String className = pTag.className();
////            if (!Utility.isStringEmpty(className)) {
//            if (getVerseNumber(bookName, pTag) == 0) {
//                nonVerseCount++;
//                Log.v("NONNE", "i=" + i +"\n" + pTag.text());
//            }
//        }
//        Log.v("NONNE", "nonVerseCount=" + nonVerseCount);

        for (int i = 0; i < len; i++) {
            Element pTag = pTags.get(i);
            String className = pTag.className();
//            Log.w("parseHtml", "Ele className=" + className);

            // Get <p> tag without class name as sentence of current chapter
//            if (Utility.isStringEmpty(className)) {
//            if (getVerseNumber(bookName, pTag) != 0) {
            try {
                String text = pTag.text();
//                    Verse verse = new Verse(bookName, chapterTitle, chapterNumber, i - nonVerseCount + 1, text);
                Verse verse = new Verse(bookName, chapterTitle, chapterNumber, i, text);
//                    Verse verse = new Verse(bookName, chapterTitle, chapterNumber, text);
                db.addVerse(verse);
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            }

            hasContent = true;
        }

        return hasContent;
    }

    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);
        if (mIOnComplete != null) {
            mIOnComplete.onComplete();
        }
    }

    public interface IOnComplete {
        void onComplete();
    }
}
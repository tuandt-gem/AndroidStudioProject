package com.example.EpubProject;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.EpubProject.utils.Constants;
import com.example.EpubProject.utils.PageUTilsBookName;
import com.example.EpubProject.utils.PageUTilsBookName.LanguageTypeBookName;
import com.example.EpubProject.utils.PageUTilsBookNumber;
import com.example.EpubProject.utils.PageUTilsBookNumber.LanguageTypeBookNumber;
import com.example.epub.Book;
import com.nivbible.EpubReader;

/*
 * Holds the logic for the App's 
 * special WebView handling
 */
public abstract class EpubWebView extends WebView {
    private final static float FLING_THRESHOLD_VELOCITY = 200;

    /*
     * The book view will show
     */
    // private ArrayList<ManifestItem> mSpine;
    private Book mBook;

    private GestureDetector mGestureDetector;

    /*
     * "root" page we're currently showing
     */
    private Uri mCurrentResourceUri;

    /*
     * Position of document
     */
    private float mScrollY = 0.0f;

    /*
     * Note that we're loading from a bookmark
     */
    private boolean mScrollWhenPageLoaded = false;

    /*
     * To speak the text
     */
    private TextToSpeechWrapper mTtsWrapper;

    /*
     * The page, as text
     */
    private ArrayList<String> mText;

    private WebViewClient mWebViewClient;
    private SharedPreferences preferencesBook;
    private SharedPreferences preferencesBookNumber;
    int position_current;
    /*
     * Current line being spoken
     */
    private int mTextLine;

    /*
     * Pick an initial default
     */
    private float mFlingMinDistance = 320;

    /*
     * The total available area for drawing on
     */
    private Rect mRawScreenDimensions;

    public EpubWebView(Context context) {
        this(context, null);
    }

    public EpubWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, mGestureListener);
        // mSpine = new ArrayList<ManifestItem>();
        setWebChromeClient(new WebChromeClient());
        setWebSettings(18);
        preferencesBook = context.getSharedPreferences("book", 0);
        preferencesBookNumber = context.getSharedPreferences("booknumber", 0);

    }

    /*
     * Do any Web settings specific to the derived class
     */
    abstract protected void addWebSettings(WebSettings settings);

    /*
     * Book to show
     */
    public void setBook(String fileName, Context context) {
        // if book already loaded, don't load again
        if ((mBook == null) || !mBook.getFileName().equals(fileName)) {
            mBook = new Book(fileName, context);
        }
    }

    public Book getBook() {
        return mBook;
    }

    public WebViewClient getWebViewClient() {
        return mWebViewClient;
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        mWebViewClient = webViewClient;
        super.setWebViewClient(webViewClient);
    }

    /*
     * Chapter of book to show
     */
    public void loadChapter(Uri resourceUri) {
        if (mBook != null) {
            // if no chapter resourceName supplied, default to first one.
            if (resourceUri == null) {
                resourceUri = mBook.firstChapter();
                if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                    position_current = Constants.POS0_START;
                } else {
                    if (preferencesBook.getInt("position", Constants.POS0_START) == 0) {
                        position_current = Constants.POS0_START;
                    } else {
                        position_current = Constants.POS1_START;
                    }

                }
            }
            String url = resourceUri.toString();
            String[] separated = url.split("/");

            if (separated != null && separated.length > 0) {
                Log.e("", "Chaptr uri-" + removeExtention(separated[separated.length - 1]));
            }

            if (resourceUri != null) {
                mCurrentResourceUri = resourceUri;
                // prevent cache, because WebSettings.LOAD_NO_CACHE doesn't
                // always work.
                clearCache(false);
                LoadUri(resourceUri);

            }
        }
    }

    public String getChapterNameAndNumber() {
        return PageUTilsBookName.getEq(position_current) + " " + PageUTilsBookNumber.getEq(position_current);
    }

    public void loadChapterPos(Uri resourceUri, int Pos) {
        if (mBook != null) {
            // if no chapter resourceName supplied, default to first one.
            if (resourceUri == null) {
                if (preferencesBookNumber.getBoolean("chapter", true)) {

                    if (preferencesBook.getBoolean("changeBookBoolean", true)) {
                        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                            position_current = preferencesBook.getInt("changeBook", 0) + 1;

                            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.EQBOOK, position_current);
                            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.EQBOOK, position_current);
                            PageUTilsBookName.getEq(position_current);
                            PageUTilsBookNumber.getEq(position_current);

                            if (PageUTilsBookName.getEq(position_current).contains("Solomon Dwom (Nnwom mu Dwom)")) {
                                ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                                        "Solomon Dwom.." + " " + PageUTilsBookNumber.getEq(position_current));
                            } else {
                                ((MainActivity) EpubReader.getActivityIsntanse())
                                        .changeBookName(PageUTilsBookName.getEq(position_current) + " "
                                                + PageUTilsBookNumber.getEq(position_current));
                            }

                        } else {
                            position_current = preferencesBook.getInt("changeBook", 0);

                            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.ENGLISHBOOK, position_current);
                            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.ENGLISHBOOK,
                                    position_current);
                            PageUTilsBookName.getEng(position_current);
                            PageUTilsBookNumber.getEng(position_current);
                            ((MainActivity) EpubReader.getActivityIsntanse())
                                    .changeBookName(PageUTilsBookName.getEng(position_current) + " "
                                            + PageUTilsBookNumber.getEng(position_current));
                        }
                    } else {
                        position_current = preferencesBookNumber.getInt("totalno", 0);
                        Log.e("preferencesBookNumber", "" + position_current);
                    }

                } else {

                    if (preferencesBook.getBoolean("changeBookBoolean", true)) {
                        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                            position_current = preferencesBook.getInt("changeBook", 0) + 1;

                            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.EQBOOK, position_current);
                            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.EQBOOK, position_current);
                            PageUTilsBookName.getEq(position_current);
                            PageUTilsBookNumber.getEq(position_current);

                            if (PageUTilsBookName.getEq(position_current).contains("Solomon Dwom (Nnwom mu Dwom)")) {
                                ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                                        "Solomon Dwom.." + " " + PageUTilsBookNumber.getEq(position_current));
                            } else {
                                ((MainActivity) EpubReader.getActivityIsntanse())
                                        .changeBookName(PageUTilsBookName.getEq(position_current) + " "
                                                + PageUTilsBookNumber.getEq(position_current));
                            }
                        } else {
                            position_current = preferencesBook.getInt("changeBook", 0);

                            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.ENGLISHBOOK, position_current);
                            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.ENGLISHBOOK,
                                    position_current);
                            PageUTilsBookName.getEng(position_current);
                            PageUTilsBookNumber.getEng(position_current);
                            ((MainActivity) EpubReader.getActivityIsntanse())
                                    .changeBookName(PageUTilsBookName.getEng(position_current) + " "
                                            + PageUTilsBookNumber.getEng(position_current));
                        }
                    } else {
                        position_current = preferencesBook.getInt("bookmarkvalue", Constants.POS0_START);
                    }

                }
                resourceUri = mBook.selectedChapter(Pos);
            }
            // String url = resourceUri.toString();
            // String[] separated = url.split("/");

            if (resourceUri != null) {
                mCurrentResourceUri = resourceUri;
                // prevent cache, because WebSettings.LOAD_NO_CACHE doesn't
                // always work.
                clearCache(false);
                LoadUri(resourceUri);
            }
        }
    }

    IOnPageChange onPageChange;

    public interface IOnPageChange {
        public void onPageChanged(int position);
    }

    public void setOnReceive(IOnPageChange onPageChange, int position) {
        this.onPageChange = onPageChange;
        position_current = position;
    }

    public void nextButton() {
        getBookNameNumberNext();
        changeChapter(mBook.nextResource(mCurrentResourceUri));

    }

    public void previousButton() {
        getBookNameNumberPrevious();
        changeChapter(mBook.previousResource(mCurrentResourceUri));

    }

    public static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory())
            return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0) {
            // No period after first character - return name as it was passed in
            return filePath;
        } else {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }

    /*
     * @ return load contents of URI into WebView, implementation is android
     * version dependent
     */
    protected abstract void LoadUri(Uri uri);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            // if no book, nothing to do
            if (mBook == null) {
                return false;
            }

            // also ignore swipes that are vertical, too slow, or too short.
            float deltaX = event2.getX() - event1.getX();
            float deltaY = event2.getY() - event1.getY();

            if ((Math.abs(deltaX) < Math.abs(deltaY)) || (Math.abs(deltaX) < mFlingMinDistance)
                    || (Math.abs(velocityX) < FLING_THRESHOLD_VELOCITY)) {
                return false;
            } else {
                if (deltaX < 0) {
                    getBookNameNumberNext();
                    return changeChapter(mBook.nextResource(mCurrentResourceUri));
                } else {
                    getBookNameNumberPrevious();
                    return changeChapter(mBook.previousResource(mCurrentResourceUri));
                }
            }
        }

        /*
         * If double tap at top/bottom fifth of screen, scroll page up/down
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float y = e.getY();
            if (y <= mRawScreenDimensions.height() / 5) {
                pageUp(false);
                return true;
            } else if (4 * mRawScreenDimensions.height() / 5 <= y) {
                pageDown(false);
                return true;
            } else {
                return false;
            }
        }
    };

    private boolean changeChapter(Uri resourceUri) {
        if (resourceUri != null) {
            loadChapter(resourceUri);
            return true;
        } else {
            return false;
        }
    }

    public void speak(TextToSpeechWrapper ttsWrapper) {
        mTtsWrapper = ttsWrapper;
        if ((mBook != null) && (mCurrentResourceUri != null)) {
            mText = new ArrayList<String>();
            XmlUtil.parseXmlResource(mBook.fetch(mCurrentResourceUri).getData(), new XhtmlToText(mText), null);

            mTextLine = 0;
            mTtsWrapper.setOnUtteranceCompletedListener(mCompletedListener);
            if (0 < mText.size()) {
                mTtsWrapper.speak(mText.get(0));
            }
        }
    }

	/*
     * public ArrayList<String> getList(Uri uri) { if ((mBook != null) && (uri
	 * != null)) { mText = new ArrayList<String>();
	 * XmlUtil.parseXmlResource(mBook.fetch(uri).getData(), new
	 * XhtmlToText(mText), null); return mText; } return new
	 * ArrayList<String>(); }
	 */

    /*
     * Send next piece of text to Text to speech
     */
    private TextToSpeech.OnUtteranceCompletedListener mCompletedListener = new TextToSpeech.OnUtteranceCompletedListener() {

        @Override
        public void onUtteranceCompleted(String utteranceId) {
            if (mTextLine < mText.size() - 1) {
                mTtsWrapper.speak(mText.get(++mTextLine));
            }
        }

    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRawScreenDimensions = new Rect(0, 0, w, h);
        mFlingMinDistance = w / 2;
    }

    /*
     * Called when page is loaded, if we're scrolling to a bookmark, we need to
     * set the page size listener here. Otherwise it can be called for pages
     * other than the one we're interested in
     */
    @SuppressWarnings("deprecation")
    public void onPageLoaded() {
        if (mScrollWhenPageLoaded) {
            setPictureListener(mPictureListener);
            mScrollWhenPageLoaded = false;
        }
    }

    public void setWebSettings(int fontSize) {
        WebSettings settings = getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setBuiltInZoomControls(false);
        settings.setDefaultFontSize(fontSize);
        // settings.setDefaultTextEncodingName("utf-8");
        addWebSettings(settings);
        setWebChromeClient(new WebChromeClient());
        invalidate();

    }

    /*
     * Need to wait until view has figured out how big web page is Otherwise, we
     * can't scroll to last position because getContentHeight() returns 0. At
     * current time, there is no replacement for PictureListener
     */
    @SuppressWarnings("deprecation")
    private PictureListener mPictureListener = new PictureListener() {
        @Override
        @Deprecated
        public void onNewPicture(WebView view, Picture picture) {
            // stop listening
            setPictureListener(null);

            scrollTo(0, (int) (getContentHeight() * mScrollY));
            mScrollY = 0.0f;
        }
    };

    public int getPositionCurrent() {
        return position_current;
    }

    public void getBookNameNumberNext() {
        Log.e("position_current 1189", "" + position_current);
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
//            if (position_current > 1255)
            if (position_current > 1187)
                return;
        } else {
//            if (position_current > 1254)
            if (position_current > 1187)
                return;
        }
        if (onPageChange != null)
            onPageChange.onPageChanged(++position_current);
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            preferencesBook.edit().putInt("bookMark", position_current).commit();
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", position_current - 1).commit();

            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.EQBOOK, position_current);
            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.EQBOOK, position_current);
            PageUTilsBookName.getEq(position_current);
            PageUTilsBookNumber.getEq(position_current);

            if (PageUTilsBookName.getEq(position_current).contains("Solomon Dwom (Nnwom mu Dwom)")) {
                ((MainActivity) EpubReader.getActivityIsntanse())
                        .changeBookName("Solomon Dwom.." + " " + PageUTilsBookNumber.getEq(position_current));
            } else {
                ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                        PageUTilsBookName.getEq(position_current) + " " + PageUTilsBookNumber.getEq(position_current));
            }

        } else {
            preferencesBook.edit().putInt("bookMark", position_current).commit();
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", position_current).commit();
            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.ENGLISHBOOK, position_current);
            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.ENGLISHBOOK, position_current);
            PageUTilsBookName.getEng(position_current);
            PageUTilsBookNumber.getEng(position_current);
			/*
			 * ((MainActivity) EpubReader.getActivityIsntanse())
			 * .changeBookNumber(PageUTilsBookNumber .getEng(position_current));
			 */
            ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                    PageUTilsBookName.getEng(position_current) + " " + PageUTilsBookNumber.getEng(position_current));

        }

    }

    public void getBookNameNumberPrevious() {
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            if (position_current < (Constants.POS0_START + 1))
                return;
            else
                --position_current;
        } else {
            if (position_current < Constants.POS0_START)
                return;
            else
                --position_current;
        }

        if (onPageChange != null)
            onPageChange.onPageChanged(position_current);
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            preferencesBook.edit().putInt("bookMark", position_current).commit();
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", position_current - 1).commit();
            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.EQBOOK, position_current);
            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.EQBOOK, position_current);
            PageUTilsBookName.getEq(position_current);
            PageUTilsBookNumber.getEq(position_current);

            if (PageUTilsBookName.getEq(position_current).contains("Song of Solomon")) {
                ((MainActivity) EpubReader.getActivityIsntanse())
                        .changeBookName("Song of Solomon.." + " " + PageUTilsBookNumber.getEq(position_current));
            } else {
                ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                        PageUTilsBookName.getEq(position_current) + " " + PageUTilsBookNumber.getEq(position_current));
            }

        } else {
            preferencesBook.edit().putInt("bookMark", position_current).commit();
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", position_current).commit();
            PageUTilsBookName.getPageCountBook(LanguageTypeBookName.ENGLISHBOOK, position_current);
            PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.ENGLISHBOOK, position_current);
            PageUTilsBookName.getEng(position_current);
            PageUTilsBookNumber.getEng(position_current);
			/*
			 * ((MainActivity) EpubReader.getActivityIsntanse())
			 * .changeBookNumber(PageUTilsBookNumber .getEng(position_current));
			 */
            ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName(
                    PageUTilsBookName.getEng(position_current) + " " + PageUTilsBookNumber.getEng(position_current));

        }

    }

    // Ph. 150418
    public void findAllAsync(String search) {
        if (Build.VERSION.SDK_INT >= 16)
            super.findAllAsync(search);

        // custom
        // findNext(true);
        @SuppressWarnings("deprecation")
        int findAll = findAll(search);
        // try {
        // Method method = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
        // method.invoke(this, true);
        // } catch (Throwable ignored) {
        // Log.e("TWI", "findAll ignored ");
        // }
    }
}

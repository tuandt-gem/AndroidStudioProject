package com.nivbible;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.adapter.IConstant;
import com.bossturban.webviewmarker.TextSelectionSupport;
import com.example.EpubProject.EpubWebView.IOnPageChange;
import com.example.EpubProject.EpubWebView30;
import com.example.EpubProject.Globals;
import com.example.EpubProject.IResourceSource;
import com.example.EpubProject.MainActivity;
import com.example.EpubProject.MainActivity.IOnBookMarkclick;
import com.example.EpubProject.MainActivity.IOnNightModeClick;
import com.example.EpubProject.MainActivity.IOnSearchclick;
import com.example.EpubProject.MainActivity.IOnShareListner;
import com.example.EpubProject.MainActivity.Search;
import com.example.EpubProject.MainActivity.launchChapters;
import com.example.EpubProject.ResourceResponse;
import com.example.EpubProject.TextToSpeechWrapper;
import com.example.EpubProject.Utility;
import com.example.EpubProject.utils.Constants;
import com.example.EpubProject.utils.MakeExternalDirectoryFromAssets;
import com.example.EpubProject.utils.MakeExternalDirectoryFromAssets.IOnCopyCompleteListener;
import com.example.EpubProject.utils.PageUTilsBookName;
import com.example.EpubProject.utils.PageUTilsBookName.LanguageTypeBookName;
import com.example.EpubProject.utils.PageUTilsBookNumber;
import com.example.EpubProject.utils.PageUTilsBookNumber.LanguageTypeBookNumber;
import com.example.WebServer.FileRequestHandler;
import com.example.WebServer.ServerSocketThread;
import com.example.WebServer.WebServer;
import com.example.epub.Book;
import com.example.epub.TableOfContents;
import com.example.epub.db.DbHelper;
import com.example.epub.selection.Selection;
import com.example.epub.selection.SelectionColor;
import com.example.epub.share.ShareUtilities;
import com.example.popwindow.QuickAction;
import com.example.popwindow.SelectionPopup;
import com.example.popwindow.SelectionPopup.ActionId;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nivbible.ChapterFragment.IOnChapterSelected;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.siegmann.epublib.epub.Main;
import nl.siegmann.epublib.util.StringUtil;

public class ReaderFragment extends Fragment implements IResourceSource, launchChapters, Search, IOnBookMarkclick,
        OnItemClickListener, OnClickListener, TextWatcher, OnEditorActionListener, IOnChapterSelected, IOnShareListner,
        IOnCopyCompleteListener, IOnPageChange, IOnSearchclick, OnDismissListener, IOnNightModeClick, MainActivity.IOnDrawerStateChanged {
    public static final String KEY_VERSE_NUMBER = "verse_number";
    public static final String KEY_PAGE_POSITION = "page_position";
    public static final String KEY_HIGHLIGHT_ON_SCROLL = "key_highlight_on_scroll";

    private final static int LIST_EPUB_ACTIVITY_ID = 0;
    private final static int LIST_CHAPTER_ACTIVITY_ID = 1;
    private final static int CHECK_TTS_ACTIVITY_ID = 2;

    private Activity mActivity;

    private EditText findBox;

    String filename;
    String pageUri;

    private TextView text_view_total_number;
    //    public static final String BOOKMARK_EXTRA = "BOOKMARK_EXTRA";
    String fileUri;
    private SharedPreferences preferencesBook;
    private SharedPreferences preferencesBookNumber;
    private int mPageNumber = 1;

    /*
     * the app's main view
     */
    private EpubWebView30 mEpubWebView;

    TextToSpeechWrapper mTtsWrapper;

    private ServerSocketThread mWebServerThread = null;

    public EpubReader application;
    public String mCurrentHighlightedCellId = "";
    //    public String mCurrentHighlightedHtml = "";
    public String mCurrentHighlightedColor = "";
    public int mCurrentMenuIndex = -1;

    /**
     * The context menu.
     */
    protected SelectionPopup mContextMenu;

    /**
     * Flag to stop from showing context menu twice.
     */
    protected boolean mContextMenuVisible = false;
    private TextSelectionSupport mTextSelectionSupport;

    private Selection mCurrentSelection = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.background_fragment, null);

        // Preferences
        SharedPreferences sharedPreferencesSize = getActivity().getSharedPreferences("fontSize", 0);
        preferencesBook = getActivity().getSharedPreferences("book", 0);
        preferencesBookNumber = getActivity().getSharedPreferences("booknumber", 0);

        initViews(rootView);

        // Init by arguments
        Bundle args = getArguments();
        int txtSize;
        if (args != null) {
            txtSize = args.getInt(IConstant.FONTSIZE);
            if (txtSize == 0) {
                txtSize = sharedPreferencesSize.getInt(IConstant.TXTFONTSIZES, 18);
            }

        } else {
            if (sharedPreferencesSize.getBoolean(IConstant.TXTFONT, true)) {
                txtSize = sharedPreferencesSize.getInt(IConstant.TXTFONTSIZES, 18);

            } else {
                txtSize = 18;
            }
        }

        // Webview reader settings
        mEpubWebView.setWebSettings(txtSize);
        mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber);

        preferencesBook.edit().putInt("bookMark", mPageNumber).commit();

        mTtsWrapper = new TextToSpeechWrapper();
        mWebServerThread = createWebServer();
        mWebServerThread.startThread();
        text_view_total_number.setText(String.valueOf(mPageNumber));

        application = (EpubReader) getActivity().getApplication();
        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            mPageNumber = Constants.POS0_START;
            preferencesBook.edit().putInt("bookMark", mPageNumber).commit();
        } else {
            mPageNumber = Constants.POS1_START;
            preferencesBook.edit().putInt("bookMark", mPageNumber).commit();
        }

        filename = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.SELECTED_FILE,
                Constants.AKUAPEM);

        if (args != null) {
            ((MainActivity) getActivity()).isboomarkmarkw();
            fileUri = args.getString(Constants.FILE_URI);
//            if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
//                onListEpubResult(Constants.AKUAPEM);
//            } else {
//                onListEpubResult(Constants.ENGLISH);
//            }

        }
//        else {
//            if (preferencesBook.getInt("position", Constants.POS0_START) == 0) {
//                onListEpubResult(Constants.AKUAPEM);
//            } else {
//                if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
//                    onListEpubResult(Constants.AKUAPEM);
//                } else {
//                    onListEpubResult(Constants.ENGLISH);
//                }
//            }
//        }
        onListEpubResult(filename);

        ((MainActivity) EpubReader.getActivityIsntanse()).setMarked(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleBookMark(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleNightMode(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).setChapters(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setSearch(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).getActionBarView().setOnItemClickListner(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName("Genesis 1");
        ((MainActivity) EpubReader.getActivityIsntanse()).setBookMarkclick(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setNightModeClick(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setOnDrawerStateChanged(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setOnShareListner(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setSearchclick(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).setSearch(this);

        initSelectionWebView();

        // Go to note page
        if (args != null) {
            int page = args.getInt(KEY_PAGE_POSITION);
            onChapterSelected(page);
        }
        return rootView;
    }

    private void initSelectionWebView() {
        mTextSelectionSupport = TextSelectionSupport.support(getActivity(), mEpubWebView);

        mTextSelectionSupport.setSelectionListener(new TextSelectionListener());

        mEpubWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // this method will proceed your url however if certification
                // issues are there or not
                handler.proceed();
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                mTextSelectionSupport.onScaleChanged(oldScale, newScale);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Disable link navigate
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                finishPage();
                super.onPageFinished(view, url);
            }

            @Override
            @SuppressWarnings("deprecation")
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return mEpubWebView.onRequest(url);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private ServerSocketThread createWebServer() {
        FileRequestHandler handler = new FileRequestHandler(this);
        WebServer server = new WebServer(handler);
        return new ServerSocketThread(server, Globals.WEB_SERVER_PORT);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void bookMark() {
        try {
            mEpubWebView.setDrawingCacheEnabled(true);
            Bitmap screenShot = mEpubWebView.getDrawingCache();
            if (screenShot == null) {
                return;
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            screenShot.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            // String url = mEpubWebView.getOriginalUrl().toString();
            // pageUri = Base64EEncoder.base64Encode(url);
            pageUri = String.valueOf(preferencesBook.getInt("bookMark", 1));
            File mainFile = new File(Environment.getExternalStorageDirectory(), filename);
            File screenShotFile = new File((mainFile.getAbsoluteFile()) + File.separator + pageUri);
            if (mainFile.mkdirs()) {
                screenShotFile = new File((mainFile.getAbsoluteFile()) + File.separator + pageUri);
                screenShotFile.createNewFile();
                FileOutputStream fo = new FileOutputStream(screenShotFile);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } else {
                if (screenShotFile.exists()) {
                    screenShotFile.delete();
                } else {
                    screenShotFile = new File((mainFile.getAbsoluteFile()) + File.separator + pageUri);
                    screenShotFile.createNewFile();
                    FileOutputStream fo = new FileOutputStream(screenShotFile);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                }
            }
            updateStatus();
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }

    }

    public void updateStatus() {
        // for screen short file to be open on gallery
        // pageUri = Base64EEncoder.base64Encode(url);
        pageUri = String.valueOf(preferencesBook.getInt("bookMark", 1));
        File mainFile = new File(Environment.getExternalStorageDirectory(), filename);
        File screenShotFile = new File((mainFile.getAbsoluteFile()) + File.separator + pageUri);
        ((MainActivity) EpubReader.getActivityIsntanse()).setMarked(screenShotFile.exists());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_TTS_ACTIVITY_ID) {
            mTtsWrapper.checkTestToSpeechResult(EpubReader.getActivityIsntanse(), resultCode);
            return;
        }
        switch (requestCode) {
            case LIST_EPUB_ACTIVITY_ID:

                // onListEpubResult(data);
                break;

            case LIST_CHAPTER_ACTIVITY_ID:

                // onListChapterResult(data);
                break;

            default:

        }

    }

    private void onListEpubResult(String dataname) {
        loadPdf(dataname);
    }

    private void onListChapterResultSele(int pos) {
        mEpubWebView.loadChapterPos(null, pos);
    }

//    public void loadEpub(String fileName, Uri chapterUri) {
//        if (fileName == null && chapterUri == null)
//            return;
//        mEpubWebView.setBook(fileName, mActivity);
//        mEpubWebView.loadChapter(chapterUri);
//    }

    public Book getBook() {
        return mEpubWebView.getBook();
    }

    @Override
    public ResourceResponse fetch(Uri resourceUri) {
        return getBook().fetch(resourceUri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTtsWrapper.onDestroy();
        mWebServerThread.stopThread();
    }

    @Override
    public void launchChapterList() {
        Book book = getBook();
        if (book == null) {
            Utility.showToast(EpubReader.getActivityIsntanse(), R.string.no_book_selected);
        } else {
            TableOfContents toc = book.getTableOfContents();
            if (toc.size() == 0) {
                Utility.showToast(EpubReader.getActivityIsntanse(), R.string.table_of_contents_missing);
            } else {

                ChapterFragment chapfrag = new ChapterFragment();
                chapfrag.setOnChapterSelected(this);
                if (!EpubReader.getFlowInstanse().isToAdd(chapfrag))
                    return;
                Bundle bundle = new Bundle();
                toc.pack(bundle, "CHAPTERS_EXTRA");
                EpubReader.getFlowInstanse().add(chapfrag, bundle, true);
            }
        }
    }

    @Override
    public void showSearch() {
        findBox.setHint("Search");
        try {
            mEpubWebView.findAllAsync(findBox.getText().toString());

            Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
            m.invoke(mEpubWebView, true);
        } catch (Exception ex) {
            Log.e(ReaderFragment.class.getName(), "showSearch error\n" + ex.getMessage());
        }
    }

    @Override
    public boolean onBookMarClicked() {
        mEpubWebView.setDrawingCacheEnabled(false);
        mEpubWebView.post(new Runnable() {

            @Override
            public void run() {
                bookMark();
            }
        });

        return true;
    }

    /**
     * Init views in fragment
     */
    private void initViews(View parent) {
        RelativeLayout relativeLayout = (RelativeLayout) parent.findViewById(R.id.serachBar);
        EpubReader.setLayout(relativeLayout);
        Button chaptersButton = (Button) parent.findViewById(R.id.btn_chapters);
        chaptersButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                launchChapterList();
            }
        });

        ImageView left_arrow_key = (ImageView) parent.findViewById(R.id.left_arrow_key);
        ImageView right_arrow_key = (ImageView) parent.findViewById(R.id.right_arrow_key);
        mEpubWebView = (EpubWebView30) parent.findViewById(R.id.webview);
        text_view_total_number = (TextView) parent.findViewById(R.id.text_view_total_number);
        findBox = (EditText) parent.findViewById(R.id.edt_search_fragment_reader);
        findBox.setFocusableInTouchMode(true);
        ImageView closeButton = (ImageView) parent.findViewById(R.id.img_cancel_search_fragment_reader);
        closeButton.setOnClickListener(this);
        findBox.addTextChangedListener(this);
        findBox.setOnEditorActionListener(this);

        left_arrow_key.setOnClickListener(this);
        right_arrow_key.setOnClickListener(this);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(findBox.getWindowToken(), 0);
    }

//    void showKeyboard() {
//        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null)
//            imm.showSoftInput(findBox, 0);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
        } else {
            preferencesBook.edit().putInt("position", Constants.POS1_START).commit();
        }

        loadPdf(Constants.FILE_NAME[position]);

        // onListEpubResult(Constants.FILE_NAME[position]);
        filename = Constants.FILE_NAME[position];
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit()
                .putString(Constants.SELECTED_FILE, Constants.FILE_NAME[position])
                .putString(Constants.FILE_URI, pageUri).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edt_search_fragment_reader:
                searchModeOn();

                break;
            case R.id.img_cancel_search_fragment_reader:
                searchModeOff();
                break;

            case R.id.left_arrow_key:
                mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber);
                mEpubWebView.previousButton();

                break;
            case R.id.right_arrow_key:
                mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber);
                mEpubWebView.nextButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        showSearch();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        showSearch();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (mEpubWebView == null)
            return false;
        if (v.getText().toString().length() > 0) {
            showSearch();
        }

        if (actionId == KeyEvent.ACTION_DOWN)
            showSearch();

        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onChapterSelected(int pos) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        Fragment f = getFragmentManager().findFragmentByTag(ChapterFragment.class.getName());
        if (f != null) {
            trans.remove(f);
            trans.commit();
            manager.popBackStack();
        }

//        if (pos != 0) {
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleBookMark(true);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(true);
            /*
             * ((MainActivity) EpubReader.getActivityIsntanse())
             * .visiblechangeBookNumber(true);
             */

        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", pos - 1).commit();
        } else {
            preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
            preferencesBook.edit().putInt("changeBook", pos).commit();

        }

        onListChapterResultSele(pos);
        Log.e("@@@", "pos=" + pos);
        if (preferencesBookNumber.getBoolean("chapter", true)) {

            if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                mPageNumber = (preferencesBookNumber.getInt("totalpage", 0));
                text_view_total_number.setText("" + mPageNumber);
                mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber + (Constants.POS0_START - 1));
                mPageNumber += (Constants.POS0_START - 1);
                preferencesBook.edit().putInt("bookMark", mPageNumber).commit();
            } else {
                mPageNumber = (preferencesBookNumber.getInt("totalpage", 0));
                text_view_total_number.setText("" + mPageNumber);
                mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber + (Constants.POS1_START - 1));
                mPageNumber += (Constants.POS1_START - 1);
                preferencesBook.edit().putInt("bookMark", mPageNumber).commit();
            }

        } else {
            mPageNumber = 1;
            text_view_total_number.setText("" + mPageNumber);
            mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber);
        }
//        }
    }

    @Override
    public boolean onShareClick(final Context context) {
        if (context == null) {
            return false;
        }

        mEpubWebView.setDrawingCacheEnabled(true);

        mEpubWebView.post(new Runnable() {

            @Override
            public void run() {
                shareImage(context);
            }
        });

        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void shareImage(Context context) {
        Bitmap screenShot = mEpubWebView.getDrawingCache();
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SharePage";
        File dir = new File(file_path);

        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "sharePage.jpeg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            screenShot.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            if (!file.exists()) {
                Toast.makeText(context, "Failed to share", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/*");
            if (context != null) context.startActivity(Intent.createChooser(shareIntent, "Share"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void loadPdf(String name) {
        new MakeExternalDirectoryFromAssets(mActivity, this).execute(name);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCopyCompleted(File file, Uri chapterUri) {
        if (file == null || !file.exists() || mActivity == null) {
            return;
        }

        mEpubWebView.setBook(file.toString(), mActivity);

        if (fileUri != null) {
            if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                mPageNumber = Integer.parseInt(fileUri);
                preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
                preferencesBook.edit().putInt("changeBook", mPageNumber - 1).commit();
                int j = Integer.parseInt(fileUri);
                mEpubWebView.setOnReceive(ReaderFragment.this, j - (Constants.POS0_START - 1));
                PageUTilsBookName.getPageCountBook(LanguageTypeBookName.EQBOOK, j);
                PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.EQBOOK, j);
                PageUTilsBookName.getEq(j);
                PageUTilsBookNumber.getEq(j);

                ((MainActivity) EpubReader.getActivityIsntanse())
                        .changeBookName(PageUTilsBookName.getEq(j) + " " + PageUTilsBookNumber.getEq(j));

                text_view_total_number.setText("" + (Integer.parseInt(fileUri) - (Constants.POS0_START - 1)));
                preferencesBook.edit().putInt("bookMark", Integer.parseInt(fileUri)).commit();
                preferencesBookNumber.edit().putBoolean("chapter", false).commit();
                mEpubWebView.loadChapterPos(null, Integer.parseInt(fileUri));
                fileUri = null;
            } else {
                mPageNumber = Integer.parseInt(fileUri);
                preferencesBook.edit().putBoolean("changeBookBoolean", true).commit();
                preferencesBook.edit().putInt("changeBook", mPageNumber).commit();
                int j = Integer.parseInt(fileUri);
                mEpubWebView.setOnReceive(ReaderFragment.this, j - (Constants.POS1_START - 1));
                PageUTilsBookName.getPageCountBook(LanguageTypeBookName.ENGLISHBOOK, j);
                PageUTilsBookNumber.getPageCountBookNumber(LanguageTypeBookNumber.ENGLISHBOOK, j);
                PageUTilsBookName.getEng(j);
                PageUTilsBookNumber.getEng(j);
                ((MainActivity) EpubReader.getActivityIsntanse())
                        .changeBookName(PageUTilsBookName.getEng(j) + " " + PageUTilsBookNumber.getEng(j));
                text_view_total_number.setText("" + (Integer.parseInt(fileUri) - (Constants.POS1_START - 1)));

                preferencesBook.edit().putInt("bookMark", Integer.parseInt(fileUri)).commit();
                preferencesBookNumber.edit().putBoolean("chapter", false).commit();
                mEpubWebView.loadChapterPos(null, Integer.parseInt(fileUri));
                fileUri = null;

            }

        } else {

            if (preferencesBook.getBoolean("changeBookBoolean", true)) {
                if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                    mPageNumber = preferencesBook.getInt("changeBook", 0);
                    text_view_total_number.setText("" + (mPageNumber - (Constants.POS0_START - 2)));
                    preferencesBook.edit().putInt("bookMark", mPageNumber + 1).commit();
                    mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber + (Constants.POS0_START - 1));
                    mEpubWebView.loadChapterPos(chapterUri, mPageNumber + 1);
                    mPageNumber = preferencesBook.getInt("changeBook", 0) + 1;
                } else {
                    mPageNumber = preferencesBook.getInt("changeBook", 0);
                    text_view_total_number.setText("" + (mPageNumber - (Constants.POS1_START - 1)));
                    preferencesBook.edit().putInt("bookMark", mPageNumber).commit();
                    mEpubWebView.setOnReceive(ReaderFragment.this, mPageNumber + (Constants.POS1_START - 2));
                    mEpubWebView.loadChapterPos(chapterUri, mPageNumber);
                }

            } else {
                ((MainActivity) EpubReader.getActivityIsntanse()).changeBookName("Genesis 1");
                mEpubWebView.loadChapter(chapterUri);
            }
        }
    }

    private boolean mIsInitialized = false;

    private void finishPage() {
        updateStatus();
        showSearch();

        mEpubWebView.setHighlightWithLanguage(application, mPageNumber);
        mEpubWebView.onPageLoaded();

//        setSelectionsInPage();
        runOnUiThread(mSetSelectionInPageRunner);

        // Night mode
        boolean isNightMode = preferencesBook.getBoolean(MainActivity.KEY_NIGHT_MODE, false);
        setNightMode(isNightMode);

        // Goto verse number
        Bundle args = getArguments();
        if (!mIsInitialized && args != null) {
            int verseNumber = args.getInt(KEY_VERSE_NUMBER, -1);
            if (verseNumber >= 0) {
                boolean highlightAfterScroll = args.getBoolean(KEY_HIGHLIGHT_ON_SCROLL, false);
                runOnUiThread(new ScrollToVerseRunner(verseNumber, highlightAfterScroll));
            }

            mIsInitialized = true;
        }

        hideContextMenu();
    }



    @Override
    public void onStateChanged(boolean opened) {
        mTextSelectionSupport.endSelectionMode();
    }

    private class ScrollToVerseRunner implements Runnable {
        private int mVerseNumber;
        private boolean mHighlightAfterScroll;

        public ScrollToVerseRunner(int verseNumber, boolean highlightAfterScroll) {
            mVerseNumber = verseNumber;
            mHighlightAfterScroll = highlightAfterScroll;
        }

        @Override
        public void run() {
            String js = "javascript:scrollToVerseNumber(" + mVerseNumber + ", " + mHighlightAfterScroll + ");";
            mEpubWebView.loadUrl(js);
        }
    }

    ;

    private Runnable mSetSelectionInPageRunner = new Runnable() {
        @Override
        public void run() {
            String removeNoteJs = "javascript:removeAllNoteIcon();";
            mEpubWebView.loadUrl(removeNoteJs);

            DbHelper db = new DbHelper(mActivity);
            try {
                List<Selection> selections = db.getByPage(filename, mEpubWebView.getPositionCurrent());

                Log.i("Reader", "Selections in page " + selections.size());
                for (Selection selection : selections) {
                    restoreSelection(selection);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            // Remove all range & clear selections
            String js = "javascript:android.selection.clearSelection();";
            mEpubWebView.loadUrl(js);
        }
    };

    /**
     * Set selections on current page
     */
    /*private void setSelectionsInPage() {
        String removeNoteJs = "javascript:removeAllNoteIcon();";
        mEpubWebView.loadUrl(removeNoteJs);

        DbHelper db = new DbHelper(mActivity);
        try {
            List<Selection> selections = db.getByPage(filename, mEpubWebView.getPositionCurrent());

            Log.i("Reader", "Selections in page " + selections.size());
            for (Selection selection : selections) {
                restoreSelection(selection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remove all range & clear selections
        String js = "javascript:android.selection.clearSelection();";
        mEpubWebView.loadUrl(js);
    }*/

    private class SelectSelectionRunner implements Runnable {
        private Selection mSelection;

        public SelectSelectionRunner(Selection mSelection) {
            this.mSelection = mSelection;
        }

        @Override
        public void run() {
            mCurrentSelection = mSelection;

            // Pass 0 to js to tell that no note in selection
            int id = StringUtil.isEmpty(mSelection.getNote()) ? 0 : mSelection.getId();
//            String js = String.format("javascript:selectSelectionRange(%d,%d,\"%s\",%d);",
//                    mSelection.getStart(), mSelection.getEnd(), mSelection.getColor(), id);
            String js = String.format("javascript:selectSelectionRange(%d,%d);",
                    mSelection.getStart(), mSelection.getEnd());
            mEpubWebView.loadUrl(js);
        }
    }

    /**
     * Select a selection
     */
    /*private void selectSelection(final Selection selection) {
        mCurrentSelection = selection;

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Pass 0 to js to tell that no note in selection
                int id = StringUtil.isEmpty(selection.getNote()) ? 0 : selection.getId();
                String js = String.format("javascript:selectSelectionRange(%d,%d,\"%s\",%d);",
                        selection.getStart(), selection.getEnd(), selection.getColor(), id);
                mEpubWebView.loadUrl(js);
            }
        });
    }*/

    /**
     * Restore a selection
     */
    private void restoreSelection(Selection selection) {
//        if (selection == null) {
//            return;
//        }
        // Pass 0 to js to tell that no note in selection
        int id = StringUtil.isEmpty(selection.getNote()) ? 0 : selection.getId();

        String js = String.format("javascript:restoreSavedSelection(%d,%d,\"%s\",%d);",
                selection.getStart(), selection.getEnd(), selection.getColor(), id);
        mEpubWebView.loadUrl(js);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageChanged(int position) {
        if (position >= 0) {
            ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(true);
            ((MainActivity) EpubReader.getActivityIsntanse()).visibleBookMark(true);

            if (preferencesBookNumber.getBoolean("chapter", true)) {
                if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                    preferencesBook.edit().putInt("bookMark", position).commit();
                    mPageNumber = position;
                    position -= (Constants.POS0_START - 1);
                    text_view_total_number.setText(String.valueOf(position));
                } else {
                    preferencesBook.edit().putInt("bookMark", position).commit();
                    mPageNumber = position;
                    position -= (Constants.POS1_START - 1);
                    text_view_total_number.setText("" + position);
                }
            } else {
                if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
                    preferencesBook.edit().putInt("bookMark", position).commit();
                    mPageNumber = position;
                    position -= (Constants.POS0_START - 1);
                    text_view_total_number.setText("" + position);
                } else {
                    preferencesBook.edit().putInt("bookMark", position).commit();
                    mPageNumber = position;
                    position -= (Constants.POS1_START - 1);
                    text_view_total_number.setText("" + position);
                }
            }
        }
    }

    @Override
    public void onSeacrchClicked(boolean f) {
//        if (f) {
//            searchModeOn();
//        } else {
//            searchModeOff();
//        }
        ((MainActivity) mActivity).gotoSearch();
        mTextSelectionSupport.endSelectionMode();
    }

    void searchModeOn() {
        findBox.requestFocus();
    }

    void searchModeOff() {
        findBox.setText("");
        hideKeyboard();

    }

    @SuppressWarnings("UnusedParameters")
    public void onMenuItemClick(View clickedView, int position) {
        Log.i(ReaderFragment.class.getSimpleName(), "onMenuItemClick " + position);
        mCurrentHighlightedColor = "";
        mCurrentMenuIndex = position;
        switch (position) {
            case 1:
            case 2:
            case 3:
            case 4:
                mCurrentHighlightedColor = Constants.highlightColors[position - 1];
                mEpubWebView.setHighlightCellIdWithColor(mCurrentHighlightedCellId, mCurrentHighlightedColor, false);
                break;
            case 5:
                mEpubWebView.setHighlightCellIdWithColor(mCurrentHighlightedCellId, null, false);
                application.mGlobals.removeHighlight(mPageNumber, mCurrentHighlightedCellId);
                break;
            case 6:
            case 7:
            case 8:
                mEpubWebView.setHighlightCellIdWithColor(mCurrentHighlightedCellId, null, true);
                break;
            default:
                mEpubWebView.setHighlightCellIdWithColor(mCurrentHighlightedCellId, null, true);
                break;
        }
    }

//    public void shareHighlightedImage(String packageName) {
//        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SharePage";
//        File dir = new File(file_path);
//        File file = new File(dir, "sharePage_highlight.jpeg");
//        if (!file.exists()) {
//            Toast.makeText(mActivity, "Failed to share", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            shareIntent.setType("image/*");
//            shareIntent.setPackage(packageName);
//            startActivity(Intent.createChooser(shareIntent, "Share"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void shareFileByEmail() {
//        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/SharePage/sharePage_highlight.jpeg";
//        File file = new File(file_path);
//        if (!file.exists()) {
//            Toast.makeText(mActivity, "Failed to share", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(mActivity);
//        builder.setType("message/rfc822");
//        builder.setSubject("");
//        builder.setStream(Uri.fromFile(file));
//        // builder.setText("I just watched a great video "
//        // + "in David Letterman�s Top Ten List app. Check it out.\n"+
//        // video.sharing_url);
//        builder.setChooserTitle("Share with...");
//
//        builder.startChooser();
//    }

    /**
     * Hide context menu
     */
    private void hideContextMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mContextMenu != null) {
                    mContextMenu.dismiss();
                }
            }
        });
    }

    /**
     * Init views & action of context menu
     */
    private void initContextMenu() {
        if (mActivity == null) {
            return;
        }

        mContextMenu = new SelectionPopup(mActivity, mCurrentSelection.getId() > 0);
        mContextMenu.setOnDismissListener(this);

        // setup the action item click listener
        mContextMenu.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(QuickAction source, View v, int pos, int actionId) {
                switch (actionId) {
                    case ActionId.ACTION_COLOR_GREEN:
                        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.GREEN));
                        break;

                    case ActionId.ACTION_COLOR_PINK:
                        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.PINK));
                        break;

                    case ActionId.ACTION_COLOR_PURPLE:
                        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.PURPLE));
                        break;


                    case ActionId.ACTION_COLOR_YELLOW:
                        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.YELLOW));
                        break;

                    case ActionId.ACTION_UNDERLINE:
                        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.UNDERLINE));
                        break;

                    case ActionId.ACTION_COLOR_REMOVE:
                        showConfirmRemoveDialog();
                        break;

                    case ActionId.ACTION_FACEBOOK:
                        ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("")).build();
                        ShareDialog.show(mActivity, content);
                        break;

                    case ActionId.ACTION_TWITTER:
                        String text = mCurrentSelection == null ? "" : mCurrentSelection.getText();
                        ShareUtilities.shareTw(v, text);
                        break;

                    case ActionId.ACTION_COPY:
                        Utility.copyToClipboard(mActivity, mCurrentSelection.getText());
                        break;

                    case ActionId.ACTION_SHARE:
                        String shareText = mCurrentSelection == null ? "" : mCurrentSelection.getText();
                        ShareUtilities.shareText(mActivity, shareText);
                        break;

                    case ActionId.ACTION_NOTE:
                        if (mCurrentSelection == null) {
                            return;
                        }

                        showNoteDialog();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    public void showConfirmRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this margin note?");
        builder.setPositiveButton("Yes, Delete it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeSelection();
            }
        });

        builder.setNeutralButton("No", null);
        builder.create().show();
    }

    private void removeSelection() {
        runOnUiThread(new MarkSelectionColorRunner(SelectionColor.INITIAL));
        hideContextMenu();
    }

    /**
     * Shows the context menu using the given region as an anchor point.
     */
    private void showContextMenu() {
        // Don't show this twice
        if (mContextMenuVisible) {
            return;
        }

        initContextMenu();

        mContextMenuVisible = true;

        // Get font size of current selection to place popup
        int fontSizeInPx = mCurrentSelection.getFontSize();
        final int fontSizeInDpi;

        if (fontSizeInPx <= 0) {
            fontSizeInDpi = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mEpubWebView.getSettings().getDefaultFontSize(),
                    getResources().getDisplayMetrics());
            Log.d(ReaderFragment.class.getSimpleName(), "getDefaultFontSize=" + mEpubWebView.getSettings().getDefaultFontSize());
        } else {
            fontSizeInDpi = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    fontSizeInPx,
                    getResources().getDisplayMetrics());
            Log.d(ReaderFragment.class.getSimpleName(), "fontSizeInDpi=" + fontSizeInDpi);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContextMenu.show(mTextSelectionSupport.getStartSelectionHandle(),
                        mTextSelectionSupport.getEndSelectionHandle(),
                        fontSizeInDpi);
            }
        });
    }

    /**
     * Shows the context menu using the given region as an anchor point.
     */
    private void showContextMenu(final int left, final int top, final int bottom, final int right, final int fontSize) {
        // Don't show this twice
        if (mContextMenuVisible) {
            return;
        }

        initContextMenu();

        mContextMenuVisible = true;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContextMenu.show(mEpubWebView, left, top, bottom, right, fontSize);
            }
        });
    }


    private void showNoteDialog() {
        final Dialog alertDialog = new Dialog(mActivity, R.style.CustomAlertDialog);

        ViewGroup dialogView = (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.dialog_note, null);

        // Edit text input
        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_note_et);
        editText.setText(mCurrentSelection.getNote());
        // Set cursor to end
        editText.setSelection(editText.getText().length());

        alertDialog.setContentView(dialogView);

        // Set note text
        TextView noteTextView =
                ((TextView) dialogView.findViewById(R.id.dialog_note_text));
        noteTextView.setText(mCurrentSelection.getText());

        // Set note color
        String color = mCurrentSelection.getColor();

        // Set verses number
        String chapterName = mEpubWebView.getChapterNameAndNumber();

        TextView versesView = (TextView) dialogView.findViewById(R.id.dialog_note_verse_tv);
        versesView.setText(Utility.formatNoteChapter(mCurrentSelection));

        if (color.equals(SelectionColor.INITIAL)) {
            color = SelectionColor.DEFAULT;
        }

        View colorView = dialogView.findViewById(R.id.dialog_note_color_iv);
        if (!color.equals(SelectionColor.UNDERLINE)) {
            colorView.setVisibility(View.VISIBLE);
            colorView.setBackgroundColor(Color.parseColor(color));
        } else {
            colorView.setVisibility(View.GONE);
            noteTextView.setPaintFlags(noteTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }


        // Save button
        dialogView.findViewById(R.id.dialog_note_save_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editText.getText().toString();
                runOnUiThread(new SaveNoteRunner(note));
                alertDialog.dismiss();
            }
        });

        // Close button
        dialogView.findViewById(R.id.dialog_note_close_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Delete button only show if existing a note
//        String note = mCurrentSelection.getNote();
//        if (note != null && note.trim().length() >= 0) {
//            dialogView.findViewById(R.id.dialog_note_save_bt).setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String note = edittext.getText().toString();
//                    runOnUiThread(new SaveNoteRunner(note));
//
//                }
//            });
//            alert.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mCurrentSelection.setNote(null);
//                    saveSelection();
//                    hideContextMenu();
////                    setSelectionsInPage();
//                    runOnUiThread(mSetSelectionInPageRunner);
//                }
//            });
//        }

        alertDialog.show();
    }

    private class SaveNoteRunner implements Runnable {
        private String mNote;

        public SaveNoteRunner(String mNote) {
            this.mNote = mNote;
        }

        @Override
        public void run() {
            // add note icon
            String js = String.format("javascript:insertNoteIcon(%d);", mCurrentSelection.getId());
            mEpubWebView.loadUrl(js);

            if (mCurrentSelection.getColor().equals(SelectionColor.INITIAL)) {
                mCurrentSelection.setColor(SelectionColor.DEFAULT);
            }

            mCurrentSelection.setNote(mNote);
//        markSelectionColor(mCurrentSelection.getColor());
            runOnUiThread(new MarkSelectionColorRunner(mCurrentSelection.getColor()));
//        hideContextMenu();
//        mTextSelectionSupport.endSelectionMode();
        }
    }

    /**
     * Add note to current selection
     */
    /*private void saveNote(String note) {
        // add note icon
        String js = String.format("javascript:insertNoteIcon(%d);", mCurrentSelection.getId());
        mEpubWebView.loadUrl(js);

        if (mCurrentSelection.getColor().equals(SelectionColor.INITIAL)) {
            mCurrentSelection.setColor(SelectionColor.DEFAULT);
        }

        mCurrentSelection.setNote(note);
//        markSelectionColor(mCurrentSelection.getColor());
        runOnUiThread(new MarkSelectionColorRunner(mCurrentSelection.getColor()));
//        hideContextMenu();
//        mTextSelectionSupport.endSelectionMode();
    }*/

    private class MarkSelectionColorRunner implements Runnable {
        private String mColor;

        public MarkSelectionColorRunner(String mColor) {
            this.mColor = mColor;
        }

        @Override
        public void run() {
            // Remove highlight or underline of selection first
//            int id = StringUtil.isEmpty(mCurrentSelection.getNote()) ? 0 : mCurrentSelection.getId();
            String removeJs = String.format("javascript:removeHighlightAndUnderline(%d,%d);",
                    mCurrentSelection.getStart(), mCurrentSelection.getEnd());
//            String removeJs = "javascript:unhighlightAll();";
            mEpubWebView.loadUrl(removeJs);

            mCurrentSelection.setColor(mColor);
            saveSelection();
            // Selecting new selection
//            if (mCurrentSelection.getId() <= 0) {
            // If underline selection
//                if (mCurrentSelection.getColor().equals(SelectionColor.UNDERLINE)) {
////                    String js = "javascript:underlineSelection();";
////                    mEpubWebView.loadUrl(js);
//                    saveSelection();
//                } else
//                if (!mCurrentSelection.getColor().equals(SelectionColor.INITIAL)) {
            // Only mark real color, Initial color mean remove
//                    String jsMarkColor = String.format("javascript:highlight(\"%s\");", mColor);
//                    mEpubWebView.loadUrl(jsMarkColor);
//                    saveSelection();
//                }
//            } else {
            // Change existing selection
//                mCurrentSelection.setColor(mColor);
//                saveSelection();
//                restoreSelection(mCurrentSelection);
//            }
            runOnUiThread(mSetSelectionInPageRunner);
            mTextSelectionSupport.endSelectionMode();
        }
    }
    /**
     * Mark selection with color
     */
    /*private void markSelectionColor(String color) {
        mCurrentSelection.setColor(color);

        // Selecting new selection
        if (mCurrentSelection.getId() <= 0) {
            // Only mark real color, Initial color mean remove
            if (!mCurrentSelection.getColor().equals(SelectionColor.INITIAL)) {
                String jsMarkColor = String.format("javascript:highlight(\"%s\");", color);
                mEpubWebView.loadUrl(jsMarkColor);
                saveSelection();
            }
        } else {
            // Change existing selection
            mCurrentSelection.setColor(color);
            saveSelection();

            restoreSelection(mCurrentSelection);

        }
    }*/

    /**
     * Save selection to DB
     */
    private void saveSelection() {
        if (mCurrentSelection == null) {
            Log.e("ReaderFragment", "Save selection null=.=");
            return;
        }

        Log.i("ReaderFragment", ":::Saving selection::: " + mCurrentSelection.getStart() + "-" + mCurrentSelection.getEnd());

//        mCurrentSelection.setColor(color);
        mCurrentSelection.setPageNumber(mEpubWebView.getPositionCurrent());
        mCurrentSelection.setFileName(filename);

        try {
            DbHelper db = new DbHelper(mActivity);
            db.createOrUpdateSelection(mCurrentSelection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        runOnUiThread(mSetSelectionInPageRunner);
//        mTextSelectionSupport.endSelectionMode();
    }

    private boolean handleSingleTouch(int start, int end) {
        DbHelper db = new DbHelper(mActivity);
        try {
            Selection selection = db.findSelectionFromPosition(filename, mEpubWebView.getPositionCurrent(), start, end);
            if (selection != null) {
//                selectSelection(selection);
                runOnUiThread(new SelectSelectionRunner(selection));
                Log.i(ReaderFragment.class.getSimpleName(), "Selected " + selection.getStart() + "-" + selection.getEnd());
                return true;
            } else {
                mTextSelectionSupport.endSelectionMode();
                Log.e(ReaderFragment.class.getSimpleName(), "Not found selection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Clears the selection when the context menu is dismissed.
     */
    public void onDismiss() {
        Log.v(this.getClass().getSimpleName(), "Dismiss popup");
        mContextMenuVisible = false;
    }

    @Override
    public void onNightModeClicked(boolean isNightMode) {
        setNightMode(isNightMode);
    }

    /**
     * Change dat/night mode
     */
    private void setNightMode(boolean isNightMode) {
        String js = String.format("javascript:changeNightMode(%b);", isNightMode);
        mEpubWebView.loadUrl(js);
    }

    /**
     * Run on main thread
     */
    private void runOnUiThread(Runnable runnable) {
        if (mActivity == null || runnable == null) {
            return;
        }

        mActivity.runOnUiThread(runnable);
    }

    /**
     * Text selection changes listener
     */
    private class TextSelectionListener implements TextSelectionSupport.SelectionListener {

        @Override
        public void startSelection() {
//                setSelectionsInPage();
            // Beware: this method is called after saveSelection method called
            showContextMenu();
        }

        @Override
        public void saveSelection(final int start, final int end, int startVerseNumber, int endVerseNumber, final String text, final int fontSize) {
            if (mCurrentSelection == null) {
                mCurrentSelection = new Selection();
            }

            mCurrentSelection.setStart(start);
            mCurrentSelection.setEnd(end);
            mCurrentSelection.setText(text);
            mCurrentSelection.setFontSize(fontSize);
            mCurrentSelection.setStartVerseNumber(startVerseNumber);
            mCurrentSelection.setEndVerseNumber(endVerseNumber);
        }

        @Override
        public void onSingleTouch(int start, int end) {
            handleSingleTouch(start, end);
        }

        @Override
        public void notifyRangeCoords(int left, int top, int bottom, int right, int fontSize) {
            showContextMenu(left, top, bottom, right, fontSize);
        }

        @Override
        public void selectionChanged(String text) {
        }

        @Override
        public void endSelection() {
            hideContextMenu();

            // If is in selection mode, reset selections after end
//            if (mCurrentSelection != null) {
//                setSelectionsInPage();
//                runOnUiThread(mSetSelectionInPageRunner);
//            }
            mCurrentSelection = null;
        }

        @Override
        public void startDrag() {
            hideContextMenu();
        }

        @Override
        public void endDrag() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String js = "javascript:endDrag();";
                    mEpubWebView.loadUrl(js);
                }
            });

//            setSelectionsInPage();
        }

        @Override
        public void onDragDone() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String js = String.format("javascript:onDragDone(\"%s\");", mCurrentSelection.getColor());
                    mEpubWebView.loadUrl(js);
                }
            });
        }

        @Override
        public void onHandlesDrawn() {
            showContextMenu();
        }

        @Override
        public void noteClicked(int id) {
            try {
                DbHelper db = new DbHelper(mActivity);
                Selection selection = db.getSelectionById(id);

                if (selection == null) {
                    return;
                }

//                selectSelection(selection);
                runOnUiThread(new SelectSelectionRunner(selection));
                showNoteDialog();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}

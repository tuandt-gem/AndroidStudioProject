package com.example.EpubProject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.EpubProject.DrawerIntializer.IOnDrawerStateChangeListner;
import com.example.EpubProject.customised.ActionBarView;
import com.example.EpubProject.utils.Constants;
import com.example.epub.Book;
import com.example.epub.db.DbHelper;
import com.example.epub.share.ShareUtilities;
import com.facebook.appevents.AppEventsLogger;
import com.nivbible.BookMarksFragment;
import com.nivbible.EpubReader;
import com.nivbible.FontSizeFragment;
import com.nivbible.HelpFragment;
import com.nivbible.NotesFragment;
import com.nivbible.R;
import com.nivbible.ReaderFragment;
import com.nivbible.SearchFragment;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
        implements IResourceSource, com.example.EpubProject.NavigationDrawer.IOnDrawerListner,
        IOnDrawerStateChangeListner, OnMenuItemClickListener, OnMenuItemLongClickListener {
    public static final String KEY_NIGHT_MODE = "isNightMode";
    private DrawerIntializer drawer;
    int _backBtnCount = 0;
    private ImageView _img_bookmark, reader_serach;
    private TextView text_view_bible_text_name;
    private ImageView reader_image, delete_icon, nightModeImage;
    // private static final int SEARCH_MENU_ID = Menu.FIRST;
    public static final String BOOKMARK_EXTRA = "BOOKMARK_EXTRA";
    private launchChapters chapters;
    private Search search;
    private SharedPreferences preferencesBook;
    private SharedPreferences preferencesBookNumber;
    private boolean isreaderfrg = false;
    private boolean isbookmarkwa = false;
    private boolean isbookreader = false;
    /*
     * the app's main view
     */
    private EpubWebView30 mEpubWebView;

    TextToSpeechWrapper mTtsWrapper;

    private DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        preferencesBook = getSharedPreferences("book", 0);
        preferencesBookNumber = getSharedPreferences("booknumber", 0);
        _img_bookmark = (ImageView) findViewById(R.id.img_bookmark);
        reader_image = (ImageView) findViewById(R.id.reader_image);
        delete_icon = (ImageView) findViewById(R.id.delete_icon);
        reader_serach = (ImageView) findViewById(R.id.reader_serach);
        text_view_bible_text_name = (TextView) findViewById(R.id.text_view_bible_text_name);
        nightModeImage = (ImageView) findViewById(R.id.img_day_night);
        // text_view_bible_text_number = (TextView)
        // findViewById(R.id.text_view_bible_text_number);

        EpubReader.initActivity(this);
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(Constants.SELECTED_FILE, Constants.FILE_NAME[0]).commit();
        preferencesBook.edit().putBoolean("changeBookBoolean", false).commit();
        EpubReader.getFlowInstanse().replace(new ReaderFragment(), false);
        EpubReader.getDrawerInstanse(this).setOnDrawerItemClickListner(this);
        EpubReader.getDrawerInstanse(this).setOnDrawerStateChangeListner(this);
        delete_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListner != null)
                    onDeleteListner.onDeleteClick();
            }
        });

        fragmentManager = getSupportFragmentManager();
        mMenuDialogFragment = ContextMenuDialogFragment
                .newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), getMenuObjects());

        // Night mode
        setNightMode(preferencesBook.getBoolean(KEY_NIGHT_MODE, false));
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<MenuObject>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_menuitem_close);

        MenuObject colorObj1 = new MenuObject("");
        colorObj1.setResource(R.drawable.menu_item_color1);
        MenuObject colorObj2 = new MenuObject("");
        colorObj2.setResource(R.drawable.menu_item_color2);
        MenuObject colorObj3 = new MenuObject("");
        colorObj3.setResource(R.drawable.menu_item_color3);
        MenuObject colorObj4 = new MenuObject("");
        colorObj4.setResource(R.drawable.menu_item_color4);

        MenuObject trash = new MenuObject();
        trash.setResource(R.drawable.ic_menuitem_trash);

        MenuObject shareFb = new MenuObject("Share to Facebook");
        shareFb.setResource(R.drawable.ic_menuitem_fb);
        MenuObject shareTw = new MenuObject("Share to Twitter");
        shareTw.setResource(R.drawable.ic_menuitem_tw);
        MenuObject shareEmail = new MenuObject("Share to Email");
        shareEmail.setResource(R.drawable.ic_menuitem_email);

        menuObjects.add(close);
        menuObjects.add(colorObj1);
        menuObjects.add(colorObj2);
        menuObjects.add(colorObj3);
        menuObjects.add(colorObj4);
        menuObjects.add(trash);
        menuObjects.add(shareFb);
        menuObjects.add(shareTw);
        menuObjects.add(shareEmail);
        return menuObjects;
    }

    public ActionBarView getActionBarView() {
        return ((ActionBarView) findViewById(R.id.activity_actionbar));
    }

    public void showDrawer(View view) {
        if (onDrawerStateChanged != null) {
            onDrawerStateChanged.onStateChanged(EpubReader.getDrawerInstanse(this).getSlider().isMenuShowing());
        }

        if (EpubReader.getDrawerInstanse(this) != null) {
            EpubReader.getDrawerInstanse(this).getSlider().toggle();
        }
    }

    private IOnBookMarkclick bookMarkclick;

    public interface IOnBookMarkclick {

        public boolean onBookMarClicked();

    }

    public void dayNightClicked(View view) {
        boolean isNightMode = preferencesBook.getBoolean(KEY_NIGHT_MODE, false);

        // Save to Preferences
        isNightMode = !isNightMode;
        Editor editor = preferencesBook.edit();
        editor.putBoolean(KEY_NIGHT_MODE, isNightMode);
        editor.commit();

        setNightMode(isNightMode);
    }

    private void setNightMode(boolean isNightMode) {
        if (isNightMode) {
            nightModeImage.setImageResource(R.drawable.ic_day);
        } else {
            nightModeImage.setImageResource(R.drawable.ic_night);
        }

        if (nightModeClick != null) {
            nightModeClick.onNightModeClicked(isNightMode);
        }
    }

    public void bookMarClick(View view) {
        if (bookMarkclick != null) {
            Log.e("not null", "not null");
            boolean isMarked = bookMarkclick.onBookMarClicked();
            setMarked(isMarked);
        } else {
            Log.e("null", "null");
        }
    }

    private boolean isClicked = true;

    public void serachBarClick(View view) {
        setClick(isClicked);
    }

    public void isboomarkmarkw() {
        isbookreader = true;
    }

    private IOnSearchclick searchclick;

    public interface IOnSearchclick {

        public void onSeacrchClicked(boolean f);

    }

    private IOnNightModeClick nightModeClick;

    public interface IOnNightModeClick {

        public void onNightModeClicked(boolean f);

    }

    private IOnDrawerStateChanged onDrawerStateChanged;

    public interface IOnDrawerStateChanged {
        public void onStateChanged(boolean opened);

    }

    private void setClick(boolean isClick) {
        Log.e(":setClick", "" + isClick);
        if (isClick) {
            EpubReader.getLayout().setVisibility(View.VISIBLE);
            searchclick.onSeacrchClicked(true);
            isClicked = false;
        } else {
            searchclick.onSeacrchClicked(false);
            EpubReader.getLayout().setVisibility(View.GONE);
            isClicked = true;
        }

    }

    @Override
    public void onBackPressed() {

        if (isreaderfrg) {
            visibleBookMark(false);
            visibleReader(false);
            visibleSearch(false);
        } else {
            visibleBookMark(true);
            visibleReader(true);
            visibleSearch(true);
        }

        if (isbookmarkwa) {
            visibleReader(true);
        }

        if (isbookreader) {
            visibleBookMark(true);
            visibleReader(true);
            visibleSearch(true);

        }

        if (!EpubReader.getFlowInstanse().hasNoMoreBack())
            super.onBackPressed();
        else {
            _backBtnCount++;
            if (_backBtnCount == 2) {
                preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
                preferencesBookNumber.edit().putBoolean("chapter", false).commit();
                preferencesBook.edit().putBoolean("changeBookBoolean", false).commit();
                System.exit(1);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Press back twice to exit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean isSelected = false;

    @Override
    public void onDrawerItemClick(View view) {
        int id = view.getId();
        if (id == R.id.lnr_bible_reader) {
            isreaderfrg = false;
            _backBtnCount = 0;
            if (!EpubReader.getFlowInstanse().hasNoMoreBack())
                EpubReader.getFlowInstanse().clearBackStack();
            if (!EpubReader.getFlowInstanse().isToAdd(new ReaderFragment()))
                return;
            changeBookName("Genesis" + " 1");
            EpubReader.getFlowInstanse().replace(new ReaderFragment(), false);
        } else if (id == R.id.lnr_bible_bookmark) {
            isbookmarkwa = true;
            isreaderfrg = true;
            _backBtnCount = 0;
            if (!EpubReader.getFlowInstanse().hasNoMoreBack())
                EpubReader.getFlowInstanse().clearBackStack();
            EpubReader.getFlowInstanse().replace(new BookMarksFragment(), false);
            isSelected = true;
        } else if (id == R.id.lnr_bible_share) {
            _backBtnCount = 0;
            isreaderfrg = false;
            isbookmarkwa = false;
            if (onShareListner != null) {
                // TwiBible.getDrawerInstanse(this).select(0);
                isSelected = onShareListner.onShareClick();
            }

            if (!isSelected)
                Toast.makeText(this, "No Page Selected To Share.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.lnr_bible_notes) {
            _backBtnCount = 0;
            isreaderfrg = true;
            isbookmarkwa = false;
            if (!EpubReader.getFlowInstanse().hasNoMoreBack())
                EpubReader.getFlowInstanse().clearBackStack();
            if (!EpubReader.getFlowInstanse().isToAdd(new NotesFragment()))
                return;
            EpubReader.getFlowInstanse().replace(new NotesFragment(), false);
        } else if (id == R.id.lnr_bible_font_size) {
            _backBtnCount = 0;
            isreaderfrg = true;
            isbookmarkwa = false;
            if (!EpubReader.getFlowInstanse().hasNoMoreBack())
                EpubReader.getFlowInstanse().clearBackStack();
            EpubReader.getFlowInstanse().replace(new FontSizeFragment(), false);
        } else if (id == R.id.lnr_bible_help) {
            _backBtnCount = 0;
            isreaderfrg = true;
            isbookmarkwa = false;
            if (!EpubReader.getFlowInstanse().hasNoMoreBack())
                EpubReader.getFlowInstanse().clearBackStack();
            EpubReader.getFlowInstanse().replace(new HelpFragment(), false);
        }

    }

    public void visibleBookMark(boolean visible) {
        _img_bookmark.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void visibleReader(boolean visible) {
        reader_image.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Go search screen
     */
    public void gotoSearch() {
        _backBtnCount = 0;
        isreaderfrg = true;
        isbookmarkwa = false;

        if (!EpubReader.getFlowInstanse().hasNoMoreBack())
            EpubReader.getFlowInstanse().clearBackStack();
        if (!EpubReader.getFlowInstanse().isToAdd(new SearchFragment()))
            return;
        EpubReader.getFlowInstanse().replace(new SearchFragment(), false);
    }

    public void visibleSearch(boolean visible) {
        reader_serach.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void visibleNightMode(boolean visible) {
        nightModeImage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void visibleDelete(boolean visible) {
        delete_icon.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void changeBookName(String text) {
        if (text != null)
            text_view_bible_text_name.setText(text);
        else {
            text_view_bible_text_name.setText("Genesis");
        }
    }

	/*
     * public void changeBookNumber(int text) { if (text != 0)
	 * text_view_bible_text_number.setText("" + text); else {
	 * text_view_bible_text_number.setText("1"); } }
	 * 
	 * public void visiblechangeBookNumber(boolean visible) { if (visible)
	 * text_view_bible_text_number.setVisibility(View.VISIBLE); else {
	 * text_view_bible_text_number.setVisibility(View.GONE); } }
	 */

    public void setMarked(boolean isMarked) {
        if (isMarked)
            _img_bookmark.setImageResource(R.drawable.bookmark_nav_icon_selected);
        else
            _img_bookmark.setImageResource(R.drawable.bookmark_nav_icon);

    }

    private IOnShareListner onShareListner;

    public interface IOnShareListner {
        public boolean onShareClick();

    }

    private IOnDeleteListner onDeleteListner;

    public interface IOnDeleteListner {
        public void onDeleteClick();

    }

    public void moveToReaderFrament(Bundle bundle) {

        EpubReader.getDrawerInstanse(this).select(0);
        EpubReader.getFlowInstanse().replace(new ReaderFragment(), bundle, false);

    }

    @Override
    protected void onDestroy() {
        preferencesBookNumber.edit().putBoolean("chapter", false).commit();
        preferencesBook.edit().putBoolean("changeBookBoolean", false).commit();
        preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
        super.onDestroy();
        preferencesBookNumber.edit().putBoolean("chapter", false).commit();
        preferencesBook.edit().putBoolean("changeBookBoolean", false).commit();
        preferencesBook.edit().putInt("position", Constants.POS0_START).commit();
        EpubReader.onDestroy();
    }

    /*
     * Book currently being used. (Hack to provide book to WebServer.)
     */
    public Book getBook() {
        return mEpubWebView.getBook();
    }

    @Override
    public ResourceResponse fetch(Uri resourceUri) {
        return getBook().fetch(resourceUri);
    }

    public void setChapters(launchChapters chapters) {
        this.chapters = chapters;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public interface Search {
        public void showSearch();
    }

    public interface launchChapters {
        public void launchChapterList();
    }

    public DrawerIntializer getDrawear() {
        return drawer;
    }

    public void setDrawear(DrawerIntializer drawer) {
        this.drawer = drawer;
    }

    public IOnBookMarkclick getBookMarkclick() {
        return bookMarkclick;
    }

    public void setBookMarkclick(IOnBookMarkclick bookMarkclick) {
        this.bookMarkclick = bookMarkclick;
    }

    public void setNightModeClick(IOnNightModeClick nightModeClick) {
        this.nightModeClick = nightModeClick;
    }

    public IOnShareListner getOnShareListner() {
        return onShareListner;
    }

    public void setOnShareListner(IOnShareListner onShareListner) {
        this.onShareListner = onShareListner;
    }

    @Override
    public void onStateChange(boolean isOpened) {

        ((MainActivity) EpubReader.getActivityIsntanse()).getActionBarView().checkboxMenu(isOpened);

        EpubReader.getFlowInstanse().hideVirtualKeyboard();
    }

    public void setOnDrawerStateChanged(IOnDrawerStateChanged onDrawerStateChanged) {
        this.onDrawerStateChanged = onDrawerStateChanged;
    }

    public IOnDeleteListner getOnDeleteListner() {
        return onDeleteListner;
    }

    public void setOnDeleteListner(IOnDeleteListner onDeleteListner) {
        this.onDeleteListner = onDeleteListner;
    }

    public IOnSearchclick getSearchclick() {
        return searchclick;
    }

    public void setSearchclick(IOnSearchclick searchclick) {
        this.searchclick = searchclick;
    }

    public void showContextMenu(String highlightClassId) {
        if (highlightClassId.length() < 1)
            return;

        if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        ReaderFragment fragment = (ReaderFragment) fragmentManager
                .findFragmentByTag(EpubReader.getFlowInstanse().getCurrentFragmentTag());
        if (fragment == null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments.size() > 1) {
                Fragment aFragment = fragments.get(fragments.size() - 2);
                if (aFragment instanceof ReaderFragment)
                    fragment = (ReaderFragment) aFragment;
            }
        }

        if (fragment != null)
            fragment.onMenuItemClick(clickedView, position);
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Log.e("TWI", "Long Click: " + position);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        boolean isNightMode = preferencesBook.getBoolean(MainActivity.KEY_NIGHT_MODE, false);
        // Prompt use night mode
        if (!isNightMode) {
            promptNightMode();
        }
    }

    /**
     * Prompt to use night mode
     * Day mode is from 6:00 to 18:00
     */
    private void promptNightMode() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour < 6 || hour > 17) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want switch to night mode?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dayNightClicked(null);
                    setNightMode(true);
                }
            });

            builder.setNegativeButton("No", null);

            // Show alert dialog
            builder.create().show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}

package com.example.EpubProject;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.EpubProject.NavigationDrawer.IOnDrawerListner;
import com.example.EpubProject.NavigationDrawer.IOnSliderListner;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.nivbible.EpubReader;
import com.nivbible.R;

/**
 * @author Wild Coder
 */
public class DrawerIntializer implements OnOpenedListener, OnClosedListener,
        IOnSliderListner {

    private SlidingMenu _menuDrawer;
    private NavigationDrawer _mainMenu;
    //	private ActionBarView _actionbar;
    private IOnDrawerStateChangeListner onDrawerStateChangeListner;

    // private LoaderImageAsThumbnail asThumbnail;

    public interface IOnDrawerStateChangeListner {
        public void onStateChange(boolean isOpened);
    }

    public void select(int index) {
        _mainMenu.select(index);
        Log.e("", "selected index is" + index);
    }

    /**
     *
     */
    /**
     *
     */
    public DrawerIntializer(final Activity activity) {
        _menuDrawer = new SlidingMenu(activity);
        _menuDrawer.setMode(SlidingMenu.LEFT);
        _menuDrawer.setBehindOffsetRes(R.dimen.com_pears_dimen_drawer_offset);
        _menuDrawer.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        _mainMenu = new NavigationDrawer(activity);
//		_actionbar = new ActionBarView(activity);
        _menuDrawer.setMenu(_mainMenu);
        _mainMenu.setOnSlideListner(this);
        _menuDrawer.setOnOpenListener(new OnOpenListener() {

            @Override
            public void onOpen() {
                EpubReader.getFlowInstanse().hideVirtualKeyboard();
            }
        });

    }

    public SlidingMenu getSlider() {
        return _menuDrawer;
    }

    public void setSlidingEnabled(boolean isSliderEnable) {
        _menuDrawer.setSlidingEnabled(isSliderEnable);
    }

    public View findViewById(int id) {
        return _mainMenu.findViewById(id);
    }

    public void setOnDrawerItemClickListner(IOnDrawerListner onDrawerListner) {
        if (_mainMenu != null)
            _mainMenu.setOnDrawerItemClickListner(onDrawerListner);
    }

    public void showContent() {
        if (_menuDrawer != null) {
            _menuDrawer.showContent();
        }
    }

    public void showMenu() {
        if (_menuDrawer != null)
            _menuDrawer.showMenu();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.salaattracker.salaat.supports.NavigationDrawer.IOnSliderListner#onSlide
     * ()
     *
     * @author Wild Coder
     */
    @Override
    public void onSlide() {
        if (_menuDrawer != null)
            _menuDrawer.showContent();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener#onClosed
     * ()
     *
     * @author Wild Coder
     */
    @Override
    public void onClosed() {
        if (onDrawerStateChangeListner != null)
            onDrawerStateChangeListner.onStateChange(false);
    }

    public void setSelectionByResourceId(int resourceId) {
        if (_mainMenu != null)
            _mainMenu.setSelectionByResourceId(resourceId);
    }

    @Override
    public void onOpened() {
        if (onDrawerStateChangeListner != null)
            onDrawerStateChangeListner.onStateChange(true);
    }

    /**
     * @param onDrawerStateChangeListner the onDrawerStateChangeListner to set
     * @author Wild Coder
     */
    public void setOnDrawerStateChangeListner(
            IOnDrawerStateChangeListner onDrawerStateChangeListner) {
        if (_menuDrawer != null) {
            _menuDrawer.setOnOpenedListener(this);
            _menuDrawer.setOnClosedListener(this);
            this.onDrawerStateChangeListner = onDrawerStateChangeListner;
        }
    }

}

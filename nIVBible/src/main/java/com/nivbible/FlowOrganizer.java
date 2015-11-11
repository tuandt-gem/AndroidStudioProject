package com.nivbible;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class FlowOrganizer {

    private String _last_fragment_name = "";
    private int _id_parent_frame_view;
    private FragmentManager _fragmnet_manager;
    private FragmentActivity _activity;

    /**
     * @author Wild Coder
     * @see {@link FlowOrganizer}
     */

    public void clearBackStack() {
        try {
            _fragmnet_manager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
        }
    }

    public FlowOrganizer(FragmentActivity _activity, int idParentFameView) {
        this._activity = _activity;
        this._id_parent_frame_view = idParentFameView;
        this._fragmnet_manager = _activity.getSupportFragmentManager();
    }

    /**
     * @param toFragment Fragment support v4
     * @author Wild Coder
     */
    public void replace(Fragment toFragment) {
        replace(toFragment, null, false);
    }

    public void updateFragment(Bundle bundle, Fragment toFragmnt) {
        _fragmnet_manager.putFragment(bundle, _last_fragment_name, toFragmnt);

    }

    /**
     * @param toFragment  Fragment support v4
     * @param isAllowBack Allow Back To the screen
     * @author Wild Coder
     */
    public void replace(Fragment toFragment, boolean isAllowBack) {
        replace(toFragment, null, isAllowBack);
    }

    /**
     * @param toFragment Fragment support v4
     * @param bundle     Bundle
     * @author Wild Coder
     */

    public void replaceAndClearBackStack(Fragment toFragment, Bundle bundle) {
        hideVirtualKeyboard();
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        try {
            if (_fragmnet_manager.getBackStackEntryCount() == 0) {
                Log.e("", "Return");
                // return;
            }
            for (int i = 0; i < _fragmnet_manager.getBackStackEntryCount(); i++) {
                _fragmnet_manager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        replace(toFragment, bundle, false);
    }

    public void goToHome(Fragment toFragment, Bundle bundle) {
        hideVirtualKeyboard();
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        try {
            for (int i = 0; i < _fragmnet_manager.getBackStackEntryCount(); i++) {
                _fragmnet_manager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        replace(toFragment, bundle, false);
    }

    public void replace(final Fragment toFragment, Bundle bundle,
                        final boolean isAllowBack) {
        hideVirtualKeyboard();
        if (toFragment == null)
            return;
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        FragmentTransaction _fragment_transiction = _fragmnet_manager
                .beginTransaction();

        if (isAllowBack) {
            _fragment_transiction.addToBackStack(_last_fragment_name);
        }
//        _fragment_transiction.replace(_id_parent_frame_view, toFragment,
//                toFragment.getClass().getName()).commit();

        /* Use commitAllowingStateLoss to avoid
        IllegalStateException: Can not perform this action after onSaveInstanceState */
        _fragment_transiction.replace(_id_parent_frame_view, toFragment,
                toFragment.getClass().getName()).commitAllowingStateLoss();

        _last_fragment_name = toFragment.getClass().getName();
    }

    /**
     * @param toFragment Fragment support v4
     * @author Wild Coder
     */
    public void add(Fragment toFragment) {
        add(toFragment, null, false);
    }

    /**
     * @param toFragment  Fragment support v4
     * @param isAllowBack Allow Back To the screen
     * @author Wild Coder
     */
    public void add(Fragment toFragment, boolean isAllowBack) {
        add(toFragment, null, isAllowBack);
    }

    /**
     * @param toFragment  Fragment support v4
     * @param bundle      Bundle
     * @param isAllowBack Allow Back To the screen
     * @author Wild Coder
     */
    public void add(Fragment toFragment, Bundle bundle, boolean isAllowBack) {
        hideVirtualKeyboard();
        if (toFragment == null)
            return;
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        FragmentTransaction _fragment_transiction = _fragmnet_manager
                .beginTransaction();
        if (isAllowBack) {
            _fragment_transiction.addToBackStack(_last_fragment_name);
        }

        _fragment_transiction.add(_id_parent_frame_view, toFragment,
                toFragment.getClass().getName()).commit();
        _last_fragment_name = toFragment.getClass().getName();
    }

    public boolean isToAdd(Fragment toFragment) {
        if (_activity == null)
            return true;
        List<Fragment> _list_fragment = _activity.getSupportFragmentManager()
                .getFragments();
        for (Fragment _fragment : _list_fragment) {
            if (_fragment == null)
                break;
            String name1 = toFragment.getClass().getName();
            String name2 = _fragment.getClass().getName();
            if (name1.equalsIgnoreCase(name2))
                return false;
        }
        return true;
    }

    public boolean isToAdd(FragmentManager fragmentManager, Fragment fragment) {
        hideVirtualKeyboard();
        if (fragment == null)
            return false;
        List<Fragment> _list_fragment = fragmentManager.getFragments();
        if (_list_fragment == null)
            return true;
        for (Fragment _fragment : _list_fragment) {
            if (_fragment == null)
                break;
            if (fragment.getClass().getName()
                    .equalsIgnoreCase(_fragment.getClass().getName()))
                return false;

        }
        return true;
    }

    public boolean hasNoMoreBack() {
        return _fragmnet_manager.getBackStackEntryCount() == 0;
    }

    public String getCurrentFragmentTag() {
        return _last_fragment_name;
    }

    public void onBackPress(String back) {
        if (back != null)
            _last_fragment_name = back;
    }

    public void hideVirtualKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) _activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View focused = _activity.getCurrentFocus();
        if (focused != null && focused.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(focused.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

package com.example.popwindow;

import com.example.EpubProject.Utility;
import com.nivbible.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class SelectionPopup extends QuickAction {
    public interface ActionId {
        int ACTION_COLOR_GREEN = 1;
        int ACTION_COLOR_PINK = 2;
        int ACTION_COLOR_PURPLE = 3;
        int ACTION_COLOR_REMOVE = 4;
        int ACTION_COLOR_YELLOW = 5;
        int ACTION_FACEBOOK = 6;
        int ACTION_TWITTER = 7;
        int ACTION_EMAIL = 8;
        int ACTION_NOTE = 9;
        int ACTION_UNDERLINE = 10;
        int ACTION_SHARE = 11;
        int ACTION_COPY = 12;
    }


    public SelectionPopup(Context context, boolean editingSelection) {
        super(context, HORIZONTAL);
        setup(editingSelection);
    }

    private void setup(boolean editingSelection) {
        // Mark yellow color
        ActionItem itemYellow = new ActionItem();

        itemYellow.setActionId(ActionId.ACTION_COLOR_YELLOW);
        itemYellow.setIcon(mContext.getResources().getDrawable(R.drawable.ic_color_yellow));
        addActionItem(itemYellow);

        // Mark green color
        ActionItem itemGreen = new ActionItem();

        itemGreen.setActionId(ActionId.ACTION_COLOR_GREEN);
        itemGreen.setIcon(mContext.getResources().getDrawable(R.drawable.ic_color_green));
        addActionItem(itemGreen);

        // Mark pink color
        ActionItem itemPink = new ActionItem();

        itemPink.setActionId(ActionId.ACTION_COLOR_PINK);
        itemPink.setIcon(mContext.getResources().getDrawable(R.drawable.ic_color_pink));
        addActionItem(itemPink);

        // Mark purple color
        ActionItem itemPurple = new ActionItem();

        itemPurple.setActionId(ActionId.ACTION_COLOR_PURPLE);
        itemPurple.setIcon(mContext.getResources().getDrawable(R.drawable.ic_color_purble));
        addActionItem(itemPurple);

        // Mark purple color
        ActionItem itemUnderline = new ActionItem();

        itemUnderline.setActionId(ActionId.ACTION_UNDERLINE);
        itemUnderline.setIcon(mContext.getResources().getDrawable(R.drawable.ic_underline));
        addActionItem(itemUnderline);

        if (editingSelection) {
            // Action remove selection
            ActionItem itemRemove = new ActionItem();

            itemRemove.setActionId(ActionId.ACTION_COLOR_REMOVE);
            itemRemove.setIcon(mContext.getResources().getDrawable(R.drawable.ic_trash));
            addActionItem(itemRemove);
        }

//        // Share FB
//        ActionItem facebookItem = new ActionItem();
//
//        facebookItem.setActionId(ActionId.ACTION_FACEBOOK);
//        facebookItem.setIcon(mContext.getResources().getDrawable(R.drawable.ic_facebook));
//        addActionItem(facebookItem);
//
//        // Share FB
//        ActionItem twitterItem = new ActionItem();
//
//        twitterItem.setActionId(ActionId.ACTION_TWITTER);
//        twitterItem.setIcon(mContext.getResources().getDrawable(R.drawable.ic_twitter));
//        addActionItem(twitterItem);

        // Copy
        ActionItem copyItem = new ActionItem();

        copyItem.setActionId(ActionId.ACTION_COPY);
        copyItem.setIcon(mContext.getResources().getDrawable(R.drawable.ic_copy));
        addActionItem(copyItem);

        // Share
        ActionItem shareItem = new ActionItem();

        shareItem.setActionId(ActionId.ACTION_SHARE);
        shareItem.setIcon(mContext.getResources().getDrawable(R.drawable.ic_share));
        addActionItem(shareItem);


        // Note current selection
        ActionItem noteItem = new ActionItem();

        noteItem.setActionId(ActionId.ACTION_NOTE);
        noteItem.setIcon(mContext.getResources().getDrawable(R.drawable.ic_note_action));
        addActionItem(noteItem);

    }

    /**
     * Show quick-action popup. Popup is automatically positioned, on top or
     * bottom of anchor view.
     */
    public void show(View startAnchor, View endAnchor, int fontSize) {
        preShow();
        waitToAnchorsReady(startAnchor, endAnchor, fontSize);
    }

    /**
     * Show popup by specific coordinates
     */
    public void show(View anchor, int left, int top, int bottom, int right, int fontSize) {
        showPopup(anchor, left, top, bottom, right, fontSize);
    }

    private boolean startReady = false, endReady = false;

    /**
     * Wait to anchors ready on screen
     */
    private void waitToAnchorsReady(final View startAnchor, final View endAnchor, final int fontSize) {
        startReady = false;
        endReady = false;

        final ViewTreeObserver startObserver = startAnchor.getViewTreeObserver();
        startObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                startReady = true;
                if (endReady) {
                    showPopup(startAnchor, endAnchor, fontSize);
                }
                startObserver.removeGlobalOnLayoutListener(this);
            }
        });

        final ViewTreeObserver endObserver = endAnchor.getViewTreeObserver();
        endObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                endReady = true;
                if (startReady) {
                    showPopup(startAnchor, endAnchor, fontSize);
                }
                endObserver.removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void showPopup(final View startAnchor, View endAnchor, final int fontSize) {
        int xPos;

        mDidAction = false;

        int[] locationStart = new int[2];
        int[] locationEnd = new int[2];

        startAnchor.getLocationInWindow(locationStart);
        endAnchor.getLocationInWindow(locationEnd);

        Rect anchorRect = new Rect(locationStart[0], locationStart[1], locationStart[0] + startAnchor.getWidth(),
                locationStart[1] + startAnchor.getHeight());

        // mRootView.setLayoutParams(new
        // LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int rootHeight = mRootView.getMeasuredHeight();

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        // automatically get X coord of popup (top left)
        if ((anchorRect.left + rootWidth) > screenWidth) {
            xPos = anchorRect.left - (rootWidth - startAnchor.getWidth());
            xPos = (xPos < 0) ? 0 : xPos;

        } else {
            if (startAnchor.getWidth() > rootWidth) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
            } else {
                xPos = anchorRect.left;
            }
        }

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = dyTop > dyBottom;

        if (onTop) {
            if (rootHeight > dyTop) {
                ViewGroup.LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - startAnchor.getHeight();
            }
        } else {
            if (rootHeight > dyBottom) {
                ViewGroup.LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
            }
        }

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

//		if (onTop) {
        mWindow.showAtLocation(startAnchor, Gravity.TOP, 0, locationStart[1] - rootHeight - fontSize - 5);
//        mWindow.showAtLocation(startAnchor, Gravity.TOP|Gravity.LEFT, 0, 0);
//		} else {
//			mWindow.showAtLocation(endAnchor, Gravity.TOP, 0, (int) (locationEnd[1] + endAnchor.getMeasuredHeight() /displaymetrics.density) );
//		}
    }

    private void showPopup(View anchor, int left, int top, int bottom, int right, int fontSize) {
        int xPos;

        mDidAction = false;

        int[] locationStart = new int[2];
//        int[] locationEnd = new int[2];

        anchor.getLocationInWindow(locationStart);
//        endAnchor.getLocationInWindow(locationEnd);

        Rect anchorRect = new Rect(locationStart[0], locationStart[1], locationStart[0] + anchor.getWidth(),
                locationStart[1] + anchor.getHeight());

        // mRootView.setLayoutParams(new
        // LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int rootHeight = mRootView.getMeasuredHeight();

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        // automatically get X coord of popup (top left)
        if ((anchorRect.left + rootWidth) > screenWidth) {
            xPos = anchorRect.left - (rootWidth - anchor.getWidth());
            xPos = (xPos < 0) ? 0 : xPos;

        } else {
            if (anchor.getWidth() > rootWidth) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
            } else {
                xPos = anchorRect.left;
            }
        }

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = dyTop > dyBottom;

        if (onTop) {
            if (rootHeight > dyTop) {
                ViewGroup.LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - anchor.getHeight();
            }
        } else {
            if (rootHeight > dyBottom) {
                ViewGroup.LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
            }
        }

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

//		if (onTop) {
        mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        top = Utility.dpToPx(top);
        mWindow.showAtLocation(anchor, Gravity.TOP, 0, top + fontSize);
//        mWindow.showAtLocation(startAnchor, Gravity.TOP|Gravity.LEFT, 0, 0);
//		} else {
//			mWindow.showAtLocation(anchor, Gravity.TOP, 0, (int) (locationStart[1] + anchor.getMeasuredHeight() /displaymetrics.density) );
//		}
    }
}
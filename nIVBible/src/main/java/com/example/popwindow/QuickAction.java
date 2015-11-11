package com.example.popwindow;

import java.util.ArrayList;
import java.util.List;

import com.nivbible.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * QuickAction dialog, shows action list as icon and text like the one in
 * Gallery3D app. Currently supports vertical and horizontal layout.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *         <p/>
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupWindows implements OnDismissListener {
    protected View mRootView;
    private LayoutInflater mInflater;
    private ViewGroup mTrack;
    protected ScrollView mScroller;
    private OnActionItemClickListener mItemClickListener;
    private OnDismissListener mDismissListener;

    private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    protected boolean mDidAction;

    private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
    private int mOrientation;
    protected int rootWidth = 0;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;

    /**
     * Constructor for default vertical layout
     *
     * @param context Context
     */
    public QuickAction(Context context) {
        this(context, HORIZONTAL);
    }

    /**
     * Constructor allowing orientation override
     *
     * @param context     Context
     * @param orientation Layout orientation, can be vartical or horizontal
     */
    public QuickAction(Context context, int orientation) {
        super(context);

        mOrientation = orientation;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mOrientation == HORIZONTAL) {
            setRootViewId(R.layout.popup_horizontal);
        } else {
            setRootViewId(R.layout.popup_vertical);
        }

        mAnimStyle = ANIM_AUTO;
        mChildPos = 0;
    }

    /**
     * Get action item at an index
     *
     * @param index Index of item (position from callback)
     * @return Action Item at the position
     */
    public ActionItem getActionItem(int index) {
        return actionItems.get(index);
    }

    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id) {
        mRootView = mInflater.inflate(id, null);
        mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);

        mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);

        // This was previously defined on show() method, moved here to prevent
        // force close that occured
        // when tapping fastly on a view to show quickaction dialog.
        // Thanx to zammbi (github.com/zammbi)
        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);
    }

    /**
     * Set animation style
     *
     * @param mAnimStyle animation style, default is set to ANIM_AUTO
     */
    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    /**
     * Set listener for action item clicked.
     *
     * @param listener Listener
     */
    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Add action item
     *
     * @param action {@link ActionItem}
     */
    public void addActionItem(ActionItem action) {
        actionItems.add(action);

        String title = action.getTitle();
        Drawable icon = action.getIcon();

        View container;

        if (mOrientation == HORIZONTAL) {
            container = mInflater.inflate(R.layout.action_item_horizontal, null);
        } else {
            container = mInflater.inflate(R.layout.action_item_vertical, null);
        }

        ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
        TextView text = (TextView) container.findViewById(R.id.tv_title);

        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }

        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }

        final int pos = mChildPos;
        final int actionId = action.getActionId();

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(QuickAction.this, v, pos, actionId);
                }

                if (!getActionItem(pos).isSticky()) {
                    mDidAction = true;

                    // TODO don't dismiss when click on action item
//					dismiss();
                }
            }
        });

        // Since I removed focusable from the popup window,
        // I need to control the selection drawable here
        container.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    v.setBackgroundResource(android.R.color.transparent);
                }

                return false;
            }

        });

        container.setFocusable(true);
        container.setClickable(true);

        if (mOrientation == HORIZONTAL && mChildPos != 0) {
            View separator = mInflater.inflate(R.layout.horiz_separator, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);

            separator.setLayoutParams(params);
            separator.setPadding(5, 0, 5, 0);

            mTrack.addView(separator, mInsertPos);

            mInsertPos++;
        }

        mTrack.addView(container, mInsertPos);

        mChildPos++;
        mInsertPos++;
    }

    /**
     * Shows the quick action menu using the given Rect as the anchor.
     *
     * @param parent
     * @param rect
     */
    // public void show(View parent, Rect rect) {
    //
    // preShow();
    //
    // int xPos, yPos;
    //
    // mDidAction = false;
    //
    // int[] location = new int[2];
    // parent.getLocationOnScreen(location);
    //
    // int parentXPos = location[0];
    // int parentYPos = location[1];
    //
    // Rect anchorRect = new Rect(parentXPos + rect.left, parentYPos + rect.top,
    // parentXPos + rect.left + rect.width(),
    // parentYPos + rect.top + rect.height());
    // int width = anchorRect.width();
    // int height = anchorRect.height();
    //
    // // mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
    // // LayoutParams.WRAP_CONTENT));
    //
    // mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    //
    // int rootHeight = mRootView.getMeasuredHeight();
    //
    // if (rootWidth == 0) {
    // rootWidth = mRootView.getMeasuredWidth();
    // }
    //
    // DisplayMetrics displaymetrics = new DisplayMetrics();
    // ((Activity)
    // mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    //
    // int screenWidth = displaymetrics.widthPixels;
    // int screenHeight = displaymetrics.heightPixels;
    //
    // // automatically get X coord of popup (top left)
    // if ((anchorRect.left + parentXPos + rootWidth) > screenWidth) {
    // xPos = anchorRect.left - (rootWidth - width);
    // xPos = (xPos < 0) ? 0 : xPos;
    //
    // } else {
    // if (width > rootWidth) {
    // xPos = anchorRect.centerX() - (rootWidth / 2);
    // } else {
    // xPos = anchorRect.left;
    // }
    // }
    //
    // int dyTop = anchorRect.top;
    // int dyBottom = screenHeight - anchorRect.bottom;
    //
    // boolean onTop = (dyTop > dyBottom) ? true : false;
    //
    // if (onTop) {
    // if (rootHeight > dyTop) {
    // yPos = 15;
    // LayoutParams l = mScroller.getLayoutParams();
    // l.height = dyTop - height;
    // } else {
    // yPos = anchorRect.top - rootHeight;
    // }
    // } else {
    // yPos = anchorRect.bottom;
    //
    // if (rootHeight > dyBottom) {
    // LayoutParams l = mScroller.getLayoutParams();
    // l.height = dyBottom;
    // }
    // }
    //
    // setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
    //
    // mWindow.showAtLocation(parent, Gravity.NO_GRAVITY, xPos, yPos);
    //
    // }

    /**
     * Show quickaction popup. Popup is automatically positioned, on top or
     * bottom of anchor view.
     */
    public void show(View anchor) {
        preShow();

        int xPos, yPos;

        mDidAction = false;

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0] +
                anchor.getWidth(),
                location[1] + anchor.getHeight());

        // mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootHeight = mRootView.getMeasuredHeight();

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)
                mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

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

        boolean onTop = (dyTop > dyBottom) ? true : false;

        if (onTop) {
            if (rootHeight > dyTop) {
                yPos = 15;
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - anchor.getHeight();
            } else {
                yPos = anchorRect.top - rootHeight;
            }
        } else {
            yPos = anchorRect.bottom;

            if (rootHeight > dyBottom) {
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
            }
        }

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX  distance from left edge
     * @param onTop       flag to indicate where the popup should be displayed. Set TRUE
     *                    if displayed on top of anchor view and vice versa
     */
    protected void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
        int arrowPos = requestedX;

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle(
                        (onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle(
                        (onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle(
                        (onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_REFLECT:
                mWindow.setAnimationStyle(
                        (onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mWindow.setAnimationStyle(
                            (onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
                    mWindow.setAnimationStyle(
                            (onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle(
                            (onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                }

                break;
        }
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if
     * the quicakction dialog is dismissed by clicking outside the dialog or
     * clicking on sticky item.
     */
    public void setOnDismissListener(QuickAction.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    /**
     * Listener for item click
     */
    public interface OnActionItemClickListener {
        public abstract void onItemClick(QuickAction source, View v, int pos, int actionId);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnDismissListener {
        public abstract void onDismiss();
    }
}
package com.bossturban.webviewmarker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.blahti.drag.DragController;
import com.blahti.drag.DragController.DragBehavior;
import com.blahti.drag.DragLayer;
import com.blahti.drag.DragListener;
import com.blahti.drag.DragSource;
import com.blahti.drag.MyAbsoluteLayout;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("DefaultLocale")
public class TextSelectionSupport
        implements TextSelectionControlListener, OnTouchListener, OnLongClickListener, DragListener {
    public interface SelectionListener {
        void startSelection();

        void startDrag();

        void selectionChanged(String text);

        // First call when drag end
        void endDrag();

        // All action is total completely
        void onDragDone();

        void endSelection();

        void onHandlesDrawn();

        void saveSelection(int start, int end, int startVerseNumber, int endVerseNumber,String text, int fontSize);

        void onSingleTouch(int start, int end);

        void notifyRangeCoords(int left, int top, int bottom, int right, int fontSize);

        void noteClicked(int id);
    }

    private enum HandleType {
        START, END, UNKNOWN
    }

    private static final String TAG = "SelectionSupport";
    private static final float CENTERING_SHORTER_MARGIN_RATIO = 12.0f / 48.0f;
    private static final int JACK_UP_PADDING = 2;
    private static final int SCROLLING_THRESHOLD = 10;

    private Activity mActivity;
    private WebView mWebView;
    private SelectionListener mSelectionListener;
    private OnTouchListener mOnTouchListener;
    private OnLongClickListener mOnLongClickListener;
    private DragLayer mSelectionDragLayer;
    private DragController mDragController;
    private ImageView mStartSelectionHandle;
    private ImageView mEndSelectionHandle;
    private Rect mSelectionBounds = null;
    private final Rect mSelectionBoundsTemp = new Rect();
    private TextSelectionController mSelectionController = null;
    private int mContentWidth = 0;
    private HandleType mLastTouchedSelectionHandle = HandleType.UNKNOWN;
    private boolean mScrolling = false;
    private boolean mDragging = false;
    private float mScrollDiffY = 0;
    private float mLastTouchY = 0;
    private float mScrollDiffX = 0;
    private float mLastTouchX = 0;
    private float mScale = 1.0f;

    private Runnable mStartSelectionModeHandler = new Runnable() {
        public void run() {
            if (mSelectionBounds != null) {
                if (mSelectionDragLayer.getParent() != null) {
                    mWebView.removeView(mSelectionDragLayer);
                }
                mWebView.addView(mSelectionDragLayer);
                drawSelectionHandles();
                final int contentHeight = (int) Math
                        .ceil(getDensityDependentValue(mWebView.getContentHeight(), mActivity));
                final int contentWidth = mWebView.getWidth();
                ViewGroup.LayoutParams layerParams = mSelectionDragLayer.getLayoutParams();
                layerParams.height = contentHeight;
                layerParams.width = Math.max(contentWidth, mContentWidth);
                mSelectionDragLayer.setLayoutParams(layerParams);
                if (mSelectionListener != null) {
                    mSelectionListener.startSelection();
                }
            }
        }
    };

    private Runnable endSelectionModeHandler = new Runnable() {
        public void run() {
            mWebView.removeView(mSelectionDragLayer);
            mSelectionBounds = null;
            mLastTouchedSelectionHandle = HandleType.UNKNOWN;
            mWebView.loadUrl("javascript: android.selection.clearSelection();");
            if (mSelectionListener != null) {
                mSelectionListener.endSelection();
            }
        }
    };

    private TextSelectionSupport(Activity activity, WebView webview) {
        mActivity = activity;
        mWebView = webview;
    }

    public static TextSelectionSupport support(Activity activity, WebView webview) {
        final TextSelectionSupport selectionSupport = new TextSelectionSupport(activity, webview);
        selectionSupport.setup();
        return selectionSupport;
    }

    public void onScaleChanged(float oldScale, float newScale) {
        mScale = newScale;
    }

    public void setSelectionListener(SelectionListener listener) {
        mSelectionListener = listener;
    }

    //
    // Interfaces of TextSelectionControlListener
    //
    @Override
    public void jsError(String error) {
        Log.e(TAG, "JSError: " + error);
    }

    @Override
    public void jsLog(String message) {
        Log.d(TAG, "JSLog: " + message);
    }

    @Override
    public void startSelectionMode() {
        mActivity.runOnUiThread(mStartSelectionModeHandler);
    }

    @Override
    public void endSelectionMode() {
        mActivity.runOnUiThread(endSelectionModeHandler);
    }

    @Override
    public void setContentWidth(float contentWidth) {
        mContentWidth = (int) getDensityDependentValue(contentWidth, mActivity);
    }

    @Override
    public void selectionChanged(String range, String text, int fontSize, String handleBounds, boolean isReallyChanged) {
        final Context ctx = mActivity;
        try {
            final JSONObject selectionBoundsObject = new JSONObject(handleBounds);
            final float scale = getDensityIndependentValue(mScale, ctx);
            Rect rect = mSelectionBoundsTemp;
            rect.left = (int) (getDensityDependentValue(selectionBoundsObject.getInt("left"), ctx) * scale);
            rect.top = (int) (getDensityDependentValue(selectionBoundsObject.getInt("top"), ctx) * scale);
            rect.right = (int) (getDensityDependentValue(selectionBoundsObject.getInt("right"), ctx) * scale);
            rect.bottom = (int) (getDensityDependentValue(selectionBoundsObject.getInt("bottom"), ctx) * scale);
            mSelectionBounds = rect;
            if (!isInSelectionMode()) {
                startSelectionMode();
            }
            drawSelectionHandles();
            if (mSelectionListener != null && isReallyChanged) {
                mSelectionListener.selectionChanged(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ImageView getStartSelectionHandle() {
        return mStartSelectionHandle;
    }

    public void performDragAtStartHandle() {
        mLastTouchedSelectionHandle = HandleType.START;
    }

    public void performDragAtEndHandle() {
        mLastTouchedSelectionHandle = HandleType.END;
    }

    public ImageView getEndSelectionHandle() {
        return mEndSelectionHandle;
    }

    //
    // Interface of OnTouchListener
    //
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final Context ctx = mActivity;
        float xPoint = getDensityIndependentValue(event.getX(), ctx) / getDensityIndependentValue(mScale, ctx);
        float yPoint = getDensityIndependentValue(event.getY(), ctx) / getDensityIndependentValue(mScale, ctx);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final String startTouchUrl = String.format("javascript:android.selection.startTouch(%f, %f);", xPoint,
                        yPoint);
                mLastTouchX = xPoint;
                mLastTouchY = yPoint;
                mWebView.loadUrl(startTouchUrl);
                break;
            case MotionEvent.ACTION_UP:
                if (!mScrolling) {
                    // Single touch
                    String singleTouchJs = "javascript:singleTouchWord();";
                    mWebView.loadUrl(singleTouchJs);

//                    endSelectionMode();
                }
                mScrollDiffX = 0;
                mScrollDiffY = 0;
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mScrollDiffX += (xPoint - mLastTouchX);
                mScrollDiffY += (yPoint - mLastTouchY);
                mLastTouchX = xPoint;
                mLastTouchY = yPoint;
                if (Math.abs(mScrollDiffX) > SCROLLING_THRESHOLD || Math.abs(mScrollDiffY) > SCROLLING_THRESHOLD) {
                    mScrolling = true;
                }
                break;
        }
        if (mOnTouchListener != null) {
            return mOnTouchListener.onTouch(v, event);
        } else {
            return false;
        }
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    @Override
    public void noteClicked(int id) {
        if (mSelectionListener != null) {
            mSelectionListener.noteClicked(id);
        }
    }

    @Override
    public void onSingleTouch(int start, int end) {
        if (mSelectionListener != null) {
            mSelectionListener.onSingleTouch(start, end);
        }
    }

    @Override
    public void notifyRangeCoords(int left, int top, int bottom, int right, int fontSize) {
        if (mSelectionListener != null) {
            mSelectionListener.notifyRangeCoords(left, top, bottom, right, fontSize);
        }
    }

    /**
     * Drag handle
     */
    @Override
    public void onDrag() {
        mDragging = true;
        // Log.v(TAG, ">> onDragEnd");
        // MyAbsoluteLayout.LayoutParams startHandleParams =
        // (MyAbsoluteLayout.LayoutParams) mStartSelectionHandle
        // .getLayoutParams();
        // MyAbsoluteLayout.LayoutParams endHandleParams =
        // (MyAbsoluteLayout.LayoutParams) mEndSelectionHandle
        // .getLayoutParams();
        //
        // final Context ctx = mActivity;
        // // final float scale = getDensityIndependentValue(mScale, ctx);
        // final float scale = getDensityIndependentValue(mWebView.getScale(),
        // ctx);
        //
        // float startX = startHandleParams.x - mWebView.getScrollX()
        // + mStartSelectionHandle.getWidth() * (1 -
        // CENTERING_SHORTER_MARGIN_RATIO);
        // float startY = startHandleParams.y - mWebView.getScrollY() -
        // JACK_UP_PADDING;
        //
        // float endX = endHandleParams.x - mWebView.getScrollX()
        // + mEndSelectionHandle.getWidth() * CENTERING_SHORTER_MARGIN_RATIO;
        // float endY = endHandleParams.y - mWebView.getScrollY() -
        // JACK_UP_PADDING;
        //
        // startX = getDensityIndependentValue(startX, ctx) / scale;
        // startY = getDensityIndependentValue(startY, ctx) / scale;
        // endX = getDensityIndependentValue(endX, ctx) / scale;
        // endY = getDensityIndependentValue(endY, ctx) / scale;
        //
        // if (mLastTouchedSelectionHandle == HandleType.START && startX > 0 &&
        // startY > 0) {
        // // Log.v(TAG, "LastTouchedStartHandle: " + String.format("%f, %f",
        // // startX, startY));
        // String saveStartString = String.format("javascript:
        // android.selection.setStartPos(%f, %f);", startX,
        // startY);
        // mWebView.loadUrl(saveStartString);
        // } else if (mLastTouchedSelectionHandle == HandleType.END && endX > 0
        // && endY > 0) {
        // // Log.v(TAG, "LastTouchedEndHandle: " + String.format("%f, %f",
        // // endX, endY));
        // String saveEndString = String.format("javascript:
        // android.selection.setEndPos(%f, %f);", endX, endY);
        // mWebView.loadUrl(saveEndString);
        // } else {
        // mWebView.loadUrl("javascript:
        // android.selection.restoreStartEndPos();");
        // }
        // Log.v(TAG, "<< onDragEnd");
    }

    //
    // Interface of OnLongClickListener
    //
    @Override
    public boolean onLongClick(View v) {
        mWebView.loadUrl("javascript:android.selection.longTouch();");
        mScrolling = true;
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onLongClick(v);
        }
        return true;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    //
    // Interface of DragListener
    //
    @Override
    public void onDragStart(DragSource source, Object info, DragBehavior dragBehavior) {
        mDragging = true;
        if (mSelectionListener != null) {
            mSelectionListener.startDrag();
        }
    }

    @Override
    public void onDragEnd() {
        mDragging = false;
        // Notify to listener
        if (mSelectionListener != null) {
            mSelectionListener.endDrag();
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, ">> onDragEnd");
                MyAbsoluteLayout.LayoutParams startHandleParams = (MyAbsoluteLayout.LayoutParams) mStartSelectionHandle
                        .getLayoutParams();
                MyAbsoluteLayout.LayoutParams endHandleParams = (MyAbsoluteLayout.LayoutParams) mEndSelectionHandle
                        .getLayoutParams();
                final Context ctx = mActivity;
                final float scale = getDensityIndependentValue(mScale, ctx);
                float startX = startHandleParams.x - mWebView.getScrollX()
                        + mStartSelectionHandle.getWidth() * (1 - CENTERING_SHORTER_MARGIN_RATIO);
                float startY = startHandleParams.y - mWebView.getScrollY() - JACK_UP_PADDING;

                float endX = endHandleParams.x - mWebView.getScrollX()
                        + mEndSelectionHandle.getWidth() * CENTERING_SHORTER_MARGIN_RATIO;
                float endY = endHandleParams.y - mWebView.getScrollY() - JACK_UP_PADDING;
                startX = getDensityIndependentValue(startX, ctx) / scale;
                startY = getDensityIndependentValue(startY, ctx) / scale;

                endX = getDensityIndependentValue(endX, ctx) / scale;
                endY = getDensityIndependentValue(endY, ctx) / scale;

                if (mLastTouchedSelectionHandle == HandleType.START && startX > 0 && startY > 0) {
                    Log.v(TAG, "START Touched StartHandle: " + String.format("%f, %f", startX, startY));
                    Log.v(TAG, "START Touched EndHandle: " + String.format("%f, %f", endX, endY));
                    String saveStartString = String.format("javascript: android.selection.setStartPos(%f, %f);", startX,
                            startY);
                    mWebView.loadUrl(saveStartString);
                } else if (mLastTouchedSelectionHandle == HandleType.END && endX > 0 && endY > 0) {
                    Log.v(TAG, "END Touched StartHandle: " + String.format("%f, %f", startX, startY));
                    Log.v(TAG, "END Touched EndHandle: " + String.format("%f, %f", endX, endY));
                    String saveEndString = String.format("javascript: android.selection.setEndPos(%f, %f);", endX,
                            endY);
                    mWebView.loadUrl(saveEndString);
                } else {
                    Log.v(TAG, "LastTouchedUnknownHandle: " + String.format("%f, %f", endX, endY));
                    mWebView.loadUrl("javascript: android.selection.restoreStartEndPos();");
                }
                Log.v(TAG, "<< onDragEnd");
            }
        });

        // Notify to listener
        if (mSelectionListener != null) {
            mSelectionListener.onDragDone();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void setup() {
        mScale = mActivity.getResources().getDisplayMetrics().density;
        mWebView.setOnLongClickListener(this);
        mWebView.setOnTouchListener(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // mWebView.getSettings().setPluginsEnabled(true);
        mSelectionController = new TextSelectionController(this);
        mWebView.addJavascriptInterface(mSelectionController, TextSelectionController.INTERFACE_NAME);
        createSelectionLayer(mActivity);
    }

    private void createSelectionLayer(Context context) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectionDragLayer = (DragLayer) inflater.inflate(R.layout.selection_drag_layer, null);
        mDragController = new DragController(context);
        mDragController.setDragListener(this);
        mDragController.addDropTarget(mSelectionDragLayer);
        mSelectionDragLayer.setDragController(mDragController);
        mStartSelectionHandle = (ImageView) mSelectionDragLayer.findViewById(R.id.startHandle);
        mStartSelectionHandle.setTag(HandleType.START);
        mEndSelectionHandle = (ImageView) mSelectionDragLayer.findViewById(R.id.endHandle);
        mEndSelectionHandle.setTag(HandleType.END);
        final OnTouchListener handleTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean handledHere = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handledHere = startDrag(v);
                    mLastTouchedSelectionHandle = (HandleType) v.getTag();
                }

                return handledHere;
            }
        };
        mStartSelectionHandle.setOnTouchListener(handleTouchListener);
        mEndSelectionHandle.setOnTouchListener(handleTouchListener);
    }

    private void drawSelectionHandles() {
        mActivity.runOnUiThread(drawSelectionHandlesHandler);
    }

    private Runnable drawSelectionHandlesHandler = new Runnable() {

        @Override
        public void run() {
            MyAbsoluteLayout.LayoutParams startParams = (MyAbsoluteLayout.LayoutParams) mStartSelectionHandle.getLayoutParams();
            final int startWidth = mStartSelectionHandle.getDrawable().getIntrinsicWidth();
            startParams.x = (int) (mSelectionBounds.left - startWidth * (1.0f - CENTERING_SHORTER_MARGIN_RATIO));
            startParams.y = mSelectionBounds.top;
            final int startMinLeft = -(int) (startWidth * (1 - CENTERING_SHORTER_MARGIN_RATIO));
            startParams.x = (startParams.x < startMinLeft) ? startMinLeft : startParams.x;
            startParams.y = (startParams.y < 0) ? 0 : startParams.y;
            mStartSelectionHandle.setLayoutParams(startParams);

            MyAbsoluteLayout.LayoutParams endParams = (MyAbsoluteLayout.LayoutParams) mEndSelectionHandle.getLayoutParams();
            final int endWidth = mEndSelectionHandle.getDrawable().getIntrinsicWidth();
            endParams.x = (int) (mSelectionBounds.right - endWidth * CENTERING_SHORTER_MARGIN_RATIO);
            endParams.y = mSelectionBounds.bottom;
            final int endMinLeft = -(int) (endWidth * (1 - CENTERING_SHORTER_MARGIN_RATIO));
            endParams.x = (endParams.x < endMinLeft) ? endMinLeft : endParams.x;
            endParams.y = (endParams.y < 0) ? 0 : endParams.y;
            mEndSelectionHandle.setLayoutParams(endParams);

            if (mSelectionListener != null && !mDragging) {
                mSelectionListener.onHandlesDrawn();
            }
        }
    };

    private boolean isInSelectionMode() {
        return this.mSelectionDragLayer.getParent() != null;
    }

    private boolean startDrag(View v) {
        mDragController.startDrag(v, mSelectionDragLayer, v, DragBehavior.MOVE);
        return true;
    }

    private float getDensityDependentValue(float val, Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return val * (metrics.densityDpi / 160f);
    }

    private float getDensityIndependentValue(float val, Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return val / (metrics.densityDpi / 160f);
    }

    @Override
    public void saveSelection(int start, int end, int startVerseNumber, int endVerseNumber,String text, int fontSize) {
        if (mSelectionListener != null) {
            mSelectionListener.saveSelection(start, end, startVerseNumber, endVerseNumber,text, fontSize);
        }
    }
}

package com.example.popwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class HintProviderPopUpMenu extends PopupWindow {
    public HintProviderPopUpMenu(View view) {

        super(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        Context context = view.getContext();
        setBackgroundDrawable(context.getResources().getDrawable(
                android.R.color.transparent));
        setTouchable(true);
        setFocusable(true);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setOutsideTouchable(false);
    }
}

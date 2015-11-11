package com.example.EpubProject.customised;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.popwindow.DiloagViewProvider;
import com.example.popwindow.DiloagViewProvider.OnActionToolBarListener;
import com.example.popwindow.HintProviderPopUpMenu;
import com.nivbible.R;

public class ActionBarView extends RelativeLayout implements
        android.view.View.OnClickListener, OnActionToolBarListener {
    private ImageView readerImageView;
    private CheckBox menuImageView;
    private HintProviderPopUpMenu hintProviderPopUpMenu;
    File BaseFolder, ScreenShot;
    String mFileName;
    int Pagenumber;

    public ActionBarView(Context context) {
        super(context);
        init(context);
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ActionBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        final View rootView = inflate(context, R.layout.custom_actionbar, this);
        if (isInEditMode()) {
            return;
        }
        menuImageView = (CheckBox) rootView.findViewById(R.id.menu_icon);
        readerImageView = (ImageView) rootView.findViewById(R.id.reader_image);
        TextView titleTextView = (TextView) rootView
                .findViewById(R.id.textvw_bible_text);

        initControls();
    }

    private void initControls() {

        ArrayList<ImageView> _drawer_items = new ArrayList<ImageView>();
        _drawer_items.add(readerImageView);
        for (int i = 0; i < _drawer_items.size(); i++) {
            _drawer_items.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        implementAction(v);

    }

    public void implementAction(View v) {
        if (v.getId() == readerImageView.getId()) {
            loadDialog(v);
            readerImageView
                    .setImageResource(R.drawable.language_nav_icon_selected);

        }
    }

    private OnItemClickListener onItemClickListner;

    public interface OnBookMarkClickedListner {
        public void onClickBookMark();
    }

    private void loadDialog(View view) {

        DiloagViewProvider diloagViewProvider = new DiloagViewProvider(getContext(), this,
                onItemClickListner);
        hintProviderPopUpMenu = new HintProviderPopUpMenu(
                diloagViewProvider.getPopupView());
        hintProviderPopUpMenu.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        hintProviderPopUpMenu.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        hintProviderPopUpMenu.showAsDropDown(view);
        hintProviderPopUpMenu.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                readerImageView.setImageResource(R.drawable.language_nav_icon);
            }
        });
    }

    @Override
    public void onDismiss() {

        if (hintProviderPopUpMenu != null) {
            hintProviderPopUpMenu.dismiss();
            readerImageView.setImageResource(R.drawable.language_nav_icon);
        }

    }

    public OnItemClickListener getOnItemClickListner() {

        return onItemClickListner;
    }

    public void setOnItemClickListner2(
            OnBookMarkClickedListner onItemClickListner) {

        Log.e("", "you are in action bar on item click");
        OnBookMarkClickedListner onItemsClick = onItemClickListner;
    }

    public OnItemClickListener getOnItemClickListner2() {

        return onItemClickListner;
    }

    public void setOnItemClickListner(OnItemClickListener onItemClickListner) {

        Log.e("", "you are in action bar on item click");
        this.onItemClickListner = onItemClickListner;
    }

    public void checkboxMenu(boolean isOpened) {
        if (isOpened) {
            menuImageView.setChecked(true);
        } else {
            menuImageView.setChecked(false);
        }
    }

}

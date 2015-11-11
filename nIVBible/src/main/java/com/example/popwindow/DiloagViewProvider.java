package com.example.popwindow;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.EpubProject.customised.CustomAdapter;
import com.nivbible.R;
import com.nivbible.ReaderFragment;

public class DiloagViewProvider {

    private final View popView;
    Context activity;
    private ListView lang;
    private final OnActionToolBarListener actionToolBarListener;
    private CustomAdapter adapter;
    private ReaderFragment rfragment;

    public interface OnActionToolBarListener {
        void onDismiss();
    }

    /**
     *
     */
    public DiloagViewProvider(final Context context,
                              final OnActionToolBarListener actionToolBarListener,
                              final OnItemClickListener clickListener) {
        this.actionToolBarListener = actionToolBarListener;
        this.activity = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popup_hint_view,
                null, false);

        lang = (ListView) popView.findViewById(R.id.listview);

        adapter = new CustomAdapter(context);
        lang.setAdapter(adapter);

        lang.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (clickListener != null)
                    clickListener.onItemClick(parent, view, position, id);

                Log.e("", "Hre we are clicking on" + position);
                actionToolBarListener.onDismiss();

            }
        });

    }

    public View getPopupView() {
        return popView;
    }

}

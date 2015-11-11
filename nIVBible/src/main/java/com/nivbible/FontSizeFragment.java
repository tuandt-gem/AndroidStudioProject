package com.nivbible;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.IConstant;
import com.example.EpubProject.MainActivity;

public class FontSizeFragment extends Fragment implements
        OnClickListener {
    private LinearLayout lnr_font_size, lnr_font_size2, lnr_font_size3,
            lnr_font_size4, lnr_font_size5;
    private ImageView imageView, imageView2, imageView3, imageView4,
            imageView5;
    private SharedPreferences sharedPreferencesSize;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.font_fragment, null);

        lnr_font_size = (LinearLayout) rootView
                .findViewById(R.id.lnr_font_size);
        lnr_font_size2 = (LinearLayout) rootView
                .findViewById(R.id.lnr_font_size2);
        lnr_font_size3 = (LinearLayout) rootView
                .findViewById(R.id.lnr_font_size3);
        lnr_font_size4 = (LinearLayout) rootView
                .findViewById(R.id.lnr_font_size4);
        lnr_font_size5 = (LinearLayout) rootView
                .findViewById(R.id.lnr_font_size5);

        imageView = (ImageView) rootView.findViewById(R.id.checkBox);
        imageView2 = (ImageView) rootView.findViewById(R.id.checkBox2);
        imageView3 = (ImageView) rootView.findViewById(R.id.checkBox3);
        imageView4 = (ImageView) rootView.findViewById(R.id.checkBox4);
        imageView5 = (ImageView) rootView.findViewById(R.id.checkBox5);

        lnr_font_size.setOnClickListener(this);
        lnr_font_size2.setOnClickListener(this);
        lnr_font_size3.setOnClickListener(this);
        lnr_font_size4.setOnClickListener(this);
        lnr_font_size5.setOnClickListener(this);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .visibleBookMark(false);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .changeBookName("FontSize");
        /*((MainActivity) EpubReader.getActivityIsntanse())
				.visiblechangeBookNumber(false);*/
        sharedPreferencesSize = getActivity().getSharedPreferences("fontSize",
                0);

        if (sharedPreferencesSize.getBoolean(IConstant.TXTFONT, true)) {
            switch (sharedPreferencesSize.getInt(IConstant.TXTSIZES, 8)) {
                case 1:
                    imageView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    imageView2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    imageView3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    imageView4.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    imageView5.setVisibility(View.VISIBLE);
                    break;

                default:
                    imageView2.setVisibility(View.VISIBLE);
                    break;
            }

        } else {
            imageView2.setVisibility(View.VISIBLE);
        }

        EpubReader.getDrawerInstanse(EpubReader.getActivityIsntanse())
                .setSlidingEnabled(true);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lnr_font_size:
                setShedPrefrnceFontSize(1, 16);
                EpubReader.getFlowInstanse().replace(new ReaderFragment(),
                        getTextSizeBundle(16), false);

                break;
            case R.id.lnr_font_size2:
                setShedPrefrnceFontSize(2, 18);
                EpubReader.getFlowInstanse().replace(new ReaderFragment(),
                        getTextSizeBundle(18), false);

                break;
            case R.id.lnr_font_size3:
                setShedPrefrnceFontSize(3, 25);
                EpubReader.getFlowInstanse().replace(new ReaderFragment(),
                        getTextSizeBundle(25), false);

                break;
            case R.id.lnr_font_size4:
                setShedPrefrnceFontSize(4, 30);
                EpubReader.getFlowInstanse().replace(new ReaderFragment(),
                        getTextSizeBundle(30), false);

                break;
            case R.id.lnr_font_size5:

                setShedPrefrnceFontSize(5, 40);
                EpubReader.getFlowInstanse().replace(new ReaderFragment(),
                        getTextSizeBundle(40), false);

                break;

            default:
                break;
        }

    }

    private Bundle getTextSizeBundle(int textSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(IConstant.FONTSIZE, textSize);
        return bundle;
    }

    public void setShedPrefrnceFontSize(int size, int fontSize) {
        sharedPreferencesSize.edit().putBoolean(IConstant.TXTFONT, true)
                .commit();
        sharedPreferencesSize.edit().putInt(IConstant.TXTSIZES, size).commit();
        sharedPreferencesSize.edit().putInt(IConstant.TXTFONTSIZES, fontSize)
                .commit();

    }

}

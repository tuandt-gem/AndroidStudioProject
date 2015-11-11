package com.nivbible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.EpubProject.MainActivity;
import com.example.EpubProject.utils.Constants;
import com.example.EpubProject.utils.ListPage;
import com.example.EpubProject.utils.ListPageBook;
import com.example.EpubProject.utils.PageUTils;
import com.example.EpubProject.utils.PageUTils.LanguageType;
import com.example.EpubProject.utils.PageUTilsBook;
import com.example.EpubProject.utils.PageUTilsBook.LanguageTypeBook;
import com.example.epub.TableOfContents;
import com.grid.adpter.GridItem;

import java.util.ArrayList;
import java.util.List;

public class ChapterFragment extends Fragment {

    private ListView listView;
    private TableOfContents mToc;
    public static final String CHAPTERS_EXTRA = "CHAPTERS_EXTRA";
    public static final String CHAPTER_EXTRA = "CHAPTER_EXTRA";
    private List<ListPage> result;
    private List<ListPageBook> resultBook;
    private SharedPreferences preferencesBook;
    int topPosition = 0;
    private SharedPreferences preferencesBookNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.chapter_fragment, null);

        preferencesBook = getActivity().getSharedPreferences("book", 0);
        preferencesBookNumber = getActivity().getSharedPreferences(
                "booknumber", 0);

        Bundle bundle = getArguments();
        // bundle.getString(key);
        mToc = new TableOfContents(bundle, CHAPTERS_EXTRA);
        listView = (ListView) rootView.findViewById(R.id.list_view);

        if (preferencesBook.getInt("position", Constants.POS0_START) == Constants.POS0_START) {
            PageUTils.getPageCount(LanguageType.EQ);
            result = PageUTils.getListPageEq();
        } else {
            PageUTils.getPageCount(LanguageType.ENGLISH);
            result = PageUTils.getListPageEnglis();
        }
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleNightMode(false);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .visibleBookMark(false);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .changeBookName("Amplified");
        /*
         * ((MainActivity) EpubReader.getActivityIsntanse())
         * .visiblechangeBookNumber(false);
         */
        listView.setAdapter(new EpubChapterAdapter(getActivity(), result));
        listView.setSelection(topPosition);

        return rootView;
    }

    private void goTOBack(int pos, int totalpage) {
//        if (pos != 0) {
            if (onChapterSelected != null)
                onChapterSelected.onChapterSelected(pos);
//        }
    }

    private class EpubChapterAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<ListPage> result;
        private Context context;
        private ArrayList<GridItem> _list_items = new ArrayList<GridItem>();

        public EpubChapterAdapter(Context context, List<ListPage> result) {
            this.mInflater = LayoutInflater.from(context);
            this.result = result;
            this.context = context;
        }

        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public ListPage getItem(int position) {
            return result.get(position);
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            final ViewHolderGroup viewHolderGruop;
            View view = row;
            if (view != null) {
                viewHolderGruop = (ViewHolderGroup) view.getTag();
            } else {
                view = mInflater.inflate(R.layout.row_item, parent, false);
                viewHolderGruop = new ViewHolderGroup(view);
                view.setTag(viewHolderGruop);
            }
            String title = result.get(position).name;

            viewHolderGruop._txt.setText(title);

            viewHolderGruop._txt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(position);
                        }
                    });
                    result.get(position).setChecked(
                            !result.get(position).isChecked());
                    notifyDataSetChanged();
                }
            });

            if (result.get(position).isChecked()) {
                if (viewHolderGruop._linear.getChildCount() > 0) {
                    viewHolderGruop._linear.removeAllViews();
                    notifyDataSetChanged();
                }
                int numberOfCount = result.get(position).pageCount;
                if (numberOfCount % 5 == 0) {
                    numberOfCount = numberOfCount / 5;
                } else {
                    numberOfCount = (numberOfCount / 5) + 1;
                }
                for (int i = 0; i < numberOfCount; i++) {

                    final GridItem item = new GridItem(mInflater.getContext());
                    LayoutParams params = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    item.setLayoutParams(params);

                    for (int j = 0; j < 5; j++) {

                        final int text = i * 5 + 1 + j;
                        item.getTextViewAt(j).setText("" + text);

                        item.getTextViewAt(j).setOnClickListener(
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        TextView textView = (TextView) v;

                                        if (preferencesBook.getInt("position",
                                                Constants.POS0_START) == Constants.POS0_START) {

                                            PageUTilsBook.getPageCount(LanguageTypeBook.EQBOOK);
                                            resultBook = PageUTilsBook.getListPageBookEq();

                                            for (int k = 0; k < resultBook.size(); k++) {
                                                int npos = position;
                                                if (resultBook.get(k).nameBook.equalsIgnoreCase(result.get(position).name)) {
                                                    int totalpage = resultBook
                                                            .get(k).pageCountBook;
                                                    int clickText = Integer
                                                            .parseInt((String) textView
                                                                    .getText());
                                                    int pos = totalpage + (Constants.POS0_START - 1)
                                                            + clickText;

                                                    if (result.get(position).name
                                                            .contains("Song of Solomon")) {
                                                        ((MainActivity) EpubReader
                                                                .getActivityIsntanse())
                                                                .changeBookName("Song of Solomon.."
                                                                        + " "
                                                                        + clickText);
                                                    } else {
                                                        ((MainActivity) EpubReader
                                                                .getActivityIsntanse())
                                                                .changeBookName(result
                                                                        .get(position).name
                                                                        + " "
                                                                        + clickText);
                                                    }

                                                    preferencesBookNumber
                                                            .edit()
                                                            .putBoolean(
                                                                    "chapter",
                                                                    true)
                                                            .commit();
                                                    preferencesBookNumber
                                                            .edit()
                                                            .putInt("totalno",
                                                                    pos)
                                                            .commit();

                                                    preferencesBookNumber
                                                            .edit()
                                                            .putInt("totalpage",
                                                                    pos - (Constants.POS0_START - 1))
                                                            .commit();
                                                    PageUTils
                                                            .getListPageEnglis()
                                                            .clear();
                                                    PageUTils.getListPageEq()
                                                            .clear();
                                                    PageUTilsBook
                                                            .getListPageBookEq()
                                                            .clear();
                                                    goTOBack(pos, totalpage);

                                                }

                                            }

                                        } else {
                                            PageUTilsBook
                                                    .getPageCount(LanguageTypeBook.ENGLISHBOOK);
                                            resultBook = PageUTilsBook
                                                    .getListPageBookEnglis();

                                            for (int k = 0; k < resultBook
                                                    .size(); k++) {
                                                if (resultBook.get(k).nameBook
                                                        .equalsIgnoreCase(result
                                                                .get(position).name)) {

                                                    int totalpage = resultBook
                                                            .get(k).pageCountBook;
                                                    int clickText = Integer
                                                            .parseInt((String) textView
                                                                    .getText());

                                                    int pos = totalpage + (Constants.POS1_START - 1)
                                                            + clickText;

                                                    if (result.get(position).name
                                                            .contains("Song of Solomon")) {
                                                        ((MainActivity) EpubReader
                                                                .getActivityIsntanse())
                                                                .changeBookName("Song of Solomon.."
                                                                        + " "
                                                                        + clickText);
                                                    } else {
                                                        ((MainActivity) EpubReader
                                                                .getActivityIsntanse())
                                                                .changeBookName(result
                                                                        .get(position).name
                                                                        + " "
                                                                        + clickText);
                                                    }

                                                    preferencesBookNumber
                                                            .edit()
                                                            .putBoolean(
                                                                    "chapter",
                                                                    true)
                                                            .commit();
                                                    preferencesBookNumber
                                                            .edit()
                                                            .putInt("totalno",
                                                                    pos)
                                                            .commit();

                                                    preferencesBookNumber
                                                            .edit()
                                                            .putInt("totalpage",
                                                                    pos - (Constants.POS1_START - 1))
                                                            .commit();
                                                    PageUTils
                                                            .getListPageEnglis()
                                                            .clear();
                                                    PageUTils.getListPageEq()
                                                            .clear();
                                                    PageUTilsBook
                                                            .getListPageBookEnglis()
                                                            .clear();
                                                    goTOBack(pos, totalpage);

                                                }

                                            }

                                        }

                                    }
                                });

                        if (text > result.get(position).pageCount) {
                            item.getTextViewAt(j).setVisibility(View.INVISIBLE);
                        }
                    }
                    viewHolderGruop._linear.addView(item);

                }

                notifyDataSetChanged();

            } else {
                if (viewHolderGruop._linear.getChildCount() > 0) {
                    viewHolderGruop._linear.removeAllViews();
                    notifyDataSetChanged();
                }
            }
            return view;
        }

        public class ViewHolderGroup {
            public TextView _txt;
            public LinearLayout _linear;

            ViewHolderGroup(View convertView) {
                _txt = (TextView) convertView.findViewById(R.id.txt_title);
                _linear = (LinearLayout) convertView.findViewById(R.id.parent);
            }

        }

    }

    private IOnChapterSelected onChapterSelected;

    public interface IOnChapterSelected {
        void onChapterSelected(int pos);
    }

    public void setOnChapterSelected(IOnChapterSelected onChapterSelected) {
        this.onChapterSelected = onChapterSelected;
    }

}

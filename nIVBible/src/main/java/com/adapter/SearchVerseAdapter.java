package com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.EpubProject.Utility;
import com.example.EpubProject.utils.Constants;
import com.example.epub.db.DbHelper;
import com.example.epub.db.html.Verse;
import com.nivbible.R;

import org.jsoup.helper.StringUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Search verse adapter
 * Created by neo on 10/30/2015.
 */
public class SearchVerseAdapter extends FilterableAdapter<Verse, SearchVerseAdapter.ViewHolder>
        implements TextWatcher {
    private List<Verse> mFilterVerses;
    private IOnVerseSelected mOnVerseSelected;
    private IOnFilter mOnFilter;
    private String mKeyText;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNoteText;
        public TextView mVerseNumberText;
        public View root;

        public ViewHolder(View v) {
            super(v);
            root = v;
            mNoteText = (TextView) root.findViewById(R.id.item_search_text);
            mVerseNumberText = (TextView) root.findViewById(R.id.item_search_verse_tv);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchVerseAdapter(Context context, List<Verse> verses, IOnVerseSelected onVerseSelected, IOnFilter onFilter) {
        super(context, verses);
        this.mOnVerseSelected = onVerseSelected;
        mFilterVerses = new ArrayList<>();
        mOnFilter = onFilter;
        mContext = context;
    }

    @Override
    public SearchVerseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_verse, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        synchronized (LOCK) {
            final Verse verse = mFilterVerses.get(position);

            holder.mVerseNumberText.setText(Utility.formatChapterVerses(verse));

            if (mOnVerseSelected != null) {
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnVerseSelected.onVerseSelected(verse.getPageNumber(), verse.getVerseNumber());
                    }
                });
            }

            String text = verse.getText();
            holder.mNoteText.setText(text);
            if (text == null || StringUtil.isBlank(mKeyText)) {
                return;
            }

            int index = text.toLowerCase().indexOf(mKeyText.toLowerCase());
            if (index < 0) {
                return;
            }

            final SpannableStringBuilder sb = new SpannableStringBuilder(text);
            final StyleSpan bss = new StyleSpan(Typeface.BOLD);

            sb.setSpan(bss, index, index + mKeyText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            holder.mNoteText.setText(sb);
        }
    }

    @Override
    public int getItemCount() {
        return mFilterVerses.size();
    }

    @Override
    protected boolean filterObject(Verse myObject, String constraint) {
        return myObject.getText().toLowerCase().contains(constraint.toLowerCase());
    }

    @Override
    protected List<Verse> filterFromAnotherResource(String search) {
//        mFilterVerses.clear();
//        for (Verse verse : mAllVerses) {
//            if (verse.getText().toLowerCase().contains(text.toLowerCase())) {
//                mFilterVerses.add(verse);
//            }
//        }

        DbHelper db = new DbHelper(mContext);
        db.getReadableDatabase().beginTransaction();

        String bookName = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(Constants.SELECTED_FILE, Constants.FILE_NAME[0]);

        List<Verse> verses = new ArrayList<>();
        try {
            verses = db.searchVerses(bookName, search);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.getReadableDatabase().endTransaction();

        Log.e("@@@", "versesverses=" + verses.size());
        return verses;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mKeyText = s.toString();

        getFilter().filter(s.toString(), new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                mFilterVerses.clear();

                // Do not filter with blank
                if (!StringUtil.isBlank(mKeyText)) {
                    mFilterVerses.addAll(getFilterItems());
                }

                if (mOnFilter != null) {
                    mOnFilter.onFilter(mFilterVerses.size());
                }
            }
        });
    }

    public interface IOnVerseSelected {
        void onVerseSelected(int pagePosition, int verseNumber);
    }

    public interface IOnFilter {
        void onFilter(int count);
    }

}


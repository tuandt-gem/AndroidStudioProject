package com.nivbible;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.adapter.SearchVerseAdapter;
import com.example.EpubProject.MainActivity;
import com.example.EpubProject.utils.Constants;
import com.example.epub.db.DbHelper;
import com.example.epub.db.html.Verse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment
        implements SearchVerseAdapter.IOnVerseSelected, SearchVerseAdapter.IOnFilter {
    public static final String KEY_PAGE_POSITION = "page_position";

    private EditText searchVerseEt;
    private RecyclerView versesRecyclerView;

    private TextView verseCountTv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, null);

        versesRecyclerView = (RecyclerView) rootView.findViewById(R.id.search_verse_rv);
        versesRecyclerView.setHasFixedSize(true);
        versesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchVerseEt = (EditText) rootView.findViewById(R.id.search_verse_search_et);

        EpubReader.getDrawerInstanse(EpubReader.getActivityIsntanse())
                .setSlidingEnabled(true);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .visibleBookMark(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleNightMode(false);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .changeBookName("Search");

        verseCountTv = (TextView) rootView.findViewById(R.id.search_verse_count_tv);

//        searchVerseEt.setOnFocusChangeListener(this);
        searchVerseEt.setFocusable(true);
//        searchVerseEt.setFocusableInTouchMode(true);
        searchVerseEt.requestFocus();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVerses();
    }

    /**
     * Get verses from DB
     */
    private void initVerses() {
        new InitAsync().execute();
    }


    @Override
    public void onVerseSelected(int pagePosition, int verseNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(ReaderFragment.KEY_PAGE_POSITION, pagePosition);
        bundle.putInt(ReaderFragment.KEY_VERSE_NUMBER, verseNumber);
        bundle.putBoolean(ReaderFragment.KEY_HIGHLIGHT_ON_SCROLL, true);
        EpubReader.getFlowInstanse().replace(new ReaderFragment(), bundle, false);
    }

    @Override
    public void onFilter(int count) {
        verseCountTv.setText(String.format(getString(R.string.verse_count_format), count));
    }

    private class InitAsync extends AsyncTask<Void, Void, List<Verse>> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected List<Verse> doInBackground(Void... params) {
            String bookName = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(Constants.SELECTED_FILE, Constants.FILE_NAME[0]);
            try {
                DbHelper db = new DbHelper(getActivity());
                return db.getAllVerses(bookName);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Verse> verses) {
            super.onPostExecute(verses);
            SearchVerseAdapter verseAdapter = new SearchVerseAdapter(getActivity(), verses, SearchFragment.this, SearchFragment.this);
            versesRecyclerView.setAdapter(verseAdapter);

            searchVerseEt.addTextChangedListener(verseAdapter);
            onFilter(0);

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }
}

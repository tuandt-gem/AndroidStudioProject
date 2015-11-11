package com.nivbible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.adapter.DividerItemDecoration;
import com.adapter.NotesAdapter;
import com.daimajia.swipe.util.Attributes;
import com.example.EpubProject.MainActivity;
import com.example.EpubProject.MainActivity.IOnDeleteListner;
import com.example.EpubProject.utils.Constants;
import com.example.epub.db.DbHelper;
import com.example.epub.selection.Selection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class NotesFragment extends Fragment implements
        OnFocusChangeListener, NotesAdapter.IOnNoteSelected, NotesAdapter.IOnFilter {

    private EditText searchNoteEt;
    private RecyclerView notesRecyclerView;

    private TextView noteCountTv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notes_fragment, null);

        notesRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_notes_rv);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

//        notesRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        searchNoteEt = (EditText) rootView.findViewById(R.id.list_notes_search_et);

        EpubReader.getDrawerInstanse(EpubReader.getActivityIsntanse())
                .setSlidingEnabled(true);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .visibleBookMark(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleReader(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleDelete(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleSearch(false);
        ((MainActivity) EpubReader.getActivityIsntanse()).visibleNightMode(false);
//        ((MainActivity) EpubReader.getActivityIsntanse())
//                .setOnDeleteListner(this);
        ((MainActivity) EpubReader.getActivityIsntanse())
                .changeBookName("Notes");
        /*((MainActivity) EpubReader.getActivityIsntanse())
                .visiblechangeBookNumber(false);*/
        noteCountTv = (TextView) rootView.findViewById(R.id.list_notes_count_tv);

        searchNoteEt.setOnFocusChangeListener(this);
        searchNoteEt.setFocusable(true);
        searchNoteEt.setFocusableInTouchMode(true);
        searchNoteEt.requestFocus();

        searchNoteEt.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    setShedPrefrnceUserLogin();
                    return true;
                }

                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initNotes();
    }

    /**
     * Get notes from DB
     */
    private void initNotes() {
        DbHelper db = new DbHelper(getActivity());
        List<Selection> notes;
        try {
            String fileName = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(Constants.SELECTED_FILE, Constants.FILE_NAME[0]);

            notes = db.getAllNotes(fileName);

            NotesAdapter notesAdapter = new NotesAdapter(getActivity(), notes, this, this);
            notesRecyclerView.setAdapter(notesAdapter);
            notesAdapter.setMode(Attributes.Mode.Single);

            searchNoteEt.addTextChangedListener(notesAdapter);
            onFilter(notes.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.e("edt", "" + "edt-" + hasFocus);
        if (!hasFocus) {
            searchNoteEt.post(new Runnable() {

                @Override
                public void run() {
//                    try {
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }
    }


    @Override
    public void onNoteSelected(int pagePosition, int verseNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(ReaderFragment.KEY_PAGE_POSITION, pagePosition);
        bundle.putInt(ReaderFragment.KEY_VERSE_NUMBER, verseNumber);
        EpubReader.getFlowInstanse().replace(new ReaderFragment(), bundle, false);
    }

    @Override
    public void onFilter(int count) {
        noteCountTv.setText(String.format(getString(R.string.note_count_format), count));
    }
}

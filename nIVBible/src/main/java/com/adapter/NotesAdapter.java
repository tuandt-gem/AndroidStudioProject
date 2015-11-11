package com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.EpubProject.Utility;
import com.example.epub.db.DbHelper;
import com.example.epub.selection.Selection;
import com.example.epub.selection.SelectionColor;
import com.nivbible.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Notes adapter
 * Created by neo on 10/29/2015.
 */
public class NotesAdapter extends RecyclerSwipeAdapter<NotesAdapter.ViewHolder>
        implements TextWatcher {
    private List<Selection> mAllNotes;
    private List<Selection> mFilterNotes;
    private NotesAdapter.IOnNoteSelected mOnNoteSelected;
    private IOnFilter mOnFilter;
    private Context mContext;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNoteContentText;
        public TextView mVerseNumberText;
        public ImageView mColorImage;
        public View root;
        public View mDataLayout;
        public SwipeLayout mSwipeLayout;
        public Button mDeleteButton;
        public TextView mNoteDescriptionText;

        public ViewHolder(View v) {
            super(v);
            root = v;
            mSwipeLayout = (SwipeLayout) root.findViewById(R.id.swipe);
            mDataLayout = root.findViewById(R.id.note_item_data_layout);
            mNoteContentText = (TextView) root.findViewById(R.id.item_note_content_text);
            mColorImage = (ImageView) root.findViewById(R.id.item_note_color_iv);
            mVerseNumberText = (TextView) root.findViewById(R.id.item_note_verse_tv);
            mNoteDescriptionText = (TextView) root.findViewById(R.id.item_note_description_text);

            mDeleteButton = (Button) itemView.findViewById(R.id.note_item_delete_bt);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotesAdapter(Context context, List<Selection> notes, NotesAdapter.IOnNoteSelected onNoteSelected, IOnFilter onFilter) {
        mContext = context;
        mAllNotes = notes;
        mOnNoteSelected = onNoteSelected;
        mFilterNotes = new ArrayList<>(mAllNotes);
        mOnFilter = onFilter;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Note to be bind data
        final Selection selection = mFilterNotes.get(position);

        // Swipe to delete
        holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                Log.e("NoteAdapter", "Swipe onOpen");
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmRemoveDialog(selection, position, holder.mSwipeLayout);
            }
        });


        holder.mNoteContentText.setText(selection.getText());
        holder.mVerseNumberText.setText(Utility.formatNoteChapter(selection));
        holder.mNoteDescriptionText.setText(selection.getNote());

        String color = selection.getColor();
        if (color.equals(SelectionColor.INITIAL)) {
            color = SelectionColor.DEFAULT;
        }
        if (!color.equals(SelectionColor.UNDERLINE)) {
            holder.mColorImage.setVisibility(View.VISIBLE);
            holder.mColorImage.setBackgroundColor(Color.parseColor(color));
        } else {
            holder.mColorImage.setVisibility(View.GONE);
            holder.mNoteContentText.setPaintFlags(holder.mNoteContentText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        if (mOnNoteSelected != null) {
            holder.mDataLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNoteSelected.onNoteSelected(selection.getPageNumber(), selection.getStartVerseNumber());
                }
            });
        }

        mItemManger.bindView(holder.root, position);
    }


    public void showConfirmRemoveDialog(final Selection selection, final int position, final SwipeLayout swipeLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this margin note?");
        builder.setPositiveButton("Yes, Delete it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeSelection(selection, position, swipeLayout);
            }
        });

        builder.setNeutralButton("No", null);
        builder.create().show();
    }

    private void removeSelection(Selection selection, int position, SwipeLayout swipeLayout) {
        selection.setColor(SelectionColor.INITIAL);
        try {
            new DbHelper(mContext).createOrUpdateSelection(selection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mItemManger.removeShownLayouts(swipeLayout);
        mFilterNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFilterNotes.size());
        mItemManger.closeAllItems();
        if (mOnFilter != null) {
            mOnFilter.onFilter(mFilterNotes.size());
        }
    }

    @Override
    public int getItemCount() {
        return mFilterNotes.size();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        // Only search text with length 2 minimum
        if (text.length() > 0) {
            filter(text);
        } else {
            mFilterNotes.clear();
            mFilterNotes.addAll(mAllNotes);
            notifyDataSetChanged();
        }

        if (mOnFilter != null) {
            mOnFilter.onFilter(mFilterNotes.size());
        }
    }

    /**
     * Filter notes with text
     */
    private void filter(String text) {
        mFilterNotes.clear();
        for (Selection selection : mAllNotes) {
            if (selection.getText().toLowerCase().contains(text.toLowerCase())) {
                mFilterNotes.add(selection);
            }
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public interface IOnNoteSelected {
        void onNoteSelected(int pagePosition, int verseNumber);
    }

    public interface IOnFilter {
        void onFilter(int count);
    }

}


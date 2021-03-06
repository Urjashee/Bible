package com.pnuema.bible.ui.adapters;

import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnuema.bible.R;
import com.pnuema.bible.data.IBook;
import com.pnuema.bible.data.firefly.Book;
import com.pnuema.bible.statics.CurrentSelected;
import com.pnuema.bible.ui.dialogs.BCVSelectionListener;
import com.pnuema.bible.ui.viewholders.BookSelectionViewHolder;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link BCVSelectionListener}.
 */
public class BookSelectionRecyclerViewAdapter extends RecyclerView.Adapter<BookSelectionViewHolder> {
    private final List<IBook> mValues;
    private final BCVSelectionListener mListener;

    public BookSelectionRecyclerViewAdapter(final List<IBook> items, final BCVSelectionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public BookSelectionViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new BookSelectionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BookSelectionViewHolder holder, final int position) {
        final TextView contentView = holder.getContentView();
        if (contentView == null) {
            return;
        }

        final IBook book = mValues.get(position);

        holder.setItem(book);
        contentView.setText(book.getName());

        final boolean currentIsSelected = CurrentSelected.getBook() != null && book.getId() == CurrentSelected.getBook();

        contentView.setTextColor(ContextCompat.getColor(contentView.getContext(), currentIsSelected ? R.color.primary : R.color.primary_text));
        contentView.setTypeface(null, currentIsSelected ? Typeface.BOLD : Typeface.NORMAL);

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null && holder.getItem() != null) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onBookSelected(holder.getItem().getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}

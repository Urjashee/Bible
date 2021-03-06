package com.pnuema.bible.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnuema.bible.R;
import com.pnuema.bible.data.IBook;
import com.pnuema.bible.data.IBookProvider;
import com.pnuema.bible.retrievers.BaseRetriever;
import com.pnuema.bible.retrievers.FireflyRetriever;
import com.pnuema.bible.statics.CurrentSelected;
import com.pnuema.bible.ui.adapters.BookSelectionRecyclerViewAdapter;
import com.pnuema.bible.ui.dialogs.BCVSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a list of books to pick from.
 * <p/>
 */
public class BookSelectionFragment extends Fragment implements Observer {
    private BCVSelectionListener mListener;
    private final List<IBook> mBooks = new ArrayList<>();
    private BaseRetriever mRetriever = new FireflyRetriever();
    private BookSelectionRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookSelectionFragment() {
    }

    @SuppressWarnings("unused")
    public static BookSelectionFragment newInstance(final BCVSelectionListener listener) {
        return new BookSelectionFragment().setListener(listener);
    }

    private BookSelectionFragment setListener(final BCVSelectionListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            mAdapter = new BookSelectionRecyclerViewAdapter(mBooks, mListener);
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRetriever.addObserver(this);
        mRetriever.getBooks();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRetriever.deleteObservers();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update(final Observable observable, final Object o) {
        mBooks.clear();
        if (o instanceof IBookProvider) {
            mBooks.addAll(((IBookProvider)o).getBooks());
            mAdapter.notifyDataSetChanged();

            if (CurrentSelected.getBook() != null && !mBooks.isEmpty()) {
                for (final IBook book : mBooks) {
                    if (book.getId() == CurrentSelected.getBook()) {
                        ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mBooks.indexOf(book), mRecyclerView.getHeight()/2);
                    }
                }
            }
        }
    }
}

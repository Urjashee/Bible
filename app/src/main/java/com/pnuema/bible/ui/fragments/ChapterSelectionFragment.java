package com.pnuema.bible.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnuema.bible.R;
import com.pnuema.bible.data.firefly.ChapterCount;
import com.pnuema.bible.retrievers.BaseRetriever;
import com.pnuema.bible.retrievers.FireflyRetriever;
import com.pnuema.bible.statics.CurrentSelected;
import com.pnuema.bible.ui.adapters.NumberSelectionAdapter;
import com.pnuema.bible.ui.dialogs.BCVSelectionListener;
import com.pnuema.bible.ui.dialogs.NumberSelectionListener;

import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a list of chapter numbers to pick from.
 * <p/>
 */
public class ChapterSelectionFragment extends Fragment implements Observer, NumberSelectionListener {
    private BCVSelectionListener mListener;
    private BaseRetriever mRetriever = new FireflyRetriever();
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChapterSelectionFragment() {
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && CurrentSelected.getBook() != null) {
            mRetriever.getChapters(String.valueOf(CurrentSelected.getBook()));
        }
    }

    @SuppressWarnings("unused")
    public static ChapterSelectionFragment newInstance(final BCVSelectionListener listener) {
        return new ChapterSelectionFragment().setListener(listener);
    }

    private ChapterSelectionFragment setListener(final BCVSelectionListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_number_list, container, false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        return mRecyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRetriever.addObserver(this);
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
        final Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (o instanceof ChapterCount) {
            mRecyclerView.setAdapter(new NumberSelectionAdapter(((ChapterCount)o).getChapterCount(),
                                                                CurrentSelected.getChapter() == null || CurrentSelected.getChapter() == null ? null : CurrentSelected.getChapter(),
                                                                this));
        }
    }

    @Override
    public void numberSelected(final int number) {
        mListener.onChapterSelected(number);
    }
}

package com.pnuema.simplebible.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.pnuema.simplebible.R;
import com.pnuema.simplebible.data.Verses;
import com.pnuema.simplebible.retrievers.VersesRetriever;
import com.pnuema.simplebible.statics.CurrentSelected;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A fragment representing a list of verse numbers to pick from.
 * <p/>
 */
public class VerseSelectionFragment extends Fragment implements Observer {
    private BCVSelectionListener mListener;
    private final List<Verses.Verse> mVerses = new ArrayList<>();
    private VersesRetriever mRetriever = new VersesRetriever();
    private GridView mGridView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VerseSelectionFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mRetriever.addObserver(this);
        if (isVisibleToUser && CurrentSelected.getChapter() != null) {
            mRetriever.loadData(getContext(), CurrentSelected.getChapter().id);
        }
    }

    @SuppressWarnings("unused")
    public static VerseSelectionFragment newInstance(BCVSelectionListener listener) {
        return new VerseSelectionFragment().setListener(listener);
    }

    private VerseSelectionFragment setListener(BCVSelectionListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGridView = (GridView) inflater.inflate(R.layout.fragment_number_list, container, false);
        return mGridView;
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
    public void update(Observable observable, Object o) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        mVerses.clear();
        if (o instanceof Verses && ((Verses)o).response.verses != null) {
            mVerses.addAll(((Verses)o).response.verses);
        }

        List<Integer> mList = new ArrayList<>();
        for (int i = 1; i < mVerses.size(); i++) {
            mList.add(i);
        }

        mGridView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_number, mList));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int verseId = Integer.parseInt(((TextView)view).getText().toString());
                mListener.onVerseSelected(mVerses.get(verseId));
            }
        });
    }
}
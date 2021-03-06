package com.pnuema.bible.ui.dialogs;

public interface BCVSelectionListener {
    void onBookSelected(int book);
    void onChapterSelected(int chapter);
    void onVerseSelected(int verse);
    void refresh();
}
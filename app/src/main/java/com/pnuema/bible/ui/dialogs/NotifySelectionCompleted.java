package com.pnuema.bible.ui.dialogs;

public interface NotifySelectionCompleted {
    void onSelectionPreloadChapter(int book, int chapter);
    void onSelectionComplete(int book, int chapter, int verse);
}

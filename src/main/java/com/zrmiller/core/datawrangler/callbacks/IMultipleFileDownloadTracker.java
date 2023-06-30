package com.zrmiller.core.datawrangler.callbacks;

public interface IMultipleFileDownloadTracker {

    void updateProgress();

    void onDownloadComplete();

}

package com.zrmiller.core.datawrangler.callbacks;

public interface IDownloadTracker {

    void onDownloadComplete();

    void onCancel();

}

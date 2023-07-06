package com.zrmiller.core.datawrangler.legacy.callbacks;

@Deprecated
public interface IStatusTracker2017 {

    void onFileDownloadComplete();

    void onFileReadComplete();

    void onFileSortComplete();

    void onCompressComplete();

    void onCancel();

}

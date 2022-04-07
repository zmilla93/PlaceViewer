package com.zrmiller.core;

public interface IDownloadTracker {

    void downloadPercentCallback(int progress);

    void textCallback(String message);

}

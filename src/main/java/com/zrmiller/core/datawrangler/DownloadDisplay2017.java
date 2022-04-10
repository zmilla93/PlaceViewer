package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IDownloadDisplay2017;
import com.zrmiller.core.enums.DownloadStage2017;

public class DownloadDisplay2017 implements IDownloadDisplay2017 {

    public void proc(DownloadStage2017 stage) {
        switch (stage) {
            case DOWNLOADING:
                displayDownloading();
                break;
            case READING:
                displayReading();
                break;
            case SORTING:
                displaySorting();
                break;
            case MINIFYING:
                displayMinifying();
                break;
        }
    }

    @Override
    public void displayDownloading() {

    }

    @Override
    public void displayReading() {

    }

    @Override
    public void displaySorting() {

    }

    @Override
    public void displayMinifying() {

    }
}

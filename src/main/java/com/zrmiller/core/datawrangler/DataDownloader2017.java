package com.zrmiller.core.datawrangler;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.strings.FileName;

public class DataDownloader2017 extends DataDownloader {

    private static final String downloadURL = "https://www.zrmiller.com/PlaceData/2017/Place_2017.placetiles";

    public DataDownloader2017() {
        super(Dataset.PLACE_2017.YEAR_STRING);
    }

    public void run() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING)) return;
        DataDownloader downloader = this;
        Thread thread = new Thread(() -> {
            DataDownloader.activeDownloader = downloader;
            downloadFile(FileName.BINARY_2017.toString(), downloadURL);
            DataDownloader.activeDownloader = null;
        });
        thread.start();
    }

}

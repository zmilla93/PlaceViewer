package com.zrmiller.core.datawrangler;

import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.strings.FileName;

public class DataDownloader2017 extends DataDownloader {

    private static final String downloadURL = "https://www.zrmiller.com/PlaceData/2017/Place_2017.placetiles";

    public void run() {
        Thread thread = new Thread(() -> {
            if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING)) return;
            downloadFile(FileName.BINARY_2017.toString(), Dataset.PLACE_2017.YEAR_STRING, downloadURL);
        });
        thread.start();
    }

    @Override
    public void cancelDownload() {
        // TODO
    }

}

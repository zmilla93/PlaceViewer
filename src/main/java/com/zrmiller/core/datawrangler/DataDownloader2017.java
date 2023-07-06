package com.zrmiller.core.datawrangler;

import com.zrmiller.core.data.Dataset;
import com.zrmiller.core.data.FileName;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.modules.stopwatch.Stopwatch;

import java.io.File;

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

    public boolean deleteData() {
        File file = new File(SaveManager.settings.data.dataDirectory + Dataset.PLACE_2017.YEAR_STRING + File.separator + FileName.BINARY_2017);
        if (file.exists()) {
            if (!file.isFile())
                return false;
            Stopwatch.start();
            return file.delete();
        }
        return true;
    }

}

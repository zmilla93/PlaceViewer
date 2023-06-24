package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IDownloadTracker;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.File;

public class DataDownloader2022 extends DataDownloader {

    private static final String downloadURLTemplate = "https://www.zrmiller.com/PlaceData/2022/Place_2022_INDEX.placetiles";
    private static final String yearString = Dataset.PLACE_2022.YEAR_STRING;
    private String directory;
    private int filesDownloaded;

    public void run() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING)) return;
        directory = SaveManager.settings.data.dataDirectory + yearString + File.separator;
        System.out.println(directory);
        int startIndex = getStartingFileIndex();
        filesDownloaded = startIndex;
        Thread thread = new Thread(() -> {
            for (int i = startIndex; i < PlaceInfo.FILE_COUNT_2022; i++) {
                String downloadPath = getIndexedURL(i);
                IDownloadTracker tracker = new IDownloadTracker() {
                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onCancel() {

                    }
                };
                setTracker(tracker);
                downloadFile(FileName.BINARY_2022.getIndexedName(i), Dataset.PLACE_2022.YEAR_STRING, downloadPath);
                filesDownloaded++;
                multipleFileTracker.updateProgress();
            }
            multipleFileTracker.downloadComplete();
        });
        thread.start();
//        downloadFile(FileName.BINARY_2017.toString(), Dataset.PLACE_2017.YEAR_STRING, downloadURL);
    }

    private int getStartingFileIndex() {
        int index;
        for (index = 0; index < PlaceInfo.FILE_COUNT_2022; index++) {
            File file = new File(directory + FileName.BINARY_2022.getIndexedName(index));
            if (!file.exists() || !file.isFile()) {
                return index;
            }
        }
        return index;
    }

    private String getIndexedURL(int index) {
        return downloadURLTemplate.replaceFirst("INDEX", Integer.toString(index));
    }

    public int getFilesDownloaded() {
        return filesDownloaded;
    }

    public int getExpectedFiles() {
        // FIXME
        return PlaceInfo.FILE_COUNT_2022;
    }

    @Override
    public void cancelDownload() {

    }

}

package com.zrmiller.core.datawrangler;

import com.zrmiller.core.datawrangler.callbacks.IDownloadTracker;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.strings.FileName;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class DataDownloader2022 extends DataDownloader {

    private static final String downloadURLTemplate = "https://www.zrmiller.com/PlaceData/2022/Place_2022_INDEX.placetiles";
    private static final String yearString = Dataset.PLACE_2022.YEAR_STRING;
    private String directory;
    private int filesDownloaded;

    public void run() {
        if (!validateDirectory(Dataset.PLACE_2017.YEAR_STRING)) return;
        directory = SaveManager.settings.data.dataDirectory + yearString + File.separator;
        // Get missing file count before starting thread so UI can show starting status
        ArrayList<Integer> fileIndexes = getMissingFileIndexes();
        filesDownloaded = PlaceInfo.FILE_COUNT_2022 - fileIndexes.size();
        Thread thread = new Thread(() -> {
            for (int index : fileIndexes) {
                String downloadPath = getIndexedURL(index);
                IDownloadTracker tracker = new IDownloadTracker() {
                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onCancel() {

                    }
                };
                setTracker(tracker);
                downloadFile(FileName.BINARY_2022.getIndexedName(index), Dataset.PLACE_2022.YEAR_STRING, downloadPath);
                filesDownloaded++;
                multipleFileTracker.updateProgress();
                System.gc();
            }
            multipleFileTracker.downloadComplete();
        });
        thread.start();
    }

    private ArrayList<Integer> getMissingFileIndexes() {
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
            File file = new File(directory + FileName.BINARY_2022.getIndexedName(i));
            if (!file.exists()) {
                indexList.add(i);
            } else {
                boolean valid = false;
                if (file.isDirectory()) valid = file.delete();
                if (valid) indexList.add(i);
            }
        }
        return indexList;
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

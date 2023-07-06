package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataDownloader2022;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IMultipleFileDownloadTracker;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.gui.frames.DatasetManagerFrame;

public class DownloaderProgressPanel2022 extends AbstractDownloadProgressPanel {

    private DataDownloader2022 downloader2022;

    public DownloaderProgressPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        progressBarLower.setVisible(true);
    }

    @Override
    protected void bindDownloader() {
        downloader2022 = (DataDownloader2022) downloader;
        setInfoUpper("Downloading 2022 dataset...");
        setInfoLower("Loading...");
        progressBar.setValue(0);
        progressBarLower.setValue(0);
        IFileDownloadTracker fileTracker = new IFileDownloadTracker() {
            @Override
            public void updateProgress() {
                updateDownloadProgress();
            }

            @Override
            public void onDownloadComplete() {
                // Do nothing, multipleFileTracker handles this
            }
        };
        IMultipleFileDownloadTracker multipleFileTracker = new IMultipleFileDownloadTracker() {
            @Override
            public void updateProgress() {
                updateFileProgress();
            }

            @Override
            public void onDownloadComplete() {
                datasetManagerFrame.swapToDownloader();
                DataValidator.runValidation2022();
            }
        };
        downloader.setFileTracker(fileTracker);
        downloader.setMultipleFileTracker(multipleFileTracker);
    }

    private void updateDownloadProgress() {
        setInfoUpper("Downloading file " + (downloader2022.getFilesDownloaded() + 1) + " / " + PlaceInfo.FILE_COUNT_2022 + "...");
        setInfoLower((downloader.getBytesProcessed() / 1000000) + " MB / " + downloader.getFileSizeInBytes() / 1000000 + " MB");
        progressBar.setValue(downloader.getProgress());
    }

    private void updateFileProgress() {
        double progress = downloader2022.getFilesDownloaded() / (double) PlaceInfo.FILE_COUNT_2022;
        progressBarLower.setValue((int) Math.ceil(progress * 100.0));
    }

}

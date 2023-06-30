package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataDownloader2022;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IMultipleFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2022;
import com.zrmiller.core.datawrangler.legacy.DataWrangler2022;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderProgressPanel2022 extends AbstractDownloadProgressPanel {

    private DataWrangler2022 dataWrangler2022;
    private DataDownloader2022 downloader2022;

    private DownloadState state = DownloadState.DOWNLOADING;

    public DownloaderProgressPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        progressBarLower.setVisible(true);
    }

    private enum DownloadState {DOWNLOADING, UNZIPPING, COMPRESSING}

    private Timer timer;

    @Override
    public void bindWrangler() {
        dataWrangler2022 = (DataWrangler2022) wrangler;
        IStatusTracker2022 tracker = new IStatusTracker2022() {
            @Override
            public void onFileDownloadComplete() {
                state = DownloadState.UNZIPPING;
            }

            @Override
            public void onUnZipComplete() {
                state = DownloadState.COMPRESSING;
            }

            @Override
            public void onCompressComplete() {
                if (dataWrangler2022.getFilesDownloaded() == dataWrangler2022.getExpectedFiles()) {
//                    datasetManagerFrame.validate2022();
                    datasetManagerFrame.swapToDownloader();
                    timer.stop();
                    timer = null;
                } else {
                    state = DownloadState.DOWNLOADING;
                }
            }
        };
        // FIXME: Timer Cleanup
        timer = new Timer(1000 / DOWNLOADER_PROGRESS_FPS, e -> proc());
        dataWrangler2022.addStatusTracker(tracker);
        timer.start();
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
//        updateFileProgress();
//        updateDownloadProgress();
    }

    private void proc() {
        switch (state) {
            case DOWNLOADING:
                OLD_updateDownloading();
                break;
            case UNZIPPING:
                updateUnzipping();
                break;
            case COMPRESSING:
                updateCompressing();
                break;
        }
    }

    private void updateDownloadProgress() {
        setInfoUpper("Downloading file " + (downloader2022.getFilesDownloaded() + 1) + " / " + downloader2022.getExpectedFiles() + "...");
        setInfoLower((downloader.getBytesProcessed() / 1000000) + " MB / " + downloader.getFileSizeInBytes() / 1000000 + " MB");
        progressBar.setValue(downloader.getProgress());
    }

    private void updateFileProgress() {
        double progress = downloader2022.getFilesDownloaded() / (double) downloader2022.getExpectedFiles();
//        int p = (int)Math.ceil(progress * 100);
        progressBarLower.setValue((int) Math.ceil(progress * 100.0));
    }

    private void OLD_updateDownloading() {
        setInfoUpper("Downloading file " + (dataWrangler2022.getFilesDownloaded() + 1) + " / " + dataWrangler2022.getExpectedFiles() + "...");
        setInfoLower((dataWrangler2022.getBytesProcessed() / 1000000) + " MB / " + dataWrangler2022.getFileSizeInBytes() / 1000000 + " MB");
        progressBar.setValue(dataWrangler2022.getProgress());
    }

    private void updateUnzipping() {
        setInfoUpper("Unzipping file " + (dataWrangler2022.getFilesDownloaded() + 1) + " / " + dataWrangler2022.getExpectedFiles() + "...");
        setInfoLower((dataWrangler2022.getBytesProcessed() / 1000000) + " MB / " + dataWrangler2022.getFileSizeInBytes() / 1000000 + " MB");
        progressBar.setValue(dataWrangler2022.getProgress());
    }

    private void updateCompressing() {
        setInfoUpper("Compressing file " + (dataWrangler2022.getFilesDownloaded() + 1) + " / " + dataWrangler2022.getExpectedFiles() + "...");
        setInfoLower((dataWrangler2022.getBytesProcessed() / 1000000) + " MB / " + dataWrangler2022.getFileSizeInBytes() / 1000000 + " MB");
        progressBar.setValue(dataWrangler2022.getProgress());
    }

}

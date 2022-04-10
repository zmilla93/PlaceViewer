package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataWrangler2022;
import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2022;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderProgressPanel2022 extends AbstractDownloadProgressPanel {

    private DataWrangler2022 dataWrangler2022;

    private DownloadState state = DownloadState.DOWNLOADING;

    public DownloaderProgressPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
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
                    datasetManagerFrame.validate2022();
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

    private void proc() {
        switch (state) {
            case DOWNLOADING:
                updateDownloading();
                break;
            case UNZIPPING:
                updateUnzipping();
                break;
            case COMPRESSING:
                updateCompressing();
                break;
        }
    }

    private void updateDownloading() {
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

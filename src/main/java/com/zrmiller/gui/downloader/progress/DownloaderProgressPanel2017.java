package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderProgressPanel2017 extends AbstractDownloadProgressPanel {

    public DownloaderProgressPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
    }

    @Override
    protected void bindDownloader() {
        setInfoUpper("Downloading 2017 dataset...");
        setInfoLower("Loading...");
        progressBar.setValue(0);
        IFileDownloadTracker fileTracker = new IFileDownloadTracker() {
            @Override
            public void updateProgress() {
                SwingUtilities.invokeLater(() -> updateDownloadProgress());
            }

            @Override
            public void onDownloadComplete() {
                DataValidator.runValidation2017();
                datasetManagerFrame.swapToDownloader();
            }
        };
        downloader.setFileTracker(fileTracker);
    }

    public void updateDownloadProgress() {
        progressBar.setValue(downloader.getProgress());
        if (downloader.getFileSizeInBytes() == 0)
            setInfoLower("Loading...");
        else
            setInfoLower(ZUtil.byteCountToString(downloader.getBytesProcessed()) + " / " + ZUtil.byteCountToString(downloader.getFileSizeInBytes()));
    }

}

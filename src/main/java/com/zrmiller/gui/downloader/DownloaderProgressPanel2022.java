package com.zrmiller.gui.downloader;

import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2022;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloaderProgressPanel2022 extends DownloadProgressPanel {

    private DownloadState state = DownloadState.DOWNLOADING;

    public DownloaderProgressPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
    }

    private enum DownloadState {DOWNLOADING, UNZIPPING, COMPRESSING}

    private Timer timer;

    @Override
    public void bindWrangler() {
        IStatusTracker2022 tracker = new IStatusTracker2022() {
            @Override
            public void onFileDownloadComplete() {

            }

            @Override
            public void onUnZipComplete() {

            }

            @Override
            public void onCompressComplete() {

            }
        };
        // FIXME: Timer Cleanup
        timer = new Timer(1000 / FPS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proc();
            }
        });
        timer.start();
    }

    private void proc() {
        switch (state) {
            case DOWNLOADING:
                displayDownloading();
                break;
            case UNZIPPING:
                displayUnZipping();
                break;
            case COMPRESSING:
                displayCompressing();
                break;
        }
    }

    private void displayDownloading() {
        setInfoLower("Downloading file " + wrangler);
    }

    private void displayUnZipping() {

    }

    private void displayCompressing() {

    }

}

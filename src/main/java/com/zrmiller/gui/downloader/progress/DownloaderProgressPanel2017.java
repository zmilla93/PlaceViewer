package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.datawrangler.callbacks.IDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IFileDownloadTracker;
import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.core.datawrangler.legacy.DataWrangler2017;
import com.zrmiller.core.utility.TileEdit;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloaderProgressPanel2017 extends AbstractDownloadProgressPanel {

    private DownloadState state;

    enum DownloadState {DOWNLOADING, COMPRESSING, SORTING, SAVING, COMPLETE, CANCELED}

    public DownloaderProgressPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        addListeners();
    }

    // TODO : Remove
    @Override
    public void bindWrangler() {
        state = DownloadState.DOWNLOADING;
        setInfoUpper("Downloading 2017 dataset...");
        setInfoLower("Loading...");
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
                setInfoUpper("Compressing dataset...");
                state = DownloadState.COMPRESSING;
            }

            @Override
            public void onFileReadComplete() {
                setInfoUpper("");
                setInfoLower("Sorting dataset... 0%");
                state = DownloadState.SORTING;
            }

            @Override
            public void onFileSortComplete() {
                setInfoUpper("Saving dataset...");
                setInfoLower("This should be quick...");
                state = DownloadState.SAVING;
            }

            @Override
            public void onCompressComplete() {
                datasetManagerFrame.validate2017();
                datasetManagerFrame.swapToDownloader();
                timer.stop();
            }

            @Override
            public void onCancel() {
                datasetManagerFrame.swapToDownloader();
            }
        };
        // FIXME: Timer Cleanup
        timer = new Timer(1000 / DOWNLOADER_PROGRESS_FPS, e -> proc(state));
        DataWrangler2017 wrangler2017 = (DataWrangler2017) (wrangler);
        wrangler2017.addStatusTracker(tracker);
        timer.start();
    }

    @Override
    protected void bindDownloader() {
        state = DownloadState.DOWNLOADING;
        setInfoUpper("Downloading 2017 dataset...");
        setInfoLower("Loading...");
        IDownloadTracker tracker = new IDownloadTracker() {
            @Override
            public void onDownloadComplete() {
                state = DownloadState.COMPLETE;
                datasetManagerFrame.validate2017();
                datasetManagerFrame.swapToDownloader();
            }

            @Override
            public void onCancel() {
                state = DownloadState.CANCELED;
                datasetManagerFrame.swapToDownloader();
            }
        };
        IFileDownloadTracker fileTracker = new IFileDownloadTracker() {
            @Override
            public void updateProgress() {
                SwingUtilities.invokeLater(() -> updateDownloadProgress());
            }
        };
        downloader.setTracker(tracker);
        downloader.setFileTracker(fileTracker);
//        startTimer();
    }

    private void addListeners() {
        // FIXME:
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadManager.cancel();
            }
        });
//        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDownloader());
    }

    private void proc(DownloadState downloadStage2017) {
        switch (downloadStage2017) {
            case DOWNLOADING:
                OLD_updateDownloading();
                break;
            case COMPRESSING:
                updateCompressing();
                break;
            case SORTING:
                updateSorting();
                break;
            case SAVING:
                updateSaving();
                break;
        }
    }

    public void updateDownloadProgress() {
        progressBar.setValue(downloader.getProgress());
        if (downloader.getFileSizeInBytes() == 0)
            setInfoLower("Loading...");
        else
            setInfoLower(ZUtil.byteCountToString(downloader.getBytesProcessed()) + " / " + ZUtil.byteCountToString(downloader.getFileSizeInBytes()));
    }

    public void OLD_updateDownloading() {
        progressBar.setValue(wrangler.getProgress());
        if (wrangler.getFileSizeInBytes() == 0)
            setInfoLower("Loading...");
        else
            setInfoLower(ZUtil.byteCountToString(wrangler.getBytesProcessed()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
    }

    public void updateCompressing() {
        progressBar.setValue(wrangler.getProgress());
        if (wrangler.getFileSizeInBytes() == 0)
            setInfoLower("Loading...");
        else
            setInfoLower(ZUtil.byteCountToString(wrangler.getBytesProcessed()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
    }

    public void updateSorting() {
        progressBar.setValue(TileEdit.getSortProgress());
        if (TileEdit.sortCount == 0)
            setInfoLower("Sorting dataset... 0%");
        else
            setInfoLower("Sorting dataset... " + TileEdit.getSortProgress() + "%");
    }

    public void updateSaving() {
        progressBar.setValue(wrangler.getProgress());
        if (wrangler.getBytesProcessed() < 0)
            setInfoLower("This should be quick...");
        else
            setInfoLower(ZUtil.byteCountToString(wrangler.getBytesProcessed()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
    }

}

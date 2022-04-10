package com.zrmiller.gui.downloader.progress;

import com.zrmiller.core.utility.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;

public class DownloaderProgressPanel2017 extends AbstractDownloadProgressPanel {

    private DownloadState state;

    enum DownloadState {DOWNLOADING, COMPRESSING, SORTING, SAVING,}

    public DownloaderProgressPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        addListeners();
    }

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
        };
        // FIXME: Timer Cleanup
        timer = new Timer(1000 / DOWNLOADER_PROGRESS_FPS, e -> proc(state));
        DataWrangler2017 wrangler2017 = (DataWrangler2017) (wrangler);
        wrangler2017.addStatusTracker(tracker);
        timer.start();
    }

    private void addListeners() {
        // FIXME:
//        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDownloader());
    }

    private void proc(DownloadState downloadStage2017) {
        switch (downloadStage2017) {
            case DOWNLOADING:
                updateDownloading();
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

    public void updateDownloading() {
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
        progressBar.setValue(TileEdit.getSortProgress());
        if (wrangler.getBytesProcessed() < 0)
            setInfoLower("This should be quick...");
        else
            setInfoLower(ZUtil.byteCountToString(wrangler.getBytesProcessed()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
    }

}

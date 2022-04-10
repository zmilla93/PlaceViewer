package com.zrmiller.gui.downloader;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadDisplay2017;
import com.zrmiller.core.datawrangler.callbacks.IStatusTracker2017;
import com.zrmiller.core.enums.DownloadStage2017;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloaderProgressPanel2017 extends DownloadProgressPanel {

    private DownloadStage2017 downloadStage2017;

    public DownloaderProgressPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        addListeners();
    }

    @Override
    public void bindWrangler() {
        downloadStage2017 = DownloadStage2017.DOWNLOADING;
        setInfoUpper("Downloading 2017 dataset...");
        setInfoLower("Loading...");
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
                setInfoUpper("Compressing dataset...");
                downloadStage2017 = DownloadStage2017.READING;
            }

            @Override
            public void onFileReadComplete() {
                setInfoUpper("");
                setInfoLower("Sorting dataset... 0%");
                downloadStage2017 = DownloadStage2017.SORTING;
            }

            @Override
            public void onFileSortComplete() {
                setInfoUpper("Saving dataset...");
                setInfoLower("This should be quick...");
                downloadStage2017 = DownloadStage2017.COMPRESSING;
            }

            @Override
            public void onCompressComplete() {
                datasetManagerFrame.validate2017();
                datasetManagerFrame.swapToDownloader();
                timer.stop();
            }
        };
        DownloadDisplay2017 downloadDisplay2017 = new DownloadDisplay2017() {
            @Override
            public void displayDownloading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(ZUtil.byteCountToString(wrangler.getBytesDownloaded()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
            }

            @Override
            public void displayReading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(ZUtil.byteCountToString(wrangler.getBytesDownloaded()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
            }

            @Override
            public void displaySorting() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (TileEdit.sortCount == 0)
                    setInfoLower("Sorting dataset... 0%");
                else
                    setInfoLower("Sorting dataset... " + TileEdit.getSortProgress() + "%");
            }

            @Override
            public void displayCompressing() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (wrangler.getBytesWritten() < 1000)
                    setInfoLower("This should be quick...");
                else
                    setInfoLower(wrangler.getBytesWritten() / 1000000 + " / " + PlaceInfo.CLEAN_LINE_COUNT * TileEdit.BYTE_COUNT);
            }
        };

        timer = new Timer(1000 / FPS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadDisplay2017.proc(downloadStage2017);
            }
        });
        System.out.println("WRANGLE:" + wrangler);
        DataWrangler2017 wrangler2017 = (DataWrangler2017) (wrangler);
        wrangler2017.addStatusTracker(tracker);
        timer.start();
    }

    private void addListeners() {
        // FIXME:
        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDownloader());
    }

    public void displayDownload2017() {
        downloadStage2017 = DownloadStage2017.DOWNLOADING;
        setInfoUpper("Downloading 2017 dataset...");
        setInfoLower("Loading...");
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloadComplete() {
                setInfoUpper("Compressing dataset...");
                downloadStage2017 = DownloadStage2017.READING;
            }

            @Override
            public void onFileReadComplete() {
                setInfoUpper("");
                setInfoLower("Sorting dataset... 0%");
                downloadStage2017 = DownloadStage2017.SORTING;
            }

            @Override
            public void onFileSortComplete() {
                setInfoUpper("Saving dataset...");
                setInfoLower("This should be quick...");
                downloadStage2017 = DownloadStage2017.COMPRESSING;
            }

            @Override
            public void onCompressComplete() {
                datasetManagerFrame.validate2017();
                datasetManagerFrame.swapToDownloader();
                timer.stop();
            }
        };
        DownloadDisplay2017 downloadDisplay2017 = new DownloadDisplay2017() {
            @Override
            public void displayDownloading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(ZUtil.byteCountToString(wrangler.getBytesDownloaded()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
            }

            @Override
            public void displayReading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(ZUtil.byteCountToString(wrangler.getBytesDownloaded()) + " / " + ZUtil.byteCountToString(wrangler.getFileSizeInBytes()));
            }

            @Override
            public void displaySorting() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (TileEdit.sortCount == 0)
                    setInfoLower("Sorting dataset... 0%");
                else
                    setInfoLower("Sorting dataset... " + TileEdit.getSortProgress() + "%");
            }

            @Override
            public void displayCompressing() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (wrangler.getBytesWritten() < 1000)
                    setInfoLower("This should be quick...");
                else
                    setInfoLower(wrangler.getBytesWritten() / 1000000 + " / " + PlaceInfo.CLEAN_LINE_COUNT * TileEdit.BYTE_COUNT);
            }
        };
        int fps = 10;
        timer = new Timer(1000 / fps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadDisplay2017.proc(downloadStage2017);
            }
        });
        DataWrangler2017 wrangler2017 = (DataWrangler2017) (wrangler);
        wrangler2017.addStatusTracker(tracker);
        timer.start();
    }

}

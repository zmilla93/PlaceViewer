package com.zrmiller.gui.downloader;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.datawrangler.DataWrangler;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadDisplay2017;
import com.zrmiller.core.datawrangler.IStatusTracker2017;
import com.zrmiller.core.enums.DownloadStage2017;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadProgressPanel extends BaseDownloaderPanel {

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel labelUpper = new JLabel();
    private final JLabel labelLower = new JLabel();
    private final JButton cancelButton = new JButton("Cancel");
    private final DatasetManagerFrame datasetManagerFrame;
    private Timer timer;

    private DownloadStage2017 downloadStage2017;

    private DataWrangler wrangler;
//    private DataWrangler2017 wrangler;


    public DownloadProgressPanel(DatasetManagerFrame datasetManagerFrame) {
        this.datasetManagerFrame = datasetManagerFrame;
        centerPanel.setLayout(new GridBagLayout());
        progressBar.setMaximum(0);
        progressBar.setMaximum(100);

        GridBagConstraints gc = ZUtil.getGC();
        centerPanel.add(labelUpper, gc);
        gc.gridy++;
        centerPanel.add(labelLower, gc);
        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        int inset = 100;
        gc.insets = new Insets(0, inset, 0, inset);
        centerPanel.add(progressBar, gc);
        gc.gridy++;
        addWestButton(cancelButton);
        addListeners();

//        timer = new Timer(1000 / 30, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                progressBar.setValue(wrangler.getProgress());
//                if (wrangler.getFileSizeInBytes() == 0)
//                    setInfoLower("Connecting...");
//                else
//                    setInfoLower(wrangler.getBytesDownloaded() / 1000000 + " MB / " + wrangler.getFileSizeInBytes() / 1000000 + " MB");
//            }
//        });
    }

    public void setInfoUpper(String text) {
        labelUpper.setText(text);
    }

    public void setInfoLower(String text) {
        labelLower.setText(text);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDatasetPanel());
    }

    public void setWrangler(DataWrangler wrangler) {
        this.wrangler = wrangler;
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void displayDownload2017() {
        downloadStage2017 = DownloadStage2017.DOWNLOADING;
        setInfoUpper("Downloading 2017 dataset...");
        IStatusTracker2017 tracker = new IStatusTracker2017() {
            @Override
            public void onFileDownloaded() {
                setInfoUpper("Scanning dataset...");
                downloadStage2017 = DownloadStage2017.READING;
            }

            @Override
            public void onFileRead() {
                setInfoUpper("Sorting dataset...");
                downloadStage2017 = DownloadStage2017.SORTING;
            }

            @Override
            public void onFileSorted() {
                setInfoUpper("Compressing dataset...");
                downloadStage2017 = DownloadStage2017.MINIFYING;
            }

            @Override
            public void onMinifiyComplete() {
                setInfoUpper("");
                setInfoUpper("Complete!");
                datasetManagerFrame.swapToDatasetPanel();
            }
        };
        DownloadDisplay2017 downloadDisplay2017 = new DownloadDisplay2017() {
            @Override
            public void displayDownloading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(wrangler.getBytesDownloaded() / 1000000 + " MB / " + wrangler.getFileSizeInBytes() / 1000000 + " MB");
            }

            @Override
            public void displayReading() {
                progressBar.setValue(wrangler.getProgress());
                if (wrangler.getFileSizeInBytes() == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(wrangler.getBytesDownloaded() / 1000000 + " MB / " + wrangler.getFileSizeInBytes() / 1000000 + " MB");
            }

            @Override
            public void displaySorting() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (TileEdit.sortCount == 0)
                    setInfoLower("Loading...");
                else
                    setInfoLower(TileEdit.getSortProgress() + "%");
            }

            @Override
            public void displayMinifying() {
                progressBar.setValue(TileEdit.getSortProgress());
                if (wrangler.getBytesWritten() == 0)
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

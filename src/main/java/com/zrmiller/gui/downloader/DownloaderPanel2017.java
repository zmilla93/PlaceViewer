package com.zrmiller.gui.downloader;

import com.zrmiller.core.DatasetManager;
import com.zrmiller.core.datawrangler.DataWrangler2017;
import com.zrmiller.core.datawrangler.DownloadManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class DownloaderPanel2017 extends DownloaderDatasetPanel {

    private final DatasetManagerFrame datasetManagerFrame;

    public DownloaderPanel2017(DatasetManagerFrame datasetManagerFrame) {
        super();
        this.datasetManagerFrame = datasetManagerFrame;
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        infoPanel.add(new JLabel("Download Size: 1 GB"), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Compressed Size: 162 MB"), gc);
        gc.gridy++;
        infoPanel.add(new JLabel("Data will be downloaded into a single file, sorted, compressed, then the original file deleted."), gc);
        gc.gridy++;

        JPanel infoBufferPanel = new JPanel(new GridBagLayout());
        gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        int inset = 4;
        gc.insets = new Insets(inset, inset, 0, 0);
        infoBufferPanel.add(infoPanel, gc);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(infoBufferPanel, BorderLayout.NORTH);
        deleteButton.setText("Delete 2017");
        downloadButton.setText("Download 2017");
        addListeners();
    }

    private void addListeners() {
        downloadButton.addActionListener(e -> {
            DataWrangler2017 dataWrangler2017 = DownloadManager.downloadAndMinify2017();
            datasetManagerFrame.getProgressPanel().setWrangler(dataWrangler2017);
//            datasetManagerFrame.getProgressPanel().startTimer();
//            datasetManagerFrame.getProgressPanel().setInfoUpper("Downloading 2017 Dataset...");
            datasetManagerFrame.getProgressPanel().displayDownload2017();
            datasetManagerFrame.swapToDownloadPanel();
//            DownloadManager.downloadAndMinify2017();
        });
    }

}

package com.zrmiller.gui.downloader;

import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class DownloadProgressPanel extends BaseDownloaderPanel {

    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel progressLabel1 = new JLabel("Downloading...");
    private final JLabel progressLabel2 = new JLabel("File 1/3");
    private final JButton cancelButton = new JButton("Cancel");
    private final DatasetManagerFrame datasetManagerFrame;

    private static int i = 0;

    public DownloadProgressPanel(DatasetManagerFrame datasetManagerFrame) {
        this.datasetManagerFrame = datasetManagerFrame;
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        centerPanel.add(progressLabel1, gc);
        gc.gridy++;
        centerPanel.add(progressLabel2, gc);
        gc.gridy++;
        centerPanel.add(progressBar, gc);
        gc.gridy++;
        addWestButton(cancelButton);
        addListeners();

        progressLabel1.setText("Downloading file #" + i);
        progressBar.setMaximum(0);
        progressBar.setMaximum(10000);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> datasetManagerFrame.swapToDatasetPanel());
    }

}

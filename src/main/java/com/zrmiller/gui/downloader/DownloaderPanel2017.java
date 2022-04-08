package com.zrmiller.gui.downloader;

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
        infoPanel.add(new JLabel("Data will be downloaded into a single file, compressed, then the original file deleted."), gc);
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
        addListeners();
    }

    private void addListeners() {
        downloadButton.addActionListener(e -> datasetManagerFrame.swapToDownloadPanel());
    }

}

package com.zrmiller.gui.downloader;

import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class DownloaderPanel2022 extends DownloaderDatasetPanel {

    public DownloaderPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        cardPanel.add(infoPanel(), "P1");
    }

    private JPanel infoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        add(new JLabel(), gc);
        gc.gridy++;
        return panel;
    }

}

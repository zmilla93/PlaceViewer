package com.zrmiller.gui.downloader;

import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public abstract class DownloaderDatasetPanel extends BaseDownloaderPanel {

    protected final DatasetManagerFrame datasetManagerFrame;
    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel cardPanel = new JPanel(cardLayout);

    public DownloaderDatasetPanel(DatasetManagerFrame datasetManagerFrame) {
        this.datasetManagerFrame = datasetManagerFrame;
    }

}

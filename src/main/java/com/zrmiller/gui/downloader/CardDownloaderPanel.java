package com.zrmiller.gui.downloader;

import com.zrmiller.gui.frames.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class CardDownloaderPanel extends BaseDownloaderPanel {

    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel cardPanel = new JPanel(cardLayout);

    /**
     * Add content to cardPanel.
     * Add buttons with addEastButton and addWestButton.
     */
    public CardDownloaderPanel(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(cardPanel, BorderLayout.CENTER);
    }

}

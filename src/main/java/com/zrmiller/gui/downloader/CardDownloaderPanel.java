package com.zrmiller.gui.downloader;

import com.zrmiller.core.colors.CustomColors;
import com.zrmiller.gui.frames.DatasetManagerFrame;
import com.zrmiller.modules.colortheme.ColorManager;

import javax.swing.*;
import java.awt.*;

public class CardDownloaderPanel extends BaseDownloaderPanel {

    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel cardPanel = new JPanel(cardLayout);
    protected final JLabel uninstalledLabel = new JLabel("Dataset Not Installed");
    protected final JLabel partiallyInstalledLabel = new JLabel("Dataset Partially Installed");
    protected final JLabel installedLabel = new JLabel("Dataset Installed");

    /**
     * Add content to cardPanel.
     * Add buttons with addEastButton and addWestButton.
     */
    public CardDownloaderPanel(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(cardPanel, BorderLayout.CENTER);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (uninstalledLabel == null) return;
        if (ColorManager.isDarkTheme()) {
            uninstalledLabel.setForeground(CustomColors.RED_LIGHT);
            partiallyInstalledLabel.setForeground(CustomColors.YELLOW_LIGHT);
            installedLabel.setForeground(CustomColors.GREEN_LIGHT);
        } else {
            uninstalledLabel.setForeground(CustomColors.RED_DARK);
            partiallyInstalledLabel.setForeground(CustomColors.YELLOW_DARK);
            installedLabel.setForeground(CustomColors.GREEN_DARK);
        }
    }

}

package com.zrmiller.gui.downloader;

import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDownloaderPanel extends JPanel {

    protected final JPanel centerPanel = new JPanel();
    protected final JPanel bottomBar = new JPanel(new BorderLayout());
    private final JPanel buttonPanelEast = new JPanel(new GridBagLayout());
    private final JPanel buttonPanelWest = new JPanel(new GridBagLayout());
    private final GridBagConstraints eastGC = ZUtil.getGC();
    private final GridBagConstraints westGC = ZUtil.getGC();

    protected final DatasetManagerFrame datasetManagerFrame;
    private static final int BUTTON_INSET = 4;

    /**
     * Add content to centerPanel.
     * Add buttons with addEastButton and addWestButton.
     */
    public BaseDownloaderPanel(DatasetManagerFrame datasetManagerFrame) {
        this.datasetManagerFrame = datasetManagerFrame;
        setLayout(new BorderLayout());
        bottomBar.add(buttonPanelWest, BorderLayout.WEST);
        bottomBar.add(buttonPanelEast, BorderLayout.EAST);

        eastGC.insets = new Insets(BUTTON_INSET, 0, BUTTON_INSET, BUTTON_INSET);
        westGC.insets = new Insets(BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, 0);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    protected void addEastButton(JComponent component) {
        buttonPanelEast.add(component, eastGC);
        eastGC.gridx++;
    }

    protected void addWestButton(JComponent component) {
        buttonPanelWest.add(component, westGC);
        westGC.gridx++;
    }

}

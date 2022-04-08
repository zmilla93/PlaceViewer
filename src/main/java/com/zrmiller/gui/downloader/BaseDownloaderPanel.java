package com.zrmiller.gui.downloader;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDownloaderPanel extends JPanel {

    protected final JPanel centerPanel = new JPanel();
    protected final JPanel bottomBar = new JPanel(new BorderLayout());
    private final JPanel buttonPanelEast = new JPanel(new GridBagLayout());
    private final JPanel buttonPanelWest = new JPanel(new GridBagLayout());
    private final GridBagConstraints eastGC = ZUtil.getGC();
    private final GridBagConstraints westGC = ZUtil.getGC();

    /**
     * Add content to centerPanel.
     * Add buttons with addEastButton and addWestButton.
     */
    public BaseDownloaderPanel() {
        setLayout(new BorderLayout());
        bottomBar.add(buttonPanelWest, BorderLayout.WEST);
        bottomBar.add(buttonPanelEast, BorderLayout.EAST);
        int inset = 2;
        eastGC.insets = new Insets(inset, inset, inset, 0);
        westGC.insets = new Insets(inset, 0, inset, inset);
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

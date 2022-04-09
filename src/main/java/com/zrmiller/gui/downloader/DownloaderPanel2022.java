package com.zrmiller.gui.downloader;

import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.windows.DatasetManagerFrame;

import javax.swing.*;
import java.awt.*;

public class DownloaderPanel2022 extends DownloaderDatasetPanel {

    public enum Panel {NOT_INSTALLED, PARTIALLY_INSTALLED, FULLY_INSTALLED}

    public DownloaderPanel2022(DatasetManagerFrame datasetManagerFrame) {
        super(datasetManagerFrame);
        cardPanel.add(uninstalledPanel(), Panel.NOT_INSTALLED.toString());
        cardPanel.add(partiallyInstalledPanel(), Panel.PARTIALLY_INSTALLED.toString());
        cardPanel.add(fullyInstalledPanel(), Panel.FULLY_INSTALLED.toString());
        showPanel(Panel.FULLY_INSTALLED);
        centerPanel.add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel uninstalledPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        panel.add(new JLabel(), gc);
        gc.gridy++;
        panel.add(new JLabel("none"));
        return panel;
    }

    private JPanel partiallyInstalledPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        panel.add(new JLabel("partial"), gc);
        return panel;
    }

    private JPanel fullyInstalledPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        panel.add(new JLabel("full"), gc);
        return panel;
    }

    public void showPanel(Panel panel) {
        cardLayout.show(cardPanel, panel.toString());
    }

}

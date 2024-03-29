package com.zrmiller.gui.mainframe;

import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {

    private JButton openDatasetsButton = new JButton("Open Dataset Manager");
    private final int BUTTON_GAP = 10;

    public IntroPanel() {
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        infoPanel.add(new JLabel("Welcome to PlaceViewer! Download a dataset to get started, or select an installed one from the top menu."), gc);
        gc.gridy++;
        gc.insets.top = BUTTON_GAP;
        infoPanel.add(openDatasetsButton, gc);
        gc.gridy++;
        add(infoPanel, BorderLayout.CENTER);
        addListeners();
    }

    private void addListeners() {
        openDatasetsButton.addActionListener(e -> FrameManager.dataDownloaderFrame.centerAndShow());
    }

}

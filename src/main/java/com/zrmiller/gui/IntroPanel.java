package com.zrmiller.gui;

import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroPanel extends JPanel {

    private JButton openDatasetsButton = new JButton("Open Dataset Manager");
    private final int BUTTON_GAP = 10;

    public IntroPanel() {
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        infoPanel.add(new JLabel("Welcome to PlaceViewer! Download a dataset to get started."), gc);
        gc.gridy++;
        gc.insets.top = BUTTON_GAP;
        infoPanel.add(openDatasetsButton, gc);
        gc.gridy++;
        add(infoPanel, BorderLayout.CENTER);
        addListeners();
    }

    private void addListeners() {
        openDatasetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameManager.dataDownloader.setVisible(true);
            }
        });
    }

}

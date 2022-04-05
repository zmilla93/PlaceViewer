package com.zrmiller.gui;

import javax.swing.*;
import java.awt.*;

public class WindowContainer extends JPanel {

    private CanvasPanel canvasPanel = new CanvasPanel();
    private JPanel sidebarPanel = new JPanel(new GridBagLayout());
    private JPanel southPanel = new JPanel(new GridBagLayout());
    private JLabel frameLabel = new JLabel("Unloaded");

    public WindowContainer(){
        setLayout(new BorderLayout());
        // Panels
        buildPanels();
        addPanels();
    }

    private void buildPanels(){
        sidebarPanel.add(new JButton("ASJFKLSDJAF"));
//        southPanel.setBorder();
    }

    private void addPanels(){
        add(sidebarPanel, BorderLayout.WEST);
        add(canvasPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

}

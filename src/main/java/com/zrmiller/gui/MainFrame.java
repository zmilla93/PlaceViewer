package com.zrmiller.gui;

import com.zrmiller.CanvasPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Container container = getContentPane();

    public MainFrame() {
        super("PlaceViewer");
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());
        CanvasPanel canvasPanel = new CanvasPanel();
//        container.add(new JButton(), BorderLayout.NORTH);
        container.add(canvasPanel, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}

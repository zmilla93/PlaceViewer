package com.zrmiller.gui;

import com.zrmiller.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    private final Container container = getContentPane();

    public MainFrame() {
        super("PlaceViewer");
        setIconImage(App.APP_ICON.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());

        WindowContainer windowContainer = new WindowContainer();
        container.add(windowContainer, BorderLayout.CENTER);

        container.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO
            }
        });

        setVisible(true);
    }

}

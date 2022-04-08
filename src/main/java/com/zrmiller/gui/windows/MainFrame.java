package com.zrmiller.gui.windows;

import com.zrmiller.App;
import com.zrmiller.gui.MainMenuBar;
import com.zrmiller.gui.WindowContainer;

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
        setMinimumSize(new Dimension(600, 400));
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());

        MainMenuBar mainMenuBar = new MainMenuBar();
        WindowContainer windowContainer = new WindowContainer();
        container.add(mainMenuBar, BorderLayout.NORTH);
        container.add(windowContainer, BorderLayout.CENTER);

        container.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO
            }
        });

    }

}

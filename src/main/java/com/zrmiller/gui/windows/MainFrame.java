package com.zrmiller.gui.windows;

import com.zrmiller.App;
import com.zrmiller.gui.MainAppPanel;
import com.zrmiller.gui.MainMenuBar;

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
        MainAppPanel mainAppPanel = new MainAppPanel();
        container.add(mainMenuBar, BorderLayout.NORTH);
        container.add(mainAppPanel, BorderLayout.CENTER);

        container.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO
            }
        });
        setLocationRelativeTo(null);
    }

}

package com.zrmiller.gui.windows;

import com.zrmiller.App;
import com.zrmiller.gui.MainMenuBar;
import com.zrmiller.gui.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    private final Container container = getContentPane();
    private final MainMenuBar mainMenuBar = new MainMenuBar();
    private final MainPanel mainPanel = new MainPanel();

    public MainFrame() {
        super("PlaceViewer");
        setIconImage(App.APP_ICON.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setSize(1200, 1000);
        container.setLayout(new BorderLayout());


        container.add(mainMenuBar, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);

        container.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO
            }
        });
        setLocationRelativeTo(null);
    }

    public void showCard(MainPanel.Card card) {
        mainPanel.showCard(card);
    }

}

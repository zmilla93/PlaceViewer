package com.zrmiller.gui;

import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar {

    // Menus
    private final JMenu optionsMenu = new JMenu("Options");
    private final JMenu displayMenu = new JMenu("Display Mode");

    // Options
    private final JMenu themeMenu = new JMenu("Color Theme");
    private final JMenuItem downloadDataButton = new JMenuItem("Download Data");
    private final JMenuItem quitButton = new JMenuItem("Quit");

    // Display


    private final ColorTheme[] themeList = new ColorTheme[]{ColorTheme.SOLARIZED_LIGHT, ColorTheme.CARBON};

    public MainMenuBar() {
        // Create theme list
        for (ColorTheme theme : themeList) {
            JMenuItem item = new JMenuItem(theme.toString());
            item.addActionListener(e -> {
                ColorManager.setTheme(theme);
            });
            themeMenu.add(item);
        }

        // Options
        optionsMenu.add(themeMenu);
        optionsMenu.add(downloadDataButton);
        optionsMenu.add(new JSeparator());
        optionsMenu.add(quitButton);

        // Build Menubar
        add(optionsMenu);
        add(displayMenu);

        addListeners();
    }

    private void addListeners(){
        // Note: Color theme listeners are added during construction
        quitButton.addActionListener(e -> System.exit(0));
    }

}

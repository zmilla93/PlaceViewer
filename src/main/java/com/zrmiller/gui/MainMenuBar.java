package com.zrmiller.gui;

import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar {

    private JMenu optionsMenu = new JMenu("Options");
    private JMenu themeMenu = new JMenu("Color Theme");

    //
    private ColorTheme[] themeList = new ColorTheme[]{ColorTheme.SOLARIZED_LIGHT, ColorTheme.CARBON};

    public MainMenuBar() {

        // Create theme list
        for (ColorTheme theme : themeList) {
            JMenuItem item = new JMenuItem(theme.toString());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ColorManager.setTheme(theme);
                    // FIXME : TODO : Repaint canvas
                }
            });
            themeMenu.add(item);
        }

        // Options
        optionsMenu.add(themeMenu);

        // Build Menubar
        add(optionsMenu);
    }

}

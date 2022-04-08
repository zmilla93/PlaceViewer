package com.zrmiller.gui;

import com.zrmiller.core.DatasetManager;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar {

    // Menus
    private final JMenu optionsMenu = new JMenu("Options");
    private final JMenu displayMenu = new JMenu("Display Mode");
    private final JMenu datasetMenu = new JMenu("Dataset");

    // Options
    private final JMenu themeMenu = new JMenu("Color Theme");

    private final JMenuItem quitButton = new JMenuItem("Quit");

    // Display

    // Dataset
    private final JMenuItem dataset2017Button = new JMenuItem("Place 2017");
    private final JMenuItem dataset2022Button = new JMenuItem("Place 2022");
    private final JMenuItem downloadDataButton = new JMenuItem("Download Datasets...");

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
        optionsMenu.add(new JSeparator());
        optionsMenu.add(quitButton);

        // Datasets
        datasetMenu.add(dataset2017Button);
        datasetMenu.add(dataset2022Button);
        datasetMenu.add(new JSeparator());
        datasetMenu.add(downloadDataButton);

        // Build Menubar
        add(optionsMenu);
        add(datasetMenu);
        add(displayMenu);

        addListeners();
    }

    private void addListeners(){
        // Note: Color theme listeners are added during construction
        // Datasets
        dataset2017Button.addActionListener(e -> DatasetManager.changeDataset(Dataset.PLACE_2017));
        dataset2022Button.addActionListener(e -> DatasetManager.changeDataset(Dataset.PLACE_2022));

        quitButton.addActionListener(e -> System.exit(0));
    }

}

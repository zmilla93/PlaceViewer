package com.zrmiller.gui;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    // Menus
    private final JMenu optionsMenu = new JMenu("Options");
    private final JMenu displayMenu = new JMenu("Display Mode");
    private final JMenu datasetMenu = new JMenu("Dataset");

    // Options
    private final JMenu themeMenu = new JMenu("Color Theme");
    private final JMenuItem githubButton = new JMenuItem("Github");
    private final JMenuItem paypalButton = new JMenuItem("Paypal");

    private final JMenuItem quitButton = new JMenuItem("Quit");

    // Display

    // Dataset
    private final JMenuItem dataset2017Button = new JMenuItem("Place 2017");
    private final JMenuItem dataset2022Button = new JMenuItem("Place 2022");
    private final JMenuItem datasetManagerButton = new JMenuItem("Manage Datasets...");

    private final ColorTheme[] themeList = new ColorTheme[]{ColorTheme.SOLARIZED_LIGHT, ColorTheme.CARBON};

    private static final int iconSize = 20;

    public MainMenuBar() {
        // Create theme list
        for (ColorTheme theme : ColorTheme.values()) {
            JMenuItem item = new JMenuItem(theme.toString());
            item.addActionListener(e -> {
                ColorManager.setTheme(theme);
                SaveManager.settings.data.colorTheme = theme;
                SaveManager.settings.saveToDisk();
            });
            themeMenu.add(item);
        }

        // Options
        optionsMenu.add(themeMenu);
        optionsMenu.add(new JSeparator());
        optionsMenu.add(githubButton);
        optionsMenu.add(paypalButton);
        optionsMenu.add(new JSeparator());
        optionsMenu.add(quitButton);

        // Datasets
        datasetMenu.add(dataset2017Button);
        datasetMenu.add(dataset2022Button);
        datasetMenu.add(new JSeparator());
        datasetMenu.add(datasetManagerButton);

        // Build Menubar
        add(optionsMenu);
        add(datasetMenu);
        add(displayMenu);

        addListeners();
    }

    private void addListeners() {
        // Note: Color theme listeners are added during construction
        // Datasets
        dataset2017Button.addActionListener(e -> App.datasetManager.changeDataset(Dataset.PLACE_2017));
        dataset2022Button.addActionListener(e -> App.datasetManager.changeDataset(Dataset.PLACE_2022));
        datasetManagerButton.addActionListener(e -> FrameManager.dataDownloader.setVisible(true));
        githubButton.addActionListener(e -> ZUtil.openLink("https://github.com/zmilla93/PlaceViewer"));
        paypalButton.addActionListener(e -> ZUtil.openLink("https://www.paypal.com/paypalme/zmilla93"));
        quitButton.addActionListener(e -> System.exit(0));
    }

}

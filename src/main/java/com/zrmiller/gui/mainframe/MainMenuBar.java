package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.colors.ColorMode;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.datawrangler.callbacks.IValidationListener2017;
import com.zrmiller.core.datawrangler.callbacks.IValidationListener2022;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.managers.listeners.IDataDirectoryListener;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.gui.FrameManager;
import com.zrmiller.modules.colortheme.ColorManager;
import com.zrmiller.modules.colortheme.ColorTheme;
import com.zrmiller.modules.strings.References;

import javax.swing.*;
import java.io.File;

public class MainMenuBar extends JMenuBar implements IDatasetListener, IValidationListener2017, IValidationListener2022, IDataDirectoryListener {

    // Menus
    private final JMenu optionsMenu = new JMenu("Options");
    private final JMenu displayMenu = new JMenu("Display Mode");
    private final JMenu datasetMenu = new JMenu("Dataset");
    private final JMenu exportMenu = new JMenu("Export");

    // Options
    private final JMenu themeMenu = new JMenu("Color Theme");
    private final JMenuItem githubButton = new JMenuItem("Github");
    private final JMenuItem quitButton = new JMenuItem("Quit");
    private final JMenu openFolderMenu = new JMenu("Open Folder");

    private final JMenuItem datasetFolderButton = new JMenuItem("Datasets");
    private final JMenuItem exportsFolderButton = new JMenuItem("Exports");

    // Display
    private final JMenuItem displayNormalButton = new JRadioButtonMenuItem("Normal");
    private final JMenuItem displayHeatmapGrayscaleButton = new JRadioButtonMenuItem("Heatmap (Grayscale)");
    private final JMenuItem displayHeatmapColorButton = new JRadioButtonMenuItem("Heatmap (Color)");

    // Datasets
    private final JMenuItem dataset2017Button = new JMenuItem("Place 2017");
    private final JMenuItem dataset2022Button = new JMenuItem("Place 2022");
    private final JMenuItem closeDatasetButton = new JMenuItem("Close Dataset");
    private final JMenuItem missingDatasetsLabel = new JMenuItem("No Data Installed");
    private final JMenuItem datasetManagerButton = new JMenuItem("Dataset Manager...");

    // Export Menu Bar
    private final JMenuItem exportImageButton = new JMenuItem("Export PNG...");
    //    private final JMenuItem exportGifButton = new JMenuItem("Export GIF...");
    private final JMenuItem openExportsButton = new JMenuItem("View Exports");

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
        missingDatasetsLabel.setEnabled(false);

        // Options
        optionsMenu.add(themeMenu);
//        optionsMenu.add(new JSeparator());
//        optionsMenu.add(exportMenu);
//        optionsMenu.add(new JSeparator());
        optionsMenu.add(openFolderMenu);
        optionsMenu.add(githubButton);
        optionsMenu.add(new JSeparator());
        optionsMenu.add(quitButton);

        // Datasets
        datasetMenu.add(dataset2017Button);
        datasetMenu.add(dataset2022Button);
        datasetMenu.add(closeDatasetButton);
//        datasetMenu.add(missingDatasetsLabel);
        datasetMenu.add(new JSeparator());
        datasetMenu.add(datasetManagerButton);

        // Open Folder Menu
        openFolderMenu.add(datasetFolderButton);
        openFolderMenu.add(exportsFolderButton);

        // Export
        exportMenu.add(exportImageButton);
//        exportMenu.add(exportGifButton);
        exportMenu.add(new JSeparator());
//        exportMenu.add(openExportsButton);

        // Display
        ButtonGroup displayGroup = new ButtonGroup();
        displayGroup.add(displayNormalButton);
        displayGroup.add(displayHeatmapGrayscaleButton);
        displayGroup.add(displayHeatmapColorButton);
        displayGroup.setSelected(displayNormalButton.getModel(), true);
        displayMenu.add(displayNormalButton);
        displayMenu.add(displayHeatmapGrayscaleButton);
        displayMenu.add(displayHeatmapColorButton);

        // Build Menu Bar
        add(optionsMenu);
        add(datasetMenu);
        add(displayMenu);
//        add(exportMenu);

        addListeners();
        DatasetManager.addDatasetListener(this);
        DataValidator.addValidationListener2017(this);
        DataValidator.addValidationListener2022(this);
        onDataDirectoryChange(SaveManager.settings.data.dataDirectory);
        FrameManager.dataDownloaderFrame.addDirectoryListener(this);
    }

    private void addListeners() {
        // Note: Color theme listeners are added during construction
        // Datasets
        dataset2017Button.addActionListener(e -> DatasetManager.setDataset(Dataset.PLACE_2017));
        dataset2022Button.addActionListener(e -> DatasetManager.setDataset(Dataset.PLACE_2022));
        closeDatasetButton.addActionListener(e -> DatasetManager.setDataset(null));
        datasetManagerButton.addActionListener(e -> FrameManager.dataDownloaderFrame.setVisible(true));
        githubButton.addActionListener(e -> ZUtil.openLink("https://github.com/zmilla93/PlaceViewer"));
        quitButton.addActionListener(e -> System.exit(0));
//        exportImageButton.addActionListener(e -> FrameManager.exportImageWindow.setVisible(true));
//        exportGifButton.addActionListener(e -> FrameManager.exportGifWindow.setVisible(true));
        datasetFolderButton.addActionListener(e -> ZUtil.openExplorer(References.getDataFolder()));
        exportsFolderButton.addActionListener(e -> ZUtil.openExplorer(References.getExportFolder()));

        // Display
        displayNormalButton.addActionListener(e -> DatasetManager.setColorMode(ColorMode.NORMAL));
        displayHeatmapGrayscaleButton.addActionListener(e -> DatasetManager.setColorMode(ColorMode.HEATMAP_GRAYSCALE));
        displayHeatmapColorButton.addActionListener(e -> DatasetManager.setColorMode(ColorMode.HEATMAP_COLOR));
    }

    @Override
    public void onDatasetChanged(Dataset datasets) {
        closeDatasetButton.setVisible(App.dataset() != null);
    }

    @Override
    public void onValidation2017(boolean valid, long fileSize) {
        dataset2017Button.setEnabled(valid);
    }

    @Override
    public void onValidation2022(boolean valid, int filecount, long fileSize) {
        dataset2022Button.setEnabled(valid);
    }

    @Override
    public void onDataDirectoryChange(String directory) {
        boolean valid = false;
        if (directory != null) {
            File file = new File(directory);
            valid = file.isDirectory();
        }
        openFolderMenu.setEnabled(valid);
    }
}

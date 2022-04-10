package com.zrmiller.gui;

import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.gui.windows.DatasetManagerFrame;
import com.zrmiller.gui.windows.MainFrame;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

public class FrameManager {

    public static MainFrame mainFrame;
    public static DatasetManagerFrame dataDownloader;

    /**
     * Handles all GUI Creation.
     */
    public static void init() {

        // Create GUI
        mainFrame = new MainFrame();
        dataDownloader = new DatasetManagerFrame();

        // Color Manager Setup
        ColorManager.addFrame(mainFrame);
        ColorManager.addFrame(dataDownloader);
        ColorTheme theme = SaveManager.settings.data.colorTheme == null ? ColorTheme.SOLARIZED_LIGHT : SaveManager.settings.data.colorTheme;
        ColorManager.setTheme(theme);

        // Visibility
        mainFrame.setVisible(true);
    }

}

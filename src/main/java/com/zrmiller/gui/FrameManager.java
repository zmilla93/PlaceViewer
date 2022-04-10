package com.zrmiller.gui;

import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.utility.PlaceInfo;
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

    public static void tryShowDataset(Dataset dataset) {
        if (dataset == null) {
            mainFrame.showCard(MainPanel.Card.INTRO);
            return;
        }
        switch (dataset) {
            case PLACE_2017:
                if (DataValidator.validate2017() > 0)
                    mainFrame.showCard(MainPanel.Card.CANVAS);
                else
                    mainFrame.showCard(MainPanel.Card.INTRO);
                break;
            case PLACE_2022:
                if (DataValidator.validateFileCount2022() == PlaceInfo.FILE_COUNT_2022)
                    mainFrame.showCard(MainPanel.Card.CANVAS);
                else
                    mainFrame.showCard(MainPanel.Card.INTRO);
                break;
        }
    }

}

package com.zrmiller.gui;

import com.zrmiller.core.data.Dataset;
import com.zrmiller.core.data.PlaceInfo;
import com.zrmiller.core.datawrangler.DataValidator;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.gui.frames.DatasetManagerFrame;
import com.zrmiller.gui.frames.MainFrame;
import com.zrmiller.gui.frames.WaitingFrame;
import com.zrmiller.gui.mainframe.CanvasPanel;
import com.zrmiller.gui.mainframe.MainPanel;
import com.zrmiller.modules.colortheme.ColorManager;
import com.zrmiller.modules.colortheme.ColorTheme;

public class FrameManager {

    // Windows
    public static MainFrame mainFrame;
    public static DatasetManagerFrame dataDownloaderFrame;
    public static WaitingFrame waitingFrame;

    // Important UI Elements
    public static CanvasPanel canvasPanel;

    /**
     * Handles all GUI Creation.
     */
    public static void init() {

        // Create GUI
        dataDownloaderFrame = new DatasetManagerFrame();
        mainFrame = new MainFrame();
        waitingFrame = new WaitingFrame();

        // Color Manager Setup
        ColorManager.addFrame(mainFrame);
        ColorManager.addFrame(dataDownloaderFrame);
        ColorManager.addFrame(waitingFrame);
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
                if (DataValidator.getFileSize2017() > 0)
                    mainFrame.showCard(MainPanel.Card.CANVAS);
                else
                    mainFrame.showCard(MainPanel.Card.INTRO);
                break;
            case PLACE_2022:
                if (DataValidator.getFileCount2022() == PlaceInfo.FILE_COUNT_2022)
                    mainFrame.showCard(MainPanel.Card.CANVAS);
                else
                    mainFrame.showCard(MainPanel.Card.INTRO);
                break;
        }
    }

}

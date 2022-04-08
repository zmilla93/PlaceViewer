package com.zrmiller.gui;

import com.zrmiller.gui.windows.DatasetManagerFrame;
import com.zrmiller.gui.windows.MainFrame;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

public class FrameManager {

    public static MainFrame mainFrame;
    public static DatasetManagerFrame dataDownloader;

    public static void init() {
        mainFrame = new MainFrame();
        dataDownloader = new DatasetManagerFrame();

        ColorManager.addFrame(mainFrame);
        ColorManager.addFrame(dataDownloader);

        ColorManager.setTheme(ColorTheme.SOLARIZED_LIGHT);

        mainFrame.setVisible(true);
        dataDownloader.setVisible(true);
    }

}

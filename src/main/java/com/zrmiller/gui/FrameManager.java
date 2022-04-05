package com.zrmiller.gui;

import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

public class FrameManager {

    public static MainFrame mainFrame;

    public static void init() {
        mainFrame = new MainFrame();

        ColorManager.addFrame(mainFrame);

        ColorManager.setTheme(ColorTheme.SOLARIZED_LIGHT);

        mainFrame.setVisible(true);
    }

}

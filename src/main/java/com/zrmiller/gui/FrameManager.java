package com.zrmiller.gui;

import com.zrmiller.gui.windows.DatasetManagerFrame;
import com.zrmiller.gui.windows.MainFrame;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.ColorTheme;

public class FrameManager {

    public static MainFrame mainFrame;
    public static DatasetManagerFrame dataDownloader;

    public static void init() {
        System.out.println("FrameInit");
        mainFrame = new MainFrame();
        dataDownloader = new DatasetManagerFrame();
        System.out.println("f1");
        ColorManager.addFrame(mainFrame);
        ColorManager.addFrame(dataDownloader);
        System.out.println("f2");
        ColorManager.setTheme(ColorTheme.SOLARIZED_LIGHT);
        System.out.println("f3");
        mainFrame.setVisible(true);
        dataDownloader.setVisible(true);
        System.out.println("fin");
    }

}

package com.zrmiller.gui;

public class FrameManager {

    public static MainFrame mainFrame;

    public static void init(){
        mainFrame = new MainFrame();

        mainFrame.setVisible(true);
    }

}

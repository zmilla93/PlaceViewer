package com.zrmiller;

import com.zrmiller.core.parser.IParserCallback;
import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class App implements IParserCallback {
    public static ImageIcon APP_ICON = new ImageIcon(Objects.requireNonNull(App.class.getResource("/place.png")));

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    FrameManager.init();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void statusUpdate(String report) {
        System.out.println(report);
    }

    @Override
    public void complete(String report) {
        System.out.println(report);
    }
}

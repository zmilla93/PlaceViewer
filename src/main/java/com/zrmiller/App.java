package com.zrmiller;

import com.zrmiller.gui.FrameManager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class App {

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
}

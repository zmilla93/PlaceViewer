package com.zrmiller.core.utility;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ZUtil {

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Returns a printable version of an enum name.
     *
     * @param input
     * @return
     */
//    public static String enumToString(String input) {
//        input = input.replaceAll("_", " ");
//        input = input.toLowerCase();
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < input.length(); i++) {
//            if (i == 0 || input.charAt(i - 1) == ' ') {
//                builder.append(Character.toUpperCase(input.charAt(i)));
//            } else {
//                builder.append(input.charAt(i));
//            }
//        }
//        return builder.toString();
//    }

    /**
     * Returns a new GridBagConstraint with gridX and gridY initialized to 0.
     * This is needed to allow incrementing either variable to work correctly.
     *
     * @return
     */
    public static GridBagConstraints getGC() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        return gc;
    }

    /**
     * Given a point on the screen, returns the bounds of the monitor containing that point.
     *
     * @param point
     * @return
     */
    public static Rectangle getScreenBoundsFromPoint(Point point) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            if (bounds.contains(point)) {
                return bounds;
            }
        }
        return null;
    }

    public static boolean openLink(String link) {
        if (link.startsWith("http:")) {
            link = link.replaceFirst("http:", "https:");
        }
        if (!link.startsWith("https://")) {
            link = "https://" + link;
        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(link));
                return true;
            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void openExplorer(String path) {
        File targetDir = new File(path);
        if (!targetDir.exists()) {
            boolean success = targetDir.mkdirs();
            if (!success) return;
        }
        if (!targetDir.exists() || !targetDir.isDirectory()) return;
        try {
            Desktop.getDesktop().open(targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.zrmiller.gui.misc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class IconFactory {

    public static ImageIcon getImageIcon(String path) {
        return getImageIcon(path, 0);
    }

    public static ImageIcon getImageIcon(String path, int size) {
        return getImageIcon(path, size, size);
    }

    public static ImageIcon getImageIcon(String path, int sizeX, int sizeY) {
        if (!path.startsWith("/"))
            path = "/" + path;
        try {
            ImageIcon icon;
            if (sizeX <= 0 || sizeY <= 0) {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(IconFactory.class.getResourceAsStream(path))));
            } else {
                icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(IconFactory.class.getResourceAsStream(path))).getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH));
            }
            return icon;
        } catch (IOException | NullPointerException e) {
            System.err.println("[IconButton] File not found: resources" + path);
            System.err.println("[IconButton] If this problem persists, clean and rebuild the project.");
            return null;
        }
    }
}

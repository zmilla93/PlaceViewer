package com.zrmiller.modules.colortheme.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Objects;

public class FlatColorIconButton extends JButton {

    private final Image[] originalList;
    private final BufferedImage[] bufferedImages;
    private final Icon[] icons;
    private int iconIndex = 0;

    private static final int ICON_SIZE = 20;

    public FlatColorIconButton(String... path) {
        originalList = new Image[path.length];
        bufferedImages = new BufferedImage[path.length];
        icons = new Icon[path.length];
        for (int i = 0; i < path.length; i++) {
            path[i] = path[i].startsWith("/") ? path[i] : "/" + path[i];
            originalList[i] = getImageFromFile(path[i]);
            if (originalList[i] == null) {
                System.err.println("[IconButton] File not found: /resources" + path[i]);
                System.err.println("[IconButton] If this file exists, try cleaning and rebuilding the project.");
                continue;
            }
            bufferedImages[i] = createBufferedImage(originalList[i]);
        }
        updateUI();
    }

    private Image getImageFromFile(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    public BufferedImage createBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    private void recolorBufferedImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableRaster raster = bufferedImage.getRaster();
        Color textColor = UIManager.getColor("Label.foreground");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] pixels = raster.getPixel(x, y, (int[]) null);
                pixels[0] = textColor.getRed();
                pixels[1] = textColor.getGreen();
                pixels[2] = textColor.getBlue();
                raster.setPixel(x, y, pixels);
            }
        }
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public void setIconIndex(int index) {
        iconIndex = index;
        setIcon(icons[index]);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (originalList == null) return;
        for (int i = 0; i < originalList.length; i++) {
            recolorBufferedImage(bufferedImages[i]);
            icons[i] = new ImageIcon(bufferedImages[i].getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
        }
        setIcon(icons[iconIndex]);
        int margin = 0;
        setMargin(new Insets(margin, margin, margin, margin));
        setBorder(new FlatButtonBorder());
    }

}

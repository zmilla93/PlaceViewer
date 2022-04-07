package com.zrmiller.modules.styles.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Objects;

public class BrokeIconButton extends JButton {

    private final String path;
    private BufferedImage image;
    private ImageIcon icon;

    private int cachedSize;
    private Color cachedColor;

    private int marginSize = 0;

    private Color color;
    private int size;

    private Image original;
    private BufferedImage colorImage;

    public BrokeIconButton(String path) {
//        this(path, null);
        this.path = path;
        setText(path);
    }

    public BrokeIconButton(String path, Color color) {
        this(path, color, 20);
    }

    public BrokeIconButton(String path, Color color, int size) {
        this.path = path;
        this.color = color;
        this.size = size;
//        setBorder(new FlatButtonBorder());
//        setBorder(new FlatBorder());
//        setMargin(new Insets(marginSize, marginSize, marginSize, marginSize));
        System.out.println("??");
        if (!getImage()) {
            // TODO : ERR
            System.err.println("ERRRRRR");
        }
        if (original == null) {
            System.out.println("BADNESS");
            return;
        }
        if (size != cachedSize || color != cachedColor) {
            System.out.println("Making...");
            // FIXME : Could keep permanent copy of image to avoid having to reread from file on theme change.
            if (color == null) icon = new ImageIcon(original.getScaledInstance(size, size, Image.SCALE_SMOOTH));
            else icon = new ImageIcon(getColorImage(color).getScaledInstance(size, size, Image.SCALE_SMOOTH));
            cachedSize = size;
            cachedColor = color;
        }
        System.out.println("icon" + icon);
        setPreferredSize(new Dimension(size, size));
        setText(">A>A>");
//        setIcon(icon);
    }

    private boolean getImage() {
        if (original == null) {
            try {
                original = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
                System.out.println("orig" + original);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private Image getColorImage(Color color) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = color.getRed();
                pixels[1] = color.getGreen();
                pixels[2] = color.getBlue();
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }

    @Override
    public void updateUI() {
        if (image == null) return;
        if (color == null) icon = new ImageIcon(image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        else icon = new ImageIcon(getColorImage(color).getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }
}

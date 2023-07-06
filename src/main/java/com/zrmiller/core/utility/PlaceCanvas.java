package com.zrmiller.core.utility;

import com.zrmiller.App;
import com.zrmiller.core.colors.ColorMode;
import com.zrmiller.core.colors.Gradient;
import com.zrmiller.core.data.References;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.exporting.IExportCallback;
import com.zrmiller.core.managers.DatasetManager;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.gui.FrameManager;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlaceCanvas {

    private static final int PAN_OOB_SIZE = 10;

    public int viewportWidth = 1400;
    public int viewportHeight = 1400;
    public int viewportPanX = 0;
    public int viewportPanY = 0;

    public int selectionX1;
    public int selectionY1;
    public int selectionX2;
    public int selectionY2;

    public boolean selection = false;
    private int selectionXLower;
    private int selectionXUpper;
    private int selectionYLower;
    private int selectionYUpper;

    private Color backgroundColor = Color.WHITE;

    private static final int COLOR_CHANNEL_COUNT = 3;
    private int[] rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];

    public ZoomLevel zoomLevel = ZoomLevel.Zoom_1;
    private final PlacePlayer player;
    private final Gradient heatGradient = new Gradient();
    private ColorMode colorMode = ColorMode.NORMAL;

    /**
     * Converts the raw color data from a PlacePlayer into something that can be easily displayed.
     * Handles viewports, zooming, panning, selections, color converting, and image exporting.
     *
     * @param player The PlacePlayer to process data for.
     */
    public PlaceCanvas(PlacePlayer player) {
        this.player = player;
        heatGradient.addKey(0f, new Color(0, 0, 0));
        heatGradient.addKey(0.25f, new Color(17, 59, 229));
        heatGradient.addKey(0.5f, new Color(198, 21, 211));
        heatGradient.addKey(0.75f, new Color(220, 10, 49));
        heatGradient.addKey(1f, new Color(231, 220, 13));
    }

    public void updateColorBuffer() {
        if (App.dataset() == null)
            return;
        calculateSelectionBounds();
        for (int y = 0; y < viewportHeight; y++) {
            for (int x = 0; x < viewportWidth; x++) {
                resolvePixel(x, y);
            }
        }
    }

    public int[] getColorBuffer() {
        return rgbColorBuffer;
    }

    private void resolvePixel(int pixelX, int pixelY) {
        int x = pixelX + viewportPanX;
        int y = pixelY + viewportPanY;
        int canvasIndex;
        if (zoomLevel.zoomOut) {
            canvasIndex = x * zoomLevel.modifier + y * zoomLevel.modifier * App.dataset().CANVAS_SIZE_X;
        } else {
            canvasIndex = x / zoomLevel.modifier + y / zoomLevel.modifier * App.dataset().CANVAS_SIZE_X;
        }
        // index of top left colorBuffer element being drawn
        int colorBufferIndex = pixelX * COLOR_CHANNEL_COUNT + pixelY * viewportWidth * COLOR_CHANNEL_COUNT;
        if (canvasIndex < 0 || canvasIndex >= player.getColorBuffer().length) {
            rgbColorBuffer[colorBufferIndex] = backgroundColor.getRed();
            rgbColorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            rgbColorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
            return;
        }
        Color color = Color.BLACK;
        int heat = 0;
        float heatNormal = 0;
        if (colorMode == ColorMode.HEATMAP_GRAYSCALE || colorMode == ColorMode.HEATMAP_COLOR) {
            heat = player.getHeatmapBuffer()[canvasIndex];
            heatNormal = ZUtil.clamp(heat / (float) PlacePlayer.heatmapMax, 0f, 1f);
        }
        switch (colorMode) {
            case NORMAL:
                int colorIndex = player.getColorBuffer()[canvasIndex];
                color = App.dataset().COLOR_ARRAY[colorIndex];
                break;
            case HEATMAP_GRAYSCALE:
                int heatColorValue = Math.round(heatNormal * 255);
                color = new Color(heatColorValue, heatColorValue, heatColorValue);
                break;
            case HEATMAP_COLOR:
                color = heatGradient.resolveColor(heatNormal);
                break;
        }
        if (selection) {
            if (pixelX < selectionXLower || pixelX >= selectionXUpper ||
                    pixelY < selectionYLower || pixelY >= selectionYUpper) {
                color = new Color((int) (color.getRed() * 0.5f), (int) (color.getGreen() * 0.5f), (int) (color.getBlue() * 0.5f));
            }
        }
        int checkX = zoomLevel.zoomOut ? App.dataset().CANVAS_SIZE_X / zoomLevel.modifier : App.dataset().CANVAS_SIZE_X * zoomLevel.modifier;
        int checkY = zoomLevel.zoomOut ? App.dataset().CANVAS_SIZE_Y / zoomLevel.modifier : App.dataset().CANVAS_SIZE_Y * zoomLevel.modifier;
        if (x < 0 || x >= checkX || y < 0 || y >= checkY * zoomLevel.modifier) {
            // Paint Out of Bounds Pixel
            rgbColorBuffer[colorBufferIndex] = backgroundColor.getRed();
            rgbColorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            rgbColorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
        } else {
            // Paint Color Pixel
            rgbColorBuffer[colorBufferIndex] = color.getRed();
            rgbColorBuffer[colorBufferIndex + 1] = color.getGreen();
            rgbColorBuffer[colorBufferIndex + 2] = color.getBlue();
        }
    }

    private void calculateSelectionBounds() {
        if (selectionX1 == selectionX2 && selectionY1 == selectionY2) {
            selection = false;
            return;
        }
        if (selectionX1 < selectionX2) {
            selectionXLower = selectionX1;
            selectionXUpper = selectionX2 + 1;
        } else {
            selectionXLower = selectionX2;
            selectionXUpper = selectionX1 + 1;
        }
        if (selectionY1 < selectionY2) {
            selectionYLower = selectionY1;
            selectionYUpper = selectionY2 + 1;
        } else {
            selectionYLower = selectionY2;
            selectionYUpper = selectionY1 + 1;
        }
        if (zoomLevel.zoomOut) {
            selectionXLower /= zoomLevel.modifier;
            selectionXUpper /= zoomLevel.modifier;
            selectionYLower /= zoomLevel.modifier;
            selectionYUpper /= zoomLevel.modifier;
        } else {
            selectionXLower *= zoomLevel.modifier;
            selectionXUpper *= zoomLevel.modifier;
            selectionYLower *= zoomLevel.modifier;
            selectionYUpper *= zoomLevel.modifier;
        }
        // Clamp selection to canvas bounds
        int scaledX = zoomLevel.scale(App.dataset().CANVAS_SIZE_X);
        int scaledY = zoomLevel.scale(App.dataset().CANVAS_SIZE_Y);
        if (selectionXLower < 0) selectionXLower = 0;
        if (selectionXUpper > scaledX) selectionXUpper = scaledX;
        if (selectionYLower < 0) selectionYLower = 0;
        if (selectionYUpper > scaledY) selectionYUpper = scaledY;
        // Modify selection based on viewport
        selectionXLower -= viewportPanX;
        selectionXUpper -= viewportPanX;
        selectionYLower -= viewportPanY;
        selectionYUpper -= viewportPanY;
        selection = true;
    }

    public Rectangle getSelectionBounds() {
        if (!selection) return null;
        return new Rectangle(zoomLevel.unscale(selectionXLower + viewportPanX), zoomLevel.unscale(selectionYLower + viewportPanY), zoomLevel.unscale(selectionXUpper - selectionXLower), zoomLevel.unscale(selectionYUpper - selectionYLower));
    }

    public void jumpToPixel(Point point) {
        jumpToPixel(point.x, point.y);
    }

    public void jumpToPixel(int x, int y) {
        if (zoomLevel.zoomOut) {
            viewportPanX = x / zoomLevel.modifier - (viewportWidth / 2);
            viewportPanY = y / zoomLevel.modifier - (viewportHeight / 2);
        } else {
            viewportPanX = x * zoomLevel.modifier - (viewportWidth / 2);
            viewportPanY = y * zoomLevel.modifier - (viewportHeight / 2);
        }
    }

    public void jumpToPixelTopLeft(int x, int y) {
        if (zoomLevel.zoomOut) {
            viewportPanX = x / zoomLevel.modifier;
            viewportPanY = y / zoomLevel.modifier;
        } else {
            viewportPanX = x * zoomLevel.modifier;
            viewportPanY = y * zoomLevel.modifier;
        }
    }

    public Point getCenterPixel() {
        Point point = new Point();
        if (zoomLevel.zoomOut) {
            point.x = (viewportWidth / 2 + viewportPanX) * zoomLevel.modifier;
            point.y = (viewportHeight / 2 + viewportPanY) * zoomLevel.modifier;
        } else {
            point.x = (viewportWidth / 2 + viewportPanX) / zoomLevel.modifier;
            point.y = (viewportHeight / 2 + viewportPanY) / zoomLevel.modifier;
        }
        return point;
    }

    public void restrictPan() {
        if (App.dataset() == null)
            return;
        int minX, maxX, minY, maxY;
        if (zoomLevel.zoomOut) {
            minX = -PAN_OOB_SIZE / zoomLevel.modifier - viewportWidth / 2;
            maxX = App.dataset().CANVAS_SIZE_X / zoomLevel.modifier - viewportWidth / 2 + PAN_OOB_SIZE / zoomLevel.modifier;
            minY = -PAN_OOB_SIZE / zoomLevel.modifier - viewportHeight / 2;
            maxY = App.dataset().CANVAS_SIZE_X / zoomLevel.modifier - viewportHeight / 2 + PAN_OOB_SIZE / zoomLevel.modifier;
        } else {
            minX = -PAN_OOB_SIZE * zoomLevel.modifier - viewportWidth / 2;
            maxX = App.dataset().CANVAS_SIZE_X * zoomLevel.modifier - viewportWidth / 2 + PAN_OOB_SIZE * zoomLevel.modifier;
            minY = -PAN_OOB_SIZE * zoomLevel.modifier - viewportHeight / 2;
            maxY = App.dataset().CANVAS_SIZE_Y * zoomLevel.modifier - viewportHeight / 2 + PAN_OOB_SIZE * zoomLevel.modifier;
        }
        if (viewportPanX < minX) viewportPanX = minX;
        if (viewportPanX > maxX) viewportPanX = maxX;
        if (viewportPanY < minY) viewportPanY = minY;
        if (viewportPanY > maxY) viewportPanY = maxY;
    }

    public void zoomOut() {
        Point looking = getCenterPixel();
        int zoom = zoomLevel.ordinal();
        if (zoom >= 1) {
            zoomLevel = ZoomLevel.values()[zoom - 1];
        }
        jumpToPixel(looking);
        restrictPan();
    }

    public void zoomIn() {
        Point looking = getCenterPixel();
        int zoom = zoomLevel.ordinal();
        if ((zoom < ZoomLevel.values().length - 1)) {
            zoomLevel = ZoomLevel.values()[zoom + 1];
        }
        jumpToPixel(looking);
        restrictPan();
    }

    public void resetZoom() {
        zoomLevel = ZoomLevel.Zoom_1;
        jumpToPixelTopLeft(0, 0);
        restrictPan();
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    public void exportImage(String fileName, int posX, int posY, int width, int height, ZoomLevel zoomLevel) {
        exportImage(fileName, posX, posY, width, height, zoomLevel, null);
    }

    public void exportImage(String fileName, int posX, int posY, int width, int height, ZoomLevel zoomLevel, IExportCallback callback) {
        if (App.dataset() == null) return;
        this.zoomLevel = zoomLevel;
        // Validate output directory
        File outDir = new File(References.getExportFolder());
        if (outDir.exists()) {
            if (!outDir.isDirectory()) return;
        } else if (!outDir.mkdirs()) return;
        // Validate output file
        String outputPath = References.getExportFolder() + fileName + ".png";
        if (!ZUtil.validateFileName(fileName, outputPath)) {
            JOptionPane.showMessageDialog(FrameManager.mainFrame, "File name '" + fileName + "' is invalid.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File file = new File(outputPath);
        if (file.exists()) {
            int answer = JOptionPane.showConfirmDialog(null, "File '" + fileName + "' already exists. Would you like to overwrite?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) return;
        }
        // Export the canvas using a separate thread
        Thread thread = new Thread(() -> {
            viewportWidth = zoomLevel.scale(width);
            viewportHeight = zoomLevel.scale(height);
            rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];
            setColorMode(DatasetManager.getColorMode());
            jumpToPixelTopLeft(posX, posY);
            updateColorBuffer();
            BufferedImage image = new BufferedImage(zoomLevel.scale(width), zoomLevel.scale(height), BufferedImage.TYPE_INT_RGB);
            image.getRaster().setPixels(0, 0, zoomLevel.scale(width), zoomLevel.scale(height), getColorBuffer());
            File outFile = new File(outputPath);
            try {
                ImageIO.write(image, "png", outFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> FrameManager.waitingFrame.hideFrame());
            if (callback != null) callback.onExportComplete();
        });
        thread.start();
        FrameManager.waitingFrame.showFrame("Exporting PNG", "Exporting image, please wait...");
    }

    public void exportGIF(int posX, int posY, int width, int height, ZoomLevel zoomLevel, int startFrame, int endFrame, int tilesPerSecond, int fps) {
        if (App.dataset() == null) return;
        viewportWidth = zoomLevel.scale(width);
        viewportHeight = zoomLevel.scale(height);
        rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];
        this.zoomLevel = zoomLevel;
        jumpToPixelTopLeft(posX, posY);
        updateColorBuffer();
        BufferedImage image = new BufferedImage(zoomLevel.scale(width), zoomLevel.scale(height), BufferedImage.TYPE_INT_RGB);
        image.getRaster().setPixels(0, 0, zoomLevel.scale(width), zoomLevel.scale(height), getColorBuffer());
        File outDir = new File(References.getExportFolder());
        File outFile = new File(References.getExportFolder() + "test.gif");
        if (outDir.exists()) {
            if (!outDir.isDirectory())
                return;
        } else if (!outDir.mkdirs())
            return;
        try {
            int frameIncrement = tilesPerSecond / fps;
            ImageOutputStream outputStream = new FileImageOutputStream(outFile);
            GifSequenceWriter writer = new GifSequenceWriter(outputStream, image.getType(), 1000 / fps, true);
            for (int i = startFrame; i <= endFrame; i += frameIncrement) {
                player.jumpToFrame(i);
                updateColorBuffer();
                image.getRaster().setPixels(0, 0, zoomLevel.scale(width), zoomLevel.scale(height), getColorBuffer());
                writer.writeToSequence(image);
            }
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportCleanup() {
        rgbColorBuffer = null;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

}

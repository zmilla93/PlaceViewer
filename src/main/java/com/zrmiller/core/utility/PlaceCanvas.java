package com.zrmiller.core.utility;

import com.zrmiller.App;
import com.zrmiller.core.colors.ColorMode;
import com.zrmiller.core.colors.Gradient;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.gui.exporting.GifSequenceWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlaceCanvas {

    private static final int PAN_OOB_SIZE = 100;

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
    int[] rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];

    public ZoomLevel zoomLevel = ZoomLevel.Zoom_1;
    private final PlacePlayer player;
    private final Gradient heatGradient = new Gradient();
    private ColorMode colorMode = ColorMode.NORMAL;

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
//        ColorMode colorMode = App.datasetManager.getColorMode();
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
        selectionXLower -= viewportPanX;
        selectionXUpper -= viewportPanX;
        selectionYLower -= viewportPanY;
        selectionYUpper -= viewportPanY;
        selection = true;
    }

    public Rectangle getSelectionBounds() {
        if (!selection)
            return null;
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

    public void exportImage(int posX, int posY, int width, int height, ZoomLevel zoomLevel) {
        if (App.dataset() == null)
            return;
        viewportWidth = zoomLevel.scale(width);
        viewportHeight = zoomLevel.scale(height);
        rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];
        this.zoomLevel = zoomLevel;
        jumpToPixelTopLeft(posX, posY);
        updateColorBuffer();
        BufferedImage image = new BufferedImage(zoomLevel.scale(width), zoomLevel.scale(height), BufferedImage.TYPE_INT_RGB);
        image.getRaster().setPixels(0, 0, zoomLevel.scale(width), zoomLevel.scale(height), getColorBuffer());
        File outDir = new File(SaveManager.settings.data.dataDirectory + "exports/");
        File outFile = new File(SaveManager.settings.data.dataDirectory + "exports/" + "test.png");
        if (outDir.exists()) {
            if (!outDir.isDirectory())
                return;
        } else if (!outDir.mkdirs())
            return;
        try {
            ImageIO.write(image, "png", outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportGIF(int posX, int posY, int width, int height, ZoomLevel zoomLevel, int startFrame, int endFrame, int tilesPerSecond, int fps) {
        if (App.dataset() == null)
            return;
//        if (zoomLevel.zoomOut) return;
        viewportWidth = zoomLevel.scale(width);
        viewportHeight = zoomLevel.scale(height);
        rgbColorBuffer = new int[viewportWidth * viewportHeight * COLOR_CHANNEL_COUNT];
        this.zoomLevel = zoomLevel;
        jumpToPixelTopLeft(posX, posY);
        updateColorBuffer();
        BufferedImage image = new BufferedImage(zoomLevel.scale(width), zoomLevel.scale(height), BufferedImage.TYPE_INT_RGB);
        image.getRaster().setPixels(0, 0, zoomLevel.scale(width), zoomLevel.scale(height), getColorBuffer());
        File outDir = new File(SaveManager.settings.data.dataDirectory + "exports/");
        File outFile = new File(SaveManager.settings.data.dataDirectory + "exports/" + "test.gif");
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

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

}

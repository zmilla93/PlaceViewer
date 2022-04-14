package com.zrmiller.core.utility;

import com.zrmiller.App;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.gui.FrameManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas {

    public int viewportWidth = 1400;
    public int viewportHeight = 1400;
    private int viewportPanX = 0;
    private int viewportPanY = 0;

    private Color backgroundColor = Color.black;

    int[] rgbColorBuffer = new int[viewportWidth * viewportHeight * 3]; // 3 entries per pixel

    private ZoomLevel zoomLevel = ZoomLevel.Zoom_1;
    private PlacePlayer player;

    public Canvas(PlacePlayer player) {
        this.player = player;
    }

    public void updateColorBuffer() {
        if (App.dataset() == null)
            return;
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
        int colorBufferIndex = pixelX * 3 + pixelY * viewportWidth * 3;   // index of top left colorBuffer element being drawn
        if (canvasIndex < 0 || canvasIndex >= player.getColorBuffer().length) {
            rgbColorBuffer[colorBufferIndex] = backgroundColor.getRed();
            rgbColorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            rgbColorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
            return;
        }
        // FIXME : Heatmap : IllegalArgumentException: Color parameter outside of expected range: Red Green Blue
        int heat = player.getHeatmapBuffer()[canvasIndex];
//        float f = heat / (float) PlacePlayer.heatmapMax;
//        Color c = new Color(f, f, f);
        int colorIndex = player.getColorBuffer()[canvasIndex];
        Color color = App.dataset().COLOR_ARRAY[colorIndex];
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

    public void jumpToPixelTopLeft(int x, int y) {
        if (zoomLevel.zoomOut) {
            viewportPanX = x / zoomLevel.modifier;
            viewportPanY = y / zoomLevel.modifier;
        } else {
            viewportPanX = x * zoomLevel.modifier;
            viewportPanY = y * zoomLevel.modifier;
        }
    }

    public void export(int posX, int posY, int width, int height, ZoomLevel zoomLevel) {
        if (App.dataset() == null)
            return;
        if (zoomLevel.zoomOut) return;
        viewportWidth = width * zoomLevel.modifier;
        viewportHeight = height * zoomLevel.modifier;
        rgbColorBuffer = new int[viewportWidth * viewportHeight * 3]; // 3 entries per pixel
        this.zoomLevel = zoomLevel;
        jumpToPixelTopLeft(posX, posY);
        updateColorBuffer();
        BufferedImage image = new BufferedImage(width * zoomLevel.modifier, height * zoomLevel.modifier, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setPixels(0, 0, width * zoomLevel.modifier, height * zoomLevel.modifier, getColorBuffer());
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

}

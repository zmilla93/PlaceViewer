package com.zrmiller.core.utility;

import com.zrmiller.App;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.SaveManager;
import com.zrmiller.core.parser.PlacePlayer;

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

    private Color backgroundColor = Color.WHITE;

    int[] rgbColorBuffer = new int[viewportWidth * viewportHeight * 3]; // 3 entries per pixel

    private ZoomLevel zoomLevel = ZoomLevel.Zoom_1;
    private PlacePlayer player;

    public PlaceCanvas(PlacePlayer player) {
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

    public void setBackgroundColor(Color color){
        backgroundColor = color;
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
        File outFile = new File(SaveManager.settings.data.dataDirectory + "exports/" + "test.gif");
        if (outDir.exists()) {
            if (!outDir.isDirectory())
                return;
        } else if (!outDir.mkdirs())
            return;
        try {

            ImageOutputStream outputStream = new FileImageOutputStream(outFile);
            GifSequenceWriter writer = new GifSequenceWriter(outputStream, image.getType(), 250, true);

            player.jumpToFrame(10);
            updateColorBuffer();
            image.getRaster().setPixels(0, 0, width * zoomLevel.modifier, height * zoomLevel.modifier, getColorBuffer());
            writer.writeToSequence(image);

            player.jumpToFrame(100);
            updateColorBuffer();
            image.getRaster().setPixels(0, 0, width * zoomLevel.modifier, height * zoomLevel.modifier, getColorBuffer());
            writer.writeToSequence(image);

            player.jumpToFrame(1000);
            updateColorBuffer();
            image.getRaster().setPixels(0, 0, width * zoomLevel.modifier, height * zoomLevel.modifier, getColorBuffer());
            writer.writeToSequence(image);

            player.jumpToFrame(10000);
            updateColorBuffer();
            image.getRaster().setPixels(0, 0, width * zoomLevel.modifier, height * zoomLevel.modifier, getColorBuffer());
            writer.writeToSequence(image);

            writer.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

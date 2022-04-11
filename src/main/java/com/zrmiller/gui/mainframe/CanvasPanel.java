package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.gui.mainframe.listeners.ICanvasListener;
import com.zrmiller.modules.colortheme.ColorManager;
import com.zrmiller.modules.colortheme.IThemeListener;
import com.zrmiller.modules.listening.ListenManagerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CanvasPanel extends ListenManagerPanel<ICanvasListener> implements IThemeListener, IDatasetListener {

    // FIXME : update canvas to dataset size
    private static int LOCAL_CANVAS_SIZE_X = App.dataset().CANVAS_SIZE_X;
    private static int LOCAL_CANVAS_SIZE_Y = App.dataset().CANVAS_SIZE_Y;

    // Viewport
    public int viewportWidth = 1400;
    public int viewportHeight = 1400;
    private int viewportPanX = 0;
    private int viewportPanY = 0;

    private static final int PAN_OOB_SIZE = 100;
    private final int targetFPS = 60;

    private ZoomLevel zoomLevel = ZoomLevel.Zoom_100;

    // Mouse Movement
    private int initialX;
    private int initialY;
    private int initialPanX;
    private int initialPanY;

    // Painting
    private final PlacePlayer player = new PlacePlayer();
    int[] rgbColorBuffer = new int[viewportWidth * viewportHeight * 3]; // 3 entries per pixel
    private BufferedImage bufferedImage = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);
    private boolean markForRepaint;
    private Color backgroundColor = Color.RED;
    int lastPaintedFrame = 0;
    private final Timer timer;

    public CanvasPanel() {
        int delay = targetFPS == -1 ? 0 : 1000 / targetFPS;
        timer = new Timer(delay, e -> {
            if (lastPaintedFrame != player.getFrameCount()) {
                markForRepaint = true;
                lastPaintedFrame = player.getFrameCount();
            }
            tryRepaint();
        });
        timer.start();
        addListeners();
        App.datasetManager.addListener(this);
    }

    private void tryRepaint() {
        tryRepaint(false);
    }

    public void tryRepaint(boolean force) {
        // TODO : Double check is this lastPaintedFrame check is needed
//        if (lastPaintedFrame != player.getFrameCount()) {
//            markForRepaint = true;
//            lastPaintedFrame = player.getFrameCount();
//        }
        if (markForRepaint || force) {
            updateColorBuffer();
            repaint();
            markForRepaint = false;
            for (ICanvasListener listener : listeners)
                listener.onDraw(lastPaintedFrame);
        }
    }

    private Point getCenterPixel() {
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

    public void panToPixel(Point point) {
        panToPixel(point.x, point.y);
    }

    public void panToPixel(int x, int y) {
        if (zoomLevel.zoomOut) {
            viewportPanX = x / zoomLevel.modifier - (viewportWidth / 2);
            viewportPanY = y / zoomLevel.modifier - (viewportHeight / 2);
        } else {
            viewportPanX = x * zoomLevel.modifier - (viewportWidth / 2);
            viewportPanY = y * zoomLevel.modifier - (viewportHeight / 2);
        }
        tryRepaint(true);
    }

    private void restrictPan() {
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

    private void resizeCanvas() {
        Dimension size = getSize();
        viewportWidth = size.width;
        viewportHeight = size.height;
        bufferedImage = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);
        updateColorBuffer();
        repaint();
    }

    private void updateColorBuffer() {
        for (int y = 0; y < viewportHeight; y++) {
            for (int x = 0; x < viewportWidth; x++) {
                resolvePixel(x, y);
            }
        }
    }

    private void resolvePixel(int pixelX, int pixelY) {
        int x = pixelX + viewportPanX;
        int y = pixelY + viewportPanY;
        int canvasIndex;
        if (zoomLevel.zoomOut) {
            canvasIndex = x * zoomLevel.modifier + y * zoomLevel.modifier * LOCAL_CANVAS_SIZE_X;
        } else {
            canvasIndex = x / zoomLevel.modifier + y / zoomLevel.modifier * LOCAL_CANVAS_SIZE_X;
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
        int checkX = zoomLevel.zoomOut ? LOCAL_CANVAS_SIZE_X / zoomLevel.modifier : LOCAL_CANVAS_SIZE_X * zoomLevel.modifier;
        int checkY = zoomLevel.zoomOut ? LOCAL_CANVAS_SIZE_Y / zoomLevel.modifier : LOCAL_CANVAS_SIZE_Y * zoomLevel.modifier;
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

    private void zoomIn() {
        Point looking = getCenterPixel();
        int zoom = zoomLevel.ordinal();
        if (zoom >= 1) {
            zoomLevel = ZoomLevel.values()[zoom - 1];
        }
        panToPixel(looking);
        restrictPan();
    }

    private void zoomOut() {
        Point looking = getCenterPixel();
        int zoom = zoomLevel.ordinal();
        if ((zoom < ZoomLevel.values().length - 1)) {
            zoomLevel = ZoomLevel.values()[zoom + 1];
        }
        panToPixel(looking);
        restrictPan();
    }

    private void addListeners() {
        ColorManager.addListener(this);
        addMouseWheelListener(e -> {
            int mod = e.getPreciseWheelRotation() < 0 ? -1 : 1;
            // Up = -1, Down = 1
            if (mod == -1) zoomOut();
            else zoomIn();
            markForRepaint = true;
            alertListeners();
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                initialX = e.getX();
                initialY = e.getY();
                initialPanX = viewportPanX;
                initialPanY = viewportPanY;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int dragX = e.getX() - initialX;
                int dragY = e.getY() - initialY;
                viewportPanX = initialPanX - dragX;
                viewportPanY = initialPanY - dragY;
                restrictPan();
                markForRepaint = true;
                for (ICanvasListener listener : listeners)
                    listener.onPan(getCenterPixel());
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCanvas();
            }
        });
    }

    private void alertListeners() {
        for (ICanvasListener listener : listeners) {
            listener.onZoom(zoomLevel);
        }

    }

    public PlacePlayer getPlayer() {
        return player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        bufferedImage.getRaster().setPixels(0, 0, viewportWidth, viewportHeight, rgbColorBuffer);
        g.drawImage(bufferedImage, 0, 0, Color.white, null);
    }

    @Override
    public void onThemeChange() {
        backgroundColor = UIManager.getColor("ComboBox.background");
        tryRepaint(true);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        LOCAL_CANVAS_SIZE_X = App.dataset().CANVAS_SIZE_X;
        LOCAL_CANVAS_SIZE_Y = App.dataset().CANVAS_SIZE_Y;
        tryRepaint(true);
    }
}

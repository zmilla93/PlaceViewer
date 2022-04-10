package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.enums.ZoomLevel;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.ZUtil;
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
    private int zoom = 1;
    private final int MAX_ZOOM = 20;
    //    private int cachedZoom = zoom;
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

    // Other
    private Dataset dataset = Dataset.PLACE_2022;

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
        point.x = (viewportWidth / 2 - viewportPanX) / zoom;
        point.y = (viewportHeight / 2 - viewportPanY) / zoom;
        return point;
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
        int x = pixelX - viewportPanX;
        int y = pixelY - viewportPanY;
        int z = zoom <= 0 ? 2 - zoom : zoom;
        int canvasIndex;
        if (zoom >= 1) {
            canvasIndex = x / z + y / z * LOCAL_CANVAS_SIZE_X;
        } else {
            canvasIndex = x * z + y * z * LOCAL_CANVAS_SIZE_X;
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
        int checkX = zoom < 1 ? LOCAL_CANVAS_SIZE_X / z : LOCAL_CANVAS_SIZE_X * z;
        int checkY = zoom < 1 ? LOCAL_CANVAS_SIZE_Y / z : LOCAL_CANVAS_SIZE_Y * z;
        if (x < 0 || x > checkX || y < 0 || y > checkY * z) {
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
        int zoom = zoomLevel.ordinal();
        if (zoom >= 0) {
            zoomLevel = ZoomLevel.values()[zoom - 1];
        }
    }

    private void zoomOut() {
        int zoom = zoomLevel.ordinal();
        if ((zoom < ZoomLevel.values().length - 1)) {
            zoomLevel = ZoomLevel.values()[zoom + 1];
        }
    }

    private void addListeners() {
        ColorManager.addListener(this);
        addMouseWheelListener(e -> {
            int mod = e.getPreciseWheelRotation() < 0 ? -1 : 1;
            // Up = -1, Down = 1
            if (mod == -1) zoomOut();
            else zoomIn();
            zoom -= mod;
            zoom = ZUtil.clamp(zoom, -1, MAX_ZOOM);
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
                viewportPanX = initialPanX + dragX;
                viewportPanY = initialPanY + dragY;
                markForRepaint = true;
                alertListeners();
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
        for (ICanvasListener listener : listeners)
            listener.onZoom(zoom);
//        for (ICanvasListener listener : canvasListeners)
//            listener.onPan(getCenterPixel());
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

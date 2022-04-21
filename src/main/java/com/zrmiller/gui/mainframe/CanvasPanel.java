package com.zrmiller.gui.mainframe;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.PlaceCanvas;
import com.zrmiller.gui.mainframe.listeners.ICanvasListener;
import com.zrmiller.gui.mainframe.listeners.IPlayerControllerListener;
import com.zrmiller.modules.colortheme.ColorManager;
import com.zrmiller.modules.colortheme.IThemeListener;
import com.zrmiller.modules.listening.ListenManagerPanel;
import com.zrmiller.modules.stopwatch.Stopwatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CanvasPanel extends ListenManagerPanel<ICanvasListener> implements IThemeListener, IDatasetListener, IPlayerControllerListener {

    private final int targetFPS = 60;

    // Mouse Movement
    private int initialX;
    private int initialY;
    private int initialPanX;
    private int initialPanY;
    private boolean leftMouseDown;
    private boolean rightMouseDown;

    // Painting
    private final PlacePlayer player = new PlacePlayer();
    private final PlaceCanvas canvas = new PlaceCanvas(player);
    private BufferedImage bufferedImage;
    private boolean markForRepaint;
    int lastPaintedFrame = 0;
    private final Timer timer;

    public CanvasPanel(PlayerControlPanel playerControlPanel) {
        int delay = targetFPS == -1 ? 0 : 1000 / targetFPS;
        Stopwatch.start();
        timer = new Timer(delay, e -> {
            tryRepaint();
        });
        timer.start();
        addListeners();
        App.datasetManager.addListener(this);
        playerControlPanel.addListener(this);
    }

    private void tryRepaint() {
        tryRepaint(false);
    }

    public void tryRepaint(boolean force) {
        // TODO : Double check is this lastPaintedFrame check is needed
        if (lastPaintedFrame != player.getFrameCount()) {
            markForRepaint = true;
            lastPaintedFrame = player.getFrameCount();
        }
        if (markForRepaint || force) {
            canvas.updateColorBuffer();
            repaint();
            markForRepaint = false;
            for (ICanvasListener listener : listeners)
                listener.onDraw(lastPaintedFrame);
        }
    }

    private void resizeCanvas() {
        Dimension size = getSize();
        canvas.viewportWidth = size.width;
        canvas.viewportHeight = size.height;
        bufferedImage = new BufferedImage(canvas.viewportWidth, canvas.viewportHeight, BufferedImage.TYPE_INT_RGB);
        canvas.updateColorBuffer();
        repaint();
    }

    private void addListeners() {
        ColorManager.addListener(this);
        addMouseWheelListener(e -> {
            int mod = e.getPreciseWheelRotation() < 0 ? -1 : 1;
            if (mod == -1) canvas.zoomIn();
            else canvas.zoomOut();
            markForRepaint = true;
            alertListeners();
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftMouseDown = true;
                    initialX = e.getX();
                    initialY = e.getY();
                    initialPanX = canvas.viewportPanX;
                    initialPanY = canvas.viewportPanY;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    rightMouseDown = true;
                    Point tile = screenPointToPixel(e.getX(), e.getY());
                    canvas.selectionX1 = tile.x;
                    canvas.selectionY1 = tile.y;
                    canvas.selectionX2 = tile.x;
                    canvas.selectionY2 = tile.y;
                    markForRepaint = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftMouseDown = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    rightMouseDown = false;
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (leftMouseDown) {
                    int dragX = e.getX() - initialX;
                    int dragY = e.getY() - initialY;
                    canvas.viewportPanX = initialPanX - dragX;
                    canvas.viewportPanY = initialPanY - dragY;
                    canvas.restrictPan();
                    markForRepaint = true;
                }
                if (rightMouseDown) {
                    Point tile = screenPointToPixel(e.getX(), e.getY());
                    canvas.selectionX2 = tile.x;
                    canvas.selectionY2 = tile.y;
                    markForRepaint = true;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                for (ICanvasListener listener : listeners)
                    listener.onPan(screenPointToPixel(e.getX(), e.getY()));
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCanvas();
            }
        });
    }

    private Point screenPointToPixel(int x, int y) {
        Point point = new Point();
        if (canvas.zoomLevel.zoomOut) {
            point.x = (x + canvas.viewportPanX) * canvas.zoomLevel.modifier;
            point.y = (y + canvas.viewportPanY) * canvas.zoomLevel.modifier;
        } else {
            point.x = (x + canvas.viewportPanX) / canvas.zoomLevel.modifier;
            point.y = (y + canvas.viewportPanY) / canvas.zoomLevel.modifier;
        }
        return point;
    }

    private void alertListeners() {
        for (ICanvasListener listener : listeners) {
            listener.onZoom(canvas.zoomLevel);
        }
    }

    public PlacePlayer getPlayer() {
        return player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        bufferedImage.getRaster().setPixels(0, 0, canvas.viewportWidth, canvas.viewportHeight, canvas.getColorBuffer());
        g.drawImage(bufferedImage, 0, 0, Color.white, null);
    }

    @Override
    public void onThemeChange() {
        canvas.setBackgroundColor(UIManager.getColor("ComboBox.background"));
        tryRepaint(true);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        tryRepaint(true);
    }

    // Player Controls

    @Override
    public void onReset() {
        player.stop();
    }

    @Override
    public void onPlay() {
        player.play();
    }

    @Override
    public void onPause() {
        player.pause();
    }

    @Override
    public void onSpeedChange(int tilesPerSecond) {
        player.setSpeed(tilesPerSecond);
    }
}

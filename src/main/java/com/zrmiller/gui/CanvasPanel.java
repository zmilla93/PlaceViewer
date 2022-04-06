package com.zrmiller.gui;

import com.zrmiller.core.parser.BufferedPlacePlayer;
import com.zrmiller.core.parser.PlacePlayer;
import com.zrmiller.core.utility.PlaceInfo;
import com.zrmiller.core.utility.ZUtil;
import com.zrmiller.modules.styles.ColorManager;
import com.zrmiller.modules.styles.IThemeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CanvasPanel extends JPanel implements IThemeListener {

    // TODO : Move
    public static int CANVAS_SIZE_X = 1001;
    public static int CANVAS_SIZE_Y = 1001;

    // Panel Settings
    public int viewportWidth = 1400;
    public int viewportHeight = 1400;
    private int targetFPS = -1;
    private int viewportPanX = 0;
    private int viewportPanY = 0;
    private final int MAX_ZOOM = 20;
    private int zoom = 1;
    private int cachedZoom = zoom;

    private boolean markForRepaint;

    // Movement
    private int initialX;
    private int initialY;
    private int initialPanX;
    private int initialPanY;

    // Listeners
    private ArrayList<ICanvasListener> canvasListeners = new ArrayList<>();

    private Thread thread;

    private Timer timer = new Timer(1, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

//    private final PlaceParser parser = new PlaceParser();
    private final PlacePlayer parser = new PlacePlayer("D:/Place/place_tiles_final");

    BufferedImage bufferedImage = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);

    int[] colorBuffer = new int[viewportWidth * viewportHeight * 3];
    private Color backgroundColor = Color.RED;

    private static final Executor executor = Executors.newSingleThreadExecutor();
    int lastPaintedFrame = 0;

    public CanvasPanel() {
        setFocusable(true);
        setRequestFocusEnabled(true);
        parser.play();
//        parser.openInputStream("D:/Place/place_tiles_final");
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (parser.ready()) {
//                        parser.getNextLine();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                parser.close();
//            }
//        });
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (lastPaintedFrame != parser.getLineCount()) {
//                    markForRepaint = true;
//                    lastPaintedFrame = parser.getLineCount();
//                }
                // FIXME :
                markForRepaint = true;
                if (markForRepaint) {
                    updateColorBuffer();
                    repaint();
                    markForRepaint = false;
                }
            }
        });
        int delay = targetFPS == -1 ? 0 : 1000 / targetFPS;
        timer.setDelay(delay);
        timer.start();
        addListeners();
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
            canvasIndex = x / z + y / z * CANVAS_SIZE_X;
        } else {
            canvasIndex = x * z + y * z * CANVAS_SIZE_X;
        }
        int colorBufferIndex = pixelX * 3 + pixelY * viewportWidth * 3;   // index of top left colorBuffer element being drawn
        if (canvasIndex < 0 || canvasIndex >= parser.getColorBuffer().length) {
            colorBuffer[colorBufferIndex] = backgroundColor.getRed();
            colorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            colorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
            return;
        }
        int colorIndex = parser.getColorBuffer()[canvasIndex];
        Color color = PlaceInfo.canvasColors[colorIndex];
        int checkX = zoom < 1 ? CANVAS_SIZE_X / z : CANVAS_SIZE_X * z;
        int checkY = zoom < 1 ? CANVAS_SIZE_Y / z : CANVAS_SIZE_Y * z;
        if (x < 0 || x > checkX || y < 0 || y > checkY * z) {
            // Paint Out of Bounds Pixel
            colorBuffer[colorBufferIndex] = backgroundColor.getRed();
            colorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            colorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
        } else {
            // Paint Color Pixel
            colorBuffer[colorBufferIndex] = color.getRed();
            colorBuffer[colorBufferIndex + 1] = color.getGreen();
            colorBuffer[colorBufferIndex + 2] = color.getBlue();
        }
    }

    private void addListeners() {
        ColorManager.addListener(this);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int mod = e.getPreciseWheelRotation() < 0 ? 1 : -1;
                zoom += mod;
                zoom = ZUtil.clamp(zoom, -1, MAX_ZOOM);
                markForRepaint = true;
                alertListeners();
            }
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
        for (ICanvasListener listener : canvasListeners)
            listener.onZoom(zoom);
        for (ICanvasListener listener : canvasListeners)
            listener.onPan(getCenterPixel());
    }

    private int getFixedZoom() {
        return zoom <= 0 ? 2 - zoom : zoom;
    }

    // Listeners
    public void addListener(ICanvasListener listener) {
        canvasListeners.add(listener);
    }

    public void removeListener(ICanvasListener listener) {
        canvasListeners.remove(listener);
    }

    public void removeAllListeners() {
        canvasListeners.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        bufferedImage.getRaster().setPixels(0, 0, viewportWidth, viewportHeight, colorBuffer);
        g.drawImage(bufferedImage, 0, 0, Color.white, null);
    }

    @Override
    public void onThemeChange() {
        backgroundColor = UIManager.getColor("ComboBox.background");
    }

}

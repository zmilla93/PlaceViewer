package com.zrmiller.gui;

import com.zrmiller.core.parser.PlaceParser;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CanvasPanel extends JPanel {

    // TODO : Move
    public static int CANVAS_SIZE_X = 1001;
    public static int CANVAS_SIZE_Y = 1001;

    // Panel Settings
    public int viewportWidth = 1400;
    public int viewportHeight = 1400;
    private int targetFPS = -1;
    private int VIEWPORT_PAN_BUFFER = 100;
    private int viewportPanX = 0;
    private int viewportPanY = 0;
    private final int MAX_ZOOM = 20;
    private int zoom = 2;
    private static int COLOR_CHANNEL_COUNT;

    // Movement
    private int initialX;
    private int initialY;
    private int initialPanX;
    private int initialPanY;

    private Timer timer = new Timer(1, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });

    private static Color[] colors = new Color[]{
            new Color(255, 255, 255),
            new Color(201, 201, 201),
            new Color(79, 79, 79),
            new Color(0, 0, 0),
            new Color(222, 156, 227),
            new Color(243, 8, 8),
            new Color(245, 107, 36),
            new Color(122, 62, 18),
            new Color(232, 196, 5),
            new Color(164, 220, 128),
            new Color(17, 47, 7),
            new Color(40, 181, 194),
            new Color(72, 132, 217),
            new Color(13, 56, 227),
            new Color(181, 60, 222),
            new Color(104, 16, 171),
    };

    private static Color backgroundColor = new Color(28, 27, 27);

    private PlaceParser placeParser = new PlaceParser();

    //    BufferedImage bufferedImage = new BufferedImage(CANVAS_SIZE_X, CANVAS_SIZE_Y, BufferedImage.TYPE_INT_RGB);
    BufferedImage bufferedImage = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);

    int[] colorBuffer = new int[viewportWidth * viewportHeight * 3];

    private static Executor executor = Executors.newSingleThreadExecutor();
    int lastPaintedFrame = 0;

    public CanvasPanel() {
        placeParser.openInputStream("D:/Place/place_tiles_final");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (placeParser.ready()) {
                        placeParser.getNextTokens();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                placeParser.close();
            }
        });

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                updateColorBuffer(placeParser.getColorBuffer());
                updateColorBuffer();
                repaint();

                if (markForRepaint || lastPaintedFrame != placeParser.getLineCount()) {
                    repaint();
                    lastPaintedFrame = placeParser.getLineCount();
                }
            }
        });
        int delay = targetFPS == -1 ? 0 : 1000 / targetFPS;
        timer.setDelay(delay);
        timer.start();
        addListeners();
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
//        int canvasIndex = x + y * CANVAS_SIZE_X;
        int canvasIndex = x / zoom + y / zoom * CANVAS_SIZE_X;
        int colorBufferIndex = pixelX * 3 + pixelY * viewportWidth * 3;   // index of top left colorBuffer element being drawn
        if (canvasIndex < 0 || canvasIndex >= placeParser.getColorBuffer().length) {
            colorBuffer[colorBufferIndex] = backgroundColor.getRed();
            colorBuffer[colorBufferIndex + 1] = backgroundColor.getGreen();
            colorBuffer[colorBufferIndex + 2] = backgroundColor.getBlue();
            return;
        }
        int colorIndex = placeParser.getColorBuffer()[canvasIndex];
        Color color = colors[colorIndex];
        if (x < 0 || x > CANVAS_SIZE_X * zoom || y < 0 || y > CANVAS_SIZE_Y * zoom) {
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

    public void updateTile(int x, int y, int colorIndex) {
        int index = x * 3 + y * CANVAS_SIZE_X * 3;
        Color color = colors[colorIndex];
        if (index + 2 > colorBuffer.length) {
            System.out.println("BAD::" + x + ", " + y);
            return;
        }
        if (index < 0) {
            System.out.println("BAD::" + x + ", " + y);
            return;
        }
        colorBuffer[index] = color.getRed();
        colorBuffer[index + 1] = color.getGreen();
        colorBuffer[index + 2] = color.getBlue();
    }

    private boolean markForRepaint = false;

    private void addListeners() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int mod = e.getPreciseWheelRotation() < 0 ? 1 : -1;
                zoom += mod;
                zoom = ZUtil.clamp(zoom, 1, MAX_ZOOM);
                markForRepaint = true;
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
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bufferedImage.getRaster().setPixels(0, 0, viewportWidth, viewportHeight, colorBuffer);
        g.drawImage(bufferedImage, 0, 0, Color.white, null);
    }

}

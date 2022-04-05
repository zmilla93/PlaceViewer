package com.zrmiller.gui;

import com.zrmiller.core.parser.PlaceParser;
import com.zrmiller.core.utility.ZUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CanvasPanel extends JPanel {

    public static int CANVAS_SIZE_X = 1001;
    public static int CANVAS_SIZE_Y = 1001;

    private int targetFPS = 60;


    public int viewportWidth = 1001;
    public int viewportHeight = 1001;

    private int VIEWPORT_PAN_BUFFER = 100;
    private int viewportPanX = 0;
    private int viewportPanY = 0;
    private final int MAX_ZOOM = 10;
    private int zoom = 1;
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

    private PlaceParser placeParser = new PlaceParser();

    //    BufferedImage bufferedImage = new BufferedImage(CANVAS_SIZE_X, CANVAS_SIZE_Y, BufferedImage.TYPE_INT_RGB);
    BufferedImage bufferedImage = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_RGB);

    int[] colorBuffer = new int[CANVAS_SIZE_X * CANVAS_SIZE_Y * 3];

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
        timer.setDelay(1000 / targetFPS);
        timer.start();
        addListeners();
    }

    private void updateColorBuffer() {
        Arrays.fill(colorBuffer, 0);
        for (int y = 0; y < CANVAS_SIZE_Y; y++) {
            for (int x = 0; x < CANVAS_SIZE_X; x++) {
//                pixelToScreen(x, y);
                resolvePixel(x, y);
            }
        }
    }

    private void resolvePixel(int pixelX, int pixelY) {
        int x = pixelX - viewportPanX;
        int y = pixelY - viewportPanY;
        int index = x + y * CANVAS_SIZE_X;


        int colorBufferIndex = pixelX * 3 + pixelY * viewportWidth * 3;   // index of top left colorBuffer element being drawn
        if (index < 0 || index >= placeParser.getColorBuffer().length) {
//            System.out.println("ERR:" + x + ", " + y);
            return;
        }
        int colorIndex = placeParser.getColorBuffer()[index];

        Color color = colors[colorIndex];
        if (x < 0 || x > CANVAS_SIZE_X || y < 0 || y > CANVAS_SIZE_Y) {
            // TODO : Draw black
            colorBuffer[colorBufferIndex] = 0;
            colorBuffer[colorBufferIndex + 1] = 0;
            colorBuffer[colorBufferIndex + 2] = 0;
        } else {
            colorBuffer[colorBufferIndex] = color.getRed();
            colorBuffer[colorBufferIndex + 1] = color.getGreen();
            colorBuffer[colorBufferIndex + 2] = color.getBlue();
        }
    }

    private void pixelToScreen(int inX, int inY) {
        int x = inX + viewportPanX;
        int y = inY + viewportPanY;
        if (x < 0 || x + 2 > viewportWidth) return;
        if (y < 0 || y + 2 > viewportHeight) return;

        int index = x + y * viewportWidth;  // index of pixel on canvas
        int colorIndex = placeParser.getColorBuffer()[index];
        Color color = colors[colorIndex];
        int colorBufferIndex = x * 3 + y * viewportWidth * 3;   // index of top left colorBuffer element being drawn

//        for (int pixelY = 0; pixelY < zoom; pixelY++) {
//            for (int pixelX = 0; pixelX < zoom; pixelX++) {
//                int bufIndex = colorBufferIndex + pixelX  * 3 + pixelY * CANVAS_SIZE_X * 3;
//                colorBuffer[bufIndex] = color.getRed();
//                colorBuffer[bufIndex + 1] = color.getGreen();
//                colorBuffer[bufIndex + 2] = color.getBlue();
//            }
//        }

        colorBuffer[colorBufferIndex] = color.getRed();
        colorBuffer[colorBufferIndex + 1] = color.getGreen();
        colorBuffer[colorBufferIndex + 2] = color.getBlue();
    }

    boolean isPointWithinViewport(int x, int y) {
        return false;
    }

    private void updateColorBuffer(int[] newColorBuffer) {
        for (int i = 0; i < newColorBuffer.length; i++) {
            int index = i * 3;
            Color color = colors[newColorBuffer[i]];
            colorBuffer[index] = color.getRed();
            colorBuffer[index + 1] = color.getGreen();
            colorBuffer[index + 2] = color.getBlue();
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

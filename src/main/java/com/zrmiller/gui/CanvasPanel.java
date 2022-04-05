package com.zrmiller.gui;

import com.zrmiller.core.parser.PlaceParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CanvasPanel extends JPanel {

    public static int CANVAS_SIZE_X = 1001;
    public static int CANVAS_SIZE_Y = 1001;

    private int targetFPS = 10;

    public int VIEWPORT_WIDTH;
    public int VIEWPORT_HEIGHT;

    private int viewportPanX = 500;
    private int viewportPanY = 0;
    private final int MAX_ZOOM = 10;
    private int zoom = 1;

    private Timer timer = new Timer(33, new ActionListener() {
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

    BufferedImage bufferedImage = new BufferedImage(CANVAS_SIZE_X, CANVAS_SIZE_Y, BufferedImage.TYPE_INT_RGB);

    int[] colorBuffer = new int[CANVAS_SIZE_X * CANVAS_SIZE_Y * 3];

    private static Executor executor = Executors.newSingleThreadExecutor();

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
            }
        });
        timer.setDelay(100);
        timer.start();
    }

    private void updateColorBuffer(){
        int[] simpleBuffer = placeParser.getColorBuffer();
        Arrays.fill(colorBuffer, 0);
        int x;
        int y;

        for(int i = 0;i<simpleBuffer.length;i++){
            int index = i * 3;
            Color color = colors[simpleBuffer[i]];
            colorBuffer[index] = color.getRed();
            colorBuffer[index + 1] = color.getGreen();
            colorBuffer[index + 2] = color.getBlue();
        }
    }

    boolean isPointWithinViewport(int x, int y){
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bufferedImage.getRaster().setPixels(0, 0, CANVAS_SIZE_X, CANVAS_SIZE_Y, colorBuffer);
        g.drawImage(bufferedImage, 0, 0, Color.white, null);
    }

}

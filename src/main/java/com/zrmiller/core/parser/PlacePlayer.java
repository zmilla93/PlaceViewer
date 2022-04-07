package com.zrmiller.core.parser;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PlacePlayer {

    //    PlaceParser2017 parser = new PlaceParser2017();
    IPlaceParser parser = new PlaceParser2022("D:/Place/2022-Binary/", "Place_2022_INDEX.placetiles");
    private int updatesPerSecond = 1000000;
    private int TEMP_FPS = 60;
    private Timer timer = new Timer();
    int frameCount = 0;

    private int[] colorBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];
    private final String inputPath;

    // Heatmap
    private int[] heatmapBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];
    public static int heatmapWeight = 10000;
    public static int heatmapMax = 100000;
    public static int heatmapDecay = 10;

    private boolean playing;

    public PlacePlayer(String inputPath) {
        this.inputPath = inputPath;
        parser = new PlaceParser2022("D:/Place/2022-Binary/", "Place_2022_INDEX.placetiles");
        parser.openStream();
    }

    public void play() {
        if (playing) return;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int iterations = updatesPerSecond / TEMP_FPS;
                try {
                    for (int i = 0; i < iterations; i++) {
                        applyNextFrame();
                    }
                } catch (IOException e) {
                    pause();
                }
                decayHeatmap(iterations);
            }
        }, 0, 1000 / TEMP_FPS);
        playing = true;
    }

    public void pause() {
        timer.cancel();
        timer.purge();
        playing = false;
    }

    public void setSpeed(int speed) {
        updatesPerSecond = speed;
    }

    public void reset() {
        pause();
        parser.closeStream();
        frameCount = 0;
        Arrays.fill(colorBuffer, 0);
        Arrays.fill(heatmapBuffer, 0);
//        parser.openInputStream(inputPath);
        parser.openStream();
    }

    public void jumpToFrame(int frame) {

    }

    private void applyNextFrame() throws IOException {
        if (parser.ready()) {
//            int[] tokens = parser.getNextLine();
            TileEdit tile = parser.readNextLine();

//            int index = tokens[0] + tokens[1] * PlaceInfo.CANVAS_SIZE_X;
            int index = tile.x + tile.y * PlaceInfo.CANVAS_SIZE_X;
            colorBuffer[index] = tile.color;
            heatmapBuffer[index] += heatmapWeight;
            if (heatmapBuffer[index] > heatmapMax) heatmapBuffer[index] = heatmapMax;
            frameCount++;
        } else {
            pause();
        }
    }

    private void decayHeatmap(int iterations) {
        for (int i = 0; i < heatmapBuffer.length; i++) {
            int heat = heatmapBuffer[i] - iterations / 10;
            if (heat < 0) heat = 0;
            heatmapBuffer[i] = heat;
        }
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }

    public int[] getHeatmapBuffer() {
        return heatmapBuffer;
    }

    public void resizeCanvas(int width, int height) {
        pause();
        colorBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];
        heatmapBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];
    }

}

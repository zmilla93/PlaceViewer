package com.zrmiller.core.parser;

import com.zrmiller.core.DatasetManager;
import com.zrmiller.core.IDatasetListener;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.SaveManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PlacePlayer implements IDatasetListener {

    //    PlaceParser2017 parser = new PlaceParser2017();
    IPlaceParser parser;
    private int updatesPerSecond = 1000000;
    private int TEMP_FPS = 60;
    private Timer timer = new Timer();
    int frameCount = 0;

    private int[] colorBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];

    // Heatmap
    private int[] heatmapBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];
    public static int heatmapWeight = 10000;
    public static int heatmapMax = 100000;
    public static int heatmapDecay = 10;

    private boolean playing;

    public PlacePlayer() {
//        this.inputPath = inputTemplate;
//        Arrays.fill(colorBuffer, DatasetManager.currentDataset().colorArray[DatasetManager.currentDataset().whiteIndex));
        parser = new PlaceParser2022(SaveManager.settingsSaveFile.data.dataDirectory + DatasetManager.currentDataset().YEAR_STRING + File.separator);
//        parser.openStream();
        DatasetManager.addListener(this);
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
        Arrays.fill(colorBuffer, DatasetManager.currentDataset().WHITE_INDEX);
        Arrays.fill(heatmapBuffer, 0);
        parser.openStream();
    }

    public boolean jumpToFrame(int frame) {
//        if (frame < frameCount) {
//            reset();
//        }
        reset();
        ;
        try {
            while (frameCount < frame) {
//                System.out.println("APPLY..." + frameCount);
                if (!applyNextFrame())
                    break;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean applyNextFrame() throws IOException {
        if (parser.ready()) {
//            int[] tokens = parser.getNextLine();
            TileEdit tile = parser.readNextLine();

//            int index = tokens[0] + tokens[1] * PlaceInfo.CANVAS_SIZE_X;
            int index = tile.x + tile.y * DatasetManager.currentDataset().CANVAS_SIZE_X;
            colorBuffer[index] = tile.color;
            heatmapBuffer[index] += heatmapWeight;
            if (heatmapBuffer[index] > heatmapMax) heatmapBuffer[index] = heatmapMax;
            frameCount++;
            return true;
        } else {
            pause();
            return false;
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
        colorBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];
        heatmapBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];
    }

    private void resizeCanvas() {
        pause();
        parser.closeStream();
        colorBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];
        heatmapBuffer = new int[DatasetManager.currentDataset().CANVAS_SIZE_X * DatasetManager.currentDataset().CANVAS_SIZE_Y];
        Arrays.fill(colorBuffer, DatasetManager.currentDataset().WHITE_INDEX);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        pause();
        parser.closeStream();
        resizeCanvas();
        frameCount = 0;
        switch (dataset) {
            case PLACE_2017:
                parser = new PlaceParser2017();
                parser.openStream();
                break;
            case PLACE_2022:
                parser = new PlaceParser2022("D:/Place/");
                parser.openStream();
                break;
        }
    }
}

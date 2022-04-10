package com.zrmiller.core.parser;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.listeners.IDatasetListener;
import com.zrmiller.core.utility.TileEdit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PlacePlayer implements IDatasetListener {

    // General
    private AbstractPlaceParser parser;
    private int tileUpdatesPerSecond = 1000000;
    private final int LOGIC_UPDATES_PER_SECOND = 60;
    private int frameCount = 0;
    private boolean streamIsOpen;
    private Timer timer = new Timer();

    // Data Arrays
    private int[] colorBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];
    private int[] heatmapBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];

    // Heatmap
    public static int heatmapWeight = 10000;
    public static int heatmapMax = 100000;
    public static int heatmapDecay = 10;

    private State state = State.STOPPED;

    /**
     * Provides a media player style state machine for interacting with a dataset.
     * Stores color and heatmap data in flat arrays.
     */
    public PlacePlayer() {
        parser = new PlaceParser2022();
        App.datasetManager.addListener(this);
    }

    private enum State {STOPPED, PLAYING, PAUSED, SEEKING}

    //
    // Public Stuff
    //

    public void play() {
        if (state == State.PLAYING) return;
        if (!streamIsOpen) {
            streamIsOpen = parser.openStream();
            if (!streamIsOpen) return;
        }
        // FIXME: tileUpdatesPerSecond can't go below LOGIC_UPDATES_PER_SECOND
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int iterations = tileUpdatesPerSecond / LOGIC_UPDATES_PER_SECOND;
                try {
                    for (int i = 0; i < iterations; i++) {
                        applyNextFrame();
                    }
                } catch (IOException e) {
                    pause();
                }
                decayHeatmap(iterations);
            }
        }, 0, 1000 / LOGIC_UPDATES_PER_SECOND);
        state = State.PLAYING;
    }

    public void pause() {
        if (state == State.PAUSED) return;
        timer.cancel();
        timer.purge();
        state = State.PAUSED;
    }

    public void stop() {
        pause();
        parser.closeStream();
        frameCount = 0;
        Arrays.fill(colorBuffer, App.dataset().WHITE_INDEX);
        Arrays.fill(heatmapBuffer, 0);
        streamIsOpen = false;
        state = State.STOPPED;
    }

    public void setSpeed(int speed) {
        tileUpdatesPerSecond = speed;
    }

    public boolean jumpToFrame(int frame) {
        state = State.SEEKING;
        stop();
        parser.openStream();
        streamIsOpen = true;
        try {
            while (frameCount < frame) {
                if (!applyNextFrame())
                    break;
            }
            state = State.PAUSED;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            stop();
            return false;
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

    //
    // Private Stuff
    //

    private boolean applyNextFrame() throws IOException {
        if (parser.ready()) {
            TileEdit tile = parser.readNextLine();
            int index = tile.x + tile.y * App.dataset().CANVAS_SIZE_X;
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

    private void resizeCanvas() {
        pause();
        parser.closeStream();
        colorBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];
        heatmapBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];
        Arrays.fill(colorBuffer, App.dataset().WHITE_INDEX);
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
                break;
            case PLACE_2022:
                parser = new PlaceParser2022();
                break;
        }
    }
}

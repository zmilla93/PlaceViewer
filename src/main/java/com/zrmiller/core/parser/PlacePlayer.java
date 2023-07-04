package com.zrmiller.core.parser;

import com.zrmiller.App;
import com.zrmiller.core.enums.Dataset;
import com.zrmiller.core.managers.DatasetManager;
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
    private int[] colorBuffer;
    private int[] heatmapBuffer;

    // Heatmap
    public static int heatmapWeight = 2000;
    public static int heatmapMax = 100000;
    public static float heatmapDecayFactor = 0.0f; // Should be 0-1

    private State state = State.STOPPED;

    /**
     * Provides a media player style state machine for interacting with a dataset.
     * Stores color and heatmap data in flat arrays.
     */
    public PlacePlayer() {
        parser = new PlaceParser2022();
        DatasetManager.addDatasetListener(this);
    }

    private enum State {STOPPED, PLAYING, PAUSED, SEEKING}

    //
    // Public Stuff
    //

    public void play() {
        if (App.dataset() == null) return;
        if (state == State.PLAYING || state == State.SEEKING) return;
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
        if (state == State.STOPPED) return;
        pause();
        streamIsOpen = false;
        if (parser != null)
            parser.closeStream();
        if (App.dataset() == null) {
            state = State.STOPPED;
            return;
        }
        frameCount = 0;
        Arrays.fill(colorBuffer, App.dataset().WHITE_INDEX);
        Arrays.fill(heatmapBuffer, 0);
        state = State.STOPPED;
    }

    public void setSpeed(int speed) {
        tileUpdatesPerSecond = speed;
    }

    public int getSpeed() {
        return tileUpdatesPerSecond;
    }

    public boolean jumpToFrame(int frame) {
        if (App.dataset() == null || frame == frameCount)
            return false;
        boolean wasPlaying = state == State.PLAYING;
        state = State.SEEKING;
        if (frame < frameCount) {
            stop();
        }
        if (!streamIsOpen) {
            parser.openStream();
            streamIsOpen = true;
        }
        try {
            while (frameCount < frame) {
                if (!applyNextFrame())
                    break;
            }
            state = State.PAUSED;
            if (wasPlaying)
                play();
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
            // FIXME : It is possible for tile to be null here
            if (tile.color == -1)
                return false;
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
        if (heatmapDecayFactor == 0) return;
        for (int i = 0; i < heatmapBuffer.length; i++) {
            int heat = heatmapBuffer[i] - Math.round(iterations * heatmapDecayFactor);
            if (heat < 0) heat = 0;
            heatmapBuffer[i] = heat;
        }
    }

    private void resizeCanvas() {
        if (App.dataset() == null) {
            colorBuffer = null;
            heatmapBuffer = null;
            return;
        }
        colorBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];
        heatmapBuffer = new int[App.dataset().CANVAS_SIZE_X * App.dataset().CANVAS_SIZE_Y];
        Arrays.fill(colorBuffer, App.dataset().WHITE_INDEX);
    }

    @Override
    public void onDatasetChanged(Dataset dataset) {
        stop();
        parser.closeStream();
        streamIsOpen = false;
        resizeCanvas();
        frameCount = 0;
        if (dataset == null)
            return;
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

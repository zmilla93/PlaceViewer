package com.zrmiller.core.parser;

import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BufferedPlacePlayer {

    PlaceParser parser = new PlaceParser();
    private int updatesPerSecond = 1000000;
    private int TEMP_FPS = 60;

    static final int bufferSize = 1000000;
    TileEdit[] currentBuffer = new TileEdit[bufferSize];
    TileEdit[] backBuffer = new TileEdit[bufferSize];
    final Executor executor = Executors.newSingleThreadExecutor();

    private Timer timer = new Timer();
    int frameIndex = 0;

    private boolean ready;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            for (int i = 0; i < updatesPerSecond / TEMP_FPS; i++) {
                applyNextFrame();
            }
        }
    };

    private int[] colorBuffer = new int[PlaceInfo.CANVAS_SIZE_X * PlaceInfo.CANVAS_SIZE_Y];

    public BufferedPlacePlayer(String inputFile) {
        parser.openInputStream("D:/Place/place_tiles_final");
        try {
            ready = parser.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fillBackBuffer();
        swapBuffers();
        fillBackBuffer();
    }

    public void play() {
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000 / TEMP_FPS);
    }

    public void pause() {
        timer.cancel();
        timer.purge();
    }

    public void jumpToFrame(int frame) {

    }

    private void applyNextFrame() {
        if (frameIndex >= currentBuffer.length) {
            if(!ready){
                pause();
                return;
            }
            swapBuffers();
            fillBackBufferAsync();
            frameIndex = 0;
        }
        TileEdit edit = currentBuffer[frameIndex];
        colorBuffer[edit.x + edit.y * PlaceInfo.CANVAS_SIZE_X] = edit.color;
        frameIndex++;
    }

    public boolean fillBackBuffer() {
        try {
            for (int i = 0; i < backBuffer.length; i++) {
                ready = parser.ready();
                if (!ready)
                    return false;
                int[] tokens = parser.getNextLine();
                backBuffer[i] = new TileEdit(tokens[0], tokens[1], tokens[2]);
            }
        } catch (
                IOException e) {
            return false;
        }
        return true;
    }

    private void fillBackBufferAsync() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ready = fillBackBuffer();
            }
        });
    }

    public void swapBuffers() {
        currentBuffer = backBuffer;
    }

    public int[] getColorBuffer() {
        return colorBuffer;
    }

}
